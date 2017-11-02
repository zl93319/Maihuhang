package task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.gzgamut.demo.helper.NoConnectException;
import com.gzgamut.demo.model.Movement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * 同步数据
 * @author lingb
 *
 */
public class SyncAsyncTask extends AsyncTask<Object, Object, Object> {
	Context context;
	private boolean running = false;
	private Movement device;

	public SyncAsyncTask(Context context, Movement device) {
		this.context = context;
		this.device = device;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean getRunning() {
		return this.running;
	}

	@Override
	protected Object doInBackground(Object... params) {

		try {
			running = true;
//			JSONObject boundInfoResult = this.device.getBoundInfo();	// 获取手环的绑定信息
//			Log.i("sync", "boundInfoResult = " + boundInfoResult); 		// 如果result = 0，则未绑定，如果result = 1，则已绑定
//			JSONObject notifyResult=this.device.write_message_notify("噢噢噢哦哦", Config.SEND_NOTI_TYPE_QQ,1);
			JSONObject setBoundNoConfirmResult = this.device.setBoundStateNoConfirm();		// 跳过验证设置手环绑定状态，从获取手环的绑定信息的结果中来判断是否需要发这条指令，如果未绑定，就发这个指令。
			Log.i("sync", "setBoundNoConfirmResult = " + setBoundNoConfirmResult);		// 如果为result = 0，则成功，否则失败

			JSONObject datetimeResult = this.device.setDateTime(Calendar.getInstance(),9,23);	// 设置手环的日期和时间
			Log.i("sync", "datetimeResult = " + datetimeResult);		// 如果为result = 0，则成功，否则失败

			JSONObject alarmResult = this.device.setAlarm(8, 0, 0x1f, 8, 0, 0, 8, 0, 0);	// 设置手环的闹钟
			Log.i("sync", "alarmResult = " + alarmResult);				// 如果为result = 0，则成功，否则失败

			JSONObject targetResult = this.device.setTarget(2000);		// 设置手环的步数目标
			Log.i("sync", "targetResult = " + targetResult);			// 如果为result = 0，则成功，否则失败

			JSONObject versionResult = this.device.getVersion();		// 获取手环的固件版本号
			Log.i("sync", "versionResult = " + versionResult);			// result返回是一串字符，即版本号

			JSONObject batteryResult = this.device.getBattery();		// 获取手环的电量
			Log.i("sync", "batteryResult= " + batteryResult);			// result 里面的数值就是电量

//			JSONObject callResult = this.device.writeCallReminder();		// 向手环写来电提醒指令，有来电时才发这个指令
//			Log.i("sync", "callResult = " + callResult);		// 如果为result = 0，则成功，否则失败

			int count = 0;
			int sn = 0;

			if (running) {
				// 获取手环存储数据的总天数和当天数据所在的位置
				JSONObject objectCount = this.device.getActivityCount();
				count = objectCount.getInt("count");	// 总天数
				sn = objectCount.getInt("sn");		// 当天数据所在的位置
				Log.i("sync", "count = " + count + ", sn = " + sn);
			}

			// 获取手环所有天数的步数，从最旧的日期开始获取，一直到最新的日期
			for (int i = 1; i <= count; i++) {
				if (running) {
					JSONObject objectActivity_0 = this.device.getActivityBySn((sn + i) % count, 0);
					parserActivityValue(objectActivity_0);
				}
				if (running) {
					JSONObject objectActivity_6 = this.device.getActivityBySn((sn + i) % count, 6);
					parserActivityValue(objectActivity_6);
				}
				if (running) {
					JSONObject objectActivity_12 = this.device.getActivityBySn((sn + i) % count, 12);
					parserActivityValue(objectActivity_12);
				}
				if (running) {
					JSONObject objectActivity_18 = this.device.getActivityBySn((sn + i) % count, 18);
					parserActivityValue(objectActivity_18);
				}
			}

			//同步睡眠数据，新手环和手表睡眠单独同步
			for (int i = 1; i <= count; i++) {
				if (running) {
					JSONObject objectActivity_0 = this.device.getSleepBySn((sn + i) % count, 0);
					parserSleepValue(objectActivity_0);
				}
				if (running) {
					JSONObject objectActivity_6 = this.device.getSleepBySn((sn + i) % count, 6);
					parserSleepValue(objectActivity_6);
				}
				if (running) {
					JSONObject objectActivity_12 = this.device.getSleepBySn((sn + i) % count, 12);
					parserSleepValue(objectActivity_12);
				}
				if (running) {
					JSONObject objectActivity_18 = this.device.getSleepBySn((sn + i) % count, 18);
					parserSleepValue(objectActivity_18);
				}
			}
//			// 设置手环同步完成
//			this.device.setFinishSync();
		} catch (NoConnectException e) {
			e.printStackTrace();
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

		running = false;
		return null;
	}

	@Override
	protected void onPostExecute(Object result) {
		Toast.makeText(context, "sync complete", Toast.LENGTH_SHORT).show();
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Object... values) {

		super.onProgressUpdate(values);
	}

	/**
	 * 解析步数
	 */
	private void parserActivityValue(JSONObject object) {
		if (running) {
			try {
				JSONArray jsonArray = object.getJSONArray("ActivityData");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

					String date_time = jsonObject.getString("year") + "-" + jsonObject.getString("month") + "-" + jsonObject.getString("day") + " "
							+ jsonObject.getString("hour");				// 时间
					String value = jsonObject.getString("value");		// 步数或睡眠分数
					String type = jsonObject.getString("type");			// 类型，如果是"step"，则表明value为步数，如果是"sleep"，则表明value是睡眠分数


					Log.i("sync", "data = " + object);
					Log.i("sync", date_time + ",    " + value + ", " + type);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解析睡眠
	 */
	private void parserSleepValue(JSONObject object) {
		if (running) {
			try {
				JSONArray jsonArray = object.getJSONArray("SleepData");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = (JSONObject) jsonArray.opt(i);

					String date_time = jsonObject.getString("year") + "-" + jsonObject.getString("month") + "-" + jsonObject.getString("day") + " "
							+ jsonObject.getString("hour");				// 时间
					String value = jsonObject.getString("value");		// 步数或睡眠分数
					String type = jsonObject.getString("type");			// 类型，如果是"step"，则表明value为步数，如果是"sleep"，则表明value是睡眠分数


					Log.i("sync", "data = " + object);
					Log.i("sync", date_time + ",    " + value + ", " + type);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}

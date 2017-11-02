package ui.activity;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.administrator.maihuhang.R;
import com.gzgamut.demo.global.Config;
import com.gzgamut.demo.helper.NoConnectException;
import com.gzgamut.demo.model.BluetoothController;
import com.gzgamut.demo.model.SDKCallBack;

/**
 * demo activity
 * 步骤：
 * 1、在onCreate里面new一个TRASENSE实例；
 * 2、使用TRASENSE实例去连接、断开和同步数据；
 * 3、退出activity时，要调用actionDisconnect(boolean isDestroy)，并传入true。
 * @author lingb
 *
 */
public class SecondPageActivity extends Activity {

	private static final String TAG="SecondPageActivity";

	private BluetoothController bluetoothController;	// 蓝牙操作类，依据协议自行开发。According to the agreement to develop your own

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);

		bluetoothController =new BluetoothController(getApplicationContext());
		bluetoothController.registCallback(sdkCallBack);
		initUI();
	}

	private SDKCallBack sdkCallBack =new SDKCallBack(){


		/**
		 * 当调用scan()方法后,扫描到蓝牙设备后回调此方法。
		 * If you call scan() method ,this will callback reporting an LE device found.
		 * @param foundDevice    该对象包含设备名称和MAC地址
		 *                  It contains LE device‘s name and address
		 * @param rssi      信号强度    signal intensity
		 * @param scanRecord    蓝牙广播数据    Bluetooth broadcast data
		 */
		@Override
		public void onDeviceFound(FoundDevice foundDevice, int rssi, byte[] scanRecord) {
			if (foundDevice.getMac().contains("16:3F:1E:35")){			//16:3F:1E:35
				bluetoothController.connect(foundDevice.getMac());
				bluetoothController.stopScan();

			}
		}


		/**
		 * 当调用scan()方法后,扫描到蓝牙设备后回调此方法。本方法返回系统包装的蓝牙对象，根据项目需求选择回调。
		 * If you call scan() method ,this will callback reporting an LE device found.BluetoothDevice contains more content.
		 * @param device    系统包装的蓝牙对象
		 *                  BluetoothDevice
		 * @param rssi      信号强度    signal intensity
		 * @param scanRecord    蓝牙广播数据    Bluetooth broadcast data
		 */
		@Override
		public void onDeviceFound(BluetoothDevice device, int rssi, byte[] scanRecord) {
			super.onDeviceFound(device, rssi, scanRecord);
		}

		/**
		 * 当调用connect()方法后，连接成功与否会回调此方法，连接断开时也会回调此方法。
		 * When the call connect () method, the connection is successful or not, and the connection is broken,will call this method.
		 * @param state     连接状态。{@link SDKCallBack#STATE_CONNECT_SUCCESS}
		 *                  or {@link SDKCallBack#STATE_CONNECT_FAIL}
		 *                  or {@link SDKCallBack#STATE_DISCONNECT}
		 *                  or {@link SDKCallBack#STATE_ALREADY_CONNECT}
		 */
		@Override
		public void onConnectionStateChange(int state) {
			if (state==STATE_CONNECT_SUCCESS){
				try {
					/*连接成功后务必调用本方法不然后续无法进行
					After the connection is successful, we must call this method or the follow-up can not be carried out */
					bluetoothController.findGattService();
				} catch (NoConnectException e) {
					e.printStackTrace();
				}
			}else if (state==STATE_DISCONNECT){
				Log.e(TAG,"连接断开");
			}else if (state==STATE_CONNECT_FAIL){
				Log.e(TAG,"连接失败");
			}
		}


		/**
		 * 写入descriptor后回调此方法
		 * Callback indicating the result of a descriptor write operation.
		 * @param status    写入descriptor的结果
		 *                  The result of the write operation
		 *                  {@link SDKCallBack#STATUS_SUCCESS}
		 */
		@Override
		public void onDescriptorWrite(int status) {
			if (status==STATUS_SUCCESS){
				Log.e(TAG, "描述写入成功。	Descriptor write success");
			}
		}


		/**
		 * 调用发现服务后的回调
		 * Callback when you called findGattService()
		 * @param status   发现结果
		 *                 result
		 */
		@Override
		public void onBluetoothServiceDiscover(boolean status) {

			if (status){
				Log.e(TAG, "成功找到服务。 BluetoothService find success");
			}
			/*如果设备的所有通讯都使用同一UUID的通讯通道，在此处设置一次则后续不用再设置。
			 If all the communication of the device uses the same UUID communication channel, once set here, the follow-up will not be reset*/
//			try {
// 				boolean result=bluetoothController.self_notify(Config.UUID_SERVICE, Config.UUID_CHARACTERISTIC_NOTI);
				/*需要顺便打开描述使用此方法
				  Need to open the description using this method*/
//				bluetoothController.self_notifyAndWriteDescriptor(Config.UUID_SERVICE, Config.UUID_CHARACTERISTIC_NOTI, Config.UUID_DESCRIPTOR_CONFIGURATION);
//			} catch (NoConnectException e) {
//				e.printStackTrace();
//			}
		}



		/**
		 * 所有发送数据通过本接口异步返回蓝牙数据,但是返回的数据是未包装的，如果同一时间发送多条返回顺序无法确定。
		 * All data sent through this interface is returned asynchronously to Bluetooth data, but the returned data is not packaged.
		 * @param value     未包装的蓝牙返回数据，按照蓝牙协议自行解析
		 *                  Unpackaged Bluetooth return data
		 */
		@Override
		public void onSelfDeviceResponse(byte[] value) {
			super.onSelfDeviceResponse(value);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 断开设备
		bluetoothController.unRegistCallback();
		bluetoothController.stopScan();
		bluetoothController.disconnect();
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button_search:
				actionScan();
				break;
			case R.id.button_sync:
				actionSync();
				break;
			case R.id.button_disconnect:
				actionDisconnect();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 点击了扫描设备
	 */
	private void actionScan() {
		/*设置你想搜索的设备名*/
		bluetoothController.setScan_device_name(new String[]{"Wristband9U"});
		bluetoothController.scan();
	}

	/**
	 * 点击了同步数据
	 */
	private void actionSync() {
		byte[] value=new byte[20];
		value[0]=0x10;
		try {
			/*本方法适用于数据需要使用不同特征值的通道 The method is suitable for the channel with different UUID*/
			boolean result=bluetoothController.self_notify(Config.UUID_SERVICE, Config.UUID_CHARACTERISTIC_NOTI);
			if (result) {
				bluetoothController.writeCharacteristic(value);
			}
		} catch (NoConnectException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 点击了断开连接任务
	 */
	private void actionDisconnect() {
		// 断开设备
		bluetoothController.disconnect();
	}

	private void initUI() {

		Button button_search = (Button) findViewById(R.id.button_search);
		button_search.setOnClickListener(myOnClickListener);

		Button button_sync = (Button) findViewById(R.id.button_sync);
		button_sync.setOnClickListener(myOnClickListener);

		Button button_disconnect = (Button) findViewById(R.id.button_disconnect);
		button_disconnect.setOnClickListener(myOnClickListener);


	}
}
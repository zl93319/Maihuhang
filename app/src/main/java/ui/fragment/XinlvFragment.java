package ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.maihuhang.BuildConfig;
import com.example.administrator.maihuhang.R;
import com.gzgamut.demo.global.Global;
import com.gzgamut.demo.helper.NoConnectException;
import com.gzgamut.demo.model.Movement;
import com.gzgamut.demo.model.SDKCallBack;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import api.Api;
import base.BaseFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import servies.KivenFileMethod;
import task.SyncAsyncTask;
import ui.activity.MainActivity;
import ui.activity.SecondPageActivity;


public class XinlvFragment extends BaseFragment implements ActivityCompat.OnRequestPermissionsResultCallback {


    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 200;
    private Movement device = null;    // 蓝牙操作类	对应目前最新TRASENSE手环及手表
    //private TRASENSE device = null;	// 蓝牙操作类	对应目前旧TRASENSE手环及手表
    private SyncAsyncTask taskSync;        // 同步手环数据任务
    public String deviceID = "";
    private ListView listViewDeviceList;
    private Button buttonSynchronization;

    private SDKCallBack.FoundDevice lastConnectDevice;
    public int mShousuoya;
    public int mShuzhangya;

    @Override
    protected View initView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_first, null);
        initBugly();
        device = new Movement(getActivity(), sdkCallBack);
        initUI(view);

        return view;
    }


    private void initBugly() {
        Context context = getActivity().getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
//        CrashReport.initCrashReport(context, "10a81202ad", isDebug, strategy);
        // 集成了应用升级后，需要注释掉原crashReport方法。使用统一的初始化方法
        Bugly.init(getActivity().getApplicationContext(), "c2c11a7ecf", BuildConfig.DEBUG);
        // 如果通过“AndroidManifest.xml”来配置APP信息，初始化方法如下
        // CrashReport.initCrashReport(context, strategy);
    }

    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            } else {
                //具有权限
                actionScan();
            }
        } else {
            //系统不高于6.0直接执行
            actionScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        doNext(requestCode, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意权限
                actionScan();
            } else {
                // 权限拒绝
                // 下面的方法最好写一个跳转，可以直接跳转到权限设置页面，方便用户
//                denyPermission();
                Toast.makeText(getActivity().getApplicationContext(), "没有蓝牙权限", Toast.LENGTH_LONG);
            }
        }
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(getActivity().getApplicationContext(), "连接成功", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(getActivity().getApplicationContext(), "检测到心率" + msg.arg1, Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getActivity().getApplicationContext(), String.format("检测到血压，收缩压%s，舒张压%s", msg.arg1, msg.arg2), Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(getActivity().getApplicationContext(), String.format("设备id %s", deviceID), Toast.LENGTH_LONG).show();
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
    };


    private SDKCallBack sdkCallBack = new SDKCallBack() {

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
            Log.e("FirstPageActivity", "onDeviceFound: " + foundDevice.getName());
//            if (foundDevice.getMac().contains("16:3F:1E:35") || true) {            //16:3F:1E:35
//                device.connect(foundDevice.getMac());
//                device.stopScan();
//            }
            deviceListAdapter.addItem(foundDevice);
            deviceListAdapter.notifyDataSetChanged();
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
            if (state == STATE_CONNECT_SUCCESS) {
                device.openDescriptor();    //连接成功后一定要调用这个否则无法接收到数据。
            } else if (state == STATE_DISCONNECT) {
                Log.e("FirstPageActivity", "断开连接");
              /*  Toast.makeText(getActivity(), "断开连接", Toast.LENGTH_LONG);*/
            } else if (state == STATE_CONNECT_FAIL) {
                Log.e("FirstPageActivity", "连接失败");
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
            if (status == STATUS_SUCCESS) {
                Log.e("FirstPageActivity", "连接成功");
                getDeviceID(device);
                Message.obtain(handler, 1).sendToTarget();
            }
        }


        /**
         * 通过SDK以封装好的接口发送数据后，异步返回蓝牙数据,如果同一时间发送多条并不能按照发送顺序返回。
         * Asynchronous return Bluetooth data.
         * @param value     返回数据
         *                  response value
         * @param type      返回数据类型
         *                  response type
         *                  {@link SDKCallBack#RESPONSE_TYPE_ACTIVITY_COUNT}...
         */
        @Override
        public void onSDKDeviceResponse(JSONObject value, int type) {
            super.onSDKDeviceResponse(value, type);
            Log.i(getActivity().getClass().getSimpleName(), "onSDKDeviceResponse: type = " + type + "> " + (value == null ? "null" : value.toString()));
            if (type == SDKCallBack.RESPONSE_TYPE_BLOOD_PRESSURE) {
                Log.i(getActivity().getClass().getSimpleName(), "onSDKDeviceResponse: 血压响应=" + value.toString());
            } else if (type == SDKCallBack.RESPONSE_TYPE_ID) {
                try {
                    deviceID = value.getString("zoon");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(getActivity().getClass().getSimpleName(), "onSDKDeviceResponse: 设备号 " + deviceID);
                Message.obtain(handler, 4).sendToTarget();
            }

        }


        /**
         * 所有发送数据通过本接口异步返回蓝牙数据,但是返回的数据是未包装的，如果同一时间发送多条并不能按照发送顺序返回。
         * All data sent through this interface is returned asynchronously to Bluetooth data, but the returned data is not packaged.
         * @param value     未包装的蓝牙返回数据，按照蓝牙协议自行解析
         *                  Unpackaged Bluetooth return data
         */
        @Override
        public void onSelfDeviceResponse(byte[] value) {
            super.onSelfDeviceResponse(value);
            Log.d(getActivity().getClass().getSimpleName(), "onSelfDeviceResponse: " + byte2HexStr(value, value.length));
            if (byte2int(value[0]) == 0xD8) {
                mShousuoya = byte2int(value[1]);
                mShuzhangya = byte2int(value[2]);
                Log.i(getActivity().getClass().getSimpleName(), String.format("onSelfDeviceResponse: 血压响应 > 收缩压：%s, 舒张压：%s", mShousuoya, mShuzhangya));
                if (TextUtils.isEmpty(deviceID)) {
                    return;
                }
                uploadBloodpRessure(deviceID, mShousuoya, mShuzhangya);
            }
        }
    };


    public static int byte2int(byte b) {
        return b & 0xFF;
    }

    private final static char[] mChars = "0123456789ABCDEF".toCharArray();

    /**
     * bytes转换成十六进制字符串
     *
     * @param b    byte[] byte数组
     * @param iLen int 取前N位处理 N=iLen
     * @return String 每个Byte值之间空格分隔
     */
    public static String byte2HexStr(byte[] b, int iLen) {
        StringBuilder sb = new StringBuilder();
        for (int n = 0; n < iLen; n++) {
            sb.append(mChars[(b[n] & 0xFF) >> 4]);
            sb.append(mChars[b[n] & 0x0F]);
            sb.append(' ');
        }
        return sb.toString().trim().toUpperCase();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (lastConnectDevice != null) {
            device.connect(lastConnectDevice.getMac());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时，断开设备，最新SDK传入的参数以不影响
//        actionDisconnect(true);
    }

    public static int requestCode;
    private View.OnClickListener myOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_search:
                    checkBluetoothPermission();
                    actionScan();
                    break;
                case R.id.button_sync:
                    actionSync();
                    break;
                case R.id.button_disconnect:
                    actionDisconnect(false);
                    break;
                case R.id.button_test:
                    actionclickTest();
                    break;
                case R.id.button_xinlv:
                    actionXinlv();
                    break;
                case R.id.button_xueya:
                    actionXueYa();

//                    Intent intent = new Intent();
//                    intent.putExtra("Xinlv", mMyData);
//                    getTargetFragment().onActivityResult(requestCode, Activity.RESULT_OK, intent);
                    break;
                case R.id.button_synchronization:
                    timeSynchronization();
                    break;
                default:
                    break;
            }
        }
    };

    private void timeSynchronization() {
        Calendar now = Calendar.getInstance();
        try {
            device.setDateTime(now, 6, 18);
            Toast.makeText(getActivity(), "同步手环时间成功", Toast.LENGTH_SHORT).show();
        } catch (NoConnectException e) {
            Toast.makeText(getActivity(), "没有连接到手环，无法同步", Toast.LENGTH_SHORT).show();
        }
    }

    private void actionclickTest() {
        Intent intent = new Intent(getActivity(), SecondPageActivity.class);
        startActivity(intent);
    }

    /**
     * 点击了扫描设备
     */
    private void actionScan() {

        deviceListAdapter.setDataSource(null);
        device.scan(new String[]{"I8", "SH09U"});
//        device.scan(new String[]{""});

//		if (taskScan != null) {
//			boolean isRunning = taskScan.getRunning();
//			if (isRunning) {
//				// 正在扫描设备
//				Log.w("scan", "************ scaning, wait...");
//				return;
//			}
//		}
//		// 启动扫描设备任务
//		taskScan = new ScanAsyncTask(FirstPageActivity.this, device);
//		taskScan.execute(null,null);
    }

    /**
     * 点击了同步数据
     */
    private void actionSync() {
        if (taskSync != null) {
            boolean isRunning = taskSync.getRunning();
            if (isRunning) {
                // 正在同步数据
                Log.w("sync", "************ synchronizing, wait...");
                return;
            }
        }
        Toast.makeText(getActivity(), "没有连接手环", Toast.LENGTH_SHORT).show();
        taskSync = new SyncAsyncTask(getActivity(), device);
        taskSync.execute(null, null);

    }

    private void actionXinlv() {
        if (taskSync != null) {
            boolean isRunning = taskSync.getRunning();
            if (isRunning) {
                // 正在同步数据
                Log.w("sync", "************ synchronizing, wait...");
                return;
            }
        }
        Log.i(this.getClass().getSimpleName(), "actionXinlv: 开始获取心率");

        AsyncTask xinlvTask = new AsyncTask<Object, Integer, Long>() {
            @Override
            protected Long doInBackground(Object... params) {
                if (params.length > 0) {
                    Movement hand = (Movement) params[0];
                    try {
                        JSONObject jsonObject = hand.writeHeart(Global.TYPE_HEART_GET);
                        Log.i(this.getClass().getSimpleName(), "doInBackground: 心率返回=" + jsonObject.toString());
                        try {
                            if (!TextUtils.isEmpty(deviceID)) {
                                String heartRate = jsonObject.getString("result");
                                uploadHeartRate(deviceID, heartRate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (NoConnectException e) {
                        e.printStackTrace();
                    }
                }
                return 0L;
            }
        };
        xinlvTask.execute(device);
    }

    private void actionXueYa() {
        if (taskSync != null) {
            boolean isRunning = taskSync.getRunning();
            if (isRunning) {
                // 正在同步数据
                Log.w("sync", "************ synchronizing, wait...");
                return;
            }
        }
        Log.i(this.getClass().getSimpleName(), "actionXinlv: 开始校准血压");

        int shousuoya;
        int shuzhangya;
        try {
            shousuoya = Integer.parseInt(editTextShousuoya.getText().toString());
            shuzhangya = Integer.parseInt(editTextShuzhangya.getText().toString());
        } catch (NumberFormatException e) {
            shousuoya = 90;
            shuzhangya = 60;
        }
        final int finalShousuoya = shousuoya;
        final int finalShuzhangya = shuzhangya;

        AsyncTask xueYaTask = new AsyncTask<Object, Integer, Long>() {
            @Override
            protected Long doInBackground(Object... params) {
                if (params.length > 0) {
                    Movement hand = (Movement) params[0];
                    try {
                        JSONObject jsonObject = hand.write_blood_pressure(finalShousuoya, finalShuzhangya);
//                        Log.i(this.getClass().getSimpleName(), "doInBackground: 血压校准返回=" + jsonObject.toString());
                    } catch (NoConnectException e) {
                        e.printStackTrace();
                    }
                }
                return 0L;
            }
        };
        ((MainActivity) getActivity()).setDeviceID(deviceID);
        ((MainActivity) getActivity()).setShuzhangya(shuzhangya);
        ((MainActivity) getActivity()).setShousuoya(shousuoya);

        xueYaTask.execute(device);
    }

    /**
     * 点击了断开连接任务
     */
    private void actionDisconnect(boolean isDestroy) {
        if (taskSync != null) {
            taskSync.setRunning(false);
        }
        // 断开设备
        try {
            device.disconnectDevice(isDestroy);
        } catch (NoConnectException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    EditText editTextShousuoya;
    EditText editTextShuzhangya;

    private void initUI(View view) {

        Button button_search = (Button) view.findViewById(R.id.button_search);
        button_search.setOnClickListener(myOnClickListener);

        Button button_sync = (Button) view.findViewById(R.id.button_sync);
        button_sync.setOnClickListener(myOnClickListener);

        Button button_disconnect = (Button) view.findViewById(R.id.button_disconnect);
        button_disconnect.setOnClickListener(myOnClickListener);

        Button button_test = (Button) view.findViewById(R.id.button_test);
        button_test.setOnClickListener(myOnClickListener);

        Button button_xinlv = (Button) view.findViewById(R.id.button_xinlv);
        button_xinlv.setOnClickListener(myOnClickListener);

        Button button_xueya = (Button) view.findViewById(R.id.button_xueya);
        button_xueya.setOnClickListener(myOnClickListener);

        editTextShousuoya = (EditText) view.findViewById(R.id.editText_shousuoya);
        editTextShuzhangya = (EditText) view.findViewById(R.id.editText_shuzhangya);

        listViewDeviceList = (ListView) view.findViewById(R.id.listView_deviceList2);
        listViewDeviceList.setAdapter(deviceListAdapter);
//        List list = new ArrayList();
//        SDKCallBack.FoundDevice foundDevice = sdkCallBack.new FoundDevice();
//        foundDevice.setName("aaa");
//        foundDevice.setMac("123");
//        list.add(foundDevice);
//        deviceListAdapter.setDataSource(list);
        listViewDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SDKCallBack.FoundDevice item = deviceListAdapter.getItem(position);
                Toast.makeText(getActivity(), "连接设备" + item.getName(), Toast.LENGTH_LONG).show();
                lastConnectDevice = item;
                device.connect(item.getMac());
                device.stopScan();
            }
        });

        buttonSynchronization = (Button) view.findViewById(R.id.button_synchronization);
        buttonSynchronization.setOnClickListener(myOnClickListener);
    }

    /**
     * 上传心率
     *
     * @param deviceId
     * @param heart
     */
    public void uploadHeartRate(String deviceId, String heart) {
        Message.obtain(handler, 2, Integer.parseInt(heart), 0).sendToTarget();
        Call<String> call = Api.service().submitHeartRate(deviceId, heart);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(getApplicationContext(), "心率数据上传完毕", Toast.LENGTH_SHORT).show();
                Log.i(getActivity().getClass().getSimpleName(), "onResponse: 心率数据上传完毕");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "心率数据上传完毕", Toast.LENGTH_SHORT).show();
                Log.e(getActivity().getClass().getSimpleName(), "onResponse: 心率数据上传失败");
            }
        });
    }

    /**
     * 上传血压
     *
     * @param deviceId
     * @param sbp      收缩压     systolic pressure   0-255
     * @param dbp      舒张压     diastolic pressure  0-255
     */
    public void uploadBloodpRessure(String deviceId, int sbp, int dbp) {
        Message.obtain(handler, 3, sbp, dbp).sendToTarget();

        Call<String> call = Api.service().submitBloodPressure(deviceId, String.valueOf(sbp),
                String.valueOf(dbp));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
//                Toast.makeText(getApplicationContext(), "血压数据上传完毕", Toast.LENGTH_SHORT).show();
                Log.i(getActivity().getClass().getSimpleName(), "onResponse: 血压数据上传完毕");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "血压数据上传完毕", Toast.LENGTH_SHORT).show();
                Log.e(getActivity().getClass().getSimpleName(), "onResponse: 血压数据上传失败");
            }
        });
    }

    public String getDeviceID(Movement device) {
//        try {
        try {
            device.getID();
        } catch (NoConnectException e) {
            e.printStackTrace();
        }
//        } catch (JSONException e) {
//            Log.e(getClass().getSimpleName(), "getDeviceID: 获取设备id失败", e);
//        } catch (NoConnectException e) {
//            Log.e(getClass().getSimpleName(), "getDeviceID: 设备没有成功连接");
//        }
        return "";
    }

    DeviceListAdapter deviceListAdapter = new DeviceListAdapter();

    public class DeviceListAdapter extends BaseAdapter {

        private List dataSource = new ArrayList();

        public void setDataSource(List dataSource) {
            this.dataSource.clear();
            if (dataSource != null) {
                this.dataSource.addAll(dataSource);
            }
        }

        public void addItem(SDKCallBack.FoundDevice item) {
            if (dataSource.size() == 0) {
                dataSource.add(item);
                return;
            }

            CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
            copyOnWriteArrayList.addAll(dataSource);

            boolean isExists = false;
            for (Object foundDevice : copyOnWriteArrayList) {
                if (((SDKCallBack.FoundDevice) foundDevice).getMac().equals(item.getMac())) {
                    isExists = true;
                }
            }
            if (!isExists) {
                dataSource.add(item);
            }
        }

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public SDKCallBack.FoundDevice getItem(int position) {
            return (SDKCallBack.FoundDevice) dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            XinlvFragment.DeviceListAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.adapter_device_list2, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (XinlvFragment.DeviceListAdapter.ViewHolder) convertView.getTag();
            }
            SDKCallBack.FoundDevice item = getItem(position);

            String blueTooh = item.getName() + " - " + item.getMac();
            String name = KivenFileMethod.getBlueTooth(blueTooh);
            viewHolder.textView.setText(name);
            return convertView;
        }

        class ViewHolder {
            TextView textView;

            public ViewHolder(View view) {
                textView = (TextView) view.findViewById(R.id.adapter_textView_deviceID);
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

}

package ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.maihuhang.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import api.RetrofitFactory;
import api.XmlService;
import base.BaseFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ui.activity.GanhuanActivity;
import ui.activity.GanjinActivity;
import ui.activity.GanruoActivity;
import ui.activity.GanshiActivity;
import ui.activity.GanxianActivity;
import ui.activity.HistoryActivity;
import ui.activity.MainActivity;
import ui.activity.ShenhuanActivity;
import ui.activity.ShenjinActivity;
import ui.activity.ShenruoActivity;
import ui.activity.ShenshiActivity;
import ui.activity.ShenxianActivity;
import ui.activity.WeijcActivity;
import ui.activity.XinhuanActivity;
import ui.activity.XinjinActivity;
import ui.activity.XinruoActivity;
import ui.activity.XinshiActivity;
import ui.activity.XinxianActivity;


public class MaixFragment extends BaseFragment {
    private static final String TAG = "MaixFragment";
    private static final int PERCENT1 = 15;
    private static final int PERCENT2 = -15;
    private static final int PERCENT3 = 10;
    Intent mIntent1;
    Intent mIntent2;
    Intent mIntent3;
    Intent mIntentHistory;

    float distance = 0;

    String deviceId = "";
    int shousuoya;
    int shuzhangya;


    Unbinder unbinder;
    @BindView(R.id.tv_xin_jiexi)
    TextView mTvXinJiexi;
    @BindView(R.id.tv_shen_jiexi)
    TextView mTvShenJiexi;
    @BindView(R.id.tv_xin_maili)
    TextView mTvMaili;
    @BindView(R.id.tv_gan_jiexi)
    TextView mTvGanJiexi;
    Unbinder unbinder1;
    @BindView(R.id.disc_xin1)
    ImageView mDiscXin1;
    @BindView(R.id.disc_xin2)
    ImageView mDiscXin2;
    @BindView(R.id.disc_xin3)
    ImageView mDiscXin3;
    @BindView(R.id.disc_xin4)
    ImageView mDiscXin4;
    @BindView(R.id.disc_xin5)
    ImageView mDiscXin5;
    @BindView(R.id.tv_shen_maili)
    TextView mTvShenMaili;
    @BindView(R.id.tv_gan_maili)
    TextView mTvGanMaili;
    @BindView(R.id.disc_shen1)
    ImageView mDiscShen1;
    @BindView(R.id.disc_shen2)
    ImageView mDiscShen2;
    @BindView(R.id.disc_shen3)
    ImageView mDiscShen3;
    @BindView(R.id.disc_shen4)
    ImageView mDiscShen4;
    @BindView(R.id.disc_shen5)
    ImageView mDiscShen5;
    @BindView(R.id.disc_gan0)
    ImageView mDiscGan1;
    @BindView(R.id.disc_gan1)
    ImageView mDiscGan2;
    @BindView(R.id.disc_gan2)
    ImageView mDiscGan3;
    @BindView(R.id.disc_gan3)
    ImageView mDiscGan4;
    @BindView(R.id.disc_gan4)
    ImageView mDiscGan5;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_celiang_time)
    TextView mTvCeliangTime;
    @BindView(R.id.img_history)
    ImageView mImgHistory;


    private int mGanmai;
    private int mShenmai;
    private int mXinmai;
    private SharedPreferences mData;
    private String mCurrentDate;

    //初始化
    @Override
    protected View initView() {
        View v = View.inflate(mContext, R.layout.maix_fragment, null);
        ButterKnife.bind(this, v);
        sendPost();
        mData = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        mGanmai = mData.getInt("ganmai", 1);
        mShenmai = mData.getInt("shenmai", 1);
        mXinmai = mData.getInt("xinmai", 1);
        mCurrentDate = mData.getString("currentTime", "");
        showGanLevel(mGanmai);
        showShenLevel(mShenmai);
        showXinLevel(mXinmai);

//        //获取系统时间
//        Calendar calendar = Calendar.getInstance();
//        long unixTime = calendar.getTimeInMillis();//这是时间戳
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        String format = df.format(new Date());
//        mSystemTime.setText(format + "");

        deviceId = ((MainActivity) getActivity()).getDeviceID();
        shousuoya = ((MainActivity) getActivity()).getShousuoya();
        shuzhangya = ((MainActivity) getActivity()).getShuzhangya();
        logD(mGanmai + "=========" + mShenmai + "===========" + mXinmai + "==========");
        logD(deviceId + "=========" + shousuoya + "===========" + shuzhangya + "==========");

        return v;
    }

    //初始化数据
    @Override
    protected void initData() {

    }

    //统一管理监听事件
    @Override
    protected void initListener() {

    }

//    //肝的旋转动画
//    private void ivRotateGan(double percent) {
//        double percentOffset = percent > 100 ? 100 : percent;
//        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
//        LinearInterpolator lin = new LinearInterpolator();
//        rotateAnimation.setInterpolator(lin);
//        rotateAnimation.setDuration(1500);
//        rotateAnimation.setFillAfter(true);  //旋转完停止到最后一帧
//        mDiscGan1.startAnimation(rotateAnimation);
//
//    }
//
//    //肾的旋转动画
//    private void ivRotateShen(double percent) {
//        double percentOffset = percent > 100 ? 100 : percent;
//        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 180 * ((int) percentOffset / 100f),
//                Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
//        rotateAnimation.setDuration(1500);
//        rotateAnimation.setFillAfter(true);  //旋转完停止到最后一帧
//        mDiscShen1.startAnimation(rotateAnimation);
//    }
//
//    //心的旋转动画
//    private void ivRotateXin(double percent) {
//        double percentOffset = percent > 100 ? 100 : percent;
//        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 90f,
//                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.9f);
//        LinearInterpolator lin = new LinearInterpolator();
//        rotateAnimation.setInterpolator(lin);
//        rotateAnimation.setDuration(5000);
//        rotateAnimation.setFillAfter(true);  //旋转完停止到最后一帧
//        mDiscXin1.startAnimation(rotateAnimation);
//    }

    private void sendPost() {

        Call<XmlService> data = RetrofitFactory.getInstance().listData(23, 1, 3.6, shousuoya, shuzhangya, deviceId);
        data.enqueue(new Callback<XmlService>() {
            @Override
            public void onResponse(Call<XmlService> call, Response<XmlService> response) {
                XmlService body = response.body();
                mGanmai = body.ganmai;
                mShenmai = body.shenmai;
                mXinmai = body.xinmai;
                showGanLevel(mGanmai);
                showShenLevel(mShenmai);
                showXinLevel(mXinmai);
                logD("成功" + mGanmai + mXinmai + mShenmai);
//                //获取本地测试时间
                SharedPreferences.Editor edit = mData.edit();
                SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                String s = format.format(date);
                edit.putString("shijain", s);
                logD("测试时间**********************" + s + "*****************************");
                // mTvCeliangTime.setText(s + "");
                edit.putInt("ganmai", mGanmai);
                edit.putInt("shenmai", mShenmai);
                edit.putInt("xinmai", mXinmai);
//                long unixTime = SystemClock.currentThreadTimeMillis();//这是时间戳
//                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                String format = df.format(new Date(unixTime));
//                edit.putString("currentTime", format);
                edit.clear().commit();

            }

            @Override
            public void onFailure(Call<XmlService> call, Throwable t) {
                logD("失败" + t.toString());
            }
        });
    }


    public void showXinLevel(int i) {
        if (i == 0) {
            mDiscXin1.setVisibility(View.VISIBLE);
            mDiscXin2.setVisibility(View.GONE);
            mDiscXin3.setVisibility(View.GONE);
            mDiscXin4.setVisibility(View.GONE);
            mDiscXin5.setVisibility(View.GONE);
            mIntent1 = new Intent(getActivity(), XinruoActivity.class);
            mTvMaili.setText("脉理: 亚健康一级");
        } else if (i == 1) {
            mDiscXin1.setVisibility(View.GONE);
            mDiscXin2.setVisibility(View.VISIBLE);
            mDiscXin3.setVisibility(View.GONE);
            mDiscXin4.setVisibility(View.GONE);
            mDiscXin5.setVisibility(View.GONE);
            mIntent1 = new Intent(getActivity(), XinhuanActivity.class);
            mTvMaili.setText("脉理: 健康");
        } else if (i == 2) {
            mDiscXin1.setVisibility(View.GONE);
            mDiscXin2.setVisibility(View.GONE);
            mDiscXin3.setVisibility(View.VISIBLE);
            mDiscXin4.setVisibility(View.GONE);
            mDiscXin5.setVisibility(View.GONE);
            mIntent1 = new Intent(getActivity(), XinxianActivity.class);
            mTvMaili.setText("脉理: 亚健康一级");
        } else if (i == 3) {
            mDiscXin1.setVisibility(View.GONE);
            mDiscXin2.setVisibility(View.GONE);
            mDiscXin3.setVisibility(View.GONE);
            mDiscXin4.setVisibility(View.VISIBLE);
            mDiscXin5.setVisibility(View.GONE);
            mIntent1 = new Intent(getActivity(), XinjinActivity.class);
            mTvMaili.setText("脉理: 亚健康二级");
        } else if (i == 4) {
            mDiscXin1.setVisibility(View.GONE);
            mDiscXin2.setVisibility(View.GONE);
            mDiscXin3.setVisibility(View.GONE);
            mDiscXin4.setVisibility(View.GONE);
            mDiscXin5.setVisibility(View.VISIBLE);
            mIntent1 = new Intent(getActivity(), XinshiActivity.class);
            mTvMaili.setText("脉理: 已病");
        } else {
            mIntent1 = new Intent(getActivity(), WeijcActivity.class);
            mDiscXin1.setVisibility(View.GONE);
            mDiscXin2.setVisibility(View.GONE);
            mDiscXin3.setVisibility(View.VISIBLE);
            mDiscXin4.setVisibility(View.GONE);
            mDiscXin5.setVisibility(View.GONE);

        }
    }

    public void showShenLevel(int i) {
        if (i == 0) {
            mDiscShen1.setVisibility(View.VISIBLE);
            mDiscShen2.setVisibility(View.GONE);
            mDiscShen3.setVisibility(View.GONE);
            mDiscShen4.setVisibility(View.GONE);
            mDiscShen5.setVisibility(View.GONE);
            mIntent2 = new Intent(getActivity(), ShenruoActivity.class);
            mTvShenMaili.setText("脉理: 亚健康一级");
        } else if (i == 1) {
            mDiscShen1.setVisibility(View.GONE);
            mDiscShen2.setVisibility(View.VISIBLE);
            mDiscShen3.setVisibility(View.GONE);
            mDiscShen4.setVisibility(View.GONE);
            mDiscShen5.setVisibility(View.GONE);
            mIntent2 = new Intent(getActivity(), ShenhuanActivity.class);
            mTvShenMaili.setText("脉理: 健康");
        } else if (i == 2) {
            mDiscShen1.setVisibility(View.GONE);
            mDiscShen2.setVisibility(View.GONE);
            mDiscShen3.setVisibility(View.VISIBLE);
            mDiscShen4.setVisibility(View.GONE);
            mDiscShen5.setVisibility(View.GONE);
            mIntent2 = new Intent(getActivity(), ShenxianActivity.class);
            mTvShenMaili.setText("脉理: 亚健康一级");
        } else if (i == 3) {
            mDiscShen1.setVisibility(View.GONE);
            mDiscShen2.setVisibility(View.GONE);
            mDiscShen3.setVisibility(View.GONE);
            mDiscShen4.setVisibility(View.VISIBLE);
            mDiscShen5.setVisibility(View.GONE);
            mIntent2 = new Intent(getActivity(), ShenjinActivity.class);
            mTvShenMaili.setText("脉理: 亚健康二级");
        } else if (i == 4) {
            mDiscShen1.setVisibility(View.GONE);
            mDiscShen2.setVisibility(View.GONE);
            mDiscShen3.setVisibility(View.GONE);
            mDiscShen4.setVisibility(View.GONE);
            mDiscShen5.setVisibility(View.VISIBLE);
            mIntent2 = new Intent(getActivity(), ShenshiActivity.class);
            mTvShenMaili.setText("脉理: 已病");
        } else {
            mIntent2 = new Intent(getActivity(), WeijcActivity.class);

        }
    }


    public void showGanLevel(int i) {
        if (i == 0) {
            mDiscGan1.setVisibility(View.VISIBLE);
            mDiscGan2.setVisibility(View.GONE);
            mDiscGan3.setVisibility(View.GONE);
            mDiscGan4.setVisibility(View.GONE);
            mDiscGan5.setVisibility(View.GONE);
            mIntent3 = new Intent(getActivity(), GanruoActivity.class);
            mTvGanMaili.setText("脉理: 亚健康一级");
        } else if (i == 1) {
            mDiscGan1.setVisibility(View.GONE);
            mDiscGan2.setVisibility(View.VISIBLE);
            mDiscGan3.setVisibility(View.GONE);
            mDiscGan4.setVisibility(View.GONE);
            mDiscGan5.setVisibility(View.GONE);
            mIntent3 = new Intent(getActivity(), GanhuanActivity.class);
            mTvGanMaili.setText("脉理: 健康");
        } else if (i == 2) {
            mDiscGan1.setVisibility(View.GONE);
            mDiscGan2.setVisibility(View.GONE);
            mDiscGan3.setVisibility(View.VISIBLE);
            mDiscGan4.setVisibility(View.GONE);
            mDiscGan5.setVisibility(View.GONE);
            mIntent3 = new Intent(getActivity(), GanxianActivity.class);
            mTvGanMaili.setText("脉理: 亚健康一级");
        } else if (i == 3) {
            mDiscGan1.setVisibility(View.GONE);
            mDiscGan2.setVisibility(View.GONE);
            mDiscGan3.setVisibility(View.GONE);
            mDiscGan4.setVisibility(View.VISIBLE);
            mDiscGan5.setVisibility(View.GONE);
            mIntent3 = new Intent(getActivity(), GanjinActivity.class);
            mTvGanMaili.setText("脉理: 亚健康二级");
        } else if (i == 4) {
            mDiscGan1.setVisibility(View.GONE);
            mDiscGan2.setVisibility(View.GONE);
            mDiscGan3.setVisibility(View.GONE);
            mDiscGan4.setVisibility(View.GONE);
            mDiscGan5.setVisibility(View.VISIBLE);
            mIntent3 = new Intent(getActivity(), GanshiActivity.class);
            mTvGanMaili.setText("脉理: 已病");
        } else {
            mIntent3 = new Intent(getActivity(), WeijcActivity.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder1 = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick({R.id.tv_xin_jiexi, R.id.tv_shen_jiexi, R.id.tv_gan_jiexi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_xin_jiexi:
                startActivity(mIntent1);
                break;
            case R.id.tv_shen_jiexi:
                startActivity(mIntent2);
                break;
            case R.id.tv_gan_jiexi:
                startActivity(mIntent3);
                break;
            default:
        }
    }


    //历史记录
    @OnClick(R.id.img_history)
    public void onViewClicked() {
        mIntentHistory = new Intent(getActivity(), HistoryActivity.class);
        startActivity(mIntentHistory);
    }
}
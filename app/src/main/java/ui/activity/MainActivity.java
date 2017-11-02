package ui.activity;

import android.app.FragmentTransaction;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.example.administrator.maihuhang.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import base.BaseActivity;
import butterknife.BindView;
import ui.fragment.MaixFragment;
import ui.fragment.XinlvFragment;
import util.FragmentUtil;


public class MainActivity extends BaseActivity implements OnTabSelectListener {

     /*@BindView(R.id.toolBar)
     Toolbar mToolBar;*/
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.bottomBar)
    BottomBar mBottomBar;


    public String deviceID;

    public int shuzhangya;

    public int shousuoya;

    public String getDeviceID() {
        return deviceID;
    }

    public int getShuzhangya() {
        return shuzhangya;
    }

    public int getShousuoya() {
        return shousuoya;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setShuzhangya(int shuzhangya) {
        this.shuzhangya = shuzhangya;
    }

    public void setShousuoya(int shousuoya) {
        this.shousuoya = shousuoya;
    }

    @Override
    protected void initData() {

        FragmentManager manager = getSupportFragmentManager();
        XinlvFragment xinlvFragment = new XinlvFragment();
        MaixFragment maixFragment = new MaixFragment();
        xinlvFragment.setTargetFragment(maixFragment, XinlvFragment.requestCode);
        manager.beginTransaction().commit();
    }


    @Override
    protected void initListener() {
        mBottomBar.setOnTabSelectListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    /**
     * 底部菜单的选中事件
     *
     * @param tabId
     */

    @Override
    public void onTabSelected(@IdRes int tabId) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        //可以根据tab获取fragment 一般加上
        transaction.replace(R.id.container, FragmentUtil.getInstance().getFragment(tabId), tabId + "");
        transaction.commit();
    }
}

package base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import butterknife.ButterKnife;
import util.LogUtil;
import util.ToastUtil;


/**
 * activity
 * fragment v4
 * fragmentactivity  v4包管理v4包frament
 * actionbarctivity  v7管理3.0之前使用actionbar
 * Appcompatactivity 高版本的v7包  可以管理v4包fragment  actionbar  toolbar
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        //绑定butterknife
        ButterKnife.bind(this);
        //setListener  setAdaper
        initListener();
        //数据初始化
        initData();
        if (hasSoftKeys(getWindowManager())) {

            //有虚拟键的取消状态栏渲染防止底部导航栏被虚拟键遮挡
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            }
        } else {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            }
        }
    }
    public static boolean hasSoftKeys(WindowManager windowManager) {

        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(displayMetrics);
        }

        int realHeight = displayMetrics.heightPixels;

        int realWidth = displayMetrics.widthPixels;

        DisplayMetrics d = new DisplayMetrics();

        display.getMetrics(d);

        int displayHeight = d.heightPixels;

        int displayWidth = d.widthPixels;

        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;

    }

    protected abstract void initData();

    protected abstract void initListener();

    //获取布局id
    protected abstract int getLayoutId();

    //log打印
    protected void logD(String msg) {
        LogUtil.logD(getClass().getSimpleName(), msg);
    }

    //toast
    protected void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.toast(BaseActivity.this, msg);
            }
        });
//        if (Looper.myLooper() == Looper.getMainLooper()) {
//            //主线程
//        } else {
//            //子线程
//            new Handler().post(new Runnable() {
//                @Override
//                public void run() {
//
//                    ToastUtil.toast(this, msg);
//                }
//            })
    }

    /**
     * 进入其他界面
     * @param clz
     * @param finish
     */
    protected void goTo(Class clz, boolean finish) {
        Intent intent = new Intent(this, clz);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }
}

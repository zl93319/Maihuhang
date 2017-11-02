package ui.activity;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.maihuhang.R;

import base.BaseActivity;
import butterknife.BindView;


public class SplashActivity extends BaseActivity implements ViewPropertyAnimatorListener {
    @BindView(R.id.imageView)
    ImageView mImageView;

    @Override
    protected void initData() {
        //缩放动画进入到主界面
        ViewCompat.animate(mImageView).scaleX(1.0f).scaleY(1.0f).setListener(this).setDuration(3000);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onAnimationStart(View view) {

    }

    @Override
    public void onAnimationEnd(View view) {
        //动画结束  进入到主界面
        goTo(MainActivity.class,true);
    }

    @Override
    public void onAnimationCancel(View view) {

    }
}

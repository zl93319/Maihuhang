package ui.activity;

import android.animation.ValueAnimator;

import com.example.administrator.maihuhang.R;

import base.BaseActivity;
import gradient.mylibrary.GradienTextView;
import gradient.mylibrary.Orientation;


/**
 * Created by Administrator on 2017/10/20.
 */

public class WeijcActivity extends BaseActivity {

    private GradienTextView gradienTextView;

    @Override
    protected void initData() {
        gradienTextView = (GradienTextView) findViewById(R.id.gradienTextView);
        gradienTextView.setOrientation(Orientation.LEFT_TO_RIGHT);
        ValueAnimator animator = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = ((Float) animation.getAnimatedValue()).floatValue();

                gradienTextView.setCurrentProgress(value);
            }
        });
        animator.start();

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {

        return R.layout.jcactivity_main;
    }

}

package ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.administrator.maihuhang.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/1.
 */

public class HistoryActivity extends BaseActivity {
    @BindView(R.id.img_Search)
    ImageView mImgSearch;

    private Context mContext;

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_Search)
    public void onViewClicked() {
        //点击跳转搜索页面..
        Intent intent = new Intent(getBaseContext(),SearchActivity.class);
        startActivity(intent);
    }
}

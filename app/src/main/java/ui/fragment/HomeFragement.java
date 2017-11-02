package ui.fragment;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.administrator.maihuhang.R;
import com.itheima.pulltorefreshlib.PullToRefreshBase;
import com.itheima.pulltorefreshlib.PullToRefreshListView;
import com.leon.loopviewpagerlib.FunBanner;

import base.BaseFragment;
import butterknife.BindView;


public class HomeFragement extends BaseFragment {
    @BindView(R.id.content_frame)
    FrameLayout contentFrame;


    @BindView(R.id.listView_deviceList)
    ListView listViewDeviceList;
    private Context mcontext;
    private int[] imageResIds = {R.mipmap.banner, R.mipmap.dw, R.mipmap.banners};

    @Override
    protected View initView() {
        return View.inflate(mContext, R.layout.hone_fragment, null);
    }

    @Override
    protected void initData() {

        FunBanner.Builder builder = new FunBanner.Builder(mContext);
        FunBanner funBanner = builder.setEnableAutoLoop(true)
                .setImageResIds(imageResIds)
                .setDotSelectedColor(Color.GREEN)
                .setHeightWidthRatio(0.5556f)
                .setLoopInterval(2000)
                .setDotNormalColor(Color.GREEN)
                .setDotRadius(4)
                .setTitleColor(Color.RED)
                .setDotSelectedColor(Color.YELLOW)
                .setShowIndicator(true)
                .setIndicatorBarHeight(14)
                .setIndicatorBackgroundColor(Color.BLUE)
                .build();
        contentFrame.addView(funBanner);

        PullToRefreshListView pullToRefreshListView = new PullToRefreshListView(getActivity());
        contentFrame.addView(pullToRefreshListView);

        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
    }


    @Override
    protected void initListener() {
       /* listViewDeviceList.setAdapter(mBaseAdapter);
*/
    }

    private BaseAdapter mBaseAdapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return 30;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_device_list, null);
            }
            return convertView;
        }

    };


}



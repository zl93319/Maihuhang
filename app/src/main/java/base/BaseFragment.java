package base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.components.RxFragment;

import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import util.JudgeNetwork;
import util.LogUtil;
import util.ToastUtil;


public abstract class BaseFragment extends RxFragment {

    protected Context mContext;
    protected Handler mHandler = new Handler();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //记录context
        mContext = getActivity();
        //初始化
        init();
    }


    protected <T> ObservableTransformer<T, T> compose(final LifecycleTransformer<T> lifecycle) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable
                        .subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                // 可添加网络连接判断等
                                if (!JudgeNetwork.isNetworkAvailable(getActivity())) {
                                    Toast.makeText(getActivity(), "网络连接异常，请检查网络", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(lifecycle);
            }
        };
    }
    /**
     * 初始化
     */
    protected void init() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    protected abstract View initView();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //绑定butterknife
        ButterKnife.bind(this,view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListener();
        initData();
    }

    protected abstract void initData();

    protected abstract void initListener();

    //log打印
    protected void logD(String msg) {
        LogUtil.logD(getClass().getSimpleName(), msg);
    }

    //toast
    protected void toast(final String msg) {
        if(Looper.myLooper()==Looper.getMainLooper()){
            ToastUtil.toast(mContext,msg);
        }else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.toast(mContext,msg);
                }
            });
        }
    }
}

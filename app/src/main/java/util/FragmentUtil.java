package util;

import com.example.administrator.maihuhang.R;

import base.BaseFragment;
import ui.fragment.HomeFragement;
import ui.fragment.MaixFragment;
import ui.fragment.SettFragement;
import ui.fragment.XinlvFragment;


public class FragmentUtil {
    private static FragmentUtil mFragmentUtil;
    private HomeFragement mHomeFragment;
    private XinlvFragment mXinlvFragment ;
    private MaixFragment mXintiaoFragment;
    private SettFragement mSettFragement;
    public FragmentUtil() {
    }

    public static FragmentUtil getInstance() {
        if (mFragmentUtil == null) {
            synchronized (FragmentUtil.class) {
                if (mFragmentUtil == null) {
                    mFragmentUtil = new FragmentUtil();
                }
            }
        }
        return mFragmentUtil;
    }

    /**
     * 根据tab的id获取对应的fragmenmt
     * @param tabId
     * @return
     */
    public BaseFragment getFragment(int tabId){
        switch (tabId){
            /*case R.id.tab_home:return getHomeFragment();*/
            case R.id.tab_jaince:
                return getXinlvFragment();
            case R.id.tab_bamai:
                return getXintiaoFragment();
           case R.id.tab_gerenz:
                return getGerenFragent();

           default:
        }
        return null;
    }



    private BaseFragment getGerenFragent() {
        if(mSettFragement==null){
            mSettFragement = new SettFragement();
        }
        return mSettFragement;
    }

    private BaseFragment getXinlvFragment() {
        if(mXinlvFragment==null){
            mXinlvFragment = new XinlvFragment();
        }
        return mXinlvFragment;
    }

    private BaseFragment getXintiaoFragment() {
        if(mXintiaoFragment==null){
            mXintiaoFragment = new MaixFragment();
        }
        return mXintiaoFragment;
    }

    private BaseFragment getHomeFragment() {
        if(mHomeFragment==null){
            mHomeFragment = new HomeFragement();
        }
        return mHomeFragment;
    }
}

package util;

import android.util.Log;


public class LogUtil {
    private static boolean showLog = true;
    public static void logD(String tag,String msg){
        if(showLog) {
            Log.d(tag, msg);
        }
    }
}

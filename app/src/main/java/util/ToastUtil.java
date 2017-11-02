package util;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {

    private static Toast toast;

    public static void toast(Context context, String msg){
        if(toast==null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        }else {
            toast.setText(msg);
        }
        toast.show();
    }
}

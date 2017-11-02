package util;

/**
 * Created by Administrator on 2017/10/16.
 */

public class KivenTool {
    public static void contentHttp(String url, String deviceid, Integer age
            , Integer sex, Double sugar, Integer high, Integer low) {

        HttpRequest_v1.sendGet(url, "deviceid=" + deviceid + "&age=" + age
                + "&sex=" + sex + "&sugar=" + sugar + "&high=" + high + "&low=" + low);
    }

    // send http mai
    public  void submitHttpMai(String deviceId, int age, int sex, int Systolic, int Disstolic) {
        String url = "http://www.maihuhang.com/QinGuo/MaiXiangApp.ashx";
        // suger is default
        double suger = 3.62;

        // http://maihuhang.taidasign.com/QinGuo/a.html
        ToolMethod.contentHttp(url, deviceId, age, sex, suger, Systolic, Disstolic);

        //System.out.println(KivenFileMethod.dataSubmitOk);
    }
}

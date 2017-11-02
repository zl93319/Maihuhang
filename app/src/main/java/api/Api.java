package api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static final String TAG = Api.class.getSimpleName();

    public static final String PARAMETER_TOKEN_ID = "accreditCode";
    public static String TOKEN = "oxADI083ExUN8VWblucNeey8wnlc";

    public static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/maihuhang/cache";// 图片缓存的路径

    public static String SERVER_ADDRESS = "http://www.maihuhang.com";

    public static String MAIX_ADDRESS = "http://www.maihuhang.com";
    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static ApiService apiService;

    public static ProgressDialog progressDialog;


    public static void init() {
        Log.d(TAG, "Api接口初始化");
//        Gson gson = new GsonBuilder()
//                .registerTypeHierarchyAdapter(AppCommonModel.class, new AppCommonModelAdapter<>())
//                .serializeNulls()
//                .create();
        Gson gson = new GsonBuilder().serializeNulls().create();
        //通用拦截器，帮所有接口加上sessionId这个参数
        //自动添加sessionId拦截器
        BasicParamsInterceptor basicParamsInterceptor = new BasicParamsInterceptor();
        //打印retrofit日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG, message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(basicParamsInterceptor)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(MAIX_ADDRESS)
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService service() {
        if (apiService == null) {
            init();
        }
        return apiService;
    }

    public static ApiService service(Context context) {
        if (Api.progressDialog != null) {
            Api.progressDialog.dismiss();
        }
        Api.progressDialog = ProgressDialog.show(context, "", "处理中");
        return service();
    }

    public static void dismissProgressDialog() {
        if (Api.progressDialog != null) {
            Api.progressDialog.dismiss();
        }
    }
}

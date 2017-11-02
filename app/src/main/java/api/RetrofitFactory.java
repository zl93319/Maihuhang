package api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by Administrator on 2017/10/18.
 */

public class RetrofitFactory {

    private static final long TIMEOUT = 30;

    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置【设置请求超时之类】
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)  //30秒
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)  //30秒
            .build();

    private static ApiService retrofitService = new Retrofit.Builder()
            .baseUrl(Api.MAIX_ADDRESS)
            // 添加Gson转换器
            .addConverterFactory(SimpleXmlConverterFactory.create())
            // 添加Retrofit到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(ApiService.class);

    public static ApiService getInstance() {
        return retrofitService;
    }
}


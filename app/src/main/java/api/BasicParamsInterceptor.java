package api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by funcong on 2017-03-16.
 */

public class BasicParamsInterceptor implements Interceptor {

    private String TAG = "BasicParamsInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        String sessionId = Api.TOKEN;
        Request oldRequest = chain.request();

        // 添加新的参数
        HttpUrl.Builder authorizedUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter(Api.PARAMETER_TOKEN_ID, sessionId);

        // 新的请求
        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(authorizedUrlBuilder.build())
                .build();

//        Log.d(TAG, "intercept: 接口请求 " + newRequest.url().toString());

        Response response = chain.proceed(newRequest);

//        Log.d(TAG, "intercept: 接口响应 " + (response.isSuccessful() ? "成功" : "失败"));
//        if (response.isSuccessful()) {
//            MediaType mediaType = response.body().contentType();
//            String content = response.body().string();
//            Log.d("api", "intercept: 响应内容 " + content);
//            return response.newBuilder().body(ResponseBody.create(mediaType, content)).build();
//        }
        return response;
    }
}

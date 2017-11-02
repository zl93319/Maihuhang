package api;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by nimeng01 on 2016/6/15.
 * 若要获得请求所返回的String，则Call的泛型类型只要写为obj类型即可
 */
public interface ApiService {

//    @Multipart
//    @POST("file/upload")
//    Call<StringResult> upload(@Part MultipartBody.Part file);
//
//    @POST("app/login")
//    Call<LoginResult> login(@Query("username") String username, @Query("password") String password);
//
//    @POST("app/register")
//    Call<AppCommonModel> register(@Query("username") String username, @Query("password") String password);


    //    http://www.maihuhang.com/QinGuo/MaiXiangApp.ashx?age=23&sex=1&sugar=3.6&high=128&low=80&deviceid=1232343567
    @GET("/QinGuo/MaiXiangApp.ashx")
    Call<XmlService> listData(@Query("age") int age, @Query("sex") int sex, @Query("sugar") double suagr, @Query("high") int high, @Query("low") int low, @Query("deviceid") String deviceid);

    /**
     * 提交血压
     *
     * @param deviceId
     * @param systolic
     * @param diastolic
     * @return
     */
    @GET("/Pulsehealth/App/Accredit?action=submitbloodpressure")
    Call<String> submitBloodPressure(@Query("deviceid") String deviceId, @Query("systolic") String systolic,
                                     @Query("diastolic") String diastolic);

    /**
     * 提交心率
     *
     * @param deviceId
     * @param heart
     * @return
     */
    @GET("/Pulsehealth/App/Accredit?action=sensor")
    Call<String> submitHeartRate(@Query("deviceid") String deviceId, @Query("heart") String heart);


//    @POST("app/pos/rec/every/month/consume")
//    Call<MonthConsumeList> posrecList();
//
//    @POST("app/user/info/detail")
//    Call<UserInfoResult> userInfo();
//
//    @POST("app/updatePassword")
//    Call<AppCommonModel> changePsw(@Query("password") String password, @Query("newPassword") String newPassword);
//
//    @POST("app/work/order/add")
//    Call<OrderInfoResult> addOrder(@Query("deviceName") String deviceName, @Query("deviceAddress") String deviceAddress, @Query("content") String content, @Query("photo") String photo);
//
//    @POST("app/user/info/bindCard")
//    Call<AppCommonModel> bindCard(@Query("fcardNo") String cardSn, @Query("projectId") String projectId);
//
//    @POST("app/addUserInfo")
//    Call<AppCommonModel> updateUserInfo(@Query("photo") String userPhoto);
//
//    @POST("app/project/detail")
//    Call<ProjectResult> myProjectDetail();
//
//    //==============商家部分===============
//    @GET("app/work/order/myProjectOrderList")
//    Call<ApplyOrderResult> orderOp();
//
//    /*查询登录者下属的项目营业收支列表*/
//    @POST("app/project/income/list")
//    Call<PaymentResult> paymentData(@Query("beginDate") String beginDate, @Query("endDate") String endDate, @Query("projectId") String projectId);
//
//    /*查询营运成本列表*/
//    @POST("app/project/costs/list")
//    Call<CostResult> costData(@Query("beginDate") String beginDate, @Query("endDate") String endDate, @Query("projectId") String projectId);
//
//    @GET("app/work/order/detail")
//    Call<OrderInfoResult> detailOrder(@Query("workOrderId") String workOrderId);
//
//    /**
//     * 用来对工单是由自己处理还是找找找反馈学校，进行操作。
//     *
//     * @param workOrderId
//     * @param handle      =0是自己处理 =1是反馈学校
//     * @return
//     */
//    @GET("app/work/order/handle/direction")
//    Call<OrderInfoResult> workOrderDirection(@Query("workOrderId") String workOrderId, @Query("handleAuthority") int handle);
//
//    @POST("app/project/list")
//    Call<ProjectListResult> myProjectList();
//
//    @POST("app/pos/list")
//    Call<PosListResult> posList(@Query("projectId") Integer projectId);
//
//    @POST("app/article/list")
//    Call<ArticleListResult> articleList();
//
//    @POST("app/employee/sql/server/mycard")
//    Call<EmployeeResult> myCard();
//
//    @POST("app/pos/rec/last/recharge")
//    Call<Posrec.PosrecResult> posrecLastRecharge();

}
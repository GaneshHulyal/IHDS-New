package in.ganeshhulyal.aidatalab.Retrofit.Interfaces;

import in.ganeshhulyal.aidatalab.models.AdminResponseModel;
import in.ganeshhulyal.aidatalab.models.ResponseModel;
import in.ganeshhulyal.aidatalab.models.UserCountDashboard;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AdminServics {

    @FormUrlEncoded
    @POST("admin-api/getCount.php")
    Call<AdminResponseModel> dashboardCount(
            @Field("text") String text
    );

    @FormUrlEncoded
    @POST("admin-api/getEachUserCount.php")
    Call<UserCountDashboard> userDashboardCount(
            @Field("email") String text
    );

    @FormUrlEncoded
    @POST("admin-api/approveUser.php")
    Call<AdminResponseModel> approveUser(
            @Field("email") String text
    );

}
package in.ganeshhulyal.ihds_dc.Retrofit.Interfaces;

import in.ganeshhulyal.ihds_dc.models.UsersModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserModel {
    @FormUrlEncoded
    @POST("test.php")
    Call<ResponseBody> createUser(@Body UsersModel user);
}
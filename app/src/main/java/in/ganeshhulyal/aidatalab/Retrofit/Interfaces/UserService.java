package in.ganeshhulyal.aidatalab.Retrofit.Interfaces;

import org.json.JSONObject;

import in.ganeshhulyal.aidatalab.models.ResponseModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {

    @FormUrlEncoded
    @POST("api/Users/addUser.php")
    Call<ResponseBody> insertUser(
            @Field("firstName") String firstName,
            @Field("lastName") String lastName,
            @Field("mobileNumber") String mobileNumber,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/Users/getUserDetails.php")
    Call<ResponseModel> userLogin(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("fileUpload.php")
    Call<ResponseBody> folders(
            @Field("SubCat") String SubCat,
            @Field("Category") String Category,
            @Field("userName") String userName
    );

}
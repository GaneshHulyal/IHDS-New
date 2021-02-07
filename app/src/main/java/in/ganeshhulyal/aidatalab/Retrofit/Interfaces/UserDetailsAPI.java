package in.ganeshhulyal.aidatalab.Retrofit.Interfaces;

import java.util.List;

import in.ganeshhulyal.aidatalab.models.UserDetailsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserDetailsAPI {

    @POST("/admin-api/getUserDetails.php")
        // API's endpoints
    Call<List<UserDetailsModel>> getUsersList();
}
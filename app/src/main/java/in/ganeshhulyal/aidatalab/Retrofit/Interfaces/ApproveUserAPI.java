package in.ganeshhulyal.aidatalab.Retrofit.Interfaces;

import java.util.List;

import in.ganeshhulyal.aidatalab.models.ModelApproveUser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApproveUserAPI {
    @POST("/admin-api/getApproveUsers.php")
        // API's endpoints
    Call<List<ModelApproveUser>> getUsersList();
}

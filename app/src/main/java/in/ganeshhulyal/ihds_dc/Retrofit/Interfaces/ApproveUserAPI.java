package in.ganeshhulyal.ihds_dc.Retrofit.Interfaces;

import java.util.List;

import in.ganeshhulyal.ihds_dc.models.ModelApproveUser;
import retrofit2.Call;
import retrofit2.http.POST;

public interface ApproveUserAPI {
    @POST("/admin-api/getApproveUsers.php")
        // API's endpoints
    Call<List<ModelApproveUser>> getUsersList();
}

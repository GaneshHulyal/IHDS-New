package in.ganeshhulyal.aidatalab.others;


import in.ganeshhulyal.aidatalab.Retrofit.Interfaces.ApproveUserAPI;
import in.ganeshhulyal.aidatalab.Retrofit.Interfaces.UserDetailsAPI;
import in.ganeshhulyal.aidatalab.models.ModelApproveUser;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AbhiAndroid
 */
public class ApproveUsersAPIClient {
    private static Retrofit retrofit = null;
    public static ApproveUserAPI getClient() {

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://210.212.192.31/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //Creating object for our interface
        ApproveUserAPI api = retrofit.create(ApproveUserAPI.class);
        return api; // return the APIInterface object
    }

}
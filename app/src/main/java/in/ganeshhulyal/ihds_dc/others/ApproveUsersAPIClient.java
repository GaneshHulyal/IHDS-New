package in.ganeshhulyal.ihds_dc.others;


import in.ganeshhulyal.ihds_dc.Retrofit.Interfaces.ApproveUserAPI;
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
                    .baseUrl("http://10.2.0.22/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //Creating object for our interface
        ApproveUserAPI api = retrofit.create(ApproveUserAPI.class);
        return api; // return the APIInterface object
    }

}
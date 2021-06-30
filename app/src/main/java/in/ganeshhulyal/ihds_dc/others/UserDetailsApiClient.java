package in.ganeshhulyal.ihds_dc.others;


import in.ganeshhulyal.ihds_dc.Retrofit.Interfaces.UserDetailsAPI;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AbhiAndroid
 */
public class UserDetailsApiClient {
    private static Retrofit retrofit = null;
    public static UserDetailsAPI getClient() {

        // change your base URL
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.2.0.22/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        //Creating object for our interface
        UserDetailsAPI api = retrofit.create(UserDetailsAPI.class);
        return api; // return the APIInterface object
    }

}
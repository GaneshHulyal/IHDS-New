package in.ganeshhulyal.ihds_dc.others;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.ganeshhulyal.ihds_dc.Retrofit.Interfaces.ApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TemplesAPIClient {

    private static Retrofit retrofit = null;

    public static ApiService getClient() {
        Gson gson = new GsonBuilder().setLenient().create();
       // retrofit = new Retrofit.Builder().baseUrl("http://10.2.0.22/").addConverterFactory(GsonConverterFactory.create(gson)).build();
        // change your base URL
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.2.0.22/")
                    .addConverterFactory(GsonConverterFactory
                            .create(gson))
                    .build();
        }
        //Creating object for our interface
        ApiService api = retrofit.create(ApiService.class);
        return api; // return the APIInterface object
    }
}

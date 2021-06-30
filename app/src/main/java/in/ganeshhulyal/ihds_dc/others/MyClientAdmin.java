package in.ganeshhulyal.ihds_dc.others;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.ganeshhulyal.ihds_dc.Retrofit.Interfaces.AdminServics;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyClientAdmin {
    private static MyClientAdmin myClient;
    private Retrofit retrofit;

    private MyClientAdmin() {
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder().baseUrl("http://10.2.0.22/").addConverterFactory(GsonConverterFactory.create(gson)).build();
    }

    public static synchronized MyClientAdmin getInstance() {
        if (myClient == null) {
            myClient = new MyClientAdmin();
        }
        return myClient;
    }

    public AdminServics getMyApi() {
        return retrofit.create(AdminServics.class);
    }
}

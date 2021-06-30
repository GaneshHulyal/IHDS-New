package in.ganeshhulyal.ihds_dc.others;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import in.ganeshhulyal.ihds_dc.Retrofit.Interfaces.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyClient {
    private static MyClient myClient;
    private Retrofit retrofit;

    private MyClient(){
        Gson gson= new GsonBuilder().setLenient().create();
        retrofit=new Retrofit.Builder().baseUrl("http://10.2.0.22/").addConverterFactory(GsonConverterFactory.create(gson)).build();
    }
    public static synchronized MyClient getInstance(){
        if (myClient==null){
            myClient=new MyClient();
        }
        return myClient;
    }
    public UserService getMyApi(){
        return retrofit.create(UserService.class);
    }
}
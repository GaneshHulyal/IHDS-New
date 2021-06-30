package in.ganeshhulyal.ihds_dc.repos;

import static in.ganeshhulyal.ihds_dc.utils.FileUtil.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.ganeshhulyal.ihds_dc.models.TemplesModel;
import in.ganeshhulyal.ihds_dc.others.TemplesAPIClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitHelper {
    List<String> temples = new ArrayList<String>();

    public List<String> getTemples(Context context) {
        (TemplesAPIClient.getClient().getTempleList()).enqueue(new Callback<List<TemplesModel>>() {
            @Override
            public void onResponse(Call<List<TemplesModel>> call, Response<List<TemplesModel>> response) {
                for (int i = 0; i < response.body().size(); i++) {
                    temples.add(response.body().get(i).getTempleName());
                    Log.d(TAG, "onResponse: " + i);
                    Log.d(TAG, "onResponse: " + response.body().get(i).getTempleName());
                }
            }

            @Override
            public void onFailure(Call<List<TemplesModel>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
        return temples;
    }

    public void setTemples(Context context,String templeName){
        (TemplesAPIClient.getClient().storeTemple(templeName)).enqueue(new Callback<List<TemplesModel>>() {
            @Override
            public void onResponse(Call<List<TemplesModel>> call, Response<List<TemplesModel>> response) {
                Toast.makeText(context,response.body().get(0).getResponseFromServer(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<TemplesModel>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.toString());
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

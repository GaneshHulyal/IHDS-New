package in.ganeshhulyal.aidatalab.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.AdminResponseModel;
import in.ganeshhulyal.aidatalab.models.ModelApproveUser;
import in.ganeshhulyal.aidatalab.others.MyClientAdmin;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApproveUserAdapter extends RecyclerView.Adapter<ApproveUserAdapter.UsersViewHolder> {

    Context context;
    List<ModelApproveUser> userListResponseData;
    private SharedPrefsManager sharedPrefsManager;

    public ApproveUserAdapter(Context context, List<ModelApproveUser> userListResponseData) {
        this.userListResponseData = userListResponseData;
        this.context = context;
        sharedPrefsManager = new SharedPrefsManager(context);
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_list_items_approve, null);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        // set the data
        holder.name.setText(userListResponseData.get(position).getFullName());
        holder.email.setText(userListResponseData.get(position).getEmail());
        if (userListResponseData.get(position).getAgreementStatus().equals("true")) {
            holder.approve.setText("Agreement Upload Status: Completed");
        } else {
            holder.approve.setText("Agreement Upload Status: Incomplete");
        }
        // implement setONCLickListtener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "User Approved.", Toast.LENGTH_SHORT).show();
                Call<AdminResponseModel> call = MyClientAdmin.getInstance().getMyApi().approveUser(userListResponseData.get(position).getEmail());
                holder.approved.setColorFilter(ContextCompat.getColor(context, android.R.color.holo_green_dark),
                        PorterDuff.Mode.MULTIPLY);
                call.enqueue(new Callback<AdminResponseModel>() {
                    @Override
                    public void onResponse(Call<AdminResponseModel> call, Response<AdminResponseModel> response) {
                        Log.d("Msg", response.toString());
                        if (response.isSuccessful()) {
                            Log.d("Msg", "Response is Successful");
                            Toast.makeText(context, "User Approved.", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Msg", "Failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<AdminResponseModel> call, Throwable t) {

                    }
                });
            }
        });
    }

    public void updateList(List<ModelApproveUser> list) {
        userListResponseData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userListResponseData.size(); // size of the list items
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name, email, approve;
        ImageView approved;

        public UsersViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
            approve = itemView.findViewById(R.id.approve);
            approved=itemView.findViewById(R.id.approved);

        }
    }
}
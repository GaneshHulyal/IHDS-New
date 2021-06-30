package in.ganeshhulyal.ihds_dc.adapters;

import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.activities.UserDetails;
import in.ganeshhulyal.ihds_dc.models.UserDetailsModel;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;


public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.UsersViewHolder> {

    Context context;
    List<UserDetailsModel> userListResponseData;
    private SharedPrefsManager sharedPrefsManager;

    public UserDetailsAdapter(Context context, List<UserDetailsModel> userListResponseData) {
        this.userListResponseData = userListResponseData;
        this.context = context;
        sharedPrefsManager=new SharedPrefsManager(context);
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_list_items, null);
        UsersViewHolder usersViewHolder = new UsersViewHolder(view);
        return usersViewHolder;
    }

    @Override
    public void onBindViewHolder(UsersViewHolder holder, final int position) {
        // set the data
        holder.name.setText(userListResponseData.get(position).getUserFullName());
        holder.email.setText(userListResponseData.get(position).getUserEmail());
        // implement setONCLickListtener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with user name
                sharedPrefsManager.saveStringValue("userEmailFromAdapter",userListResponseData.get(position).getUserEmail());
                sharedPrefsManager.saveStringValue("userNameFromAdapter",userListResponseData.get(position).getUserFullName());
                context.startActivity(new Intent(context, UserDetails.class));
            }
        });
    }

    public void updateList(List<UserDetailsModel> list){
        userListResponseData = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userListResponseData.size(); // size of the list items
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name, email;

        public UsersViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
        }
    }
}
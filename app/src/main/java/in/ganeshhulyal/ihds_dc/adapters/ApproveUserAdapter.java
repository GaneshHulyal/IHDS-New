package in.ganeshhulyal.ihds_dc.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.activities.ApproveUserProfile;
import in.ganeshhulyal.ihds_dc.models.ModelApproveUser;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;


public class ApproveUserAdapter extends RecyclerView.Adapter<ApproveUserAdapter.UsersViewHolder> {

    private Context context;
    private List<ModelApproveUser> userListResponseData;
    private boolean agreementUploadStatus;
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
            agreementUploadStatus = true;
        } else {
            holder.approve.setText("Agreement Upload Status: Incomplete");
            agreementUploadStatus = false;
        }
        // implement setONCLickListtener on itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = userListResponseData.get(position).getEmail();
                sharedPrefsManager.saveStringValue("approveUserEmail", userEmail);
                sharedPrefsManager.saveStringValue("userFullName", userListResponseData.get(position).getFullName());
                sharedPrefsManager.saveStringValue("agreementUrl", userListResponseData.get(position).getAgreementUrl());
                context.startActivity(new Intent(context, ApproveUserProfile.class));
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
            approved = itemView.findViewById(R.id.approved);

        }
    }
}
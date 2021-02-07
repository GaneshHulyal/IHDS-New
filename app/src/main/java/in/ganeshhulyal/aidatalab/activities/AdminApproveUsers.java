package in.ganeshhulyal.aidatalab.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.adapters.ApproveUserAdapter;
import in.ganeshhulyal.aidatalab.models.AdminResponseModel;
import in.ganeshhulyal.aidatalab.models.ModelApproveUser;
import in.ganeshhulyal.aidatalab.others.ApproveUsersAPIClient;
import in.ganeshhulyal.aidatalab.others.MyClientAdmin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminApproveUsers extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    RecyclerView recyclerView;
    List<ModelApproveUser> userListResponseData;
    TextInputLayout searcBarLayout;
    TextInputEditText searchInputEditText;
    ApproveUserAdapter usersAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_users);
        context=this;
        toolbarClick();
        backButton();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        try {
            getUserListData();
             // call a method in which we have implement our GET type web API
        }catch(Exception e){
            Toast.makeText(this, "All users are verified.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AdminApproveUsers.this,AdminDashboard.class));
            finish();
        }
        searcBarLayout = findViewById(R.id.search_bar_layout);
        searchInputEditText = findViewById(R.id.search_bar_input);
        searchInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
                filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());
                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Call<List<ModelApproveUser>> call = ApproveUsersAPIClient.getClient().getUsersList();
        call.enqueue(new Callback<List<ModelApproveUser>>() {
            @Override
            public void onResponse(Call<List<ModelApproveUser>> call, Response<List<ModelApproveUser>> response) {
                Log.d("Msg", response.toString());
                if (response.isSuccessful()) {
                    Log.d("Msg", "Response is Successful");
                } else {
                    Log.d("Msg", "Failed");
                }
            }

            @Override
            public void onFailure(Call<List<ModelApproveUser>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        (ApproveUsersAPIClient.getClient().getUsersList()).enqueue(new Callback<List<ModelApproveUser>>() {
            @Override
            public void onResponse(Call<List<ModelApproveUser>> call, Response<List<ModelApproveUser>> response) {
                try {
                    Log.d("responseGET", response.body().get(0).getFullName());
                    userListResponseData = response.body();
                    setDataInRecyclerView();
                } catch (Exception e) {
                    Toast.makeText(AdminApproveUsers.this, "No new users found.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminApproveUsers.this, AdminDashboard.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<ModelApproveUser>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(AdminApproveUsers.this, t.toString(), Toast.LENGTH_LONG).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }


    private void getUserListData() {
        // display a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(AdminApproveUsers.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Please Wait"); // set message
        progressDialog.show(); // show progress dialog


        (ApproveUsersAPIClient.getClient().getUsersList()).enqueue(new Callback<List<ModelApproveUser>>() {
            @Override
            public void onResponse(Call<List<ModelApproveUser>> call, Response<List<ModelApproveUser>> response) {
                try {
                    Log.d("responseGET", response.body().get(0).getFullName());
                    progressDialog.dismiss(); //dismiss progress dialog
                    userListResponseData = response.body();
                    setDataInRecyclerView();
                } catch (Exception e) {
                    Toast.makeText(AdminApproveUsers.this, "No new users found.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AdminApproveUsers.this, AdminDashboard.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<ModelApproveUser>> call, Throwable t) {
                // if error occurs in network transaction then we can get the error in this method.
                Toast.makeText(AdminApproveUsers.this, t.toString(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss(); //dismiss progress dialog
            }
        });
    }

    private void setDataInRecyclerView() {
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AdminApproveUsers.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        // call the constructor of UsersAdapter to send the reference and data to Adapter
        usersAdapter = new ApproveUserAdapter(AdminApproveUsers.this, userListResponseData);
        recyclerView.setAdapter(usersAdapter); // set the Adapter to RecyclerView
    }

    public void filter(String text) {
        List<ModelApproveUser> temp = new ArrayList();
        for (ModelApproveUser d : userListResponseData) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getFullName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        usersAdapter.updateList(temp);
    }
    public void logoutDialogue() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        Toast.makeText(AdminApproveUsers.this, "Logging out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminApproveUsers.this, UserLoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }
    
    private void toolbarClick() {
        ImageView feedback, logout;
        feedback = findViewById(R.id.toolbar_feedback);
        feedback.setVisibility(View.GONE);
        logout = findViewById(R.id.toolbar_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialogue();

            }
        });

        feedback.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Feedback", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }


}
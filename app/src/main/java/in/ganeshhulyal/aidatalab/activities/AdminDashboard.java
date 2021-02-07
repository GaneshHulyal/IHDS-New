package in.ganeshhulyal.aidatalab.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.AdminResponseModel;
import in.ganeshhulyal.aidatalab.others.MyClientAdmin;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDashboard extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private TextView totalUsers;
    private TextView totalImages;
    private TextView totalHumanCentric;
    private TextView totalNonHumanCentric;
    private MaterialButton viewUsers, approveUsers, launchApp;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Context context;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        init();
        initDashboard();
        toolbarClick();
        backButton();
    }

    private void init() {
        totalUsers = findViewById(R.id.noOfPeople);
        totalImages = findViewById(R.id.noOfTotalImages);
        totalHumanCentric = findViewById(R.id.noOfHumanCentric);
        totalNonHumanCentric = findViewById(R.id.noOfNonHumanCentric);
        viewUsers = findViewById(R.id.viewUsers);
        approveUsers = findViewById(R.id.approveUsers);
        launchApp=findViewById(R.id.launchUser);
        launchApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this,UserCategoryActivity.class));
                finish();
            }
        });
        context = this;
        viewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, AdminViewUsers.class));
            }
        });
        approveUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboard.this, AdminApproveUsers.class));

            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void initDashboard() {
        Call<AdminResponseModel> call = MyClientAdmin.getInstance().getMyApi().dashboardCount("text");
        call.enqueue(new Callback<AdminResponseModel>() {
            @Override
            public void onResponse(Call<AdminResponseModel> call, Response<AdminResponseModel> response) {
                Log.d("Msg", response.toString());
                if (response.isSuccessful()) {
                    Log.d("Msg", "Response is Successful");
                    totalUsers.setText(response.body().getTotalUsers());
                    totalHumanCentric.setText(response.body().getTotalHumanCentric());
                    totalImages.setText(response.body().getTotalImages());
                    totalNonHumanCentric.setText(response.body().getTotalNonHumanCentric());
                } else {
                    Log.d("Msg", "Failed");
                }
            }

            @Override
            public void onFailure(Call<AdminResponseModel> call, Throwable t) {

            }
        });
    }

    public void logoutDialogue() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        Toast.makeText(AdminDashboard.this, "Logging out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AdminDashboard.this, UserLoginActivity.class));
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
                if (exit) {
                    finish();
                    moveTaskToBack(true);
                } else {
                    Toast.makeText(context, "Press Back again to Exit.",
                            Toast.LENGTH_SHORT).show();
                    exit = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            exit = false;
                        }
                    }, 3 * 1000);

                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
            moveTaskToBack(true);
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
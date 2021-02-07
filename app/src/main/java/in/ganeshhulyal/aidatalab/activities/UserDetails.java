package in.ganeshhulyal.aidatalab.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.UserCountDashboard;
import in.ganeshhulyal.aidatalab.others.MyClientAdmin;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetails extends AppCompatActivity {
    TextView userName, userEmail, totalImages, textCentric, humanCentric, nonHumanCentric;
    SharedPrefsManager sharedPrefsManager;
    MaterialButton goBack;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        context=this;
        toolbarClick();
        sharedPrefsManager = new SharedPrefsManager(this);
        init();
        backButton();
    }

    private void init() {
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        totalImages = findViewById(R.id.totalUploads);
        textCentric = findViewById(R.id.noOfTextCentric);
        humanCentric = findViewById(R.id.noOfHumanCentric);
        nonHumanCentric = findViewById(R.id.noOfNonHumanCentric);
        goBack = findViewById(R.id.goBack);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserDetails.this, AdminViewUsers.class));
            }
        });
        userName.setText(sharedPrefsManager.getStringValue("userNameFromAdapter", "Ganesh H"));
        userEmail.setText(sharedPrefsManager.getStringValue("userEmailFromAdapter", "abc@gmail.com"));
        String email = sharedPrefsManager.getStringValue("userEmailFromAdapter", "abc@gmail.com");
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        Call<UserCountDashboard> call = MyClientAdmin.getInstance().getMyApi().userDashboardCount(email);
        call.enqueue(new Callback<UserCountDashboard>() {
            @Override
            public void onResponse(Call<UserCountDashboard> call, Response<UserCountDashboard> response) {
                Log.d("Msg", response.toString());
                if (response.isSuccessful()) {
                    Log.d("Msg", "Response is Successful");
                    totalImages.setText(response.body().getTotalImages());
                    humanCentric.setText(response.body().getTotalHumanCentric());
                    textCentric.setText(response.body().getTotalTextCentric());
                    nonHumanCentric.setText(response.body().getTotalNonHumanCentric());
                } else {
                    Log.d("Msg", "Failed");
                }
            }

            @Override
            public void onFailure(Call<UserCountDashboard> call, Throwable t) {

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
                        Toast.makeText(UserDetails.this, "Logging out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UserDetails.this, UserLoginActivity.class));
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
                onBackPressed();
            }
        });


    }

}
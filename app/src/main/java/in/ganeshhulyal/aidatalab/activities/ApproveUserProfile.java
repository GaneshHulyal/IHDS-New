package in.ganeshhulyal.aidatalab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.imageloader.ImageLoader;
import com.budiyev.android.imageloader.ImageUtils;
import com.budiyev.android.imageloader.LoadCallback;
import com.google.android.material.button.MaterialButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URL;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.AdminResponseModel;
import in.ganeshhulyal.aidatalab.others.MyClientAdmin;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApproveUserProfile extends AppCompatActivity {
    private TextView userName, userEmail, userMobile;
    private String userNameString, userEmailString, userMobileString, agreementUrl;
    private AppCompatImageView agreementImageView;
    private SharedPrefsManager sharedPrefsManager;
    private Context context;
    private MaterialButton approveUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_user_profile);
        context = this;
        init();
        toolbarClick();
        backButton();
        try {
            mainMethod();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userMobile = findViewById(R.id.userMobile);
        agreementImageView = findViewById(R.id.agreementImage);
        approveUser = findViewById(R.id.approveUser);
        sharedPrefsManager = new SharedPrefsManager(this);
        userEmailString = sharedPrefsManager.getStringValue("approveUserEmail", "abc@gmaail.com");
        agreementUrl = sharedPrefsManager.getStringValue("agreementUrl", "https://learn.getgrav.org/user/pages/11.troubleshooting/01.page-not-found/error-404.png");
        userNameString = sharedPrefsManager.getStringValue("userFullName", "abc");
    }


    private void approveUser() {
        Call<AdminResponseModel> call = MyClientAdmin.getInstance().getMyApi().approveUser(sharedPrefsManager.getStringValue("approveUserEmail", "abc@gmail.com"));
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

    public void showDialogueDialog() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        Toast.makeText(ApproveUserProfile.this, "Thank you!", Toast.LENGTH_SHORT).show();
                        finish();
                        moveTaskToBack(true);
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


    private void mainMethod() throws IOException {
        agreementUrl = "http://210.212.192.31/" + agreementUrl;
        Log.d("Msg", "mainMethod: " + agreementUrl);
        URL url = new URL(agreementUrl);
        ImageLoader.with(this)
                .from(agreementUrl)
                .size(1000, 1000)
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .errorDrawable(R.drawable.not_found)
                .transform(ImageUtils.cropCenter())
                .transform(ImageUtils.convertToGrayScale())
                .load(agreementImageView);

        userEmail.setText(userEmailString);
        userName.setText(userNameString);

        approveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "User Approved Successfully!", Toast.LENGTH_SHORT).show();
                approveUser();
                startActivity(new Intent(ApproveUserProfile.this, AdminApproveUsers.class));
                finish();
            }
        });
    }

    private void toolbarClick() {
        ImageView feedback, logout;
        feedback = findViewById(R.id.toolbar_feedback);
        feedback.setVisibility(View.GONE);
        logout = findViewById(R.id.toolbar_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogueDialog();

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
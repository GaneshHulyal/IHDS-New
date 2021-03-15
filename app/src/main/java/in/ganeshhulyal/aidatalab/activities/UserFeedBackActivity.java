package in.ganeshhulyal.aidatalab.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.ResponseModel;
import in.ganeshhulyal.aidatalab.others.MyClient;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFeedBackActivity extends AppCompatActivity {
    private Button UserFeedBackActivityButton, btnExit;
    private TextInputLayout emailLayout;
    private TextInputEditText emailField;
    private SharedPrefsManager sharedPrefsManager;
    private String userEmail, feedback;
    private Context context;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        context = this;
        init();
        emailField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    GradientDrawable drawable = (GradientDrawable)emailField.getBackground();
//                    drawable.setStroke(2, Color.RED);
                    emailField.setHintTextColor(Color.RED);
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogueDialog();
            }
        });
        UserFeedBackActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = emailField.getText().toString();
                if(feedback.isEmpty()){
                    emailField.setError("Please Enter Feedback");
                }else {
                    storeFeedback();
                }
            }
        });
    }

    public void  showDialogueDialog() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        Toast.makeText(UserFeedBackActivity.this, "Thank you!", Toast.LENGTH_SHORT).show();
                        finish();
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton("No", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        startActivity(new Intent(UserFeedBackActivity.this, UserCategoryActivity.class));
                        finish();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    private void init() {
        sharedPrefsManager = new SharedPrefsManager(this);
        userEmail = sharedPrefsManager.getStringValue("userEmail", "abc@gmail.com");
        UserFeedBackActivityButton=findViewById(R.id.feedback_button);
        emailLayout = findViewById(R.id.feedback_layout);
        emailField = findViewById(R.id.feedback_field);
        btnExit = findViewById(R.id.exit_button);
    }

    private void storeFeedback() {
        Call<ResponseModel> call = MyClient.getInstance().getMyApi().addFeedBack(userEmail, feedback);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.d("Msg", response.toString());
                if (response.isSuccessful()) {
                    Toast.makeText(UserFeedBackActivity.this, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                    moveTaskToBack(true);
                    finish();
                } else {
                    Log.d("Msg", "Failed");
                    Toast.makeText(UserFeedBackActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(UserFeedBackActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            showDialogueDialog();
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
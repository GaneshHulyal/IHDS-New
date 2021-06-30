package in.ganeshhulyal.ihds_dc.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.models.ResponseModel;
import in.ganeshhulyal.ihds_dc.others.MyClient;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatePassword extends AppCompatActivity {
    private Button submitFeedbackBtn, btnExit;
    private TextInputLayout feedbackLayout;
    private TextInputEditText feedbackField;
    private SharedPrefsManager sharedPrefsManager;
    private String userEmail, feedback;
    private TextView message;
    private Context context;
    private boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        context = this;
        init();
        feedbackField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    GradientDrawable drawable = (GradientDrawable)feedbackField.getBackground();
//                    drawable.setStroke(2, Color.RED);
                    feedbackField.setHintTextColor(Color.RED);
                }
            }
        });
        submitFeedbackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback = feedbackField.getText().toString();
                message.setText("Password reset link sent to " + feedback);
                if (feedback.isEmpty()) {
                    feedbackField.setError("Please Enter Feedback");
                } else {
                    storeFeedback();
                }
            }
        });
    }

    private void init() {
        sharedPrefsManager = new SharedPrefsManager(this);
        userEmail = sharedPrefsManager.getStringValue("userEmail", "abc@gmail.com");
        submitFeedbackBtn = findViewById(R.id.feedback_button);
        feedbackLayout = findViewById(R.id.feedback_layout);
        feedbackField = findViewById(R.id.feedback_field);
        btnExit = findViewById(R.id.exit_button);
        message = findViewById(R.id.message);
    }

    private void storeFeedback() {
        Call<ResponseModel> call = MyClient.getInstance().getMyApi().resetPassword(feedback);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                Log.d("Msg", response.toString());
                if (response.isSuccessful()) {

                } else {
                    Log.d("Msg", "Failed");
                    //Toast.makeText(UpdatePassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Toast.makeText(UpdatePassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
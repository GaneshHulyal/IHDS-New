package in.ganeshhulyal.aidatalab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.others.MyClient;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegistration extends AppCompatActivity {

    private static final String TAG = "MSG";
    private TextInputEditText userName, userEmail, userUSN, userPassword, userConfirmPassword, userMobileNumber, userOTP;
    private TextView toolbarName;
    private TextInputLayout userNameLayout, userEmailLayout, userUSNLayout, userPasswordLayout, userConfirmationLayout, userMobileNumberLauout, sendOTPLayout, userOTPLayout;
    private String userNameString, userEmailString, userConfirmPasswordString, userUSNString, userPasswordString, userMobileString;
    private Button registerButton, sendOTP;
    private DatabaseReference mDatabase;
    String verificationCodeBySystem;
    protected SharedPrefsManager SharedPrefs;
    private SharedPrefsManager sharedPrefsManager;
    private FirebaseAuth mAuth;
    LottieAnimationView progressBar;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        sharedPrefsManager=new SharedPrefsManager(this);
        init();
        initToolbar();
        backButton();
        sendOTP();
        verifyOTP();
    }

    private void verifyOTP() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=userOTP.getText().toString();
                if (otp.isEmpty() || otp.length() < 6) {
                    userOTPLayout.setError("Wrong OTP...");
                    userOTPLayout.requestFocus();
                    return;
                }
                else{
                    verifyCode(otp);
                }
            }
        });
    }

    private void sendOTP() {
        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                number = userMobileNumber.getText().toString();
                if (number.length() < 10 || number.length() > 10) {
                    Toast.makeText(UserRegistration.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                    userMobileNumberLauout.setError("Invalid Mobile Number");
                } else {
                    phoneAuth(number);
                }
            }
        });
    }

    private void phoneAuth(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91" + number)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationCodeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                    Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                    String code = phoneAuthCredential.getSmsCode();
                    System.out.println(code);
                    if (code != null) {
                        //progressBarOTP.setVisibility(View.VISIBLE);
                        verifyCode(code);
                    } else {
                        System.out.print("No OTP");
                    }
                }

                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(UserRegistration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    private void verifyCode(String codeByUser) {
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
            signInTheUserByCredentials(credential);
        } catch (Exception e) {
            userOTPLayout.setError("Invalid OTP!");
        }

    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(UserRegistration.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            sharedPrefsManager.saveStringValue("mobileNumber",number);
                            Toast.makeText(UserRegistration.this, "Your account has been created successfully!", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In or do something required
                            mainMethod();

                        } else {
                            Toast.makeText(UserRegistration.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            userOTPLayout.setError(task.getException().getMessage());
                            sharedPrefsManager.saveBoolValue("User", false);
                        }
                    }
                });
    }


    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserRegistration.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void mainMethod() {
                if (inputValidation()) {
                    Call<ResponseBody> call = MyClient.getInstance().getMyApi().insertUser(userNameString, userUSNString, userMobileString, userEmailString, userPasswordString);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Toast.makeText(UserRegistration.this, "User Created Successfully!", Toast.LENGTH_SHORT).show();
                            SharedPrefs.saveStringValue("userEmail", userEmailString);
                            startActivity(new Intent(UserRegistration.this, LoginActivity.class));
                            finish();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(UserRegistration.this, "User Creation Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
    }

    private boolean inputValidation() {
        userNameString = userName.getText().toString();
        userEmailString = userEmail.getText().toString();
        userConfirmPasswordString = userConfirmPassword.getText().toString();
        userUSNString = userUSN.getText().toString();
        userPasswordString = userPassword.getText().toString();
        userMobileString = userMobileNumber.getText().toString();
        if (userNameString.length() < 3 || userNameString.isEmpty()) {
            userNameLayout.setError("Invalid Input");
            return false;
        } else if (userMobileString.length() < 10 || userMobileString.isEmpty()) {
            userMobileNumberLauout.setError("Invalid Mobile");
            return false;
        } else if (!userPasswordString.equals(userConfirmPasswordString) || userPasswordString.isEmpty()) {
            userPasswordLayout.setError("Password not matched");
            return false;
        } else if (userPasswordString.length() < 3 || userPasswordString.isEmpty()) {
            userPasswordLayout.setError("Password should be Minimum 3 Letters");
            return false;
        } else {
            return true;
        }
    }

    private void initToolbar() {
        toolbarName.setText("User Registration");
    }

    private void init() {
        toolbarName = findViewById(R.id.toolbar_name);
        userName = findViewById(R.id.fullName);
        userEmail = findViewById(R.id.userEmail);
        userUSN = findViewById(R.id.userUSN);
        userOTP=findViewById(R.id.userOTP);
        userOTPLayout=findViewById(R.id.userOtpLayout);
        userPassword = findViewById(R.id.userPassword);
        userConfirmPassword = findViewById(R.id.userConfirmPassword);
        userNameLayout = findViewById(R.id.fullNameLayout);
        userEmailLayout = findViewById(R.id.userEmailLayout);
        userUSNLayout = findViewById(R.id.userUSNLayout);
        userPasswordLayout = findViewById(R.id.userPasswordLayout);
        userConfirmationLayout = findViewById(R.id.userConfirmPasswordLayout);
        registerButton = findViewById(R.id.registerButton);
        sendOTP = findViewById(R.id.requestOTP);
        sendOTPLayout = findViewById(R.id.userOtpLayout);
        userMobileNumber = findViewById(R.id.userMobileNumber);
        progressBar=findViewById(R.id.progress_bar);

        //Initialize Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Initialize Shared Preferences
        SharedPrefs = new SharedPrefsManager(this);

        //Initialize Firebase Instance
        mAuth = FirebaseAuth.getInstance();

    }
}
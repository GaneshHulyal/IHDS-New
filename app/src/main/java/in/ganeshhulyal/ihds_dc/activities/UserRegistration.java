package in.ganeshhulyal.ihds_dc.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.models.ResponseModel;
import in.ganeshhulyal.ihds_dc.others.MyClient;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;
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
    private String verificationCodeBySystem;
    protected SharedPrefsManager SharedPrefs;
    private SharedPrefsManager sharedPrefsManager;
    private FirebaseAuth mAuth;
    private LottieAnimationView progressBar;
    private String number;
    private boolean isEmailExist, isMobileNumberExist;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        sharedPrefsManager = new SharedPrefsManager(this);
        context=this;
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
                String otp = userOTP.getText().toString();
                if (otp.isEmpty() || otp.length() < 6) {
                    userOTPLayout.setError("Wrong OTP...");
                    userOTPLayout.requestFocus();
                    return;
                } else {
                    verifyCode(otp);
                }
            }
        });
    }

    private void sendOTP() {
        try {
            sendOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        number = userMobileNumber.getText().toString();
                    } catch (Exception e) {
                        Toast.makeText(UserRegistration.this, "Enter MobileNumber First!", Toast.LENGTH_SHORT).show();
                    }
                    if (number.length() < 10 || number.length() > 10) {
                        Toast.makeText(UserRegistration.this, "Invalid Number!", Toast.LENGTH_SHORT).show();
                        userMobileNumberLauout.setError("Invalid Mobile Number");
                    } else if (!inputValidation()) {


                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        phoneAuth(number);
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "OTP Already Sent!", Toast.LENGTH_SHORT).show();
        }
    }

    private void toolbarClick() {
        ImageView feedback,logout;
        feedback=findViewById(R.id.toolbar_feedback);
        logout=findViewById(R.id.toolbar_logout);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserRegistration.this, "Please Login...", Toast.LENGTH_SHORT).show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserRegistration.this, "Please Login...", Toast.LENGTH_SHORT).show();
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

                            sharedPrefsManager.saveStringValue("mobileNumber", number);
                            Log.d("Msg", "User Added");
                            sharedPrefsManager.saveBoolValue("isFromRegister",true);
                            Toast.makeText(UserRegistration.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                            userOTP.setText("OTP Verified");
                            userOTP.setTextColor(getResources().getColor(R.color.green));
                            userOTPLayout.setEndIconDrawable(R.drawable.ic_baseline_check_circle_24);
                            mainMethod();
                            //Toast.makeText(UserRegistration.this, "Your account has been created successfully!", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In or do something required

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
                startActivity(new Intent(UserRegistration.this, DownloadAgreementActivity.class));
                finish();
            }
        });
    }

    private void mainMethod() {
        if (inputValidation()) {
            Log.d("Msg", "Main Method Called");
            Call<ResponseModel> call = MyClient.getInstance().getMyApi().insertUser(userNameString, userUSNString, userMobileString, userEmailString, userPasswordString);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    Log.d("Msg", response.toString());
                    if (response.isSuccessful()) {
                        Log.d("Msg", "Response is Successful");
                        if (response.body().isAuthenticated()) {
                            Log.d("Msg", "Already Exist");
                            Toast.makeText(UserRegistration.this, "User account already exist!", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(UserRegistration.this, LoginActivity.class))

                        } else {
                            SharedPrefs.saveStringValue("userEmail", userEmailString);
                            new SharedPrefsManager(context).saveBoolValue("isAllAgreementUploaded", false);
                            Runnable r = new Runnable() {

                                @Override
                                public void run() {
                                    // if you are redirecting from a fragment then use getActivity() as the context.
                                    startActivity(new Intent(UserRegistration.this, UserLoginActivity.class));
                                    finish();

                                }
                            };
                            Handler h = new Handler();
                            h.postDelayed(r, 2000);
                        }
                    } else {
                        Log.d("Msg", "Failed");
                        Toast.makeText(UserRegistration.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Toast.makeText(UserRegistration.this, "User Creation Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean inputValidation() {
        userNameString = userName.getText().toString();
        userEmailString = userEmail.getText().toString().trim();
        userConfirmPasswordString = userConfirmPassword.getText().toString();
        userUSNString = userUSN.getText().toString();
        userPasswordString = userPassword.getText().toString();
        userMobileString = userMobileNumber.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (userNameString.length() < 3 || userNameString.isEmpty()) {
            userNameLayout.setError("Invalid Input");
            return false;
        } else {
            userNameLayout.setError(null);
        }
        if (isEmailExist || !emailValidator(userEmailString)) {
            userEmailLayout.setError("Invalid Email");
            return false;
        } else {
            userEmailLayout.setError(null);
        }
        if (userMobileString.length() < 10 || userMobileString.isEmpty() || isMobileNumberExist) {
            userMobileNumberLauout.setError("Invalid Mobile");
            return false;
        } else {
            userMobileNumberLauout.setError(null);
        }
        if (!userPasswordString.equals(userConfirmPasswordString) || userPasswordString.isEmpty()) {
            userPasswordLayout.setError("Password not matched");
            return false;
        } else {
            userPasswordLayout.setError(null);
        }
        if (userPasswordString.length() < 3 || userPasswordString.isEmpty()) {
            userPasswordLayout.setError("Password should be Minimum 3 Letters");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserRegistration.this, UserLoginActivity.class));
        finish();

    }


    private void initToolbar() {
        toolbarName.setText("User Registration");
    }

    private void init() {
        ScrollView view = (ScrollView)findViewById(R.id.scrollView);
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        toolbarName = findViewById(R.id.toolbar_name);
        userName = findViewById(R.id.fullName);
        userEmail = findViewById(R.id.userEmail);
        userUSN = findViewById(R.id.userUSN);
        userOTP = findViewById(R.id.userOTP);
        userOTPLayout = findViewById(R.id.userOtpLayout);
        userPassword = findViewById(R.id.userPassword);
        userConfirmPassword = findViewById(R.id.userConfirmPassword);
        userNameLayout = findViewById(R.id.fullNameLayout);
        userEmailLayout = findViewById(R.id.userEmailLayout);
        userMobileNumberLauout = findViewById(R.id.userMobileNumberLayout);
        userUSNLayout = findViewById(R.id.userUSNLayout);
        userPasswordLayout = findViewById(R.id.userPasswordLayout);
        userConfirmationLayout = findViewById(R.id.userConfirmPasswordLayout);
        registerButton = findViewById(R.id.registerButton);
        sendOTP = findViewById(R.id.requestOTP);
        sendOTPLayout = findViewById(R.id.userOtpLayout);
        userMobileNumber = findViewById(R.id.userMobileNumber);
        progressBar = findViewById(R.id.progress_bar);

        //Initialize Database Reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Initialize Shared Preferences
        SharedPrefs = new SharedPrefsManager(this);

        //Initialize Firebase Instance
        mAuth = FirebaseAuth.getInstance();

        userEmail.addTextChangedListener(
                new TextWatcher() {
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    private Timer timer=new Timer();
                    private final long DELAY = 1000; // milliseconds

                    @Override
                    public void afterTextChanged(final Editable s) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        userEmailString = userEmail.getText().toString().trim();


                                        Call<ResponseModel> call = MyClient.getInstance().getMyApi().checkEmail(userEmailString);
                                        call.enqueue(new Callback<ResponseModel>() {
                                            @Override
                                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                Log.d("Msg", response.toString());
                                                if (response.isSuccessful()) {
                                                    Log.d("Msg", "Response is Successful");
                                                    if (response.body().isEmailExist()) {
                                                        Log.d("Msg", "Already Exist");
                                                        userEmailLayout.setError("Email Already Exist");
                                                        userMobileNumber.setEnabled(false);
                                                        userMobileNumber.setFocusable(false);
                                                        userPassword.setEnabled(false);
                                                        userPassword.setFocusable(false);
                                                        userConfirmPassword.setEnabled(false);
                                                        userConfirmPassword.setFocusable(false);
                                                        userOTP.setEnabled(false);
                                                        userOTP.setFocusable(false);
                                                        sendOTP.setEnabled(false);
                                                        registerButton.setEnabled(false);
                                                        isEmailExist = true;
                                                    } else {
                                                        userEmailLayout.setError(null);
                                                        userMobileNumber.setEnabled(true);
                                                        userMobileNumber.setFocusableInTouchMode(true);
                                                        userPassword.setEnabled(true);
                                                        userPassword.setFocusableInTouchMode(true);
                                                        userConfirmPassword.setEnabled(true);
                                                        userConfirmPassword.setFocusableInTouchMode(true);
                                                        userOTP.setEnabled(true);
                                                        userOTP.setFocusableInTouchMode(true);
                                                        sendOTP.setEnabled(true);
                                                        registerButton.setEnabled(true);
                                                        isEmailExist = false;
                                                    }
                                                } else {
                                                    Log.d("Msg", "Failed");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                            }
                                        });


                                    }
                                },
                                DELAY
                        );
                    }
                }
        );

        userMobileNumber.addTextChangedListener(
                new TextWatcher() {
                    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
                    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    private Timer timer=new Timer();
                    private final long DELAY = 1000; // milliseconds

                    @Override
                    public void afterTextChanged(final Editable s) {
                        timer.cancel();
                        timer = new Timer();
                        timer.schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        userMobileString = userMobileNumber.getText().toString();

                                        Call<ResponseModel> call = MyClient.getInstance().getMyApi().checkMobileNumber(userMobileString);
                                        call.enqueue(new Callback<ResponseModel>() {
                                            @Override
                                            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                                                Log.d("Msg", response.toString());
                                                if (response.isSuccessful()) {
                                                    Log.d("Msg", "Response is Successful");
                                                    if (response.body().isMobileExist()) {
                                                        Log.d("Msg", "Already Exist");
                                                        userMobileNumberLauout.setError("Mobile Number Already Exist");
                                                        userPassword.setEnabled(false);
                                                        userPassword.setFocusable(false);
                                                        userConfirmPassword.setEnabled(false);
                                                        userConfirmPassword.setFocusable(false);
                                                        userOTP.setEnabled(false);
                                                        userOTP.setFocusable(false);
                                                        sendOTP.setEnabled(false);
                                                        registerButton.setEnabled(false);
                                                        isMobileNumberExist = true;
                                                    } else {
                                                        userMobileNumberLauout.setError(null);
                                                        userPassword.setEnabled(true);
                                                        userPassword.setFocusableInTouchMode(true);
                                                        userConfirmPassword.setEnabled(true);
                                                        userConfirmPassword.setFocusableInTouchMode(true);
                                                        userOTP.setEnabled(true);
                                                        userOTP.setFocusableInTouchMode(true);
                                                        sendOTP.setEnabled(true);
                                                        registerButton.setEnabled(true);
                                                        isMobileNumberExist = false;
                                                    }
                                                } else {
                                                    Log.d("Msg", "Failed");
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<ResponseModel> call, Throwable t) {
                                            }
                                        });


                                    }
                                },
                                DELAY
                        );
                    }
                }
        );

//        userMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//
//            }
//        });

        userPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }

        });

    }

}
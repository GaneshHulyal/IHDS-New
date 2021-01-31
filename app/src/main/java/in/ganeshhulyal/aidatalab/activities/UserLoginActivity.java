package in.ganeshhulyal.aidatalab.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.balsikandar.crashreporter.CrashReporter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.TimeUnit;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.ResponseModel;
import in.ganeshhulyal.aidatalab.others.MyClient;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoginActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private TextInputEditText userEmailEdit, userPasswordEdit;
    private TextView toolbarName;
    private TextInputLayout userEmailLayout, userPasswordLayout;
    private String Email, Password;
    private Button loginButton, backToRegister;
    private String email;
    private SharedPrefsManager sharedPrefsManager;
    private TextView resetPassword;
    private SharedPrefsManager SharedPref;
    private FirebaseAuth mAuth;
    private boolean exit = false;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        init();
///        initToolbar();
        main();

        if (checkPermission()) {
            Log.d("Msg", "Permission alreday granted");
        } else {
            requestPermission();
        }
        if (sharedPrefsManager.getBoolValue("isLoggedIn", false)) {
            //startActivity(new Intent(LoginActivity.this, AgreementActivity.class));
            //finish();
        }

    }


    private void main() {
        Log.d("Msg", Email + " " + Password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = userEmailEdit.getText().toString().trim();
                Password = userPasswordEdit.getText().toString().trim();
                if (Email.isEmpty() || Email.length() < 5) {
                    userEmailLayout.setError("Invalid Email");
                } else {
                    userEmailLayout.setError(null);
                }
                if (Password.isEmpty()) {
                    userPasswordLayout.setError("Invalid Password");
                } else {
                    userPasswordLayout.setError(null);
                    Call<ResponseModel> call = MyClient.getInstance().getMyApi().userLogin(Email, Password);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            Log.d("Msg", response.toString());
                            if (response.isSuccessful()) {
                                if (response.body().isAuthenticated()) {
                                    if (response.body().isVerified()) {
                                        Toast.makeText(UserLoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                        sharedPrefsManager.saveStringValue("userEmail", response.body().getEmail());
                                        sharedPrefsManager.saveBoolValue("isLoggedIn", true);
                                        sharedPrefsManager.saveBoolValue("isFromRegister", false);
                                        sharedPrefsManager.saveLongValue("ExpiredDate", System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(1));
                                        startActivity(new Intent(UserLoginActivity.this, UploadAgreementActivity.class));
                                        finish();
                                    } else {
                                        Toast.makeText(UserLoginActivity.this, "Waiting for admin approval.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(UserLoginActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(UserLoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Log.e("Msg", t.toString());
                            Toast.makeText(UserLoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


//                        mAuth.signInWithEmailAndPassword(Email, Password)
//                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        if (!task.isSuccessful()) {
//                                            // there was an error
//                                            if (Password.length() < 6) {
//                                                userPasswordLayout.setError("Invalid Password!");
//                                            } else {
//                                                Toast.makeText(LoginActivity.this, "Authentication Failed!", Toast.LENGTH_LONG).show();
//                                            }
//                                        } else {
//                                            SharedPref.saveStringValue("userEmail",Email);
//                                            Intent intent = new Intent(LoginActivity.this, AgreementActivity.class);
//                                            startActivity(intent);
//                                            finish();
//                                        }
//                                    }
//                                });
//
            }
        });


    }


    private void init() {
        try {
            // Do your stuff
        } catch (Exception e) {
            CrashReporter.logException(e);
        }

        //toolbarName = findViewById(R.id.toolbar_name);
        userPasswordLayout = findViewById(R.id.userPasswordLayout);
        userEmailLayout = findViewById(R.id.userUSNLayout);
        userEmailEdit = findViewById(R.id.userEmail);
        userPasswordEdit = findViewById(R.id.userPassword);
        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();
        backToRegister = findViewById(R.id.backToRegister);
        resetPassword = findViewById(R.id.resendPassword);
        SharedPref = new SharedPrefsManager(this);
        sharedPrefsManager = new SharedPrefsManager(this);
        backToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefsManager.saveBoolValue("isFromLogin", true);
                startActivity(new Intent(UserLoginActivity.this, DownloadAgreementActivity.class));
            }
        });
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(UserLoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(UserLoginActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(UserLoginActivity.this, "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(UserLoginActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (exit)
            moveTaskToBack(true);
        else {
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


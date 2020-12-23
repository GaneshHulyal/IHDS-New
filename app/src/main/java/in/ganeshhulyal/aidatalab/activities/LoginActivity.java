package in.ganeshhulyal.aidatalab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.Locale;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.ResponseModel;
import in.ganeshhulyal.aidatalab.others.MyClient;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText userEmailEdit, userPasswordEdit;
    private TextView toolbarName;
    private TextInputLayout userEmailLayout, userPasswordLayout;
    private String Email, Password;
    Button loginButton, backToRegister;
    private String email;
    SharedPrefsManager sharedPrefsManager;
    TextView resetPassword;
    SharedPrefsManager SharedPref;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initToolbar();
        main();
        backButton();
    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, UserRegistration.class));
                finish();
            }
        });
    }

    private void main() {
        Log.d("Msg", Email + " " + Password);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = userEmailEdit.getText().toString();
                Password = userPasswordEdit.getText().toString();
                if (Email.isEmpty() || Email.length() < 5) {
                    userEmailLayout.setError("Invalid Email");
                } else if (Password.isEmpty()) {
                    userPasswordLayout.setError("Invalid Password");
                } else {
                    Call<ResponseModel> call = MyClient.getInstance().getMyApi().userLogin(Email, Password);
                    call.enqueue(new Callback<ResponseModel>() {
                        @Override
                        public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                            Log.d("Msg", response.toString());
                            if (response.isSuccessful()) {
                                if (response.body().isAuthenticated()) {
                                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                                    sharedPrefsManager.saveStringValue("userEmail", response.body().getEmail());
                                    sharedPrefsManager.saveBoolValue("isLoggedIn", true);
                                    startActivity(new Intent(LoginActivity.this, AgreementActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "Something went wrong" + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseModel> call, Throwable t) {
                            Log.e("Msg", t.toString());
                            Toast.makeText(LoginActivity.this, "No Users Found!", Toast.LENGTH_SHORT).show();
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


    private void initToolbar() {
        toolbarName.setText("Login");
    }

    private void init() {
        toolbarName = findViewById(R.id.toolbar_name);
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
                startActivity(new Intent(LoginActivity.this, UserRegistration.class));
                finish();
            }
        });
    }
}
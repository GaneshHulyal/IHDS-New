package in.ganeshhulyal.ihds_dc.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shreyaspatil.MaterialDialog.MaterialDialog;

import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.databinding.ActivityClassNameBinding;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;
import in.ganeshhulyal.ihds_dc.repos.RetrofitHelper;

public class UserCategoryActivity extends AppCompatActivity {

    private Button addClass;
    private AutoCompleteTextView categoryName, subcategoryName;
    protected String categoryString, subcategoryString;
    private ImageView backButton;
    private Context context;
    private boolean exit = false;
    private RetrofitHelper retrofitHelper;
    private SharedPrefsManager sharedPrefsManager;
    private ActivityClassNameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityClassNameBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        context = this;
        retrofitHelper=new RetrofitHelper();
        if (ContextCompat.checkSelfPermission(UserCategoryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(UserCategoryActivity.this, Manifest.permission_group.MICROPHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.MICROPHONE}, 100);

        }
        toolbarClick();
        initToolbar();
        backButton();
        init();
        storeTemples();
    }

    private void storeTemples() {
        binding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.addTempleLayout.setVisibility(View.VISIBLE);
            }
        });
        binding.addTemple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.templeName.getText().toString().isEmpty()){
                    binding.templeNameLayout.setError("Please enter temple name");
                }else{
                    String templeName=binding.templeName.getText().toString();
                    retrofitHelper.setTemples(context,templeName);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        ArrayAdapter catAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        retrofitHelper.getTemples(this));

        AutoCompleteTextView editTextFilledExposedDropdownCat =
                categoryName.findViewById(R.id.category);
        editTextFilledExposedDropdownCat.setAdapter(catAdapter);


    }

    private void init() {
        addClass = findViewById(R.id.addClass);
        categoryName = findViewById(R.id.category);
        sharedPrefsManager=new SharedPrefsManager(this);
        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryString = categoryName.getText().toString();
                if (categoryString.isEmpty()) {
                    categoryName.setError("Invalid Input");
                } else {
                    Intent intent = new Intent(UserCategoryActivity.this, UserMenu.class);
                    sharedPrefsManager.saveStringValue("categoryName", categoryString);
                    sharedPrefsManager.saveStringValue("subCategoryName", "");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void initToolbar() {
        TextView toolbarName;
        toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setText("Categories");
    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCategoryActivity.this,UserDashboard.class));
                finish();
            }
        });
    }

    public void  logoutDialogue() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        Toast.makeText(UserCategoryActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                        sharedPrefsManager.saveStringValue("userEmail", null);
                        sharedPrefsManager.saveBoolValue("isLoggedIn", false);
                        startActivity(new Intent(UserCategoryActivity.this,UserLoginActivity.class));
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
        ImageView feedback,logout;
        feedback=findViewById(R.id.toolbar_feedback);
        logout=findViewById(R.id.toolbar_logout);
        feedback.setVisibility(View.VISIBLE);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCategoryActivity.this, UserFeedBackActivity.class));
                finish();

            }
        });

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

    @Override
    public void onBackPressed() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserCategoryActivity.this,UserDashboard.class));
                finish();
            }
        });

    }

}

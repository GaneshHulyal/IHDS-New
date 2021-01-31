package in.ganeshhulyal.aidatalab.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;

public class UserCategoryActivity extends AppCompatActivity {

    private Button addClass;
    private AutoCompleteTextView categoryName, subcategoryName;
    protected String categoryString, subcategoryString;
    private ImageView backButton;
    private Context context;
    private boolean exit = false;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_name);
        context = this;
        if (ContextCompat.checkSelfPermission(UserCategoryActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(UserCategoryActivity.this, Manifest.permission_group.MICROPHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.MICROPHONE}, 100);

        }
        toolbarClick();
        initToolbar();
        backButton();
        floatingActionButton();
        init();
    }

    private void floatingActionButton(){
        FloatingActionButton fab=findViewById(R.id.floating_action_button);
        SharedPrefsManager sharedPrefsManager=new SharedPrefsManager(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefsManager.saveBoolValue("isFromLogin",false);
                startActivity(new Intent(UserCategoryActivity.this,DownloadAgreementActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] dataCat = new String[]{"Home", "Office", "Malls", "Roads", "Sceneries", "Flora", "Other Water Bodies", "Shops", "Advertisements", "Bus Board", "Traffic Signs", "Name Boards", "Sign Boards", "From Books"};
        String[] subCatWithoutText = new String[]{"People Centric", "Non People Centric"};
        String[] subCatTextCentric = new String[]{"Text Centric"};

        ArrayAdapter catAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        dataCat);

        AutoCompleteTextView editTextFilledExposedDropdownCat =
                categoryName.findViewById(R.id.category);
        editTextFilledExposedDropdownCat.setAdapter(catAdapter);

        categoryName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryName.getText().toString();
                if (category.equals("Home") || category.equals("Office") || category.equals("Malls") || category.equals("Roads") || category.equals("Sceneries") || category.equals("Flora") || category.equals("Other Water Bodies")) {
                    ArrayAdapter subLocationAdapter =
                            new ArrayAdapter<>(
                                    context,
                                    R.layout.dropdown_menu_popup_item,
                                    subCatWithoutText);

                    AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
                            subcategoryName.findViewById(R.id.subCategory);
                    editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);
                } else if (category.equals("Shops") || category.equals("Advertisements") || category.equals("Bus Board") || category.equals("Traffic Signs") || category.equals("Name Boards") || category.equals("Sign Boards") || category.equals("From Books")) {
                    ArrayAdapter subLocationAdapter =
                            new ArrayAdapter<>(
                                    context,
                                    R.layout.dropdown_menu_popup_item,
                                    subCatTextCentric);

                    AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
                            subcategoryName.findViewById(R.id.subCategory);
                    editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);
                }
            }
        });
    }

    private void init() {
        addClass = findViewById(R.id.addClass);
        categoryName = findViewById(R.id.category);
        subcategoryName = findViewById(R.id.subCategory);
        sharedPrefsManager=new SharedPrefsManager(this);
        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryString = categoryName.getText().toString();
                subcategoryString = subcategoryName.getText().toString();
                if (subcategoryString.isEmpty()) {
                    categoryName.setError("Invalid Input");
                } else if (subcategoryString.isEmpty()) {
                    categoryName.setError("Inavlid Input");
                } else {
                    Intent intent = new Intent(UserCategoryActivity.this, UserMenu.class);
                    sharedPrefsManager.saveStringValue("categoryName", categoryString);
                    sharedPrefsManager.saveStringValue("subCategoryName", subcategoryString);
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

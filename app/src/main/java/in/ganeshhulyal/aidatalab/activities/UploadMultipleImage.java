package in.ganeshhulyal.aidatalab.activities;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.Retrofit.Interfaces.ApiService;
import in.ganeshhulyal.aidatalab.adapters.MyAdapter;
import in.ganeshhulyal.aidatalab.others.InternetConnection;
import in.ganeshhulyal.aidatalab.others.PostData;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;
import in.ganeshhulyal.aidatalab.utils.FileUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadMultipleImage extends AppCompatActivity {
    private static final String TAG = UploadMultipleImage.class.getSimpleName();

    private ListView listView;
    private ProgressBar mProgressBar;
    private MaterialButton btnChoose, btnUpload;
    private ArrayList<Uri> arrayList;
    private final int REQUEST_CODE_PERMISSIONS = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
    private SharedPrefsManager sharedPrefsManager;
    private boolean exit = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_image_upload);
        sharedPrefsManager = new SharedPrefsManager(this);
        context = this;
        backButton();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        listView = findViewById(R.id.listView);
        mProgressBar = findViewById(R.id.progressBar);
        toolbarClick();
        btnChoose = findViewById(R.id.btnChoose);
        btnChoose.setOnClickListener(v -> {
            // Display the file chooser dialog
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askForPermission();
            } else {
                showChooser();
            }
        });

        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(v -> uploadImagesToServer());

        arrayList = new ArrayList<>();
    }

    public void  showDialogueDialog() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Upload Successful!")
                .setMessage("Do you want to upload more?")
                .setCancelable(false)
                .setPositiveButton("Yes?", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        startActivity(new Intent(UploadMultipleImage.this, UserCategoryActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        startActivity(new Intent(UploadMultipleImage.this, UserFeedBackActivity.class));
                        finish();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    public void  logoutDialogue() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        Toast.makeText(UploadMultipleImage.this, "Logging out", Toast.LENGTH_SHORT).show();
                        sharedPrefsManager.saveStringValue("userEmail", null);
                        sharedPrefsManager.saveBoolValue("isLoggedIn", false);
                        startActivity(new Intent(UploadMultipleImage.this,UserLoginActivity.class));
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
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadMultipleImage.this, UserFeedBackActivity.class));
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

    private void showChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_READ_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        btnUpload.setVisibility(View.VISIBLE);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_READ_STORAGE) {
                if (resultData != null) {
                    if (resultData.getClipData() != null) {
                        int count = resultData.getClipData().getItemCount();
                        if (count >= 0) {
                            btnUpload.setVisibility(View.VISIBLE);
                            btnChoose.setText("Add More");
                            //btnChoose.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(this, "Select 2 or more Images", Toast.LENGTH_SHORT).show();
                            btnUpload.setVisibility(View.VISIBLE);
                            btnChoose.setVisibility(View.VISIBLE);
                        }
                        int currentItem = 0;
                        while (currentItem < count) {
                            Uri imageUri = resultData.getClipData().getItemAt(currentItem).getUri();
                            currentItem = currentItem + 1;

                            Log.d("Uri Selected", imageUri.toString());

                            try {
                                arrayList.add(imageUri);
                                MyAdapter mAdapter = new MyAdapter(UploadMultipleImage.this, arrayList);
                                listView.setAdapter(mAdapter);

                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                    } else if (resultData.getData() != null) {

                        final Uri uri = resultData.getData();
                        Log.i(TAG, "Uri = " + uri.toString());

                        try {
                            arrayList.add(uri);
                            MyAdapter mAdapter = new MyAdapter(UploadMultipleImage.this, arrayList);
                            listView.setAdapter(mAdapter);

                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                }
            }
        }
    }

    private void uploadImagesToServer() {
        if (InternetConnection.checkConnection(UploadMultipleImage.this)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            showProgress();

            // create list of file parts (photo, video, ...)
            List<MultipartBody.Part> parts = new ArrayList<>();

            // create upload service client
            ApiService service = retrofit.create(ApiService.class);

            if (arrayList != null) {
                // create part for file (photo, video, ...)
                for (int i = 0; i < arrayList.size(); i++) {
                    parts.add(prepareFilePart("image" + i, arrayList.get(i)));
                }
            }

            // create a map of data to pass along
            RequestBody description = createPartFromString("www.androidlearning.com");
            RequestBody size = createPartFromString("" + parts.size());
            PostData data = new PostData();
            // finally, execute the request
            String email = sharedPrefsManager.getStringValue("userEmail", "unknown@gmail.com");
            int index = email.indexOf('@');
            String Email = email.substring(0, index);
            SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);
            RequestBody Category = createPartFromString(sharedPrefsManager.getStringValue("categoryName", "Other"));
            RequestBody humanCentricAgreement = createPartFromString(sharedPrefsManager.getStringValue("humanAgreementName", "None"));
            RequestBody subCategory = createPartFromString(sharedPrefsManager.getStringValue("subCategoryName", "Other"));
            RequestBody dataType = createPartFromString("Image");
            RequestBody location = createPartFromString(sharedPrefsManager.getStringValue("location", "Null"));
            RequestBody subLocation = createPartFromString(sharedPrefsManager.getStringValue("subLocation", "Null"));
            RequestBody timing = createPartFromString(sharedPrefsManager.getStringValue("timing", "Null"));
            RequestBody lighting = createPartFromString(sharedPrefsManager.getStringValue("lighting", "Null"));
            RequestBody model = createPartFromString(sharedPrefsManager.getStringValue("model", "Null"));
            RequestBody orientation = createPartFromString(sharedPrefsManager.getStringValue("orientation", "Null"));
            RequestBody screenSize = createPartFromString(sharedPrefsManager.getStringValue("screenSize", "Null"));
            RequestBody dslr = createPartFromString(sharedPrefsManager.getStringValue("dslr", "Null"));
            RequestBody category = createPartFromString(sharedPrefsManager.getStringValue("category", "Null"));
            RequestBody isHumanPresent = createPartFromString(sharedPrefsManager.getStringValue("isHumanPresnt", "Null"));
            RequestBody selfie = createPartFromString(sharedPrefsManager.getStringValue("selfie", "Null"));
            RequestBody children = createPartFromString(sharedPrefsManager.getStringValue("children", "Null"));
            RequestBody consent = createPartFromString(sharedPrefsManager.getStringValue("consent", "Null"));
            RequestBody props = createPartFromString(sharedPrefsManager.getStringValue("props", "Null"));
            RequestBody imageType = createPartFromString(sharedPrefsManager.getStringValue("imageType", "Null"));
            RequestBody username = createPartFromString(Email);

            Call<ResponseBody> call = service.uploadMultiple(humanCentricAgreement, imageType, description, size, Category, subCategory, username, dataType, model, screenSize, orientation, dslr, category, location, subLocation, timing, lighting, isHumanPresent, selfie, createPartFromString("Single"), children, props, consent, parts);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    hideProgress();
                    if (response.isSuccessful()) {
                        showDialogueDialog();
                    } else {
                        Snackbar.make(findViewById(android.R.id.content),
                                "Something went wrong", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    hideProgress();
                    Log.e(TAG, "Image upload failed!", t);
                    Snackbar.make(findViewById(android.R.id.content),
                            "Image upload failed!", Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            hideProgress();
            Toast.makeText(UploadMultipleImage.this,
                    "Internet Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        btnChoose.setEnabled(false);
        btnUpload.setVisibility(View.GONE);
    }

    private void hideProgress() {
        mProgressBar.setVisibility(View.GONE);
        btnChoose.setEnabled(true);
        btnUpload.setVisibility(View.VISIBLE);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtil.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // use the FileUtil to get the actual file by uri
        File file = FileUtil.getFile(this, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse(FileUtil.MIME_TYPE_IMAGE), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    /**
     * Runtime Permission
     */
    private void askForPermission() {
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                != PackageManager.PERMISSION_GRANTED) {
            /* Ask for permission */
            // need to request permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(this.findViewById(android.R.id.content),
                        "Please grant permissions to write data in sdcard",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        v -> ActivityCompat.requestPermissions(UploadMultipleImage.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_PERMISSIONS)).show();
            } else {
                /* Request for permission */
                ActivityCompat.requestPermissions(UploadMultipleImage.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            }

        } else {
            showChooser();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showChooser();
            } else {
                // Permission Denied
                Toast.makeText(UploadMultipleImage.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadMultipleImage.this);
        final AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(arg0 -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                    ContextCompat.getColor(UploadMultipleImage.this, android.R.color.holo_blue_light));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                    ContextCompat.getColor(UploadMultipleImage.this, android.R.color.holo_red_light));
        });

        dialog.show();
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(UploadMultipleImage.this, UserUploadMenuActivity.class));
        finish();
    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadMultipleImage.this, UserUploadMenuActivity.class));
                finish();
            }
        });


    }

}
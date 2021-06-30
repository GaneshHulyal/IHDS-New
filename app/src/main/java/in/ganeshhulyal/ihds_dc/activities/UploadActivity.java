package in.ganeshhulyal.ihds_dc.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.others.AndroidMultiPartEntity;
import in.ganeshhulyal.ihds_dc.others.CheckInternet;
import in.ganeshhulyal.ihds_dc.others.Config;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = UserUploadMenuActivity.class.getSimpleName();

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private MaterialCardView btnUpload;
    private SharedPrefsManager sharedPrefsManager;
    private boolean exit = false;
    private long totalSize = 0;
    private Context context;
    private CheckInternet checkInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initToolbar();
        sharedPrefsManager = new SharedPrefsManager(this);
        context = this;
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        backButton();
        toolbarClick();
        checkInternet = new CheckInternet(this);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);

        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");

        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }
        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // uploading the file to server
                if (checkInternet.isInternetAvailable()) {
                    new UploadActivity.UploadFileToServer().execute();
                } else {
                    Toast.makeText(UploadActivity.this, "Internet not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initToolbar() {
        TextView toolbarName;
        toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setText("Upload Image");
    }


    public void dialogue() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Upload Successful!")
                .setMessage("Do you want to upload more?")
                .setCancelable(false)
                .setPositiveButton("Yes?", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        startActivity(new Intent(UploadActivity.this, UserCategoryActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        startActivity(new Intent(UploadActivity.this, UserFeedBackActivity.class));
                        finish();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    /**
     * Displaying captured image/video on the screen
     */
    public void  logoutDialogue() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        Toast.makeText(UploadActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                        sharedPrefsManager.saveStringValue("userEmail", null);
                        sharedPrefsManager.saveBoolValue("isLoggedIn", false);
                        startActivity(new Intent(UploadActivity.this, UserLoginActivity.class));
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
                startActivity(new Intent(UploadActivity.this, UserFeedBackActivity.class));
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

    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }

    /**
     * Uploading the file to server
     */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            Log.d("MSG", "Upload file to server called");
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        private void backButton() {
            ImageView backButton = findViewById(R.id.toolbar_image);
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UploadActivity.this, UserUploadMenuActivity.class));
                    finish();
                }
            });


        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {


            String email = sharedPrefsManager.getStringValue("userEmail", "admin@klesamsung");
            String Category = sharedPrefsManager.getStringValue("categoryName", "Other");
            String subCategory = sharedPrefsManager.getStringValue("subCategoryName", "Other");
            String dataType = "Image";
            String humanCentricAgreement = sharedPrefsManager.getStringValue("humanAgreementName", "Null");
            String location = sharedPrefsManager.getStringValue("location", "Null");
            String subLocation = sharedPrefsManager.getStringValue("subLocation", "Null");
            String timing = sharedPrefsManager.getStringValue("timing", "Null");
            String lighting = sharedPrefsManager.getStringValue("lighting", "Null");
            String model = sharedPrefsManager.getStringValue("model", "Null");
            String orientation = sharedPrefsManager.getStringValue("orientation", "Null");
            String screenSize = sharedPrefsManager.getStringValue("screenSize", "Null");
            String dslr = sharedPrefsManager.getStringValue("dslr", "Null");
            String category = sharedPrefsManager.getStringValue("category", "Null");
            String isHumanPresent = sharedPrefsManager.getStringValue("isHumanPresent", "Null");
            String selfie = sharedPrefsManager.getStringValue("selfie", "Null");
            String children = sharedPrefsManager.getStringValue("children", "Null");
            String consent = sharedPrefsManager.getStringValue("consent", "Null");
            String props = sharedPrefsManager.getStringValue("props", "Null");
            String imageType = sharedPrefsManager.getStringValue("imageType", "Null");

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));
                entity.addPart("humanAgreementName", new StringBody(humanCentricAgreement));
                entity.addPart("Category", new StringBody(Category));
                entity.addPart("SubCat", new StringBody(subCategory));
                entity.addPart("imageType", new StringBody(imageType));
                entity.addPart("dataType", new StringBody(dataType));
                entity.addPart("deviceUsed", new StringBody(model));
                entity.addPart("resolution", new StringBody(screenSize));
                entity.addPart("orientation", new StringBody(orientation));
                entity.addPart("dslrOrMobile", new StringBody(dslr));
                entity.addPart("dataMajorCategory", new StringBody(category));
                entity.addPart("location", new StringBody(location));
                entity.addPart("subLocation", new StringBody(subLocation));
                entity.addPart("timing", new StringBody(timing));
                entity.addPart("lighting", new StringBody(lighting));
                entity.addPart("isHumanPresent", new StringBody(isHumanPresent));
                entity.addPart("selfie", new StringBody(selfie));
                entity.addPart("type", new StringBody(imageType));
                entity.addPart("children", new StringBody(children));
                entity.addPart("props", new StringBody(props));
                entity.addPart("consentObtained", new StringBody(consent));
                entity.addPart("userName", new StringBody(email));


                httpclient.getConnectionManager().getSchemeRegistry().register(
                        new Scheme("https", SSLSocketFactory.getSocketFactory(), 443)
                );
                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            //showAlert(result);
            dialogue();
            super.onPostExecute(result);
        }

    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadActivity.this, UserUploadMenuActivity.class));
                finish();
            }
        });
    }

    /**
     * Method to show alert dialog
     */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do nothing
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UploadActivity.this, UserUploadMenuActivity.class));
        finish();
    }


}
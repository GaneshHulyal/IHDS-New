package in.ganeshhulyal.aidatalab.activities;

/* User has all the upload options in this activity- This activity comes after MetaData Screen */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import com.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.others.Config;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;

public class UserUploadMenuActivity extends AppCompatActivity {

    // LogCat tag
    private static final String TAG = UserUploadMenuActivity.class.getSimpleName();


    // Camera activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public SharedPrefsManager sharedPrefsManager;
    public String categoryName;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private Context context;
    private Uri fileUri; // file url to store image/video
    private MaterialCardView btnCapturePicture, btnRecordVideo, btnUploadImage, btnUploadVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_upload);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        context=this;
        backButton();
        sharedPrefsManager = new SharedPrefsManager(this);
        categoryName = sharedPrefsManager.getStringValue("categoryString", "Null");
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(getResources().getString(R.color.action_bar))));
        btnCapturePicture = findViewById(R.id.btnCapturePicture);
        btnRecordVideo = findViewById(R.id.btnRecordVideo);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnUploadVideo = findViewById(R.id.btnUploadVideo);
        toolbarClick();
        //floatingActionButton();

        /**
         * Capture image button click event
         */

        //Upload Button click event
        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserUploadMenuActivity.this, UploadMultipleImage.class));
                finish();
            }
        });

        //capture button click event
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        //upload button click event
        btnUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserUploadMenuActivity.this, UploadMultipleVideo.class));
                finish();
            }
        });
        /**
         * Record video button click event
         */

        //upload record button click event
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                recordVideo();
            }
        });

        // Checking camera availability
//        if (!isDeviceSupportCamera()) {
//            Toast.makeText(getApplicationContext(),
//                    "Sorry! Your device doesn't support camera",
//                    Toast.LENGTH_LONG).show();
//            // will close the app if the device does't have camera
//            finish();
//        }

        initToolbar();
    }

    private void initToolbar() {
        TextView toolbarName;
        toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setText("Upload");
    }

    /**
     * Checking device has camera hardware or not
     */
    public void  logoutDialogue() {

        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Do you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", R.drawable.ic_baseline_check_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(UserUploadMenuActivity.this, "Logging out", Toast.LENGTH_SHORT).show();
                        sharedPrefsManager.saveStringValue("userEmail", null);
                        sharedPrefsManager.saveBoolValue("isLoggedIn", false);
                        startActivity(new Intent(UserUploadMenuActivity.this, UserLoginActivity.class));
                        finish();
                    }
                })
                .setNegativeButton("No", R.drawable.ic_baseline_cancel_24, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    //toolbar click even
    private void toolbarClick() {
        ImageView feedback,logout;
        feedback=findViewById(R.id.toolbar_feedback);
        logout=findViewById(R.id.toolbar_logout);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserUploadMenuActivity.this, UserFeedBackActivity.class));
                finish();

            }
        });

        //logout click event
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialogue();

            }
        });

        //toolbar feedback button
        feedback.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Feedback", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        //toolbar logout button
        logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Logout", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }


    /**
     * Launching camera app to capture image
     */

    //launch capture image intent
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Launching camera app to record video
     */

    //floating action button for download activity
    private void floatingActionButton(){
        FloatingActionButton fab=findViewById(R.id.floating_action_button);
        SharedPrefsManager sharedPrefsManager=new SharedPrefsManager(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefsManager.saveBoolValue("isFromLogin",false);
                startActivity(new Intent(UserUploadMenuActivity.this,DownloadAgreementActivity.class));
            }
        });
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }


    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // successfully captured the image
                // launching upload activity
                launchUploadActivity(true);


            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // video successfully recorded
                // launching upload activity
                launchUploadActivity(false);

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    //Launching Upload activity
    private void launchUploadActivity(boolean isImage) {
        Intent i = new Intent(UserUploadMenuActivity.this, UploadActivity.class);
        i.putExtra("filePath", fileUri.getPath());
        i.putExtra("isImage", isImage);
        startActivity(i);
        finish();
    }

    /**
     * ------------ Helper Methods ---------------------- 
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Config.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + Config.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    //toolbar back button
    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checking whether the Activity is from HumanMetaData Activity or Not
                if (sharedPrefsManager.getBoolValue("isFromHumanMetaData", false)) {
                    startActivity(new Intent(UserUploadMenuActivity.this, MetaDataHumanCentricActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(UserUploadMenuActivity.this, MetaDataNonHumanCentricActivity.class));
                    finish();
                }
            }
        });


    }


    //on BackPressed event
    @Override
    public void onBackPressed() {

        if (sharedPrefsManager.getBoolValue("isFromHumanMetaData", false)) {
            startActivity(new Intent(UserUploadMenuActivity.this, MetaDataHumanCentricActivity.class));
            finish();
        } else {
            startActivity(new Intent(UserUploadMenuActivity.this, MetaDataNonHumanCentricActivity.class));
            finish();
        }
    }

}
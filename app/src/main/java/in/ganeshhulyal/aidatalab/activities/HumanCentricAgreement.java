package in.ganeshhulyal.aidatalab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import in.ganeshhulyal.aidatalab.BuildConfig;
import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.others.DownloadHelper;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;

public class HumanCentricAgreement extends AppCompatActivity {

    private TextView toolbarName;
    private CheckBox agreementOne;
    private View agreementLayoutOne;
    private Button acceptAgreement;
    private SharedPrefsManager SharedPref;
    private StorageReference StoreRef;
    private FirebaseDatabase database;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference mRef;

    private static final String TAG = "HumanCentricAgreement";
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};


    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_centric_agreement);
        initView();
        main();
        initToolbar();
        backButton();
    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void main() {
        agreementOne.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                acceptAgreement.setVisibility(View.VISIBLE);
            }
        });

        acceptAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                download();
                SharedPref.saveBoolValue("isUserAcceptedAgreement", true);
                String EmailString = SharedPref.getStringValue("userEmail", "noVal");
                int index = EmailString.indexOf('@');
                String email = EmailString.substring(0, index);
                mRef.child("Users").child(email).child("humanCentricAgreement").setValue(true);
                startActivity(new Intent(HumanCentricAgreement.this, MetaDataHumanCentric.class));
                finish();
            }
        });
    }

    private void initToolbar() {
        toolbarName.setText("Agreement");
    }

    private void initView() {
        toolbarName = findViewById(R.id.toolbar_name);
        agreementOne = findViewById(R.id.agreement_one);
        agreementLayoutOne = findViewById(R.id.agreement_layout_one);
        acceptAgreement = findViewById(R.id.accept_agreement);
        SharedPref = new SharedPrefsManager(this);
        StoreRef = storage.getReference();
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
    }

    public void request(View view) {

        ActivityCompat.requestPermissions(HumanCentricAgreement.this, PERMISSIONS, 112);

    }

    public void view(View view) {
        Log.v(TAG, "view() Method invoked ");

        if (!hasPermissions(HumanCentricAgreement.this, PERMISSIONS)) {

            Log.v(TAG, "download() Method DON'T HAVE PERMISSIONS ");

            Toast t = Toast.makeText(getApplicationContext(), "You don't have read access !", Toast.LENGTH_LONG);
            t.show();

        } else {
            File d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);  // -> filename = maven.pdf
            File pdfFile = new File(d, "maven.pdf");

            Log.v(TAG, "view() Method pdfFile " + pdfFile.getAbsolutePath());

            Uri path = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", pdfFile);


            Log.v(TAG, "view() Method path " + path);

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(HumanCentricAgreement.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }
        Log.v(TAG, "view() Method completed ");

    }

    public void download() {
        Log.v(TAG, "download() Method invoked ");

        if (!hasPermissions(HumanCentricAgreement.this, PERMISSIONS)) {

            Log.v(TAG, "download() Method DON'T HAVE PERMISSIONS ");

            Toast t = Toast.makeText(getApplicationContext(), "You don't have write access !", Toast.LENGTH_LONG);
            t.show();

        } else {
            Log.v(TAG, "download() Method HAVE PERMISSIONS ");

            //new DownloadFile().execute("http://maven.apache.org/maven-1.x/maven.pdf", "maven.pdf");
            new DownloadFile().execute("https://firebasestorage.googleapis.com/v0/b/aidatalab-a7127.appspot.com/o/AgreementPDF%2FUserAgreement.pdf?alt=media&token=2835d7dd-f4f7-4bb4-8971-677348f13852", "agreement_" + SharedPref.getStringValue("userEmail", "abc@gmail.com") + ".pdf");
            Toast.makeText(HumanCentricAgreement.this, "Agreement file saved in Downloads", Toast.LENGTH_SHORT).show();
            finish();

        }

        Log.v(TAG, "download() Method completed ");

    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Log.v(TAG, "doInBackground() Method invoked ");

            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File pdfFile = new File(folder, fileName);
            Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.getAbsolutePath());
            Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.getAbsoluteFile());

            try {
                pdfFile.createNewFile();
                Log.v(TAG, "doInBackground() file created" + pdfFile);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground() error" + e.getMessage());
                Log.e(TAG, "doInBackground() error" + e.getStackTrace());


            }
            DownloadHelper.downloadFile(fileUrl, pdfFile);
            Log.v(TAG, "doInBackground() file download completed");
            //

            return null;
        }
    }


}
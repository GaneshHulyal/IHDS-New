package in.ganeshhulyal.ihds_dc.activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;

public class DownloadAgreementActivity extends AppCompatActivity {
    private MaterialCardView downloadAgreement, downloadConsent;
    private Button btnNext;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_agreement);
        isDownloadManagerAvailable(this);
        sharedPrefsManager=new SharedPrefsManager(this);
        init();
        backButton();
        toolbarClick();
    }

    public static boolean isDownloadManagerAvailable(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return true;
        }
        return false;
    }

    private void init() {
        downloadAgreement = findViewById(R.id.btnDownloadAgreement);
        downloadConsent = findViewById(R.id.btnDownloadConsent);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPrefsManager.getBoolValue("isFromLogin",false)){
                startActivity(new Intent(DownloadAgreementActivity.this, UserRegistration.class));
                finish();}
                else{
                    onBackPressed();
                }
            }
        });
        downloadAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.2.0.22/Register.pdf";
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("Some descrition");
                request.setTitle("Samsung_Agreement");
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Samsung_Agreement.pdf");
                Toast.makeText(DownloadAgreementActivity.this, "File Downloaded at Downloads", Toast.LENGTH_SHORT).show();

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });

        downloadConsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://10.2.0.22/Consent.pdf";
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setDescription("Some descrition");
                request.setTitle("Samsung_Consent");
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Human_Centric_Consent.pdf");
                Toast.makeText(DownloadAgreementActivity.this, "File Downloaded at Downloads", Toast.LENGTH_SHORT).show();

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }
        });
    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void toolbarClick() {
        ImageView feedback, logout;
        feedback = findViewById(R.id.toolbar_feedback);
        logout = findViewById(R.id.toolbar_logout);
        feedback.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);
    }
}
package in.ganeshhulyal.ihds_dc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import in.ganeshhulyal.ihds_dc.R;
import in.ganeshhulyal.ihds_dc.others.SharedPrefsManager;

public class UserMenu extends AppCompatActivity {


    private MaterialCardView nonHumanCentric, humanCentric;
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        init();
        if(sharedPrefsManager.getStringValue("subCategoryName",null).equals("People Centric")){
            startActivity(new Intent(UserMenu.this, UploadHumanCentricAgreementActivity.class));
            sharedPrefsManager.saveStringValue("imageType", "Human Centric");
            finish();
        }
        else{
            startActivity(new Intent(UserMenu.this, MetaDataNonHumanCentricActivity.class));
            sharedPrefsManager.saveStringValue("imageType", "Non Human Centric");
            sharedPrefsManager.saveStringValue("humanAgreementName", "Null");
            finish();
        }
    }

    private void init() {
        nonHumanCentric = findViewById(R.id.non_human_centric);
        humanCentric = findViewById(R.id.human_centric);
        sharedPrefsManager = new SharedPrefsManager(this);
        nonHumanCentric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenu.this, MetaDataNonHumanCentricActivity.class));
                finish();
                sharedPrefsManager.saveStringValue("imageType", "Non Human Centric");
            }
        });

        humanCentric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenu.this, UploadHumanCentricAgreementActivity.class));
                sharedPrefsManager.saveStringValue("imageType", "Human Centric");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

}
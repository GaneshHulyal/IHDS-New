package in.ganeshhulyal.aidatalab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.text.BreakIterator;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;

public class UserMenu extends AppCompatActivity {


    MaterialCardView nonHumanCentric, humanCentric;
    SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);
        init();
        backButton();
        initToolbar();
    }

    private void init() {
        nonHumanCentric = findViewById(R.id.non_human_centric);
        humanCentric = findViewById(R.id.human_centric);
        sharedPrefsManager = new SharedPrefsManager(this);
        nonHumanCentric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenu.this, MetaDataNonHumanCentric.class));
                sharedPrefsManager.saveStringValue("imageType", "Non Human Centric");
            }
        });

        humanCentric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenu.this, HumanCentricAgreement.class));
                sharedPrefsManager.saveStringValue("imageType", "Human Centric");
                finish();
            }
        });
    }

    private void initToolbar() {
        TextView toolbarName;
        toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setText("Select type");
    }
    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserMenu.this,ClassName.class));
            }
        });
    }
}
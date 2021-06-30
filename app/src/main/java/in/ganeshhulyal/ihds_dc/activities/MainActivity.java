package in.ganeshhulyal.ihds_dc.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import in.ganeshhulyal.ihds_dc.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

}
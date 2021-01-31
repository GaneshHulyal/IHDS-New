package in.ganeshhulyal.aidatalab.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import in.ganeshhulyal.aidatalab.R;

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
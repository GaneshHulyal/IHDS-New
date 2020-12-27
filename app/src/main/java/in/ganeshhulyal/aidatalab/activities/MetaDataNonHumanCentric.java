package in.ganeshhulyal.aidatalab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.models.MetaDataModelNonHumanCentric;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;

import static java.security.AccessController.getContext;

public class MetaDataNonHumanCentric extends AppCompatActivity {

    private AutoCompleteTextView dataMajorCategory;
    private AutoCompleteTextView timingView;
    private AutoCompleteTextView subLocationView;
    private AutoCompleteTextView locationView;
    private AutoCompleteTextView lightingView;
    private Button addMetaData;
    private String categoryString, timingString, subLocationString, locationString, lightingString;
    private SharedPrefsManager sharedPrefsManager;
    private TextInputEditText subLocationOther, lightingOther;
    private TextInputLayout subLocationOtherLayout, lightingOtherLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_data_non_human_centric);
        context = this;
        backButton();
        initToolbar();
        init();
    }

    private void initToolbar() {
        TextView toolbarName;
        toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setText("Non Human Centric");
    }


    public String getScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        return width + "x" + height;
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] dataCat = new String[]{"People", "Text", "Natural"};
        String[] locationType = new String[]{"Indoor", "Outdoor"};
        String[] subLocationIndoor = new String[]{"Home", "Office", "Mall", "School", "Theater", "Other"};
        String[] subLocationOutdoor = new String[]{"Road", "Beach", "In water", "Mountain", "Desert", "Natural Landscape", "Forest", "Other"};
        String[] timing = new String[]{"Sunrise", "Morning", "Forenoon", "Noon", "Afternoon", "Evening", "Night"};
        String[] lighting = new String[]{"Incandescent", "Florescent", "Diffused", "Natural Light", "Other"};

        ArrayAdapter catAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        dataCat);

        AutoCompleteTextView editTextFilledExposedDropdownCat =
                dataMajorCategory.findViewById(R.id.data_major_category);
        editTextFilledExposedDropdownCat.setAdapter(catAdapter);


        ArrayAdapter locationAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        locationType);

        AutoCompleteTextView editTextFilledExposedDropdownLocation =
                locationView.findViewById(R.id.location_type);
        editTextFilledExposedDropdownLocation.setAdapter(locationAdapter);
        locationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = locationView.getText().toString();
                Toast.makeText(MetaDataNonHumanCentric.this, location, Toast.LENGTH_SHORT).show();
                if (location.equals("Indoor")) {
                    ArrayAdapter subLocationAdapter =
                            new ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, subLocationIndoor);

                    AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
                            subLocationView.findViewById(R.id.sub_location);
                    editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);
                    subLocationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String subLocation = subLocationView.getText().toString();
                            if (subLocation.equals("Other")) {
                                subLocationOtherLayout.setVisibility(View.VISIBLE);
                                subLocationString=subLocationOther.getText().toString();
                            }
                        }
                    });

                } else if (location.equals("Outdoor")) {
                    final ArrayAdapter subLocationAdapter =
                            new ArrayAdapter<String>(context, R.layout.dropdown_menu_popup_item, subLocationOutdoor);

                    AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
                            subLocationView.findViewById(R.id.sub_location);
                    editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);
                    subLocationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String subLocation = subLocationView.getText().toString();
                            if (subLocation.equals("Other")) {
                                subLocationOtherLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }

            }
        });

        ArrayAdapter lightingAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        lighting);

        AutoCompleteTextView editTextFilledExposedDropdownLighting =
                lightingView.findViewById(R.id.lighting);
        editTextFilledExposedDropdownLighting.setAdapter(lightingAdapter);

        lightingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lighting=lightingView.getText().toString();
                if(lighting.equals("Other")){
                    lightingOtherLayout.setVisibility(View.VISIBLE);
                    lightingString=lightingOther.getText().toString();
                }
            }
        });

        ArrayAdapter timingAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        timing);

        AutoCompleteTextView editTextFilledExposedDropdownTiming =
                timingView.findViewById(R.id.timing);
        editTextFilledExposedDropdownTiming.setAdapter(timingAdapter);


        addMetaData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryString = dataMajorCategory.getText().toString();
                timingString = timingView.getText().toString();
                subLocationString = subLocationView.getText().toString();
                lightingString = lightingView.getText().toString();
                locationString = locationView.getText().toString();
                subLocationString=subLocationOther.getText().toString();
                lightingString=lightingOther.getText().toString();
                String myDeviceModel = android.os.Build.MODEL;
                String screenSize = getScreenSize();
                sharedPrefsManager.saveStringValue("categoryString", categoryString);
                sharedPrefsManager.saveStringValue("location", locationString);
                sharedPrefsManager.saveStringValue("subLocation", subLocationString);
                sharedPrefsManager.saveStringValue("timing", timingString);
                sharedPrefsManager.saveStringValue("lighting", lightingString);
                sharedPrefsManager.saveStringValue("model", myDeviceModel);
                sharedPrefsManager.saveStringValue("orientation", "Potrait");
                sharedPrefsManager.saveStringValue("screenSize", screenSize);
                sharedPrefsManager.saveStringValue("dslr", "No");
                sharedPrefsManager.saveStringValue("category", categoryString);
                sharedPrefsManager.saveStringValue("isHumanPresent", "Null");
                sharedPrefsManager.saveStringValue("selfie", "Null");
                sharedPrefsManager.saveStringValue("type", "Null");
                sharedPrefsManager.saveStringValue("children", "Null");
                sharedPrefsManager.saveStringValue("props", "Null");
                sharedPrefsManager.saveStringValue("consent", "Null");
                startActivity(new Intent(MetaDataNonHumanCentric.this, CameraUploadActivity.class));
            }
        });
    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MetaDataNonHumanCentric.this, UserMenu.class));
            }
        });
    }

    private void init() {
        dataMajorCategory = findViewById(R.id.data_major_category);
        timingView = findViewById(R.id.timing);
        subLocationView = findViewById(R.id.sub_location);
        locationView = findViewById(R.id.location_type);
        lightingView = findViewById(R.id.lighting);
        addMetaData = findViewById(R.id.addMetaData);
        subLocationOther = findViewById(R.id.sub_location_other);
        lightingOther = findViewById(R.id.lighting_other);
        subLocationOtherLayout = findViewById(R.id.sub_location_other_layout);
        lightingOtherLayout = findViewById(R.id.lighting_other_layout);
        sharedPrefsManager = new SharedPrefsManager(this);
    }
}
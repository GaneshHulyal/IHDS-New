package in.ganeshhulyal.aidatalab.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;

import static in.ganeshhulyal.aidatalab.R.layout.dropdown_menu_popup_item;

public class MetaDataHumanCentric extends AppCompatActivity {

    private AutoCompleteTextView dataMajorCategory, humanPresentView, selfieView, typeView, aboveEighteenView, props, consentView;
    private AutoCompleteTextView timingView;
    private AutoCompleteTextView subLocationView;
    private AutoCompleteTextView locationView;
    private AutoCompleteTextView lightingView;
    private Button addMetaData;
    private String categoryString, timingString, subLocationString, locationString, lightingString, humanPresentString, selfieString, typeString, aboveEighteenString, consentString, propsString;
    private SharedPrefsManager sharedPrefsManager;
    private TextInputEditText subLocationOther, lightingOther;
    private TextInputLayout subLocationOtherLayout, lightingOtherLayout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta_data_human_centric);
        context = this;
        backButton();
        initToolbar();
        init();
    }

    private void initToolbar() {
        TextView toolbarName;
        toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setText("Human Centric");
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
        String[] humanPresent = new String[]{"Yes"};
        String[] selfie = new String[]{"Yes", "No"};
        String[] type = new String[]{"Single", "Group", "Crowd", "Image of Human"};
        String[] aboveEighteen = new String[]{"Yes", "No"};
        String[] Consent = new String[]{"Yes"};


        ArrayAdapter catAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        dataCat);

        AutoCompleteTextView editTextFilledExposedDropdownCat =
                dataMajorCategory.findViewById(R.id.data_major_category);
        editTextFilledExposedDropdownCat.setAdapter(catAdapter);

//        ArrayAdapter subLocationAdapter =
//                new ArrayAdapter<>(this, R.layout.dropdown_menu_popup_item, subLocationIndoor);
//
//        AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
//                subLocationView.findViewById(R.id.sub_location);
//        editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);


        ArrayAdapter locationAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        locationType);
        AutoCompleteTextView editTextFilledExposedDropdownLocation =
                locationView.findViewById(R.id.location_type);
        editTextFilledExposedDropdownLocation.setAdapter(locationAdapter);

        locationView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String location = locationView.getText().toString();
                Toast.makeText(MetaDataHumanCentric.this, location, Toast.LENGTH_SHORT).show();
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
                            else{
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
                                subLocationString=subLocationOther.getText().toString();
                            }
                        }
                    });

                }

            }
        });
        ArrayAdapter lightingAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
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

        ArrayAdapter humanPresentAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        humanPresent);

        AutoCompleteTextView editTextFilledExposedDropdownHumanPresent =
                humanPresentView.findViewById(R.id.humanPresent);
        editTextFilledExposedDropdownHumanPresent.setAdapter(humanPresentAdapter);

        ArrayAdapter typeAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        type);

        AutoCompleteTextView editTextFilledExposedDropdownType =
                typeView.findViewById(R.id.type);
        editTextFilledExposedDropdownType.setAdapter(typeAdapter);

        ArrayAdapter selfieAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        selfie);


        AutoCompleteTextView editTextFilledExposedDropdownSelfie =
                selfieView.findViewById(R.id.selfie);
        editTextFilledExposedDropdownSelfie.setAdapter(selfieAdapter);

        ArrayAdapter aboveEighteenAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        aboveEighteen);

        AutoCompleteTextView editTextFilledExposedDropdownAboveEighteen =
                aboveEighteenView.findViewById(R.id.above_eighteen);
        editTextFilledExposedDropdownAboveEighteen.setAdapter(aboveEighteenAdapter);

        ArrayAdapter consentAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        Consent);

        AutoCompleteTextView editTextFilledExposedDropdownConsent =
                consentView.findViewById(R.id.consent);
        editTextFilledExposedDropdownConsent.setAdapter(consentAdapter);

        ArrayAdapter timingAdapter =
                new ArrayAdapter<>(
                        this,
                        dropdown_menu_popup_item,
                        timing);

        AutoCompleteTextView editTextFilledExposedDropdownTiming =
                timingView.findViewById(R.id.timing);
        editTextFilledExposedDropdownTiming.setAdapter(timingAdapter);


        addMetaData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValidation()) {
                    categoryString = dataMajorCategory.getText().toString();
                    timingString = timingView.getText().toString();
                    subLocationString = subLocationView.getText().toString();
                    lightingString = lightingView.getText().toString();
                    locationString = locationView.getText().toString();
                    humanPresentString = humanPresentView.getText().toString();
                    typeString = typeView.getText().toString();
                    aboveEighteenString = aboveEighteenView.getText().toString();
                    propsString = props.getText().toString();
                    selfieString = selfieView.getText().toString();
                    consentString = consentView.getText().toString();
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
                    sharedPrefsManager.saveStringValue("isHumanPresent", humanPresentString);
                    sharedPrefsManager.saveStringValue("selfie", selfieString);
                    sharedPrefsManager.saveStringValue("type", typeString);
                    sharedPrefsManager.saveStringValue("children", aboveEighteenString);
                    sharedPrefsManager.saveStringValue("props", propsString);
                    sharedPrefsManager.saveStringValue("consent", consentString);
                    startActivity(new Intent(MetaDataHumanCentric.this, CameraUploadActivity.class));
                }
            }
        });
    }

    public boolean inputValidation() {
        categoryString = dataMajorCategory.getText().toString();
        timingString = timingView.getText().toString();
        subLocationString = subLocationView.getText().toString();
        lightingString = lightingView.getText().toString();
        locationString = locationView.getText().toString();
        humanPresentString = humanPresentView.getText().toString();
        typeString = typeView.getText().toString();
        aboveEighteenString = aboveEighteenView.getText().toString();
        propsString = props.getText().toString();
        selfieString = selfieView.getText().toString();
        consentString = consentView.getText().toString();
        if (categoryString.isEmpty()) {
            dataMajorCategory.setError("Invalid Input");
            return false;
        } else if (locationString.isEmpty()) {
            locationView.setError("Invalid Input");
            return false;

        } else if (subLocationString.isEmpty()) {
            subLocationView.setError("Invalid Input");
            return false;
        } else if (timingString.isEmpty()) {
            timingView.setError("Invalid Input");
            return false;
        } else if (lightingString.isEmpty()) {
            lightingView.setError("Invalid Input");
            return false;
        } else if (humanPresentString.isEmpty()) {
            humanPresentView.setError("Invalid Input");
            return false;
        } else if (selfieString.isEmpty()) {
            selfieView.setError("Invalid Input");
            return false;
        } else if (typeString.isEmpty()) {
            typeView.setError("Invalid Input");
            return false;
        } else if (aboveEighteenString.isEmpty()) {
            aboveEighteenView.setError("Invalid Input");
            return false;
        } else if (consentString.isEmpty()) {
            consentView.setError("Invalid Input");
            return false;
        } else {
            return true;
        }
    }

    private void backButton() {
        ImageView backButton = findViewById(R.id.toolbar_image);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MetaDataHumanCentric.this, HumanCentricAgreement.class));
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
        humanPresentView = findViewById(R.id.humanPresent);
        selfieView = findViewById(R.id.selfie);
        typeView = findViewById(R.id.type);
        lightingOther = findViewById(R.id.lighting_other);
        subLocationOther = findViewById(R.id.sub_location_other);
        aboveEighteenView = findViewById(R.id.above_eighteen);
        consentView = findViewById(R.id.consent);
        props = findViewById(R.id.props);
        subLocationOtherLayout=findViewById(R.id.sub_location_other_layout);
        lightingOtherLayout=findViewById(R.id.lighting_other_layout);
        sharedPrefsManager = new SharedPrefsManager(this);
    }
}
package in.ganeshhulyal.aidatalab.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import in.ganeshhulyal.aidatalab.R;
import in.ganeshhulyal.aidatalab.others.SharedPrefsManager;

public class ClassName extends AppCompatActivity {

    private Button addClass;
    private AutoCompleteTextView categoryName, subcategoryName;
    protected String categoryString, subcategoryString;
    private ImageView backButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_name);
        context=this;
        initToolbar();
        backButton();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String[] dataCat = new String[]{"Home","Office","Malls","Roads","Sceneries","Flora","Other Water Bodies","Shops","Advertisements","Bus Board","Traffic Signs","Name Boards","Sign Boards", "From Books"};
        String[] subCatWithoutText = new String[]{"People Centric","Non People Centric"};
        String[] subCatTextCentric = new String[]{"Text Centric"};

        ArrayAdapter catAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        dataCat);

        AutoCompleteTextView editTextFilledExposedDropdownCat =
                categoryName.findViewById(R.id.category);
        editTextFilledExposedDropdownCat.setAdapter(catAdapter);

        ArrayAdapter subLocationAdapter =
                new ArrayAdapter<>(
                        this,
                        R.layout.dropdown_menu_popup_item,
                        subCatTextCentric);

        AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
                subcategoryName.findViewById(R.id.subCategory);
        editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);

        categoryName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category=categoryName.getText().toString();
                if(category.equals("Home") || category.equals("Office") || category.equals("Malls") || category.equals("Roads") || category.equals("Sceneries") || category.equals("Flora") || category.equals("Other Water Bodies")){
                    ArrayAdapter subLocationAdapter =
                            new ArrayAdapter<>(
                                    context,
                                    R.layout.dropdown_menu_popup_item,
                                    subCatWithoutText);

                    AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
                            subcategoryName.findViewById(R.id.subCategory);
                    editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);
                } else{
                    ArrayAdapter subLocationAdapter =
                            new ArrayAdapter<>(
                                    context,
                                    R.layout.dropdown_menu_popup_item,
                                    subCatTextCentric);

                    AutoCompleteTextView editTextFilledExposedDropdownSubLoc =
                            subcategoryName.findViewById(R.id.subCategory);
                    editTextFilledExposedDropdownSubLoc.setAdapter(subLocationAdapter);

                }
            }
        });
    }

    private void init() {
        addClass = findViewById(R.id.addClass);
        categoryName = findViewById(R.id.category);
        subcategoryName = findViewById(R.id.subCategory);
        SharedPrefsManager sharedPrefsManager = new SharedPrefsManager(this);
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryString = categoryName.getText().toString();
                subcategoryString = subcategoryName.getText().toString();
                if(subcategoryString.isEmpty()){
                    categoryName.setError("Invalid Input");
                }
                else if(subcategoryString.isEmpty()){
                    categoryName.setError("Inavlid Input");
                }
                else {
                    Intent intent = new Intent(ClassName.this, UserMenu.class);
                    sharedPrefsManager.saveStringValue("categoryName", categoryString);
                    sharedPrefsManager.saveStringValue("subCategoryName", subcategoryString);
                    startActivity(intent);
                }
            }
        });
    }

    private void initToolbar() {
        TextView toolbarName;
        toolbarName = findViewById(R.id.toolbar_name);
        toolbarName.setText("Add Class Name");
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
}
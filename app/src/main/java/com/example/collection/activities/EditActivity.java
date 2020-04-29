package com.example.collection.activities;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.collection.R;
import com.example.collection.database.DBConstants;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;
import com.example.collection.model.Item;

public class EditActivity extends Activity {
    Button cancelButton;
    Button saveButton;

    EditText editTextTitle;
    EditText editTextDescription;
    AutoCompleteTextView typeDropDown;
    EditText editTextAuthor;
    TextView editTextViewAuthor;
    DBQueries dbQueries;
    DBHelper dbHelper;
    ImageView dropDownImageType;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        dbHelper = new DBHelper(this);
        dbQueries = new DBQueries(this);

        cancelButton = (Button) findViewById(R.id.button_cancel);
        saveButton = (Button) findViewById(R.id.button_act_edit);


        editTextTitle = (EditText) findViewById(R.id.editText_title);
        editTextDescription = (EditText) findViewById(R.id.editText_description);
        editTextAuthor = (EditText) findViewById(R.id.editText_author);
        editTextViewAuthor = (TextView) findViewById(R.id.editTextView_author);


        typeDropDown = (AutoCompleteTextView) findViewById(R.id.editText_type);
        dropDownImageType = (ImageView) findViewById(R.id.dropDownArrow);
        typeDropDown.setThreshold(1);

        editTextAuthor.setVisibility(View.INVISIBLE);
        editTextViewAuthor.setVisibility(View.INVISIBLE);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.97), (int) (height * 0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 20;

        getWindow().setAttributes(params);

        Bundle extras = getIntent().getExtras();
        category = null;
        String title = null;
        String description = null;
        String type = null;
        String author = null;
        int id = 0;
        if (extras != null) {
            category = extras.getString("category");
            title = extras.getString("title");
            description = extras.getString("description");
            type = extras.getString("type");
            author = extras.getString("author");
            id = extras.getInt("id");
        }
        if (category.equals(DBConstants.TABLE_NAME_BOOKS)) {
            editTextAuthor.setVisibility(View.VISIBLE);
            editTextViewAuthor.setVisibility(View.VISIBLE);
            getWindow().setLayout((int) (width * 0.9), (int) (height * 0.7));
        }

        editTextTitle.setText(title);
        editTextDescription.setText(description);
        typeDropDown.setText(type);
        if (category.equals(DBConstants.TABLE_NAME_BOOKS)) {
            editTextAuthor.setText(author);
        }
        typeDropDown.setThreshold(1);
        chooseType();

        doCancelButton();
        doSaveButton(category, id);

    }

    public void doCancelButton() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void doSaveButton(final String category, final int id) {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Item item = new Item(id, category, editTextTitle.getText().toString(), editTextDescription.getText().toString(), typeDropDown.getText().toString(), editTextAuthor.getText().toString());

                dbQueries.open();
                dbQueries.updateItem(item);

                //dbQueries.close();
                Toast.makeText(getApplicationContext(), "" + editTextTitle.getText().toString() + " został zmieniony", Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }


    public void chooseType() {
        dropDownImageType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTypes();
                typeDropDown.showDropDown();
            }
        });

    }

    public void setTypes() {
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getTypes());
        typeDropDown.setAdapter(adapterType);
    }

    public String[] getTypes() {
        String[] types = new String[]{""};
        if (!category.isEmpty()) {
            dbQueries.open();
            Cursor res = dbQueries.findTypeByCategory(category);
            // dbQueries.close();
            types = new String[res.getCount()];
            int i = 0;
            while (res.moveToNext()) {
                String type = res.getString(0);
                types[i] = type;
                i++;
            }
        }
        return types;
    }


}

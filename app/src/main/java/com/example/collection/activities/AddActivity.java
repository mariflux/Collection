package com.example.collection.activities;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collection.R;
import com.example.collection.database.DBConstants;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;
import com.example.collection.model.Item;

public class AddActivity extends AppCompatActivity {

    DBQueries dbQueries;
    DBHelper dbHelper;

    TextView author;
    EditText editTitle, editDescription, editAuthor;
    Button addDataButton;

    AutoCompleteTextView categoryDropDown;
    AutoCompleteTextView typeDropDown;
    ImageView dropDownImage;
    ImageView dropDownImageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Dodaj nowy obiekt");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbHelper = new DBHelper(this);
        dbQueries = new DBQueries(this);

        author = (TextView) findViewById(R.id.author);
        editTitle = (EditText) findViewById(R.id.editText_title);
        editDescription = (EditText) findViewById(R.id.editText_description);
        editAuthor = (EditText) findViewById(R.id.editText_author);
        editAuthor.setVisibility(View.INVISIBLE);
        author.setVisibility(View.INVISIBLE);
        addDataButton = (Button) findViewById(R.id.button_addData);
        addData();

        categoryDropDown = (AutoCompleteTextView) findViewById(R.id.chooseCategoryAdd);
        dropDownImage = (ImageView) findViewById(R.id.dropDownImageAdd);

        typeDropDown = (AutoCompleteTextView) findViewById(R.id.editText_type);
        dropDownImageType = (ImageView) findViewById(R.id.dropDownImageAddType);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, DBConstants.CATEGORY);
        categoryDropDown.setAdapter(adapter);
        chooseCategory();

        chooseType();
        categoryDropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String category = categoryDropDown.getText().toString();
                showAuthor(category);
                hideAuthor(category);
            }
        });

        //showAuthor();

    }

    public void showAuthor(String category) {
        //String category = categoryDropDown.getText().toString();
        if (category.equals(DBConstants.TABLE_NAME_BOOKS)) {
            editAuthor.setVisibility(View.VISIBLE);
            author.setVisibility(View.VISIBLE);
        }
    }

    public void hideAuthor(String category) {
        //String category = categoryDropDown.getText().toString();
        if (!category.equals(DBConstants.TABLE_NAME_BOOKS)) {
            editAuthor.setVisibility(View.INVISIBLE);
            author.setVisibility(View.INVISIBLE);
        }
    }

    public void setTypes() {
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getTypes());
        typeDropDown.setAdapter(adapterType);
    }

    public String[] getTypes() {
        String category = categoryDropDown.getText().toString();
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

    public void addData() {
        addDataButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cat = categoryDropDown.getText().toString();
                        String title = editTitle.getText().toString();
                        String description = editDescription.getText().toString();
                        String type = typeDropDown.getText().toString();
                        String author = editAuthor.getText().toString();

                        if (DBConstants.CATEGORY_LIST.contains(cat)) {
                            if (!title.trim().isEmpty()) {
                                dbQueries.open();
                                if (!dbQueries.alreadyInDatabase(cat, title)) {
                                    addItem(cat, title, description, type, author);
                                } else {
                                    showMessage("Powtórka!", "" + title + " znajduje się na naszej liście\n" + "Czy na pewno chcesz go dodać?", cat, title, description, type, author);
                                }
                                //dbQueries.close();

                            } else {
                                Toast.makeText(AddActivity.this, "Podaj tytuł ", Toast.LENGTH_LONG).show();

                            }
                            //editCategory.clearComposingText();
                        } else {
                            Toast.makeText(AddActivity.this, "Podaj dostępną kategorię ", Toast.LENGTH_LONG).show();

                        }
                        if (!title.trim().isEmpty()) {
                            categoryDropDown.setText("");
                            editTitle.setText("");
                            editDescription.setText("");
                            typeDropDown.setText("");
                            if (cat.equals(DBConstants.TABLE_NAME_BOOKS))
                                editAuthor.setText("");
                        }
                    }
                }
        );
    }

    public void chooseCategory() {
        dropDownImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDropDown.showDropDown();

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

    public void addItem(String category, String title, String description, String type, String author) {
        Item item = new Item(category, title, description, type, author);
        dbQueries.open();
        boolean isInserted = dbQueries.insertItem(item);
        //dbQueries.close();
        if (isInserted && !title.trim().isEmpty()) {
            Toast.makeText(AddActivity.this, "Pomyślnie dodano " + title, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddActivity.this, "Wystąpił błąd podczas dodawania " + title, Toast.LENGTH_LONG).show();

        }
    }

    public void showMessage(String msgTitle, String msg, final String category, final String title, final String description, final String type, final String author) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(msgTitle);
        builder.setMessage(msg);
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        addItem(category, title, description, type, author);
                        dialog.cancel();
                    }
                }

        );
        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        dialog.cancel();
                    }
                }
        );
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}

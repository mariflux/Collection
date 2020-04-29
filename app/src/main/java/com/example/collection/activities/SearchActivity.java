package com.example.collection.activities;

import android.content.Intent;
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

public class SearchActivity extends AppCompatActivity {
    Button viewAllFilmsButton;
    Button viewAllTVSeriesButton;
    Button viewAllBooksButton;
    Button viewAllGamesButton;
    DBQueries dbQueries;
    DBHelper dbHelper;

    TextView textViewAuthor;
    EditText editTitle, editDescription, editAuthor;
    Button searchDataButton;

    AutoCompleteTextView categoryDropDown;
    ImageView dropDownImage;

    AutoCompleteTextView typeDropDown;
    ImageView dropDownImageType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Wyszukiwanie");
        dbHelper = new DBHelper(this);
        dbQueries = new DBQueries(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewAllFilmsButton = (Button) findViewById(R.id.button_showFilms);
        viewAllTVSeriesButton = (Button) findViewById(R.id.button_showTVSeries);
        viewAllBooksButton = (Button) findViewById(R.id.button_showBooks);
        viewAllGamesButton = (Button) findViewById(R.id.button_showGames);

        viewAll(DBConstants.TABLE_NAME_FILMS, viewAllFilmsButton);
        viewAll(DBConstants.TABLE_NAME_TV_SERIES, viewAllTVSeriesButton);
        viewAll(DBConstants.TABLE_NAME_BOOKS, viewAllBooksButton);
        viewAll(DBConstants.TABLE_NAME_GAMES, viewAllGamesButton);

        editTitle = (EditText) findViewById(R.id.filterText_title);
        editDescription = (EditText) findViewById(R.id.filterText_description);

        editAuthor = (EditText) findViewById(R.id.filterText_author);
        textViewAuthor = (TextView) findViewById(R.id.filterTextView_author);
        editAuthor.setVisibility(View.INVISIBLE);
        textViewAuthor.setVisibility(View.INVISIBLE);

        searchDataButton = (Button) findViewById(R.id.button_advancedSearch);

        viewFilteredData();


        categoryDropDown = (AutoCompleteTextView) findViewById(R.id.chooseCategoryFilter);
        dropDownImage = (ImageView) findViewById(R.id.dropDownArrow);
        categoryDropDown.setThreshold(1); //write 1 letter to hint category

        typeDropDown = (AutoCompleteTextView) findViewById(R.id.filterText_type);
        dropDownImageType = (ImageView) findViewById(R.id.dropDownArrowType);

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

    }

    public void showAuthor(String category) {
        //String category = categoryDropDown.getText().toString();
        if (category.equals(DBConstants.TABLE_NAME_BOOKS)) {
            editAuthor.setVisibility(View.VISIBLE);
            textViewAuthor.setVisibility(View.VISIBLE);
        }
    }

    public void hideAuthor(String category) {
        //String category = categoryDropDown.getText().toString();
        if (!category.equals(DBConstants.TABLE_NAME_BOOKS)) {
            editAuthor.setVisibility(View.INVISIBLE);
            textViewAuthor.setVisibility(View.INVISIBLE);
        }
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

    public void setTypes() {
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, getTypes());
        typeDropDown.setAdapter(adapterType);
    }

    public String[] getTypes() {
        String category = categoryDropDown.getText().toString();
        // String category = DatabaseHelper.TABLE_NAME_FILMS;
        String[] types = new String[]{""};
        if (!category.isEmpty()) {
            dbQueries.open();
            Cursor res = dbQueries.findTypeByCategory(category);
            //dbQueries.close();
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

    public void viewAll(String category, Button viewButton) {
        final String cat = category;
        viewButton.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              dbQueries.open();

                                              Cursor res = dbQueries.getAllData(cat);
                                             // dbQueries.close();
                                              showList(res, cat, "", "", "", "");
                                          }
                                      }
        );
    }

    public void viewFilteredData() {

        searchDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String category = categoryDropDown.getText().toString();
                final String title = editTitle.getText().toString();
                final String description = editDescription.getText().toString();
                final String type = typeDropDown.getText().toString();

                if (!category.isEmpty()) {
                    if (category.equals(DBConstants.TABLE_NAME_BOOKS)) {
                        final String author = editAuthor.getText().toString();
                        dbQueries.open();
                        Cursor res = dbQueries.getFilteredData(category, title, description, type, false, author);
                        //dbQueries.close();
                        showList(res, category, title, description, type, author);
                    } else {
                        final String author = "";
                        dbQueries.open();

                        Cursor res = dbQueries.getFilteredData(category, title, description, type, false, author);
                        //dbQueries.close();
                        showList(res, category, title, description, type, author);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Wybierz dostępną kategorię ", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void showMessage(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }

    public void showList(Cursor res, String category, String title, String description, String type, String author) {

        if (res.getCount() == 0) {
            Toast.makeText(SearchActivity.this, "Brak obiektów spełniających kryteria ", Toast.LENGTH_LONG).show();
            return;
        } else {


            //Toast.makeText(SearchActivity.this,"Lista nie jest pusta ",Toast.LENGTH_LONG).show();
            moveToFilterListActivity(category, title, description, type, author);

//            StringBuffer sb = new StringBuffer();
//            while (res.moveToNext()){
//                sb
//                        .append("{\n")
//                        .append(DatabaseHelper.COLUMN_NAME_ID).append(" : ").append(res.getString(0)).append("\n")
//                        .append(DatabaseHelper.COLUMN_NAME_TITLE).append(" : ").append(res.getString(1)).append("\n")
//                        .append(DatabaseHelper.COLUMN_NAME_DES).append(" : ").append(res.getString(2)).append("\n")
//                        .append("}\n")
//                ;
//                //.append(DatabaseHelper.)
//            }
            //showMessage(category,sb.toString());
        }
    }

    private void moveToFilterListActivity(String category, String title, String description, String type, String author) {
        Intent intent = new Intent(SearchActivity.this, FilterListActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        intent.putExtra("type", type);
        intent.putExtra("typeEquals", false);
        intent.putExtra("author", author);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }


}

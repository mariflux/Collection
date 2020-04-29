package com.example.collection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collection.R;
import com.example.collection.database.DBConstants;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;

public class TypeActivity extends AppCompatActivity {

    Button filmsButton, tvSeriesButton, booksButton, gamesButton;
    DBQueries dbQueries;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        dbHelper = new DBHelper(this);
        dbQueries = new DBQueries(this);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        filmsButton = (Button) findViewById(R.id.button_showFilms);
        tvSeriesButton = (Button) findViewById(R.id.button_showTVSeries);
        booksButton = (Button) findViewById(R.id.button_showBooks);
        gamesButton = (Button) findViewById(R.id.button_showGames);
        goTo(filmsButton, DBConstants.TABLE_NAME_FILMS);
        goTo(tvSeriesButton,DBConstants.TABLE_NAME_TV_SERIES);
        goTo(booksButton,DBConstants.TABLE_NAME_BOOKS);
        goTo(gamesButton,DBConstants.TABLE_NAME_GAMES);

    }
    private  void goTo(final Button button, final String category){

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbQueries.open();
                int n = dbQueries.findTypeByCategory(category).getCount();
                //dbQueries.close();
                if(n>0){
                moveToFindListActivity(category);}
                else {
                    Toast.makeText(getApplicationContext(), "Brak wyników wyszukiwania dla kategorii "+category , Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void moveToFindListActivity(String category) {
        Intent intent = new Intent(TypeActivity.this, FindListActivity.class);
        intent.putExtra("category",category);
        startActivity(intent);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}

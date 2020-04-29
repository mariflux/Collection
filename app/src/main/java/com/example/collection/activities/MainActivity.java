package com.example.collection.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collection.R;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fb_add;
    FloatingActionButton fb_search;
    FloatingActionButton fb_type;
    FloatingActionButton fb_export;
    FloatingActionButton fb_import;
    DBQueries dbQueries;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Twoja Kolekcja");
        dbHelper = new DBHelper(getApplicationContext());
        dbQueries = new DBQueries(getApplicationContext());


        fb_add = findViewById(R.id.floatingActionButton_add);
        fb_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToAddActivity();
            }
        });

        fb_search = findViewById(R.id.floatingActionButton_search);
        fb_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSearchActivity();
            }
        });

        fb_type = findViewById(R.id.floatingActionButton_type);
        fb_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTypeActivity();
            }
        });

        fb_export = findViewById(R.id.floatingActionButton_export);
        fb_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    moveToExportActivity();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Eksport zakończony niepowodzeniem ", Toast.LENGTH_LONG).show();
                }
            }
        });

        fb_import = findViewById(R.id.floatingActionButton_import);
        fb_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToImportActivity();
            }
        });
    }

    private void moveToSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void moveToAddActivity() {
        Intent intent = new Intent(MainActivity.this, AddActivity.class);
        startActivity(intent);
    }

    private void moveToTypeActivity() {
        Intent intent = new Intent(MainActivity.this, TypeActivity.class);
        startActivity(intent);
    }

    private void moveToExportActivity() throws IOException {
        Intent intent = new Intent(MainActivity.this, SQLite2ExcelActivity.class);
        startActivity(intent);
    }

    private void moveToImportActivity() {
        Intent intent = new Intent(MainActivity.this, Excel2SQLiteActivity.class);
        startActivity(intent);
    }


}

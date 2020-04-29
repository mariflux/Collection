package com.example.collection.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.example.collection.R;
import com.example.collection.database.DBConstants;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;
import com.example.collection.model.Item;
import com.example.collection.util.DBUtil;

import java.io.File;
import java.util.ArrayList;

public class SQLite2ExcelActivity extends AppCompatActivity {

    Button btnExport;

    ArrayList<ArrayList<Item>> itemList = new ArrayList<>();

    DBHelper dbHelper;
    DBQueries dbQueries;
    EditText editPath;
    String dir;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_2_excel);
        setTitle("Eksportowanie");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(getApplicationContext());
        dbQueries = new DBQueries(getApplicationContext());

        dir = this.getExternalFilesDirs("Backup")[0].getAbsolutePath() + "/";
        btnExport = (Button) findViewById(R.id.btn_export);
        editPath = (EditText) findViewById(R.id.editPath);
        editPath.setText(dir);

        dbQueries.open();
        itemList = dbQueries.readItems();
        //dbQueries.close();


        btnExport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(final View view) {

                String directory_path = createFileDirectory();
                SQLiteToExcel sqliteToExcel = new SQLiteToExcel(getApplicationContext(), DBHelper.DB_NAME, directory_path);
                sqliteToExcel.exportSpecificTables(DBConstants.CATEGORY_LIST, "Kolekcja.xls", new SQLiteToExcel.ExportListener() {

                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String filePath) {
                        DBUtil.showSnackBar(view, "Successfully Exported" + filePath);
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
            }
        });
    }

    //  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String createFileDirectory() {
        // + File.separator + "BackupBase";

        //create new file directory object
        File directory = new File(dir);
        if (!directory.exists())
            Toast.makeText(this,
                    (directory.mkdirs() ? "Directory has been created" : "Directory not created"),
                    Toast.LENGTH_SHORT).show();
        //else
        //Toast.makeText(this, "Directory exists", Toast.LENGTH_SHORT).show();

        return dir;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }

}
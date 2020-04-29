package com.example.collection.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collection.R;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;
import com.example.collection.database.ExcelToSQLite;
import com.example.collection.util.DBUtil;

import java.io.File;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Excel2SQLiteActivity extends AppCompatActivity {

    EditText edtFilePath;
    Button btnImport;
    DBHelper dbHelper;
    DBQueries dbQueries;
    String directory_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel_2_sqlite);
        directory_path =this.getExternalFilesDirs("Backup")[0].getAbsolutePath()+"/Kolekcja.xls";
        dbHelper = new DBHelper(getApplicationContext());
        dbQueries = new DBQueries(getApplicationContext());
        setTitle("Importowanie");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtFilePath = (EditText) findViewById(R.id.edt_file_path);
        btnImport = (Button) findViewById(R.id.btn_import);
        edtFilePath.setText(directory_path);
        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                File file = new File(directory_path);
                if (!file.exists()) {
                    DBUtil.showSnackBar(view, "No file");
                    return;
                }
                dbQueries.open();
                // Is used to import data from excel without dropping table
                // ExcelToSQLite excelToSQLite = new ExcelToSQLite(getApplicationContext(), DBHelper.DB_NAME);

                // if you want to add column in excel and import into DB, you must drop the table
                ExcelToSQLite excelToSQLite = new ExcelToSQLite(getApplicationContext(), DBHelper.DB_NAME, true);
                // Import EXCEL FILE to SQLite
                excelToSQLite.importFromFile(directory_path, new ExcelToSQLite.ImportListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onCompleted(String dbName) {
                        DBUtil.showSnackBar(view, "Excel imported into " + dbName);
                    }

                    @Override
                    public void onError(Exception e) {
                        DBUtil.showSnackBar(view, "Error : " + e.getMessage());
                    }
                });
                //dbQueries.close();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}
package com.example.collection.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collection.R;
import com.example.collection.activities.adapters.TypeListAdapter;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;

import java.util.ArrayList;

public class FindListActivity extends AppCompatActivity {


    ArrayList<String> itemListType;
    ArrayList<Integer> itemListCount;
    DBQueries dbQueries;
    DBHelper dbHelper;
    ListView listView;
    //MyAdapter adapter;
    TypeListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_list);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemListType = new ArrayList<>();
        itemListCount = new ArrayList<>();
        dbHelper = new DBHelper(this);
        dbQueries = new DBQueries(this);

        Bundle extras = getIntent().getExtras();
        String category = null;

        if (extras != null) {
            category = extras.getString("category");
        }
        setTitle(category);


        listView = (ListView) findViewById(R.id.listView_types);
        // now create an adapter class
        getFilteredList(category);

    }


    public void getFilteredList(final String category) {
        dbQueries.open();

        Cursor res = dbQueries.findTypeByCategory(category);
        //dbQueries.close();

        while (res.moveToNext()) {
            String type = res.getString(0);
            dbQueries.open();
            Cursor countRes = dbQueries.findItemsByType(category, type);
            //dbQueries.close();
            itemListType.add(type);
            itemListCount.add(countRes.getCount());
        }


        adapter = new TypeListAdapter(this, category, itemListType, itemListCount);
        adapter.setNotifyOnChange(true);
        listView.setAdapter(adapter);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}

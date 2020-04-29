package com.example.collection.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collection.R;
import com.example.collection.activities.adapters.ListAdapter;
import com.example.collection.database.DBConstants;
import com.example.collection.database.DBHelper;
import com.example.collection.database.DBQueries;
import com.example.collection.model.Item;

import java.util.ArrayList;

public class FilterListActivity extends AppCompatActivity {//} implements ListAdapter.customButtonListener {

    ArrayList<String> itemListTitle;
    ArrayList<String> itemListDescription;
    ArrayList<String> itemListType;
    ArrayList<String> itemListAuthor;
    ArrayList<Item> itemList;
    DBQueries dbQueries;
    DBHelper dbHelper;
    ListView listView;
    //MyAdapter adapter;
    ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);
        setTitle("Wyniki wyszukiwania");
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemListTitle = new ArrayList<>();
        itemListDescription = new ArrayList<>();
        itemListType = new ArrayList<>();
        itemListAuthor = new ArrayList<>();
        itemList = new ArrayList<>();
        dbHelper = new DBHelper(this);
        dbQueries = new DBQueries(this);

        Bundle extras = getIntent().getExtras();
        String category = null;
        String title = null;
        String description = null;
        String type = null;
        String author = null;
        boolean typeEquals = false;

        if (extras != null) {
            category = extras.getString("category");
            title = extras.getString("title");
            description = extras.getString("description");
            type = extras.getString("type");
            typeEquals = extras.getBoolean("typeEquals");
            author = extras.getString("author");

        }


        listView = (ListView) findViewById(R.id.listView_items);
        // now create an adapter class
        getFilteredList(category, title, description, type, typeEquals, author);

    }


    public void getFilteredList(final String category, String title, String description, String type, boolean typeEquals, String author) {
        dbQueries.open();
        Cursor res = dbQueries.getFilteredData(category, title, description, type, typeEquals, author);
       // dbQueries.close();
        while (res.moveToNext()) {
            itemListTitle.add(res.getString(1));
            itemListDescription.add(res.getString(2));
            itemListType.add(res.getString(3));
            Item item = null;
            int itemId = res.getInt(0);
            String itemTitle = res.getString(1);
            String itemType = res.getString(3);
            String itemDescription = res.getString(2);
            if (category.equals(DBConstants.TABLE_NAME_BOOKS)) {
                String itemAuthor = res.getString(4);
                item = new Item(itemId, category, itemTitle, itemDescription, itemType, itemAuthor);
            } else {
                item = new Item(itemId, category, itemTitle, itemDescription, itemType);
            }
            itemList.add(item);
        }


        adapter = new ListAdapter(this, itemList, category, itemListTitle);
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

package com.example.collection.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.collection.model.Item;

import java.util.ArrayList;

public class DBQueries {

    private Context context;
    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public DBQueries(Context context) {
        this.context = context;

    }

    public DBQueries open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    // Users
    public boolean insertItem(Item item) {
        String table = item.getCategory();
        ContentValues contentValues = new ContentValues();
       // contentValues.put(DBConstants.COLUMN_NAME_ID, item.getId());
        contentValues.put(DBConstants.COLUMN_NAME_TITLE, item.getTitle());
        contentValues.put(DBConstants.COLUMN_NAME_DES, item.getDescription());
        contentValues.put(DBConstants.COLUMN_NAME_TYPE, item.getType());
        if (table.equals(DBConstants.TABLE_NAME_BOOKS)) {
            contentValues.put(DBConstants.COLUMN_NAME_AUTHOR, item.getAuthor());
        }

        return database.insert(table, null, contentValues) > -1;
    }

    public ArrayList<ArrayList<Item>> readItems() {
        ArrayList<ArrayList<Item>> list = new ArrayList<>();
        ArrayList<Item> temp ;
        try {
            list.clear();
            for (String cat : DBConstants.CATEGORY_LIST) {
                Cursor cursor;
                database = dbHelper.getReadableDatabase();
                cursor = database.rawQuery(DBConstants.sqlSelectAll(cat), null);
                temp= new ArrayList<>();
                if (cursor.getCount() > 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            int id = cursor.getInt(cursor.getColumnIndex(DBConstants.COLUMN_NAME_ID));
                            String title = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_TITLE));
                            String description = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_DES));
                            String type = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_TYPE));
                            Item item = null;
                            if (cat.equals(DBConstants.TABLE_NAME_BOOKS)) {
                                String author = cursor.getString(cursor.getColumnIndex(DBConstants.COLUMN_NAME_AUTHOR));
                                item = new Item(id, cat, title, description, type, author);
                            } else {
                                item = new Item(id, cat, title, description, type);
                            }

                            temp.add(item);
                        } while (cursor.moveToNext());
                    }
                    list.add(temp);
                }
                cursor.close();

            }


        } catch (Exception e) {
            Log.v("Exception", e.getMessage());
        }
        return list;
    }



    public void deleteItem(String category, int id) {

        String whereClause = " " + DBConstants.COLUMN_NAME_ID + "=" + id + " ";
        database.delete(category, whereClause, null);

    }



    public Cursor getAllData(String category) {

        String sql = DBConstants.sqlSelectAll(category);
        Cursor res = database.rawQuery(sql, null);
        return res;
    }

    public Cursor getFilteredData(String category, String title, String description, String type, boolean typeEquals,String author) {

        StringBuilder sb = new StringBuilder();
        sb
                .append("select distinct * from ")
                .append(category);
        if (!title.isEmpty() || !description.isEmpty() || !type.isEmpty() || !author.isEmpty()) {
            sb.append(" where ");
            if (!title.isEmpty()) {
                sb.append(DBConstants.COLUMN_NAME_TITLE).append(" like '%").append(title.trim()).append("%' ");
                if (!description.isEmpty() || !type.isEmpty() || !author.isEmpty()) {
                    sb.append(" AND ");

                }
            }
            if (!description.isEmpty()) {
                sb.append(DBConstants.COLUMN_NAME_DES).append(" like '%").append(description.trim()).append("%' ");
                if (!type.isEmpty() || !author.isEmpty()) {
                    sb.append(" AND ");

                }
            }
            if (!type.isEmpty()) {
                if (typeEquals) {
                    sb.append(DBConstants.COLUMN_NAME_TYPE).append(" = '").append(type.trim()).append("' ");
                } else {
                    sb.append(DBConstants.COLUMN_NAME_TYPE).append(" like '%").append(type.trim()).append("%' ");
                }
                if (!author.isEmpty()) {
                    sb.append(" AND ");

                }
            }
            if(category.equals(DBConstants.TABLE_NAME_BOOKS) && !author.isEmpty()){
                sb.append(DBConstants.COLUMN_NAME_AUTHOR).append(" like '%").append(author.trim()).append("%' ");

            }

        }
        sb.append(";");

        Cursor res = database.rawQuery(sb.toString(), null);
        return res;
    }

    public boolean alreadyInDatabase(String category, String title) {

        String sql = "select * from " + category + " where " + DBConstants.COLUMN_NAME_TITLE + "='" + title + "';";

        Cursor res = database.rawQuery(sql, null);
//        if (res.getCount() == 0) {
//            return false;
//        } else {
//            return true;
//        }

        return res.getCount() != 0;

    }

    public Cursor findTypeByCategory(String category) {
        String sql = "select distinct " + DBConstants.COLUMN_NAME_TYPE + " from " + category;
        Cursor res = database.rawQuery(sql, null);
        return res;
    }

    public Cursor findItemsByType(String category, String type) {
        String sql = "select * from " + category + " where " + DBConstants.COLUMN_NAME_TYPE + " ='" + type + "';";
        Cursor res = database.rawQuery(sql, null);
        return res;
    }


    public void updateItem(String category, String title, String description, String type,String author, int id) {
        String whereClause = " " + DBConstants.COLUMN_NAME_ID + "=" + id + " ";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.COLUMN_NAME_TITLE, title);
        contentValues.put(DBConstants.COLUMN_NAME_DES, description);
        contentValues.put(DBConstants.COLUMN_NAME_TYPE, type);
        if(category.equals(DBConstants.TABLE_NAME_BOOKS)){
            contentValues.put(DBConstants.COLUMN_NAME_AUTHOR,author);
        }
        database.update(category, contentValues, whereClause, null);

    }
    public void updateItem(Item item) {
        String whereClause = " " + DBConstants.COLUMN_NAME_ID + "=" + item.getId() + " ";
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBConstants.COLUMN_NAME_TITLE, item.getTitle());
        contentValues.put(DBConstants.COLUMN_NAME_DES, item.getDescription());
        contentValues.put(DBConstants.COLUMN_NAME_TYPE, item.getType());
        if(item.getCategory().equals(DBConstants.TABLE_NAME_BOOKS)){
            contentValues.put(DBConstants.COLUMN_NAME_AUTHOR,item.getAuthor());
        }
        String category = item.getCategory();
        database.update(category, contentValues, whereClause, null);

    }


}

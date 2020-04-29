package com.example.collection.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    public static String DB_NAME = "MojaKolekcja";
    private static int DB_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBConstants.CREATE_TABLE_FILMS);
        db.execSQL(DBConstants.CREATE_TABLE_TV_SERIES);
        db.execSQL(DBConstants.CREATE_TABLE_BOOKS);
        db.execSQL(DBConstants.CREATE_TABLE_GAMES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME_FILMS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME_TV_SERIES);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + DBConstants.TABLE_NAME_GAMES);

    }
}

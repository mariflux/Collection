package com.example.collection.database;

import java.util.ArrayList;
import java.util.Arrays;

public class DBConstants {

    public static final String TABLE_NAME_FILMS = "Filmy";
    public static final String TABLE_NAME_TV_SERIES = "Seriale";
    public static final String TABLE_NAME_BOOKS = "Książki";
    public static final String TABLE_NAME_GAMES = "Gry";
    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_TITLE = "Tytuł";
    public static final String COLUMN_NAME_TYPE = "Gatunek";
    public static final String COLUMN_NAME_AUTHOR = "Autor";
    public static final String COLUMN_NAME_DES = "Opis";
    public static final String[] CATEGORY = new String[]{TABLE_NAME_FILMS, TABLE_NAME_TV_SERIES, TABLE_NAME_BOOKS, TABLE_NAME_GAMES};
    public static final ArrayList<String> CATEGORY_LIST = new ArrayList<>(Arrays.asList(CATEGORY));

    //static final String SELECT_QUERY = "SELECT * FROM " + USER_TABLE;
    static final String CREATE_TABLE_FILMS = sqlCreateTable(TABLE_NAME_FILMS);
    static final String CREATE_TABLE_TV_SERIES = sqlCreateTable(TABLE_NAME_TV_SERIES);
    static final String CREATE_TABLE_BOOKS = sqlCreateTable(TABLE_NAME_BOOKS);
    static final String CREATE_TABLE_GAMES = sqlCreateTable(TABLE_NAME_GAMES);

    protected static String sqlCreateTable(String tableName) {
        StringBuilder sqlQuery = new StringBuilder()
                .append("CREATE TABLE  ")
                .append(tableName)
                .append("(")
                .append(COLUMN_NAME_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(COLUMN_NAME_TITLE).append(" TEXT,")
                .append(COLUMN_NAME_DES).append(" TEXT,")
                .append(COLUMN_NAME_TYPE).append(" TEXT");
        if (tableName.equals(TABLE_NAME_BOOKS)) {
            sqlQuery.append(",").append(COLUMN_NAME_AUTHOR).append(" TEXT");
        }

        sqlQuery.append(" );");
        return sqlQuery.toString();
    }

    public static String sqlSelectAll(String table) {
        return "SELECT * FROM " + table + ";";

    }
}

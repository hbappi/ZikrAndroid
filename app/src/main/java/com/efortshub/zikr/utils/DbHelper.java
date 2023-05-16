package com.efortshub.zikr.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static DbHelper dbHelper;


    public static final String SQL_CREATE_HISTORY_ENTRIES =
            "CREATE TABLE history_en (" +
                    HistoryReaderCoontract.HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_GLOBAL_ID + " INTEGER UNIQUE," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_SEGMENT_ID + " TEXT," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_READ_TIME + " TEXT)";

    public static final String SQL_CREATE_HISTORY_BN_ENTRIES =
            "CREATE TABLE history_bn (" +
                    HistoryReaderCoontract.HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_GLOBAL_ID + " INTEGER UNIQUE," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_SEGMENT_ID + " TEXT," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_READ_TIME + " TEXT)";

    private static final String SQL_DELETE_HISTORY_ENTRIES =
            "DROP TABLE IF EXISTS history_en";
    private static final String SQL_DELETE_HISTORY_BN_ENTRIES =
            "DROP TABLE IF EXISTS history_bn";


    //======================================================================================================================
    private static final String SQL_CREATE_FAVORITE_ENTRIES =
            "CREATE TABLE favorite_en (" +
                    FavotireReaderCoontract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                    FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_GLOBAL_ID + " INTEGER UNIQUE," +
                    FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_SEGMENT_ID + " TEXT)";
    private static final String SQL_CREATE_FAVORITE_BN_ENTRIES =
            "CREATE TABLE favorite_bn (" +
                    FavotireReaderCoontract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                    FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_GLOBAL_ID + " INTEGER UNIQUE," +
                    FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_SEGMENT_ID + " TEXT)";

    private static final String SQL_DELETE_FAVORITE_ENTRIES =
            "DROP TABLE IF EXISTS favorite_en";
    private static final String SQL_DELETE_FAVORITE_BN_ENTRIES =
            "DROP TABLE IF EXISTS favorite_bn";


    public static DbHelper getInstance(Context context) {
        if (dbHelper == null) dbHelper = new DbHelper(context);


        return dbHelper;
    }

    public static final String DB_NAME = "hbdb";
    public static final int DB_VERSION = 4;



    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_HISTORY_ENTRIES);
        db.execSQL(SQL_CREATE_FAVORITE_ENTRIES);
        db.execSQL(SQL_CREATE_HISTORY_BN_ENTRIES);
        db.execSQL(SQL_CREATE_FAVORITE_BN_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_HISTORY_ENTRIES);
        db.execSQL(SQL_DELETE_FAVORITE_ENTRIES);
        db.execSQL(SQL_DELETE_HISTORY_BN_ENTRIES);
        db.execSQL(SQL_DELETE_FAVORITE_BN_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

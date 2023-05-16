package com.efortshub.zikr.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static DbHelper dbHelper;
    public static DbHelper getInstance(Context context){
        if(dbHelper==null) dbHelper = new DbHelper(context);

        return dbHelper;
    }
    public static final String DB_NAME = "hbdb";
    public static final int DB_VERSION = 1;


    private static final String SQL_CREATE_HISTORY_ENTRIES =
            "CREATE TABLE " + HistoryReaderCoontract.HistoryEntry.TABLE_NAME + " (" +
                    HistoryReaderCoontract.HistoryEntry._ID + " INTEGER PRIMARY KEY," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_GLOBAL_ID + " INTEGER," +
                    HistoryReaderCoontract.HistoryEntry.COLUMN_READ_TIME + " TEXT)";

    private static final String SQL_DELETE_HISTORY_ENTRIES =
            "DROP TABLE IF EXISTS " + HistoryReaderCoontract.HistoryEntry.TABLE_NAME;



    //======================================================================================================================
    private static final String SQL_CREATE_FAVORITE_ENTRIES =
            "CREATE TABLE " + FavotireReaderCoontract.FavoriteEntry.TABLE_NAME + " (" +
                    FavotireReaderCoontract.FavoriteEntry._ID + " INTEGER PRIMARY KEY," +
                    FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_GLOBAL_ID + " INTEGER)";

    private static final String SQL_DELETE_FAVORITE_ENTRIES =
            "DROP TABLE IF EXISTS " + FavotireReaderCoontract.FavoriteEntry.TABLE_NAME;


    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME,null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_HISTORY_ENTRIES);
        db.execSQL(SQL_CREATE_FAVORITE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_HISTORY_ENTRIES);
        db.execSQL(SQL_DELETE_FAVORITE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

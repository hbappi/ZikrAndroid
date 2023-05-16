package com.efortshub.zikr.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.efortshub.zikr.models.Favorite;
import com.efortshub.zikr.models.History;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DbUtils {
    public static void addToHistory(Context context, int duaId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_GLOBAL_ID, duaId);
            values.put(HistoryReaderCoontract.HistoryEntry.COLUMN_READ_TIME, System.currentTimeMillis() + "");
            db.insert(HistoryReaderCoontract.HistoryEntry.TABLE_NAME, null, values);

        });

    }

    public static void addToFavorite(Context context, int duaId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_GLOBAL_ID, duaId);
            db.insertWithOnConflict(FavotireReaderCoontract.FavoriteEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_ROLLBACK);

        });

    }


    public static List<History> getAllHistory(Context context) {
        SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_GLOBAL_ID,
                HistoryReaderCoontract.HistoryEntry.COLUMN_READ_TIME
        };

// Filter results WHERE "title" = 'My Title'
//        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                HistoryReaderCoontract.HistoryEntry.COLUMN_READ_TIME + " DESC";

        Cursor cursor = db.query(
                HistoryReaderCoontract.HistoryEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<History> list = new ArrayList<>();

        while (cursor.moveToNext()){
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(HistoryReaderCoontract.HistoryEntry._ID)
            );
            int duaId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(HistoryReaderCoontract.HistoryEntry.COLUMN_DUA_GLOBAL_ID)
            );
            String time = cursor.getString(
                    cursor.getColumnIndexOrThrow(HistoryReaderCoontract.HistoryEntry.COLUMN_READ_TIME)
            );
            list.add(new History(itemId, duaId, Integer.parseInt(time)));
        }

        return list;

    }

    public static List<Favorite> getAllFavorite(Context context) {
        SQLiteDatabase db = DbHelper.getInstance(context).getReadableDatabase();

// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_GLOBAL_ID
        };

// Filter results WHERE "title" = 'My Title'
//        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
//        String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_GLOBAL_ID + " ASC";

        Cursor cursor = db.query(
                FavotireReaderCoontract.FavoriteEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Favorite> list = new ArrayList<>();

        while (cursor.moveToNext()){
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(FavotireReaderCoontract.FavoriteEntry._ID)
            );
            int duaId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavotireReaderCoontract.FavoriteEntry.COLUMN_DUA_GLOBAL_ID)
            );
            list.add(new Favorite(itemId, duaId));
        }

        return list;

    }




}

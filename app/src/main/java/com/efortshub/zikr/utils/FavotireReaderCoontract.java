package com.efortshub.zikr.utils;

import android.provider.BaseColumns;

public final class FavotireReaderCoontract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FavotireReaderCoontract() {}

    /* Inner class that defines the table contents */
    public static class FavoriteEntry implements BaseColumns {
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_DUA_GLOBAL_ID = "dua_id";
    }
}

package com.efortshub.zikr.utils;

import android.provider.BaseColumns;

public final class HistoryReaderCoontract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HistoryReaderCoontract() {}

    /* Inner class that defines the table contents */
    public static class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_DUA_GLOBAL_ID = "dua_id";
        public static final String COLUMN_READ_TIME = "time";
    }
}

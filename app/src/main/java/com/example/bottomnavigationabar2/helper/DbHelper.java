package com.example.bottomnavigationabar2.helper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.bottomnavigationabar2.utils.HistorySearchUtil;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DbHelper(@Nullable Context context, @Nullable String name, int version, @NonNull SQLiteDatabase.OpenParams openParams) {
        super(context, name, version, openParams);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HistorySearchUtil.CREATE_HISTORY_SEARCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

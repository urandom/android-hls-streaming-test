package org.sugr.androidhlsstreaming.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;

    private static final String CREATE_PENDING_REGISTRATION = ""
            + "CREATE TABLE " + PendingRegistration.TABLE + "("
            + PendingRegistration.EMAIL + " TEXT NOT NULL UNIQUE,"
            + PendingRegistration.DATE + " INTEGER NOT NULL"
            + ")";

    public DBOpenHelper(Context context) {
        super(context, "hls.db", null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PENDING_REGISTRATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

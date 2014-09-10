package com.firstapp.appletopmusic.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by nahid on 9/7/14.
 */
public class SongDao extends SQLiteOpenHelper {
    static final String TAG = "SongDao";
    static final String DB_NAME = "appletoplistfav.db";
    static final int DB_VERSION = 1; //
    public static final String TABLE = "fav_songs"; //
    public static final String C_NAME = "name";
    public static final String C_ARTIST = "artist";
    public static final String C_COVERIMAGE = "cover_image";
    Context context;

    public SongDao(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE + " (" + C_NAME + " text primary key, "
                + C_ARTIST + " text, " + C_COVERIMAGE + " blob)"; //
        db.execSQL(sql); //
        Log.d(TAG, "onCreated sql: " + sql);
    }

    // Called whenever newVersion != oldVersion
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //
        // Typically do ALTER TABLE statements, but...we're just in development,
        // so:
        db.execSQL("drop table if exists " + TABLE); // drops the old database
        Log.d(TAG, "onUpdated");
        onCreate(db); // run onCreate to get new database
    }
}

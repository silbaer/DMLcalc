package de.silbaer.dmlcalc;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.silbaer.dmlcalc.BreedCacheContract;

/**
 * Created by silbaer on 11.02.17.
 */

public class BreedCacheDbHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BreedCacheContract.BreedCacheEntry.TABLE_NAME + " (" +
                    BreedCacheContract.BreedCacheEntry._ID + " INTEGER PRIMARY KEY," +
                    BreedCacheContract.BreedCacheEntry.COLUMN_NAME_KEY + " TEXT," +
                    BreedCacheContract.BreedCacheEntry.COLUMN_NAME_VALUE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BreedCacheContract.BreedCacheEntry.TABLE_NAME;


    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BreedCache.db";

    public BreedCacheDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

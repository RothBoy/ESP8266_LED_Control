package de.axeldiewald.ESP8266_LED_Control.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Axel on 04.11.2015.
 */
public class mySQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "FavouritesDatabase";
    public final static String TABLE_NAME = "FavouritesTable";
    public final static String FAVOURITE_ID = "_ID";
    public final static String FAVOURITE_NAME = "name";
    public final static String FAVOURITE_RED = "redValue";
    public final static String FAVOURITE_GREEN = "greenValue";
    public final static String FAVOURITE_BLUE = "blueValue";

    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME +
            " ( " + FAVOURITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            FAVOURITE_NAME + " TEXT NOT NULL," +
            FAVOURITE_RED + " INTEGER, " +
            FAVOURITE_GREEN + " INTEGER, " +
            FAVOURITE_BLUE + " INTEGER );";

    public mySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(mySQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public long createRecord(String name, int red, int green, int blue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAVOURITE_NAME, name);
        values.put(FAVOURITE_RED, red);
        values.put(FAVOURITE_GREEN, green);
        values.put(FAVOURITE_BLUE, blue);
        long idReturned = db.insert(TABLE_NAME, null, values);
        db.close();
        return idReturned;
    }

    public int deleteRecord(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, FAVOURITE_ID + "=" + String.valueOf(id), null);
        db.close();
        return rows;
    }

    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] cols = new String[] {
                FAVOURITE_ID,
                FAVOURITE_NAME,
                FAVOURITE_RED,
                FAVOURITE_GREEN,
                FAVOURITE_BLUE};
        Cursor mCursor = db.query(true, TABLE_NAME,
                cols, null, null, null, null, null, null);
        return mCursor; // iterate to get each value.
    }

}

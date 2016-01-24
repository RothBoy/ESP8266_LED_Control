package de.axeldiewald.ESP8266_LED_Control.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NAME = "BundleDatabase";
    public final static String BUNDLE_ID = "_ID";
    public final static String BUNDLE_NAME = "name";
    public final static String FAVOURITE_TABLE_NAME = "FavouritesTable";
    public final static String ALARM_TABLE_NAME = "AlarmsTable";
    public static String[] FAVOURITE_BUNDLE_VALUE_NAME;
    public static String[] ALARM_BUNDLE_VALUE_NAME;

    private static String FAVOURITE_TABLE_CREATE = "CREATE TABLE FavouritesTable ( " +
            BUNDLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            BUNDLE_NAME + " TEXT NOT NULL, " +
            "redValue INTEGER, greenValue INTEGER, blueValue INTEGER );";

    private static String ALARM_TABLE_CREATE = "CREATE TABLE AlarmsTable ( " +
            BUNDLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            BUNDLE_NAME + " TEXT NOT NULL," +
            "hour INTEGER, minute INTEGER, second INTEGER );";

    public mySQLHelper(Context context, String[] pValueName1, String[] pValueName2) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        FAVOURITE_BUNDLE_VALUE_NAME = pValueName1;
        ALARM_BUNDLE_VALUE_NAME = pValueName2;
        /*FAVOURITE_TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " ( " +
                BUNDLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                BUNDLE_NAME + " TEXT NOT NULL, ";
        int i = 1;
        for (String valueName: FAVOURITE_BUNDLE_VALUE_NAME){
            FAVOURITE_TABLE_CREATE += valueName + " INTEGER";
                if (i != FAVOURITE_BUNDLE_VALUE_NAME.length){
                    FAVOURITE_TABLE_CREATE += ", ";
                }
                i++;
        }
        FAVOURITE_TABLE_CREATE += ");";*/
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAVOURITE_TABLE_CREATE);
        db.execSQL(ALARM_TABLE_CREATE);
        Log.w(mySQLHelper.class.getName(),
                "CREATING NEW TABLE!!!!!!!!!!!!!!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database,int oldVersion,int newVersion){
        Log.w(mySQLHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + FAVOURITE_TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + ALARM_TABLE_NAME);
        onCreate(database);
    }

    public long createRecord(String TABLE_NAME, String[] BUNDLE_VALUE_NAME, String name,
                             int[] pValues){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUNDLE_NAME, name);
        int i = 0;
        for (String value: BUNDLE_VALUE_NAME){
            values.put(value, pValues[i]);
            i++;
        }
        long idReturned = db.insert(TABLE_NAME, null, values);
        db.close();
        return idReturned;
    }

    public int deleteRecord(String TABLE_NAME, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_NAME, BUNDLE_ID + "=" + String.valueOf(id), null);
        db.close();
        return rows;
    }

    public Cursor getAllRecords(String pBundleClassName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] colsTemp = new String[] { BUNDLE_ID, BUNDLE_NAME};
        String[] cols;
        if (pBundleClassName == "Favourite") {
            cols = new String[colsTemp.length + FAVOURITE_BUNDLE_VALUE_NAME.length];
            System.arraycopy(colsTemp, 0, cols, 0, colsTemp.length);
            System.arraycopy(FAVOURITE_BUNDLE_VALUE_NAME, 0, cols, colsTemp.length,
                    FAVOURITE_BUNDLE_VALUE_NAME.length);
        } else {
            cols = new String[colsTemp.length + ALARM_BUNDLE_VALUE_NAME.length];
            System.arraycopy(colsTemp, 0, cols, 0, colsTemp.length);
            System.arraycopy(ALARM_BUNDLE_VALUE_NAME, 0, cols, colsTemp.length,
                    ALARM_BUNDLE_VALUE_NAME.length);
        }
        return db.query(true, pBundleClassName + "sTable",
                cols, null, null, null, null, null, null);
    }

}

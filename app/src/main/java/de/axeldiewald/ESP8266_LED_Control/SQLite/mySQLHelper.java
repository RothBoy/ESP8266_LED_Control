package de.axeldiewald.ESP8266_LED_Control.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class mySQLHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 16;
    private static final String DATABASE_NAME = "BundleDatabase";
    public final static String BUNDLE_ID = "_ID";
    public final static String BUNDLE_NAME = "name";
    public final static String FAVOURITE_TABLE_NAME = "FavouritesTable";
    public final static String ALARM_TABLE_NAME = "AlarmsTable";
    public final static String[] FAVOURITE_BUNDLE_VALUE_NAME = {"red", "green", "blue"};
    public final static String[] ALARM_BUNDLE_VALUE_NAME = {"hour", "minute", "second"};
    private static String FAVOURITE_TABLE_CREATE;
    private static String ALARM_TABLE_CREATE;

    public mySQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        FAVOURITE_TABLE_CREATE = createCreateCommand(FAVOURITE_TABLE_NAME, FAVOURITE_BUNDLE_VALUE_NAME);
        ALARM_TABLE_CREATE = createCreateCommand(ALARM_TABLE_NAME, ALARM_BUNDLE_VALUE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FAVOURITE_TABLE_CREATE);
        db.execSQL(ALARM_TABLE_CREATE);
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

    public long createRecord(String pTableName, String pBundleName, String[] pBundleValueName,
                             int[] pBundleValue){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUNDLE_NAME, pBundleName);
        int i = 0;
        for (String value: pBundleValueName){
            values.put(value, pBundleValue[i]);
            i++;
        }
        long idReturned = db.insert(pTableName, null, values);
        db.close();
        return idReturned;
    }

    public int deleteRecord(String pTableName, int pBundleId){
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(pTableName, BUNDLE_ID + "=" + String.valueOf(pBundleId), null);
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

    public String createCreateCommand(String pTableName, String[] pBundleValueName) {
        String tableCreateCommand = "CREATE TABLE " + pTableName + " ( " +
                BUNDLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                BUNDLE_NAME + " TEXT NOT NULL, ";
        int i = 1;
        for (String valueName: pBundleValueName){
            tableCreateCommand += valueName + " INTEGER";
            if (i != pBundleValueName.length){
                tableCreateCommand += ", ";
            }
            i++;
        }
        tableCreateCommand += " );";
        return tableCreateCommand;
    }

}

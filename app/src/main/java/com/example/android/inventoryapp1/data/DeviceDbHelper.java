package com.example.android.inventoryapp1.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventoryapp1.data.DeviceContract.DeviceEntry;

public class DeviceDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public DeviceDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_DEVICE_TABLE = "CREATE TABLE " + DeviceEntry.TABLE_NAME + " ("
                + DeviceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DeviceEntry.COLUMN_DEVICE_NAME + " TEXT NOT NULL, "
                + DeviceEntry.COLUMN_DEVICE_SUPPLIER + " TEXT NOT NULL, "
                + DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER + " TEXT, "
                + DeviceEntry.COLUMN_DEVICE_TYPE + " INTEGER NOT NULL, "
                + DeviceEntry.COLUMN_DEVICE_QUANTITY + " INTEGER NOT NULL DEFAULT 1,"
                + DeviceEntry.COLUMN_DEVICE_PRICE + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_DEVICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}

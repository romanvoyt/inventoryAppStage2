package com.example.android.inventoryapp1.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

import com.example.android.inventoryapp1.data.DeviceContract.DeviceEntry;

/**
 * {@link ContentProvider} for Pets app.
 */
public class DeviceProvider extends ContentProvider {
    public static final String LOG_TAG = DeviceProvider.class.getSimpleName();
    private DeviceDbHelper mDbHelper;
    private static final int DEVICES = 100;
    private static final int DEVICES_ID = 101;
    private static final int ADD_DEVICE_ID = 102;
    private static final int REMOVE_DEVICE_ID = 103;

    private SQLiteStatement ADD = null;
    private SQLiteStatement REMOVE = null;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DeviceContract.CONTENT_AUTHORITY, DeviceContract.PATH_DEVICES, DEVICES);
        sUriMatcher.addURI(DeviceContract.CONTENT_AUTHORITY, DeviceContract.PATH_DEVICES + "/#", DEVICES_ID);
        // sUriMatcher.addURI(DeviceContract.CONTENT_AUTHORITY, DeviceContract.PATH_ADD_DEVICE + "/#/#", ADD_DEVICE_ID);
        // sUriMatcher.addURI(DeviceContract.CONTENT_AUTHORITY, DeviceContract.PATH_REMOVE_DEVICE + "/#/#", REMOVE_DEVICE_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DeviceDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                cursor = database.query(DeviceEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DEVICES_ID:
                selection = DeviceEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DeviceEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                return insertDevice(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertDevice(Uri uri, ContentValues values) {
        String name = values.getAsString(DeviceEntry.COLUMN_DEVICE_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Device requires a name");
        }

        Integer quantity = values.getAsInteger(DeviceEntry.COLUMN_DEVICE_QUANTITY);
        if (quantity == null || ! DeviceEntry.isValidQuantity(quantity)) {
            throw new IllegalArgumentException("Device requires valid quantity");
        }

        Double price = values.getAsDouble(DeviceEntry.COLUMN_DEVICE_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Device requires valid price");
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_SUPPLIER)) {
            String supplierName = values.getAsString(DeviceEntry.COLUMN_DEVICE_SUPPLIER);
            if (supplierName == null) {
                throw new IllegalArgumentException("Device requires a supplier name");
            }
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER)) {
            String supplierPhone = values.getAsString(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER);
            if (supplierPhone == null) {
                throw new IllegalArgumentException("Device requires valid supplier phone");
            }
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(DeviceEntry.TABLE_NAME, null, values);
        if (id == - 1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                return updateDevice(uri, contentValues, selection, selectionArgs);
            case DEVICES_ID:

                selection = DeviceEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateDevice(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateDevice(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_NAME)) {
            String name = values.getAsString(DeviceEntry.COLUMN_DEVICE_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Device requires a name");
            }
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_QUANTITY)) {
            Integer quantity = values.getAsInteger(DeviceEntry.COLUMN_DEVICE_QUANTITY);
            if (quantity == null || ! DeviceEntry.isValidQuantity(quantity)) {
                throw new IllegalArgumentException("Device requires valid quantity");
            }
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_PRICE)) {

            Double price = values.getAsDouble(DeviceEntry.COLUMN_DEVICE_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Device requires valid price");
            }
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_SUPPLIER)) {
            String supplierName = values.getAsString(DeviceEntry.COLUMN_DEVICE_SUPPLIER);
            if (supplierName == null) {
                throw new IllegalArgumentException("Device requires a supplier name");
            }
        }

        if (values.containsKey(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER)) {
            String supplierPhone = values.getAsString(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER);
            if (supplierPhone == null) {
                throw new IllegalArgumentException("Device requires valid supplier phone");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsUpdated = database.update(DeviceEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:

                rowsDeleted = database.delete(DeviceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case DEVICES_ID:

                selection = DeviceEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(DeviceEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DEVICES:
                return DeviceEntry.CONTENT_LIST_TYPE;
            case DEVICES_ID:
                return DeviceEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
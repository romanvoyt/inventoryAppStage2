package com.example.android.inventoryapp1;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.inventoryapp1.data.DeviceContract.DeviceEntry;

public class MainScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    //private DeviceDbHelper mDbHelper;

    private static final int DEVICE_LOADER = 0;

    public ListView deviceListView;
    DeviceCursorAdapter mDeviceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreenActivity.this, EditorScreenActivity.class);
                startActivity(intent);
            }
        });


        deviceListView = findViewById(R.id.list);
        deviceListView.setEmptyView(findViewById(R.id.empty_view));

        mDeviceAdapter = new DeviceCursorAdapter(this, null);

        deviceListView.setAdapter(mDeviceAdapter);

        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainScreenActivity.this, DeviceDetails.class);
                Uri currentUri = ContentUris.withAppendedId(DeviceEntry.CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(DEVICE_LOADER, null, this);
        //mDbHelper = new DeviceDbHelper(this);
    }


    private void deleteAllDevices() {
        int rowsDeleted = getContentResolver().delete(DeviceEntry.CONTENT_URI, null, null);
    }



    /*MENU*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all_devices:
                deleteAllDevices();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projections = {
                DeviceEntry._ID,
                DeviceEntry.COLUMN_DEVICE_NAME,
                DeviceEntry.COLUMN_DEVICE_SUPPLIER,
                DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER,
                DeviceEntry.COLUMN_DEVICE_TYPE,
                DeviceEntry.COLUMN_DEVICE_QUANTITY,
                DeviceEntry.COLUMN_DEVICE_PRICE
        };

        return new CursorLoader(this,
                DeviceEntry.CONTENT_URI,
                projections,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDeviceAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDeviceAdapter.swapCursor(null);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }





    /*
    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInformation();

    }*/
/*
    private void displayDatabaseInformation() {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        String[] projections = {
                DeviceEntry._ID,
                DeviceEntry.COLUMN_DEVICE_NAME,
                DeviceEntry.COLUMN_DEVICE_SUPPLIER,
                DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER,
                DeviceEntry.COLUMN_DEVICE_TYPE,
                DeviceEntry.COLUMN_DEVICE_QUANTITY,
                DeviceEntry.COLUMN_DEVICE_PRICE
        };

        Cursor cursor = database.query(
                DeviceEntry.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayView = findViewById(R.id.text_view_device);

        try {
            displayView.setText("Inveto contains: " + cursor.getCount() + " devices.\n");

            displayView.append(
                    DeviceEntry._ID + " - " +
                            DeviceEntry.COLUMN_DEVICE_NAME + " - " +
                            DeviceEntry.COLUMN_DEVICE_SUPPLIER + " - " +
                            DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER + " - " +
                            DeviceEntry.COLUMN_DEVICE_TYPE + " - " +
                            DeviceEntry.COLUMN_DEVICE_QUANTITY + " - " +
                            DeviceEntry.COLUMN_DEVICE_PRICE
            );

            int columnID = cursor.getColumnIndex(DeviceEntry._ID);
            int columnName = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_NAME);
            int columnSupplier = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER);
            int columnSupplierPhone = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER);
            int columnType = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_TYPE);
            int columnQuantity = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_QUANTITY);
            int columnPrice = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_PRICE);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(columnID);
                String currentName = cursor.getString(columnName);
                String currentSupplier = cursor.getString(columnSupplier);
                String currentPhoneNumber = cursor.getString(columnSupplierPhone);
                String currentType = cursor.getString(columnType);
                int currentQuantity = cursor.getInt(columnQuantity);
                int currentPrice = cursor.getInt(columnPrice);


                displayView.append((
                        "\n" + currentID + " - " +
                                currentName + " - " +
                                currentSupplier + " - " +
                                currentPhoneNumber + " - " +
                                currentType + " - " +
                                currentQuantity + " - " +
                                currentPrice));

            }

        } finally {
            cursor.close();
        }
    }*/
}

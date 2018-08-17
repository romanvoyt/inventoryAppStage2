package com.example.android.inventoryapp1;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.inventoryapp1.data.DeviceContract.DeviceEntry;

public class EditorScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private EditText mDeviceName;
    private EditText mDeviceSupplierName;
    private EditText mDeviceSupplierPhoneNumber;
    private EditText mDevicePrice;
    private EditText mDeviceQuantity;
    private Spinner mDeviceType;

    private Uri mCurrentDeviceUri;
    private int mType = DeviceEntry.TYPE_UNKNOWN;

    public static String deviceName;
    public static String supplierName;
    public static String supPhone;
    public static String devicePrice;
    public static String deviceQuantity;

    private int quantity;
    private int price;
    private boolean mDeviceInfoHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor_screen);


        Intent intent = getIntent();
        mCurrentDeviceUri = intent.getData();

        if (mCurrentDeviceUri == null) {
            setTitle(getString(R.string.editor_screen_activity_title_new));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_screen_activity_title_edit));

            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        mDeviceName = findViewById(R.id.edit_gadget_name);
        mDeviceSupplierName = findViewById(R.id.edit_supplier_name);
        mDeviceSupplierPhoneNumber = findViewById(R.id.edit_supplier_phone);
        mDevicePrice = findViewById(R.id.edit_gadget_price);
        mDeviceQuantity = findViewById(R.id.edit_gadget_quantity);
        mDeviceType = findViewById(R.id.spinner_type);

        setupSpinner();
    }

    private void setupSpinner() {

        ArrayAdapter deviceAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type_options, android.R.layout.simple_spinner_item);

        deviceAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mDeviceType.setAdapter(deviceAdapter);

        mDeviceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (! TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.type_smartphone))) {
                        mType = DeviceEntry.TYPE_SMARTPHONE;
                    } else if (selection.equals(getString(R.string.type_laptop))) {
                        mType = DeviceEntry.TYPE_LAPTOP;
                    } else if (selection.equals(getString(R.string.type_tablet))) {
                        mType = DeviceEntry.TYPE_TABLET;
                    } else {
                        mType = DeviceEntry.TYPE_UNKNOWN;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mType = DeviceEntry.TYPE_UNKNOWN;
            }
        });
    }

    private void insertDevice() {

        devicePrice = mDevicePrice.getText().toString().trim();
        deviceQuantity = mDeviceQuantity.getText().toString().trim();
        quantity = Integer.parseInt(deviceQuantity);
        price = Integer.parseInt(devicePrice);


        /*DeviceDbHelper mDbHelper = new DeviceDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();*/

        ContentValues values = new ContentValues();
        values.put(DeviceEntry.COLUMN_DEVICE_NAME, deviceName);
        values.put(DeviceEntry.COLUMN_DEVICE_SUPPLIER, supplierName);
        values.put(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER, supPhone);
        values.put(DeviceEntry.COLUMN_DEVICE_TYPE, mType);
        values.put(DeviceEntry.COLUMN_DEVICE_QUANTITY, quantity);
        values.put(DeviceEntry.COLUMN_DEVICE_PRICE, price);

        /*long newRowId = db.insert(DeviceEntry.TABLE_NAME, null, values);*/

        if (mCurrentDeviceUri == null) {

            Uri newUri = getContentResolver().insert(DeviceEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_device_failed),
                        Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, getString(R.string.editor_insert_device_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentDeviceUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_device_failed),
                        Toast.LENGTH_SHORT).show();
            } else {

                Intent i = new Intent(EditorScreenActivity.this, MainScreenActivity.class);

                i.setData(mCurrentDeviceUri);

                startActivity(i);

                Toast.makeText(this, getString(R.string.editor_update_device_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }


/*
        if (newRowId == - 1) {
            Toast.makeText(this, "Error with saving device", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Device saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }*/


    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteDeviceConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteDevice();
            }
        });


        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteDevice() {
        if (mCurrentDeviceUri != null) {
            int rowDeleted = getContentResolver().delete(mCurrentDeviceUri, null, null);

            if (rowDeleted == 0) {
                Toast.makeText(this, "Deletion has failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Deletion was successful", Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                deviceName = mDeviceName.getText().toString().trim();
                supplierName = mDeviceSupplierName.getText().toString().trim();
                supPhone = mDeviceSupplierPhoneNumber.getText().toString().trim();
                devicePrice = mDevicePrice.getText().toString().trim();
                deviceQuantity = mDeviceQuantity.getText().toString().trim();

                if (! TextUtils.isEmpty(deviceName) && ! TextUtils.isEmpty(supplierName) && ! TextUtils.isEmpty(supPhone) && ! TextUtils.isEmpty(devicePrice) && ! TextUtils.isEmpty(deviceQuantity)) {
                    insertDevice();
                    finish();
                    return true;
                } else {
                    Toast.makeText(this, getString(R.string.editor_insert_device_failed), Toast.LENGTH_SHORT).show();
                    break;
                }

                /*insertDevice();
                finish();*/
            case android.R.id.home:
                if (! mDeviceInfoHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorScreenActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorScreenActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
                /*NavUtils.navigateUpFromSameTask(this);
                return true;*/
            case R.id.action_delete:
                showDeleteDeviceConfirmationDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (! mDeviceInfoHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                DeviceEntry._ID,
                DeviceEntry.COLUMN_DEVICE_NAME,
                DeviceEntry.COLUMN_DEVICE_SUPPLIER,
                DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER,
                DeviceEntry.COLUMN_DEVICE_TYPE,
                DeviceEntry.COLUMN_DEVICE_PRICE,
                DeviceEntry.COLUMN_DEVICE_QUANTITY
        };

        return new CursorLoader(this,
                mCurrentDeviceUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_NAME);
            int supplierNameColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER);
            int typeColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_TYPE);
            int quantityColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_PRICE);

            String deviceName = cursor.getString(nameColumnIndex);
            String supplierName = cursor.getString(supplierNameColumnIndex);
            String supplierPhone = cursor.getString(supplierPhoneColumnIndex);
            int deviceType = cursor.getInt(typeColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            int price = cursor.getInt(priceColumnIndex);

            mDeviceName.setText(deviceName);
            mDeviceSupplierName.setText(supplierName);
            mDeviceSupplierPhoneNumber.setText(supplierPhone);
            mDevicePrice.setText(Integer.toString(price));
            mDeviceQuantity.setText(Integer.toString(quantity));

            switch (deviceType) {
                case DeviceEntry.TYPE_SMARTPHONE:
                    mDeviceType.setSelection(DeviceEntry.TYPE_SMARTPHONE);
                    break;
                case DeviceEntry.TYPE_LAPTOP:
                    mDeviceType.setSelection(DeviceEntry.TYPE_LAPTOP);
                    break;
                case DeviceEntry.TYPE_TABLET:
                    mDeviceType.setSelection(DeviceEntry.TYPE_TABLET);
                    break;
                default:
                    mDeviceType.setSelection(DeviceEntry.TYPE_UNKNOWN);
                    break;

            }

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mDeviceName.setText("");
        mDeviceSupplierName.setText("");
        mDeviceSupplierPhoneNumber.setText("");
        mDevicePrice.setText("");
        mDeviceQuantity.setText("");
        mDeviceType.setSelection(0);

    }
}

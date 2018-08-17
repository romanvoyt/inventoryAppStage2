package com.example.android.inventoryapp1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.inventoryapp1.data.DeviceContract.DeviceEntry;

public class DeviceDetails extends AppCompatActivity {
    TextView nameTV;
    TextView supplierTV;
    TextView phoneTV;
    TextView typeTV;
    TextView priceTV;
    TextView quantityTV;

    Button contactButton;
    Button removeButton;
    Button addButton;

    Uri currentUri;
    Uri newUri;

    Integer quantity;

    public String supplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_view);

        Intent intent = getIntent();
        currentUri = intent.getData();

        Intent newIntent = getIntent();
        newUri = newIntent.getData();

        nameTV = findViewById(R.id.device_name);
        supplierTV = findViewById(R.id.supplier_name);
        phoneTV = findViewById(R.id.supplier_phone);
        typeTV = findViewById(R.id.device_type);
        priceTV = findViewById(R.id.device_price);
        quantityTV = findViewById(R.id.device_quantity);

        contactButton = findViewById(R.id.contact_button);
        addButton = findViewById(R.id.add);
        removeButton = findViewById(R.id.remove);

        Cursor cursor = managedQuery(currentUri, null, null, null, "name");

        if (cursor.moveToFirst()) {
            do {
                String deviceName = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_NAME));
                String supplierName = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER));
                String supPhone = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER));
                String deviceType = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_TYPE));
                String devicePrice = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_PRICE));
                quantity = cursor.getInt(cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_QUANTITY));
                supplierPhone = cursor.getString(cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER));

                nameTV.setText(" " + deviceName);
                supplierTV.setText(" " + supplierName);
                phoneTV.setText(" " + supPhone);
                typeTV.setText(" " + deviceType);
                priceTV.setText(" " + devicePrice);
                quantityTV.setText(" " + quantity.toString());

            } while (cursor.moveToNext());
        }

        Cursor newCursor = managedQuery(newUri, null, null, null, "name");

        if (newCursor.moveToFirst()) {
            do {
                String deviceName = newCursor.getString(newCursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_NAME));
                String supplierName = newCursor.getString(newCursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER));
                String supPhone = newCursor.getString(newCursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER));
                String deviceType = newCursor.getString(newCursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_TYPE));
                String devicePrice = newCursor.getString(newCursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_PRICE));
                quantity = newCursor.getInt(newCursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_QUANTITY));

                nameTV.setText(" " + deviceName);
                supplierTV.setText(" " + supplierName);
                phoneTV.setText(" " + supPhone);
                typeTV.setText(" " + deviceType);
                priceTV.setText(" " + devicePrice);
                quantityTV.setText(" " + quantity.toString());

            } while (newCursor.moveToNext());
        }

        contactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + supplierPhone));
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                ContentValues contentValues = new ContentValues();
                contentValues.put(DeviceEntry.COLUMN_DEVICE_QUANTITY, quantity);
                getContentResolver().update(currentUri, contentValues, null, null);
                quantityTV.setText(quantity.toString());
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) {
                    quantity--;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DeviceEntry.COLUMN_DEVICE_QUANTITY, quantity);
                    getContentResolver().update(currentUri, contentValues, null, null);
                    quantityTV.setText(quantity.toString());
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_editing_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(DeviceDetails.this, EditorScreenActivity.class);
                intent.setData(currentUri);
                this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

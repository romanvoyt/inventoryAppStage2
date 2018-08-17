package com.example.android.inventoryapp1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventoryapp1.data.DeviceContract.DeviceEntry;

public class DeviceCursorAdapter extends CursorAdapter {

    public DeviceCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        TextView nameTV = view.findViewById(R.id.device_name);
        final TextView quantityTV = view.findViewById(R.id.device_quantity);
        TextView priceTV = view.findViewById(R.id.device_price);
        Button saleB = view.findViewById(R.id.device_sell);

        int nameColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_QUANTITY);
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(DeviceEntry.COLUMN_DEVICE_QUANTITY));
        int priceColumnIndex = cursor.getColumnIndex(DeviceEntry.COLUMN_DEVICE_PRICE);

        final String productName = cursor.getString(nameColumnIndex);
        final String productQuantity = cursor.getString(quantityColumnIndex);
        final String productPrice = cursor.getString(priceColumnIndex);

        final Uri uri = ContentUris.withAppendedId(DeviceEntry.CONTENT_URI,
                cursor.getInt(cursor.getColumnIndexOrThrow(DeviceEntry._ID)));

        nameTV.setText(productName);
        quantityTV.setText(" " + productQuantity);
        priceTV.setText(" " + productPrice);

        saleB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (quantity > 0) {
                    Integer decreaseQuantity = quantity - 1;

                    ContentValues values = new ContentValues();
                    values.put(DeviceEntry.COLUMN_DEVICE_QUANTITY, decreaseQuantity);
                    context.getContentResolver().update(uri, values, null, null);

                    quantityTV.setText(decreaseQuantity.toString());
                }
            }
        });
    }

}

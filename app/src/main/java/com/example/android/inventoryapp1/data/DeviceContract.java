package com.example.android.inventoryapp1.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DeviceContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapp1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_DEVICES = "devices";

    private DeviceContract() {

    }

    public static final class DeviceEntry implements BaseColumns {
        public final static String TABLE_NAME = "devices";

        public static final String CONTENT_LIST_TYPE = String.format("%s/%s/%s", ContentResolver.CURSOR_DIR_BASE_TYPE, CONTENT_AUTHORITY, PATH_DEVICES);
        public static final String CONTENT_ITEM_TYPE = String.format("%s/%s/%s", ContentResolver.CURSOR_ITEM_BASE_TYPE, CONTENT_AUTHORITY, PATH_DEVICES);

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DEVICES);


        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DEVICE_NAME = "name";
        public final static String COLUMN_DEVICE_PRICE = "price";
        public final static String COLUMN_DEVICE_SUPPLIER = "supplier";
        public final static String COLUMN_DEVICE_SUPPLIER_PHONE_NUMBER = "phone";
        public final static String COLUMN_DEVICE_TYPE = "type";
        public final static String COLUMN_DEVICE_QUANTITY = "quantity";

        public static final int TYPE_UNKNOWN = 0;
        public static final int TYPE_SMARTPHONE = 1;
        public static final int TYPE_LAPTOP = 2;
        public static final int TYPE_TABLET = 3;

        public static boolean isValidQuantity(int quantity) {
            if (quantity >= 0) {
                return true;
            }
            return false;
        }
    }

}

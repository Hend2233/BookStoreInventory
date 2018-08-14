package com.example.tito.bookstoreapp.data;

import android.provider.BaseColumns;

public final class BookContract {

    private BookContract() {}

    public static final class BooKInventoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "book_inventory";

        public static final String _ID = BaseColumns._ID;

        public static final String BOOK_NAME_COLUMN = "title";

        public static final String BOOK_PRICE_COLUMN = "price";

        public static final String BOOK_QUANTITY_COLUMN = "quantity";

        public static final String SUPPLIER_NAME = "supplier_name";

        public static final String SUPPLIER_PHONE = "supplier_phone";


    }
}

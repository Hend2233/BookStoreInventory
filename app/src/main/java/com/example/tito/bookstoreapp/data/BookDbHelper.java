package com.example.tito.bookstoreapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;

public class BookDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int VERSION_NUMBER = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String BOOK_INVENTORY_TABLE = "CREATE TABLE " + BooKInventoryEntry.TABLE_NAME + " ("
                + BooKInventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BooKInventoryEntry.BOOK_NAME_COLUMN + " TEXT NOT NULL, "
                + BooKInventoryEntry.BOOK_PRICE_COLUMN + " BLOB, "
                + BooKInventoryEntry.BOOK_QUANTITY_COLUMN + " INTEGER, "
                + BooKInventoryEntry.SUPPLIER_NAME + " TEXT NOT NULL, "
                + BooKInventoryEntry.SUPPLIER_PHONE + " BLOB NOT NULL);";

        db.execSQL(BOOK_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

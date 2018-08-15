package com.example.tito.bookstoreapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;
import com.example.tito.bookstoreapp.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity {

    private TextView tableText;
    private FloatingActionButton fabButton;
    private BookDbHelper bookDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        bookDbHelper = new BookDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryData();
    }

    private void queryData() {

        bookDbHelper = new BookDbHelper(this);

        SQLiteDatabase db = bookDbHelper.getReadableDatabase();

        String[] projection = {
                BooKInventoryEntry._ID,
                BooKInventoryEntry.BOOK_NAME_COLUMN,
                BooKInventoryEntry.BOOK_PRICE_COLUMN,
                BooKInventoryEntry.BOOK_QUANTITY_COLUMN,
                BooKInventoryEntry.SUPPLIER_NAME,
                BooKInventoryEntry.SUPPLIER_PHONE};

        Cursor cursor = db.query(
                BooKInventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null);

        tableText = (TextView) findViewById(R.id.items_view);

        try {
            tableText.setText(getString(R.string.book_store) + cursor.getCount() + "\n\n");

            tableText.append(BooKInventoryEntry._ID + " - " +
                    BooKInventoryEntry.BOOK_NAME_COLUMN + "  -  " +
                    BooKInventoryEntry.BOOK_PRICE_COLUMN + "  -  " +
                    BooKInventoryEntry.BOOK_QUANTITY_COLUMN + "  -  " +
                    BooKInventoryEntry.SUPPLIER_NAME + "  -  " +
                    BooKInventoryEntry.SUPPLIER_PHONE + "\n");

            int idColumn = cursor.getColumnIndex(BooKInventoryEntry._ID);
            int bookTitleColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_NAME_COLUMN);
            int bookPriceColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_PRICE_COLUMN);
            int bookQuantityColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_QUANTITY_COLUMN);
            int supplierNameColumn = cursor.getColumnIndex(BooKInventoryEntry.SUPPLIER_NAME);
            int supplierPhoneColumn = cursor.getColumnIndex(BooKInventoryEntry.SUPPLIER_PHONE);

            while (cursor.moveToNext()) {

                int currentId = cursor.getInt(idColumn);
                String currentBookTitle = cursor.getString(bookTitleColumn);
                double currentBookPrice = cursor.getDouble(bookPriceColumn);
                int currentBookQuantity = cursor.getInt(bookQuantityColumn);
                String currentSupplierName = cursor.getString(supplierNameColumn);
                long currentSupplierPhone = cursor.getLong(supplierPhoneColumn);

                tableText.append(("\n\n" + currentId + " - " + currentBookTitle +
                        "  -  " + currentBookPrice + "$" + "  -  " + currentBookQuantity + "  -  " +
                        currentSupplierName + "  -  " + currentSupplierPhone));
            }
        } finally {
            cursor.close();
        }
    }
}

package com.example.tito.bookstoreapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri currentBookUri;
    private static final int Book_Loader = 0;
    TextView bookTitle, bookPrice, bookQuantity, supplierName, supplierPhone;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        currentBookUri = intent.getData();

        if (currentBookUri == null) {
            Toast.makeText(this, "There isn\'t any book details", Toast.LENGTH_SHORT).show();
        } else {
            getSupportLoaderManager().initLoader(Book_Loader, null, this);
        }

        bookTitle = (TextView) findViewById(R.id.book_title);
        bookPrice = (TextView) findViewById(R.id.book_price);
        bookQuantity = (TextView) findViewById(R.id.book_quantity);
        supplierName = (TextView) findViewById(R.id.supplier_name);
        supplierPhone = (TextView) findViewById(R.id.supplier_phone);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String [] projection = {BooKInventoryEntry._ID,
                BooKInventoryEntry.BOOK_NAME_COLUMN,
                BooKInventoryEntry.BOOK_PRICE_COLUMN,
                BooKInventoryEntry.BOOK_QUANTITY_COLUMN,
                BooKInventoryEntry.SUPPLIER_NAME,
                BooKInventoryEntry.SUPPLIER_PHONE};

        return new CursorLoader(this,
                currentBookUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(BooKInventoryEntry.BOOK_NAME_COLUMN);
            int priceColumnIndex = cursor.getColumnIndex(BooKInventoryEntry.BOOK_PRICE_COLUMN);
            int quantityColumnIndex = cursor.getColumnIndex(BooKInventoryEntry.BOOK_QUANTITY_COLUMN);
            int spNameColumnIndex = cursor.getColumnIndex(BooKInventoryEntry.SUPPLIER_NAME);
            int spPhoneColumnIndex = cursor.getColumnIndex(BooKInventoryEntry.SUPPLIER_PHONE);

            String title = cursor.getString(titleColumnIndex);
            double price = cursor.getDouble(priceColumnIndex);
            int quantity = cursor.getInt(quantityColumnIndex);
            String spName = cursor.getString(spNameColumnIndex);
            String spPhone = cursor.getString(spPhoneColumnIndex);

            bookTitle.setText(title);
            bookPrice.setText(String.valueOf(price));
            bookQuantity.setText(String.valueOf(quantity));
            supplierName.setText(spName);
            supplierPhone.setText(spPhone);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

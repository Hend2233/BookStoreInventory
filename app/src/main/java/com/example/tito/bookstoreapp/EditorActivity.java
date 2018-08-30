package com.example.tito.bookstoreapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public EditText bookName;
    public EditText bookPrice;
    public EditText quantity;
    public EditText supplierName;
    public EditText supplierPhone;
    private static final int BOOK_LOADER = 0;
    Uri currentBookUri;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent intent = getIntent();

        currentBookUri = intent.getData();

        if (currentBookUri == null){
            setTitle(getString(R.string.add_a_book));
        } else {
            setTitle(getString(R.string.edit_a_book));

            getSupportLoaderManager().initLoader(BOOK_LOADER, null, this);
        }

        bookName = (EditText) findViewById(R.id.book_title);
        bookPrice = (EditText) findViewById(R.id.book_price);
        quantity = (EditText) findViewById(R.id.quantity);
        supplierName = (EditText) findViewById(R.id.supplier_name);
        supplierPhone = (EditText) findViewById(R.id.supplier_phone);
    }

    private void insertData() {
        String bookTitleInfo = bookName.getText().toString();
        String bookPriceInfo = bookPrice.getText().toString();
        String quantityInfo = quantity.getText().toString();
        String supplierNameInfo = supplierName.getText().toString();
        String supplierPhoneInfo = supplierPhone.getText().toString();

        if (currentBookUri == null && TextUtils.isEmpty(bookTitleInfo)
                || TextUtils.isEmpty(supplierNameInfo) || TextUtils.isEmpty(supplierPhoneInfo)
                || TextUtils.isEmpty(bookPriceInfo) || TextUtils.isEmpty(quantityInfo))
        {
            Toast.makeText(this, "Complete the book info", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();

        values.put(BooKInventoryEntry.BOOK_NAME_COLUMN, bookTitleInfo);
        values.put(BooKInventoryEntry.BOOK_PRICE_COLUMN, bookPriceInfo);
        values.put(BooKInventoryEntry.BOOK_QUANTITY_COLUMN, quantityInfo);
        values.put(BooKInventoryEntry.SUPPLIER_NAME, supplierNameInfo);
        values.put(BooKInventoryEntry.SUPPLIER_PHONE, supplierPhoneInfo);

        if (currentBookUri == null) {

            Uri newUri = getContentResolver().insert(BooKInventoryEntry.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "successful",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(currentBookUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, "failed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "successful",
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                insertData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        return;
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
            int bookTitleColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_NAME_COLUMN);
            int bookPriceColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_PRICE_COLUMN);
            int bookQuantityColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_QUANTITY_COLUMN);
            int supplierNameColumn = cursor.getColumnIndex(BooKInventoryEntry.SUPPLIER_NAME);
            int supplierPhoneColumn = cursor.getColumnIndex(BooKInventoryEntry.SUPPLIER_PHONE);

            String currentBookTitle = cursor.getString(bookTitleColumn);
            double currentBookPrice = cursor.getDouble(bookPriceColumn);
            int currentBookQuantity = cursor.getInt(bookQuantityColumn);
            String currentSupplierName = cursor.getString(supplierNameColumn);
            String currentSupplierPhone = cursor.getString(supplierPhoneColumn);

            bookName.setText(currentBookTitle);
            bookPrice.setText(String.valueOf(currentBookPrice));
            quantity.setText(String.valueOf(currentBookQuantity));
            supplierName.setText(currentSupplierName);
            supplierPhone.setText(currentSupplierPhone);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookName.setText("");
        bookPrice.setText(Integer.toString(0));
        quantity.setText(Integer.toString(0));
        supplierName.setText("");
        supplierPhone.setText("");
    }
}

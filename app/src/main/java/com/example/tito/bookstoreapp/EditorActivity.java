package com.example.tito.bookstoreapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;
import com.example.tito.bookstoreapp.data.BookDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText bookName, bookPrice, quantity, supplierName, supplierPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

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

        double bookPrice = Double.parseDouble(bookPriceInfo);
        int bookQuantity = Integer.parseInt(quantityInfo);
        long supplierNumber = Long.parseLong(supplierPhoneInfo);

        BookDbHelper bookDbHelper = new BookDbHelper(this);

        SQLiteDatabase db = bookDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(BooKInventoryEntry.BOOK_NAME_COLUMN, bookTitleInfo);
        values.put(BooKInventoryEntry.BOOK_PRICE_COLUMN, bookPrice);
        values.put(BooKInventoryEntry.BOOK_QUANTITY_COLUMN, bookQuantity);
        values.put(BooKInventoryEntry.SUPPLIER_NAME, supplierNameInfo);
        values.put(BooKInventoryEntry.SUPPLIER_PHONE, supplierNumber);

        long newRow = db.insert(BooKInventoryEntry.TABLE_NAME, null, values);

        if (newRow == -1) {
            Toast.makeText(this, "There is some errors", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "There is a new book " + newRow, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                insertData();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.example.tito.bookstoreapp;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;


public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri currentBookUri;
    private static final int Book_Loader = 0;
    TextView bookTitle, bookPrice, bookQuantity, supplierName, supplierPhone;
    Button contactBtn, increaseBtn, decreaseBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        contactBtn = (Button) findViewById(R.id.contact);
        increaseBtn = (Button) findViewById(R.id.increase_btn);
        decreaseBtn = (Button) findViewById(R.id.decrease_btn);
        deleteBtn = (Button) findViewById(R.id.delete_btn);

        Intent intent = getIntent();
        currentBookUri = intent.getData();
        if (currentBookUri == null) {
            Toast.makeText(this, getString(R.string.no_details), Toast.LENGTH_SHORT).show();
        } else {
            getSupportLoaderManager().initLoader(Book_Loader, null, this);
        }

        bookTitle = (TextView) findViewById(R.id.book_title);
        bookPrice = (TextView) findViewById(R.id.book_price);
        bookQuantity = (TextView) findViewById(R.id.book_quantity);
        supplierName = (TextView) findViewById(R.id.supplier_name);
        supplierPhone = (TextView) findViewById(R.id.supplier_phone);

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                String phone = supplierPhone.getText().toString();
                intent.setData(Uri.parse("tel:" + phone.trim()));
                startActivity(intent);
            }
        });
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityIncrease = Integer.valueOf(bookQuantity.getText().toString());

                quantityIncrease++;

                ContentValues values = new ContentValues();
                values.put(BooKInventoryEntry.BOOK_QUANTITY_COLUMN, quantityIncrease);

                int newQuantity = getContentResolver().update(currentBookUri, values, null, null);
                if (newQuantity == 0) {
                    Toast.makeText(DetailsActivity.this, getString(R.string.not_increased), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, getString(R.string.increased), Toast.LENGTH_SHORT).show();
                }
            }
        });
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantityT = Integer.valueOf(bookQuantity.getText().toString());
                if (quantityT > 0 && quantityT != -1) {
                    quantityT--;
                } else {
                    Toast.makeText(DetailsActivity.this, getString(R.string.no_0s_value), Toast.LENGTH_SHORT).show();
                    return;
                }
                ContentValues values = new ContentValues();
                values.put(BooKInventoryEntry.BOOK_QUANTITY_COLUMN, quantityT);
                int newRow = getContentResolver().update(currentBookUri, values, null, null);
                if (newRow == 0) {
                    Toast.makeText(DetailsActivity.this, getString(R.string.no_decreased), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailsActivity.this, getString(R.string.decreased), Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook();
            }
        });
    }

    private void deleteBook() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_book);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void delete() {
        if (currentBookUri != null) {
            int deleteRow = getContentResolver().delete(currentBookUri, null, null);
            if (deleteRow == 0) {
                Toast.makeText(this, getString(R.string.failed_to_delete_one_book), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.one_book_deleted), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                currentBookUri,
                null,
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
        bookTitle.setText("");
        bookPrice.setText(Integer.toString(0));
        bookQuantity.setText(Integer.toString(0));
        supplierName.setText("");
        supplierPhone.setText("");
    }
}

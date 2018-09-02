package com.example.tito.bookstoreapp;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;
import com.example.tito.bookstoreapp.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton fabButton;
    BookDbHelper bookDbHelper;
    ListView bookListView;
    private static final int Book_Loader = 0;
    BookCursorAdapter bookCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        bookDbHelper = new BookDbHelper(this);

        fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        bookListView = (ListView) findViewById(R.id.list);
        bookCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(bookCursorAdapter);
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        getSupportLoaderManager().initLoader(Book_Loader, null, this);

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
                Uri bookUri = ContentUris.withAppendedId(BooKInventoryEntry.CONTENT_URI, id);
                intent.setData(bookUri);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllBooks() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_all);
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        builder.setPositiveButton(R.string.delete_all, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteAll();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteAll() {
        if (BooKInventoryEntry.CONTENT_URI != null) {
            int deleteAll = getContentResolver().delete(BooKInventoryEntry.CONTENT_URI, null, null);
            if (deleteAll == 0) {
                Toast.makeText(this, getString(R.string.faild_to_delete), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.all_books_deleted), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BooKInventoryEntry._ID,
                BooKInventoryEntry.BOOK_NAME_COLUMN,
                BooKInventoryEntry.BOOK_QUANTITY_COLUMN,
                BooKInventoryEntry.BOOK_PRICE_COLUMN,
                BooKInventoryEntry.SUPPLIER_NAME,
                BooKInventoryEntry.SUPPLIER_PHONE};

        return new CursorLoader(this,
                BooKInventoryEntry.CONTENT_URI,
                projection,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookCursorAdapter.swapCursor(null);
    }

}


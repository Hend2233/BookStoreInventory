package com.example.tito.bookstoreapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;

public class BookProvider extends ContentProvider {

    private static final int BOOKS = 100;
    private static final int BOOK_ID = 101;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private BookDbHelper bookDbHelper;
    private static final String LOG_TAG = BookProvider.class.getSimpleName();

    static {
        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_TableName, BOOKS);
        uriMatcher.addURI(BookContract.CONTENT_AUTHORITY, BookContract.PATH_TableName + "/#", BOOK_ID);
    }

    @Override
    public boolean onCreate() {
        bookDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = bookDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                cursor = database.query(
                        BooKInventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ID:
                selection = BooKInventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        BooKInventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return insertBook(uri, values);
            default:
                throw new IllegalArgumentException("Inserting is not supported for this uri " + uri);
        }
    }

    private Uri insertBook(Uri uri, ContentValues values) {
        String bookTitle = values.getAsString(BooKInventoryEntry.BOOK_NAME_COLUMN);
        double bookPrice = values.getAsDouble(BooKInventoryEntry.BOOK_PRICE_COLUMN);
        int bookQuantity = values.getAsInteger(BooKInventoryEntry.BOOK_QUANTITY_COLUMN);
        String supplierName = values.getAsString(BooKInventoryEntry.SUPPLIER_NAME);
        String supplierPhone = values.getAsString(BooKInventoryEntry.SUPPLIER_PHONE);
        if (bookTitle == null || bookTitle.length() == 0) {
            throw new IllegalArgumentException("Book need a Title");
        }
        if (bookPrice == 0) {
            throw new IllegalArgumentException("Book need a Price");
        }
        if (bookQuantity == -1) {
            throw new IllegalArgumentException("Book need a valid Quantity");
        }
        if (supplierName == null || supplierName.length() == 0) {
            throw new IllegalArgumentException("Supplier Name is Required");
        }
        if (supplierPhone == null) {
            throw new IllegalArgumentException("Supplier Phone is Required");
        }

        SQLiteDatabase database = bookDbHelper.getWritableDatabase();

        long id = database.insert(BooKInventoryEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "insertBook: Failed" + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return updateBook(uri, values, selection, selectionArgs);
            case BOOK_ID:
                selection = BooKInventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for this uri " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(BooKInventoryEntry.BOOK_NAME_COLUMN)) {
            String bookTitle = values.getAsString(BooKInventoryEntry.BOOK_NAME_COLUMN);
            if (bookTitle == null || bookTitle.length() == 0) {
                throw new IllegalArgumentException("Book need a Title");
            }
        }
        if (values.containsKey(BooKInventoryEntry.BOOK_PRICE_COLUMN)) {
            double bookPrice = values.getAsDouble(BooKInventoryEntry.BOOK_PRICE_COLUMN);
            if (bookPrice == 0.0) {
                throw new IllegalArgumentException("Book need a Price");
            }
        }
        if (values.containsKey(BooKInventoryEntry.BOOK_QUANTITY_COLUMN)) {
            int bookQuantity = values.getAsInteger(BooKInventoryEntry.BOOK_QUANTITY_COLUMN);
            if (bookQuantity == -1) {
                throw new IllegalArgumentException("Book need a valid Quantity");
            }
        }
        if (values.containsKey(BooKInventoryEntry.SUPPLIER_NAME)) {
            String supplierName = values.getAsString(BooKInventoryEntry.SUPPLIER_NAME);
            if (supplierName == null || supplierName.length() == 0) {
                throw new IllegalArgumentException("Supplier Name is Required");
            }
        }
        if (values.containsKey(BooKInventoryEntry.SUPPLIER_PHONE)) {
            String supplierPhone = values.getAsString(BooKInventoryEntry.SUPPLIER_PHONE);
            if (supplierPhone == null) {
                throw new IllegalArgumentException("Supplier Phone is Required");
            }
        }

        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = bookDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(BooKInventoryEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = bookDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = uriMatcher.match(uri);

        switch (match) {
            case BOOKS:
                rowsDeleted = database.delete(BooKInventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BooKInventoryEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(BooKInventoryEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("deletion is not supported for this uri " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BooKInventoryEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BooKInventoryEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri " + uri + " with match " + match);
        }
    }
}

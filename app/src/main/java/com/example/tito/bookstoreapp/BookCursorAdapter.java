package com.example.tito.bookstoreapp;

import android.app.Activity;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.tito.bookstoreapp.data.BookContract.BooKInventoryEntry;

public class BookCursorAdapter extends CursorAdapter {

    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.book_list_items, parent, false);

    }

    @Override
    public void bindView(final View view, final Context context, final Cursor cursor) {

        TextView title = (TextView) view.findViewById(R.id.book_title_item);
        TextView quantityT = (TextView) view.findViewById(R.id.book_quantity);
        TextView price = (TextView) view.findViewById(R.id.book_price_item);
        Button saleButton = (Button) view.findViewById(R.id.sale_btn);
        Button editButton = (Button) view.findViewById(R.id.edit_btn);


        int bookTitleColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_NAME_COLUMN);
        int bookPriceColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_PRICE_COLUMN);
        int bookQuantityColumn = cursor.getColumnIndex(BooKInventoryEntry.BOOK_QUANTITY_COLUMN);

        String bookTitle = cursor.getString(bookTitleColumn);
        int bookQuantity = cursor.getInt(bookQuantityColumn);
        double bookPrice = cursor.getDouble(bookPriceColumn);

        title.setText(bookTitle);
        quantityT.setText(String.valueOf(bookQuantity));
        price.setText(String.valueOf(bookPrice));

        final long currentId = mCursor.getLong(mCursor.getColumnIndex(BooKInventoryEntry._ID));

        final Uri contentUri = Uri.withAppendedPath(BooKInventoryEntry.CONTENT_URI, Long.toString(currentId));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView quantityT = (TextView) view.findViewById(R.id.book_quantity);

                int quantity = Integer.valueOf(quantityT.getText().toString());

                if (quantity == 0) {
                    Toast.makeText(context, R.string.no_0s_value, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (quantity > 0) {
                    quantity--;
                }
                ContentValues values = new ContentValues();
                values.put(BooKInventoryEntry.BOOK_QUANTITY_COLUMN, quantity);

                mContext.getContentResolver().update(contentUri, values, null, null);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditorActivity.class);
                Uri bookUri = ContentUris.withAppendedId(BooKInventoryEntry.CONTENT_URI, currentId);
                intent.setData(bookUri);
                ((Activity) mContext).startActivityForResult(intent, EditorActivity.CONTEXT_INCLUDE_CODE);
            }
        });
    }
}


package com.example.tito.bookstoreapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.example.tito.bookstoreapp.data.BookDbHelper;

public class CatalogActivity extends AppCompatActivity {

    private TextView tableText;
    private FloatingActionButton fabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        tableText = (TextView) findViewById(R.id.items_view);

        tableText.setText(getString(R.string.book_store));

        fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        showData();

    }

   private void showData() {

        BookDbHelper bookDbHelper = new BookDbHelper(this);

        SQLiteDatabase db = bookDbHelper.getReadableDatabase();

    }
}

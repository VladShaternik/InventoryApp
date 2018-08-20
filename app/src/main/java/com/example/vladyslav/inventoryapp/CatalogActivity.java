package com.example.vladyslav.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.vladyslav.inventoryapp.data.Contract.InventoryEntry;
import com.example.vladyslav.inventoryapp.data.DbHelper;

public class CatalogActivity extends AppCompatActivity {

    DbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);


        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new DbHelper(this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the pets database.
     */
    private void displayDatabaseInfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE};

        Cursor cursor = db.query(InventoryEntry.TABLE_NAME, projection,null,
                null, null, null, null);

        TextView displayView = findViewById(R.id.items_view);

        try {
            displayView.setText("The table contains " + cursor.getCount() + " items.\n\n");
            displayView.append(InventoryEntry._ID + " - " +
                    InventoryEntry.COLUMN_PRODUCT_NAME + " - " +
                    InventoryEntry.COLUMN_PRICE + " - " +
                    InventoryEntry.COLUMN_QUANTITY + " - " +
                    InventoryEntry.COLUMN_SUPPLIER_NAME  + " - " +
                    InventoryEntry.COLUMN_SUPPLIER_PHONE  + "\n");

            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int productNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentProductName = cursor.getString(productNameColumnIndex);
                float currentPrice =
                        cursor.getFloat(priceColumnIndex) == 0 ?
                        0 :
                        cursor.getFloat(priceColumnIndex)/ 100;
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                String currentSupplierName = cursor.getString(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                displayView.append(("\n" + currentID + " - " +
                        currentProductName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertItem() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "Laptop");
        values.put(InventoryEntry.COLUMN_PRICE, 1300);
        values.put(InventoryEntry.COLUMN_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, "Republic of Gamers");
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, "(949)494-4499");

        db.insert(InventoryEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertItem();
                displayDatabaseInfo();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

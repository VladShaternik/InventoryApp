package com.example.vladyslav.inventoryapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vladyslav.inventoryapp.data.Contract.InventoryEntry;
import com.example.vladyslav.inventoryapp.data.DbHelper;

public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the product name */
    private EditText productNameEditText;

    /** EditText field to enter the price */
    private EditText priceEditText;

    /** EditText field to enter the quantity */
    private EditText quantityEditText;

    /** EditText field to enter the supplier name */
    private EditText supplierNameEditText;

    /** EditText field to enter the supplier phone number */
    private EditText supplierPhoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        productNameEditText = findViewById(R.id.edit_product_name);
        priceEditText = findViewById(R.id.edit_price);
        quantityEditText = findViewById(R.id.edit_quantity);
        supplierNameEditText = findViewById(R.id.edit_supplier_name);
        supplierPhoneEditText = findViewById(R.id.edit_supplier_phone);
    }

    /**
     * Get user input from editor and save new Item into database.
     */
    private void insertItem() {
        String productNameString = productNameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String supplierNameString = supplierNameEditText.getText().toString().trim();
        String supplierPhoneString = supplierPhoneEditText.getText().toString().trim();
        int quantity = Integer.parseInt(quantityString);
        // Store price as an integer for easier storing in the db
        int price = (int)(Float.parseFloat(priceString) * 100);

        DbHelper mDbHelper = new DbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, productNameString);
        values.put(InventoryEntry.COLUMN_PRICE, price);
        values.put(InventoryEntry.COLUMN_QUANTITY, quantity);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, supplierNameString);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, supplierPhoneString);

        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            Toast.makeText(this, "Saving error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saving successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertItem();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

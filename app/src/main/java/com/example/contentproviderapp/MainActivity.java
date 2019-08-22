package com.example.contentproviderapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import android.provider.ContactsContract;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ListView contactNames;
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static boolean READ_CONTACTS_GRANTED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        contactNames = (ListView) findViewById(R.id.contact_names);

        int hasReadContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        Log.d(TAG, "onCreate, checkSelfPermission: " + hasReadContactsPermission);


        if(hasReadContactsPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCREATE: permission granted");
            READ_CONTACTS_GRANTED = true;
        } else {
            Log.d(TAG, "onCreate: requesting permission");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "fab click: start");
                String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY}; // columns
                ContentResolver contentResolver = getContentResolver();
                Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                        projection,
                        null, // WHERE clause
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY); // sort order ie ORDER_BY

                if(cursor != null) {
                    List<String> contacts = new ArrayList<String>();
                    while(cursor.moveToNext()){
                           contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                    }
                    cursor.close();
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.contact_detail, R.id.name, contacts);
                    contactNames.setAdapter(adapter);
                }
                Log.d(TAG, "fab click: end");
            }
        });
        Log.d(this, "onCREATE: ends here");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

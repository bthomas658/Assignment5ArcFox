package com.example.assignment5arcfox;

import static android.R.id.list;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class DisplalyDB extends AppCompatActivity{

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_displaly_db);
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), AddUserActivity.class);
                startActivity(intent);
            });

            //instead of array and preferences have the DB stuff here

            PersonDbHelper dbHelper = new PersonDbHelper(getApplicationContext());
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            //
            //out of dataset which columns to use projection

            String[] projection = {
                    PersonContract.PersonEntry.COLUMN_NAME_FIRST,
                    PersonContract.PersonEntry.COLUMN_NAME_LAST,
                    PersonContract.PersonEntry.COLUMN_PHONE,
                    PersonContract.PersonEntry.COLUMN_EMAIL
            };

            String[] bind = {
                    PersonContract.PersonEntry._ID,
                    PersonContract.PersonEntry.COLUMN_NAME_FIRST,
                    PersonContract.PersonEntry.COLUMN_NAME_LAST,
                    PersonContract.PersonEntry.COLUMN_PHONE,
                    PersonContract.PersonEntry.COLUMN_EMAIL
            };

            //now going to call method to return cursor from DB

            Cursor cursor = db.query(PersonContract.PersonEntry.TABLE_NAME, //table to query
                    bind,
                    null, //columns for where, Null will return all rows
                    null, //values for where
                    null, //Group By, null is no group by
                    null, //Having, null says return all rows
                    PersonContract.PersonEntry.COLUMN_NAME_LAST + " ASC" //names in alpabetical order
            );


            //the list items from the layout, will find these in the row_item,
            //these are the 4 fields being displayed
            int[] to = new int[]{
                    R.id.first,  R.id.last, R.id.phone, R.id.email
            };

            //create the adapter
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(), R.layout.row_item, cursor, projection, to, 0);

            //set the adapter to the list
            final ListView listView = (ListView) findViewById(list);
            listView.setAdapter(adapter);

            //set up for the empty non data messaged
            TextView emptyView = (TextView) findViewById(android.R.id.empty);
            listView.setEmptyView(emptyView);

            //need to set the On Item Click Listener
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Cursor cursor1 = (Cursor) parent.getItemAtPosition(position);

                //this is returning a cursor this time, so need to get the string out of the cursor
                int columnIndex = cursor1.getColumnIndex(PersonContract.PersonEntry.COLUMN_NAME_FIRST);
                String selectedItem = (String) cursor1.getString(columnIndex);

                //Used a Snackbar instead of a toast, just something different
               Snackbar.make(view, selectedItem, Snackbar.LENGTH_LONG)
                       .setAction("Action", null).show();

            });

        }



    @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_display_db, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();
            if (id == R.id.enterValues) {
                Intent intent = new Intent(getApplicationContext(), AddUserActivity.class);
                startActivity(intent);
                return true;
            }


            return super.onOptionsItemSelected(item);
        }

}

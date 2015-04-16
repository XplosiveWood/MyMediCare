package com.edgehill.mad.mymedicare;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

//TODO Design XML for this activity and also develop the class
public class AppSettings extends ActionBarActivity {
    Cursor cur;
    String name;
    String lastname;
    long dob;
    float height;
    String gpname;
    long gptelephone;
    String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        ApplicationController ac = (ApplicationController)getApplicationContext();
        cur = ac.getSharedCursor();
        getUserDetails();
        setEditTextFields();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_settings, menu);
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

    private void getUserDetails(){
        name = cur.getString(1);
        lastname = cur.getString(2);
        dob = cur.getLong(3);
        height = cur.getFloat(4);
        gpname = cur.getString(5);
        gptelephone = cur.getLong(6);
        password = cur.getString(7);
    }

    private void setEditTextFields() {
        EditText placeholder = (EditText) findViewById(R.id.edit_text_name_settings);
        placeholder.setText(name);
        placeholder = (EditText) findViewById(R.id.edit_text_surname_settings);
        placeholder.setText(lastname);

    }
}

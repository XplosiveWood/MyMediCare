package com.edgehill.mad.mymedicare;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;


public class ExistingUser extends ActionBarActivity {
    MMCDatabase database;
    String name, surname, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Login");
        database = new MMCDatabase(this);
        database.open();
        setContentView(R.layout.activity_existing_user);
        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUserInput()){
                    if (checkDBIfUserExists(name, surname)) {
                        if (checkDBPassword(name, surname, pass)) {
                            Intent intent = new Intent(ExistingUser.this, MainScreen.class);
                            startActivity(intent);
                            finish();
                        } else {
                            showSnackbar("Password Incorrect.");
                        }
                    } else {
                        showSnackbar("User does not exist.");
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_existing_user, menu);
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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }
    public boolean checkDBIfUserExists(String name, String lastname){
        return database.checkIfUserExists(name, lastname);
    }

    public boolean checkDBPassword(String name, String lastname, String pass){
        Cursor returnedCursor = database.checkPassword(name, lastname, pass);
        if (returnedCursor.moveToFirst()){
            ApplicationController ac = (ApplicationController)getApplicationContext();
            ac.setSharedCursor(returnedCursor);
            return true;
        } else {
            returnedCursor.close();
            return false;
        }
    }

    public void showSnackbar(String error){
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(error) // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }

    private boolean checkUserInput(){
        EditText editName = (EditText) findViewById(R.id.edit_text_name);
        EditText editSurname = (EditText) findViewById(R.id.edit_text_surname);
        EditText editPass = (EditText) findViewById(R.id.edit_text_password);
        name = editName.getText().toString();
        surname = editSurname.getText().toString();
        pass = editPass.getText().toString();

        if(name.isEmpty()){
            showSnackbar("Please enter your name.");
            return false;
        }
        if(surname.isEmpty()){
            showSnackbar("Please enter your surname.");
            return false;
        }
        if(pass.isEmpty()){
            showSnackbar("Please enter your password");
            return false;
        }
        return true;
    }
}

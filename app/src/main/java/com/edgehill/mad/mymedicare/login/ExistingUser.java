package com.edgehill.mad.mymedicare.login;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.edgehill.mad.mymedicare.ApplicationController;
import com.edgehill.mad.mymedicare.MMCDatabase;
import com.edgehill.mad.mymedicare.MainScreen;
import com.edgehill.mad.mymedicare.R;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

/**
 * This ActionBarActivity class is shown to users that tapped the existing user button, it has 3
 * text fields for the user to enter their details and then a button that when tapped checks the
 * details and either progresses the user on or shows an error.
 */
public class ExistingUser extends ActionBarActivity {
    // Fields for the database and the users firstname, lastname and password
    private MMCDatabase database;
    private String name, surname, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the title for the activity
        setTitle("Login");
        // Init the database and open it
        database = new MMCDatabase(this);
        database.open();
        setContentView(R.layout.activity_existing_user);
        // An object reference to a button defined in XML with an on click listener attached.
        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First check if the user has input all the correct fields.
                if(checkUserInput()){
                    // If they have then check if the user exists.
                    if (checkDBIfUserExists(name, surname)) {
                        // If the user exists check the password
                        if (checkDBPassword(name, surname, pass)) {
                            // If the password was correct, update the shared cursor and start a new
                            // intent to move the user to the next activity.
                            getCursorForCurrentUser(name, surname, pass);
                            Intent intent = new Intent(ExistingUser.this, MainScreen.class);
                            intent.putExtra("firstTimeUser", false);
                            startActivity(intent);
                            finish();
                        } else {
                            // If the password was wrong show the user an error.
                            showSnackbar("Password Incorrect.");
                        }
                    } else {
                        // If the user does not exist show the user an error.
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

    /**
     * Check if the user exists in the database using a method stored in the MMCDatabase class
     * @param name The first name of the user
     * @param lastname The last name of the user
     * @return Whether the user exists in the database, true = yes, false = no
     */
    public boolean checkDBIfUserExists(String name, String lastname){
        return database.checkIfUserExists(name, lastname);
    }

    /**
     * Check if the user trying to log in has the correct name and password combination
     * @param name The first name of the user
     * @param lastname The last name of the user
     * @param pass The password of the user
     * @return Whether the password was correct
     */
    public boolean checkDBPassword(String name, String lastname, String pass){
        // Get a cursor object using the parameters, even if no user is found the cursor will still
        // return but will contain no rows
        Cursor returnedCursor = database.checkPassword(name, lastname, pass);
        // Try and move the cursor to the first entry, if this retruns false then no match was found
        if (returnedCursor.moveToFirst()){
            //Update the shared cursor with this new user.
            ApplicationController ac = (ApplicationController)getApplicationContext();
            ac.setSharedCursor(returnedCursor);
            return true;
        } else {
            returnedCursor.close();
            return false;
        }
    }

    /**
     * Convenience method used to show the user snackbars
     * @param error String containing the error message to display
     */
    public void showSnackbar(String error){
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(error) // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }

    /**
     * Checks if all the fields on the display have been entered correctly
     * @return If the fields are filled in this should return true
     */
    private boolean checkUserInput(){
        EditText editName = (EditText) findViewById(R.id.edit_text_name);
        EditText editSurname = (EditText) findViewById(R.id.edit_text_surname_settings);
        EditText editPass = (EditText) findViewById(R.id.edit_text_password_settings);
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

    /**
     * Update the shared cursor for the current user
     * @param name The first name of the user
     * @param lastname The last name of the user
     * @param pass The password of the user
     */
    public void getCursorForCurrentUser(String name, String lastname, String pass){
        Cursor returnedCursor = database.getPersonalDetails(name, lastname, pass);
        returnedCursor.moveToFirst();
        ApplicationController ac = (ApplicationController)getApplicationContext();
        ac.setSharedCursor(returnedCursor);
    }
}

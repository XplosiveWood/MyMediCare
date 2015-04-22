package com.edgehill.mad.mymedicare.login;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.edgehill.mad.mymedicare.ApplicationController;
import com.edgehill.mad.mymedicare.Format;
import com.edgehill.mad.mymedicare.MMCDatabase;
import com.edgehill.mad.mymedicare.MainScreen;
import com.edgehill.mad.mymedicare.R;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

/*
* This activity class is displayed if the user is new to the application, they must enter all their
* personal information into the fields and then tap the next button. This will take all the data
* they entered and place it inside the database.
*/
public class NewUser extends ActionBarActivity {
    // Fields to store the database reference and also the users information.
    private MMCDatabase database;
    private String name;
    private String surname;
    private long dateOfBirth;
    private float height;
    private String gpName;
    private long gpTelephone;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the title of the activity
        setTitle("Register new user");
        setContentView(R.layout.activity_new_user);
        // Init the database reference and open the database.
        database = new MMCDatabase(this);
        database.open();
        // Reference to a button defined in XML with an on click listener attached.
        Button registerButton = (Button) findViewById(R.id.button_delete_user_settings);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if all the fields have been filled out.
                if (checkUserEntry()) {
                    // If they have then check if the user already exists.
                    if (checkDBIfUserExists(name, surname)) {
                    } else {
                        // If they don't exist then try to add them into the database.
                        if (addUserToDatabase(name, surname, dateOfBirth, height, gpName, gpTelephone, pass)) {
                            // Retrieve a cursor object with a single row, this row should contain
                            // only the currently logged in user.
                            getCursorForCurrentUser(name, surname, pass);
                            // Create a new intent
                            Intent intent = new Intent(NewUser.this, MainScreen.class);
                            // Use the putExtra method to pass forward a key value pair
                            intent.putExtra("firstTimeUser", true);
                            startActivity(intent);
                            // Method to add the GP contact information to the phones address book
                            insertGPContact();
                            // Finish stops this activity from backgrounding, this is done so that
                            // when the user progresses onto the main screen they cannot go back
                            // to the new user screen and will instead be taken back to the welcome
                            // screen
                            finish();
                        } else {
                            showErrorSnackbar("User could not be added to database.");
                        }

                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_user, menu);
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
        MenuItem item = menu.findItem(R.id.action_settings);
        item.setVisible(false);
        return true;
    }

    /**
     * For the sake of this implementation it is assumed that the unlikely event two people with
     * the same name using the same device will ever register won't happen.
     * @param name The first name of the user
     * @param lastname The last name of the user
     * @return Whether the user already exists
     */
    public boolean checkDBIfUserExists(String name, String lastname) {
        if (database.checkIfUserExists(name, lastname)) {
            // If the user already exists show a snackbar with the error message
            SnackbarManager.show(
                    Snackbar.with(getApplicationContext()) // context
                            .text("User " + name + " " + lastname + " already exists.") // text to be displayed
                            .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                            .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                    , this);
            return true;
        } else {
            // If the user does not exist then return false
            return false;
        }
    }

    /**
     * This method tried to add the user to the database, if it can it will return true, if not
     * then it returns false.
     * @param name The first name of the user
     * @param lastname The last name of the user
     * @param dateOfBirth Their date of birth
     * @param height Their height
     * @param gpName Their GPs name
     * @param gpTelephone Their GPs telephone number
     * @param password The password of the user
     * @return Whether the user could be added to the database successfully
     */
    public boolean addUserToDatabase(String name, String lastname, long dateOfBirth, float height, String gpName, long gpTelephone, String password) {
        long rowID = database.insertNewPerson(name, lastname, dateOfBirth, height, gpName, gpTelephone, password);
        if (rowID == -1) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method retrieves a cursor object for the currently logged in user, i.e. the user that
     * is creating their account on this screen and then sets the shared cursor for the entire
     * application.
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

    /**
     * Convenience method used for showing snackbars to the user.
     * @param error A string containing the error message to display
     */
    public void showErrorSnackbar(String error) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(error) // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }

    /**
     * Checks all the fields on the screen and makes sure that the user has filled them all out,
     * if they havent then a snackbar is shown telling them to entire specific data.
     * @return If any field is empty it returns false, if it is all filled return true
     */
    public boolean checkUserEntry() {
        // Variables to store the EditTexts and Datepickers
        EditText editName = (EditText) findViewById(R.id.edit_text_name);
        EditText editSurname = (EditText) findViewById(R.id.edit_text_surname_settings);
        DatePicker dob = (DatePicker) findViewById(R.id.datepicker_dob_settings);
        EditText editHeight = (EditText) findViewById(R.id.edit_text_height_settings);
        EditText editPass = (EditText) findViewById(R.id.edit_text_password_settings);
        EditText editGPName = (EditText) findViewById(R.id.edit_text_gp_name_settings);
        EditText editGPTelephone = (EditText) findViewById(R.id.edit_text_gp_telephone_settings);
        //Get the chosen day, month and year from the date picker
        int day = dob.getDayOfMonth();
        int month = dob.getMonth();
        int year = dob.getYear();
        // Use a convenience method in another class to output a usable string
        String date = Format.date(day, month, year);
        // Parse the string to long
        dateOfBirth = Long.parseLong(date);
        // Set the class fields to what the user has entered
        name = editName.getText().toString();
        surname = editSurname.getText().toString();
        pass = editPass.getText().toString();
        gpName = editGPName.getText().toString();
        String strGPTelephone = editGPTelephone.toString();
        String strHeight = editHeight.getText().toString();
        // Check if any of the class fields are empty, if they are then the user has missed one of
        // the fields and an error is shown.
        if (name.isEmpty()) {
            showErrorSnackbar("Please enter your first name.");
            return false;
        }
        if (surname.isEmpty()) {
            showErrorSnackbar("Please enter your last name.");
            return false;
        }
        if (strHeight.isEmpty()) {
            showErrorSnackbar("Please enter your height");
            return false;
        } else {
            height = Float.valueOf(strHeight);
        }
        if (gpName.isEmpty()) {
            showErrorSnackbar("Please enter your GP name.");
            return false;
        }

        if (strGPTelephone.isEmpty()) {
            showErrorSnackbar("Please enter your GP telephone.");
            return false;
        } else {
            gpTelephone = Long.valueOf(editGPTelephone.getText().toString());
        }
        if (pass.isEmpty()) {
            showErrorSnackbar("Please enter your password.");
            return false;
        }
        return true;
    }

    /**
     * Inserts a contact into the addressbook of the user based on what they entered in the fields
     */
    private void insertGPContact(){
        Intent contactIntent = new Intent(Intent.ACTION_INSERT);
        contactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, gpName);
        contactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, "0" + String.valueOf(gpTelephone));
        startActivity(contactIntent);
    }
}

package com.edgehill.mad.mymedicare.login;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.edgehill.mad.mymedicare.ApplicationController;
import com.edgehill.mad.mymedicare.MMCDatabase;
import com.edgehill.mad.mymedicare.MainScreen;
import com.edgehill.mad.mymedicare.R;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;


public class NewUser extends ActionBarActivity {
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
        setTitle("Register new user");
        setContentView(R.layout.activity_new_user);
        database = new MMCDatabase(this);
        database.open();
        Button registerButton = (Button) findViewById(R.id.button_delete_user_settings);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkUserEntry()) {
                    if (checkDBIfUserExists(name, surname)) {
                    } else {
                        if (addUserToDatabase(name, surname, dateOfBirth, height, gpName, gpTelephone, pass)) {
                            getCursorForCurrentUser(name,surname, pass);
                            MainScreen screen = new MainScreen();
                            Intent intent = new Intent(NewUser.this, MainScreen.class);
                            intent.putExtra("firstTimeUser", true);
                            startActivity(intent);
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

    // For the sake of this implementation it is assumed that the unlikely event two people with
    // the same name using the same device will ever register won't happen.
    public boolean checkDBIfUserExists(String name, String lastname) {
        if (database.checkIfUserExists(name, lastname)) {
            SnackbarManager.show(
                    Snackbar.with(getApplicationContext()) // context
                            .text("User " + name + " " + lastname + " already exists.") // text to be displayed
                            .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                            .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                    , this);
            return true;
        } else {
            return false;
        }
    }

    public boolean addUserToDatabase(String name, String lastname, long dateOfBirth, float height, String gpName, long gpTelephone, String password) {
        long rowID = database.insertNewPerson(name, lastname, dateOfBirth, height, gpName, gpTelephone, password);
        if (rowID == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void getCursorForCurrentUser(String name, String lastname, String pass){
        Cursor returnedCursor = database.checkPassword(name, lastname, pass);
        returnedCursor.moveToFirst();
        ApplicationController ac = (ApplicationController)getApplicationContext();
        ac.setSharedCursor(returnedCursor);
    }

    public void showErrorSnackbar(String error) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(error) // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }

    public String formatDate(int day, int month, int year) {
        String strMonth, strDay, strYear;
        if (month < 10) {
            strMonth = Integer.toString(month);
            strMonth = "0" + strMonth;
        } else {
            strMonth = Integer.toString(month);
        }
        if (day < 10) {
            strDay = Integer.toString(day);
            strDay = "0" + strDay;
        } else {
            strDay = Integer.toString(day);
        }
        strYear = Integer.toString(year);

        return strYear + "" + strMonth + "" + strDay;
    }

    public boolean checkUserEntry() {
        EditText editName = (EditText) findViewById(R.id.edit_text_name);
        EditText editSurname = (EditText) findViewById(R.id.edit_text_surname_settings);
        DatePicker dob = (DatePicker) findViewById(R.id.datepicker_dob_settings);
        EditText editHeight = (EditText) findViewById(R.id.edit_text_height_settings);
        EditText editPass = (EditText) findViewById(R.id.edit_text_password_settings);
        EditText editGPName = (EditText) findViewById(R.id.edit_text_gp_name_settings);
        EditText editGPTelephone = (EditText) findViewById(R.id.edit_text_gp_telephone_settings);

        int day = dob.getDayOfMonth();
        int month = dob.getMonth();
        int year = dob.getYear();
        String date = formatDate(day, month, year);
        dateOfBirth = Long.parseLong(date);
        name = editName.getText().toString();
        surname = editSurname.getText().toString();
        pass = editPass.getText().toString();
        gpName = editGPName.getText().toString();
        String strGPTelephone = editGPTelephone.toString();
        String strHeight = editHeight.getText().toString();

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
}

package com.edgehill.mad.mymedicare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.edgehill.mad.mymedicare.login.WelcomeScreen;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * This activity class is shown if the user taps the settings menu on the action bar, it contains
 * all the same fields as the new user screen and allows users to change their information and also
 * delete their profile from the application.
 */
public class AppSettings extends ActionBarActivity {
    // Class fields used to store references to the database and also the users details
    private MMCDatabase database;
    private Cursor cur;
    private String name;
    private String surname;
    private int dateOfBirth;
    private float height;
    private String gpName;
    private long gpTelephone;
    private String pass;
    private String year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_settings);
        // Init the database reference and open the database
        database = new MMCDatabase(this);
        database.open();
        // Get the shared cursor for the current user
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

    /**
     * This method retrieves all the information about the user from the cursor object, the cursor
     * object uses the SQLite database as a backing store. However querying a database takes up CPU
     * time and is not necessary if you keep hold of the cursor object returned upon a query.
     */
    private void getUserDetails(){
        cur.moveToFirst();
        name = cur.getString(cur.getColumnIndex(MMCDatabase.KEY_FIRSTNAME));
        surname = cur.getString(cur.getColumnIndex(MMCDatabase.KEY_LASTNAME));
        dateOfBirth = cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_DOB));
        height = cur.getFloat(cur.getColumnIndex(MMCDatabase.KEY_HEIGHT));
        gpName = cur.getString(cur.getColumnIndex(MMCDatabase.KEY_GPNAME));
        gpTelephone = cur.getLong(cur.getColumnIndex(MMCDatabase.KEY_GPTELEPHONE));
        pass = cur.getString(cur.getColumnIndex(MMCDatabase.KEY_PASSWORD));

        Integer[] dobEdited = getDigits(dateOfBirth);
        year = dobEdited[0] +""+ dobEdited[1] + dobEdited[2] + dobEdited[3];
        month = dobEdited[4] +""+ dobEdited[5];
        day = dobEdited[6] +""+ dobEdited[7];
    }

    /**
     * Set the fields on the GUI to the details restored from the cursor
     */
    private void setEditTextFields() {
        EditText placeholder = (EditText) findViewById(R.id.edit_text_name_settings);
        DatePicker picker = (DatePicker) findViewById(R.id.datepicker_dob_settings);
        placeholder.setText(name);
        placeholder = (EditText) findViewById(R.id.edit_text_surname_settings);
        placeholder.setText(surname);
        picker.updateDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        placeholder = (EditText) findViewById(R.id.edit_text_height_settings);
        placeholder.setText(String.valueOf(height));
        placeholder = (EditText) findViewById(R.id.edit_text_gp_name_settings);
        placeholder.setText(gpName);
        placeholder = (EditText) findViewById(R.id.edit_text_gp_telephone_settings);
        placeholder.setText(String.valueOf(gpTelephone));
        placeholder = (EditText) findViewById(R.id.edit_text_password_settings);
        placeholder.setText(pass);
    }


    private static Integer[] getDigits(int num) {
        List<Integer> digits = new ArrayList<Integer>();
        collectDigits(num, digits);
        return digits.toArray(new Integer[]{});
    }

    private static void collectDigits(int num, List<Integer> digits) {
        if(num / 10 > 0) {
            collectDigits(num / 10, digits);
        }
        digits.add(num % 10);
    }

    /**
     * This method is called when the user taps the DELETE PROFILE button, it first prompts the user
     * with an alert dialog asking if they are sure they wish delete.
     * @param v An object reference to the view that called it
     */
    public void deleteUser(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Profile Deletion");
        alert.setMessage("Are you sure you wish to delete your profile?");
        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                database.deleteUser(cur);
                cur.close();
                Intent intent = new Intent(AppSettings.this, WelcomeScreen.class);
                startActivity(intent);
                finish();

            }
        });
        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        alert.show();
    }

    /**
     * This method is called to update the database with the new data the user has entered.
     * @param v An object reference to the view that called it
     */
    public void alterUser(View v){
        if(checkUserEntry()){
            if(database.updatePerson(name, surname, new Long(dateOfBirth), height, gpName, gpTelephone, pass, cur) == 1){
                showErrorSnackbar("Details updated.");
            } else {
                showErrorSnackbar("Could not change details.");
            }
        }
    }

    /**
     * This method checks that all the fields have information entered in them, if they don't then
     * it alerts the user.
     * @return If all the fields are filled return true
     */
    private boolean checkUserEntry() {
        EditText editName = (EditText) findViewById(R.id.edit_text_name_settings);
        EditText editSurname = (EditText) findViewById(R.id.edit_text_surname_settings);
        DatePicker dob = (DatePicker) findViewById(R.id.datepicker_dob_settings);
        EditText editHeight = (EditText) findViewById(R.id.edit_text_height_settings);
        EditText editPass = (EditText) findViewById(R.id.edit_text_password_settings);
        EditText editGPName = (EditText) findViewById(R.id.edit_text_gp_name_settings);
        EditText editGPTelephone = (EditText) findViewById(R.id.edit_text_gp_telephone_settings);

        int day = dob.getDayOfMonth();
        int month = dob.getMonth();
        int year = dob.getYear();
        String date = Format.date(day, month, year);
        dateOfBirth = Integer.parseInt(date);
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

    /**
     * Convenience method used to show the user snackbars
     * @param error A string containing the error message to be displayed to the user.
     */
    private void showErrorSnackbar(String error) {
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text(error) // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }
}

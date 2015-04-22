package com.edgehill.mad.mymedicare.dataentry;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.edgehill.mad.mymedicare.ApplicationController;
import com.edgehill.mad.mymedicare.Format;
import com.edgehill.mad.mymedicare.MMCDatabase;
import com.edgehill.mad.mymedicare.R;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.Calendar;

/**
 * This activity class is loaded from the main screen of the application if the user pressed the
 * enter data button for blood pressure.
 */

public class BloodPressure extends ActionBarActivity {
    // Class fields to store the cursor and database references
    private MMCDatabase database;
    private Cursor cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure);
        // Set the date picker to todays date
        DatePicker datepick = (DatePicker) findViewById(R.id.datepicker_bloodpressure);
        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        datepick.updateDate(year, month, day);
        // Init the database and open it
        database = new MMCDatabase(this);
        database.open();
        // Get the shared cursor
        ApplicationController ac = (ApplicationController)getApplicationContext();
        cur = ac.getSharedCursor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_blood_pressure, menu);
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
     * Checks if the user has entered data into all fields
     * @param v An object reference to the view that called it
     */
    public void checkUserEntry(View v){
        EditText editSystolic = (EditText) findViewById(R.id.edit_text_systolic);
        EditText editDiastolic = (EditText) findViewById(R.id.edit_text_diastolic);
        String strSystolic = editSystolic.getText().toString();
        String strDiastolic = editDiastolic.getText().toString();
        if(strSystolic.isEmpty()){
            SnackbarManager.show(
                    Snackbar.with(getApplicationContext()) // context
                            .text("Enter your systolic blood pressure") // text to be displayed
                            .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                            .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                    , this);
        } else if(strDiastolic.isEmpty()) {
            SnackbarManager.show(
                    Snackbar.with(getApplicationContext()) // context
                            .text("Enter your diastolic blood pressure") // text to be displayed
                            .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                            .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                    , this);
        } else {
            int intSystolic = Integer.parseInt(strSystolic);
            int intDiastolic = Integer.parseInt(strDiastolic);
            saveBloodPressureData(intSystolic, intDiastolic);
        }
    }

    /**
     * A method used to convert and save the data the user enters.
     * @param systolic The systolic BP of the user
     * @param diastolic The diastolic BP of the user
     */
    public void saveBloodPressureData(int systolic, int diastolic){
        int userID = cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID));
        DatePicker date = (DatePicker) findViewById(R.id.datepicker_bloodpressure);
        TimePicker timePick = (TimePicker) findViewById(R.id.timepicker_bloodpressure);
        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year = date.getYear();
        String strDate = Format.date(day, month, year);
        long measurementDate = Long.parseLong(strDate);
        int hour = timePick.getCurrentHour();
        int minute = timePick.getCurrentMinute();
        int time = Integer.parseInt(hour + minute + "");

        database.insertBloodpressure(diastolic, systolic, measurementDate, userID);
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text("Blood Pressure Saved") // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }
}

package com.edgehill.mad.mymedicare;

import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.edgehill.mad.mymedicare.R;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.Calendar;
import java.util.Date;


public class HeartRate extends ActionBarActivity {
    private MMCDatabase database;
    private Cursor cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate);
        DatePicker datepick = (DatePicker) findViewById(R.id.datepicker_heartrate);
        Calendar cal = Calendar.getInstance();
        int year=cal.get(Calendar.YEAR);
        int month=cal.get(Calendar.MONTH);
        int day=cal.get(Calendar.DAY_OF_MONTH);
        datepick.updateDate(year, month, day);
        database = new MMCDatabase(this);
        database.open();
        ApplicationController ac = (ApplicationController)getApplicationContext();
        cur = ac.getSharedCursor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_heart_rate, menu);
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

    public void checkUserEntry(View v){
        EditText heartrate = (EditText) findViewById(R.id.edit_text_heartrate);
        String strHeartRate = heartrate.getText().toString();
        if(strHeartRate.isEmpty()){
            SnackbarManager.show(
                    Snackbar.with(getApplicationContext()) // context
                            .text("Enter your heart rate reading") // text to be displayed
                            .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                            .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                    , this);
        } else {
            int intHeartRate = Integer.parseInt(strHeartRate);
            saveHeartRateData(intHeartRate);
        }
    }
    public void saveHeartRateData(int heartRate){
        int userID = cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID));
        DatePicker date = (DatePicker) findViewById(R.id.datepicker_heartrate);
        TimePicker timePick = (TimePicker) findViewById(R.id.timepicker_heartrate);
        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year = date.getYear();
        String strDate = Format.date(day, month, year);
        long measurementDate = Long.parseLong(strDate);
        int hour = timePick.getCurrentHour();
        int minute = timePick.getCurrentMinute();
        int time = Integer.parseInt(hour + minute + "");

        database.insertHeartRate(measurementDate, time, heartRate, userID);
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text("Heart Rate Saved") // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }
}

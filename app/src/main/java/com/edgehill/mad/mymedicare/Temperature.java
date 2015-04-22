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

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import java.util.Calendar;


public class Temperature extends ActionBarActivity {
    private MMCDatabase database;
    private Cursor cur;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        DatePicker datepick = (DatePicker) findViewById(R.id.datepicker_temperature);
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
        getMenuInflater().inflate(R.menu.menu_temperature, menu);
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
        EditText editTemperature = (EditText) findViewById(R.id.edit_text_temperature);
        String strTemperature = editTemperature.getText().toString();
        if(strTemperature.isEmpty()){
            SnackbarManager.show(
                    Snackbar.with(getApplicationContext()) // context
                            .text("Enter your temperature reading") // text to be displayed
                            .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                            .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                    , this);
        } else {
            float floatTemperature = Float.valueOf(strTemperature);
            saveTemperatureData(floatTemperature);
        }
    }
    public void saveTemperatureData(float temperature){
        int userID = cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID));
        DatePicker date = (DatePicker) findViewById(R.id.datepicker_temperature);
        TimePicker timePick = (TimePicker) findViewById(R.id.timepicker_temperature);
        int day = date.getDayOfMonth();
        int month = date.getMonth();
        int year = date.getYear();
        String strDate = Format.date(day, month, year);
        long measurementDate = Long.parseLong(strDate);
        int hour = timePick.getCurrentHour();
        int minute = timePick.getCurrentMinute();
        int time = Integer.parseInt(hour + minute + "");

        database.insertTemperature(temperature, measurementDate, userID);
        SnackbarManager.show(
                Snackbar.with(getApplicationContext()) // context
                        .text("Temperature Saved") // text to be displayed
                        .textColor(getBaseContext().getResources().getColor(R.color.primary_text)) // change the text color
                        .color(getBaseContext().getResources().getColor(R.color.primary)) // change the background color
                , this);
    }
}

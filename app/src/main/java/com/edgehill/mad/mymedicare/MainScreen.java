package com.edgehill.mad.mymedicare;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.edgehill.mad.mymedicare.dataentry.BloodPressure;
import com.edgehill.mad.mymedicare.dataentry.HeartRate;
import com.edgehill.mad.mymedicare.dataentry.Temperature;

/**
 * This activity class is loaded once the user logs into the application, it retrieves all the
 * information about heart rate, temperature etc and then display information about those readings
 */
public class MainScreen extends ActionBarActivity {
    // Class fields to hold the cursor object and the database.
    private Cursor cur;
    private MMCDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        // Bundle variable
        Bundle extras;
        boolean firstTimeUser;
        // Init database and open
        database = new MMCDatabase(this);
        database.open();
        // Get the shared cursor
        ApplicationController ac = (ApplicationController)getApplicationContext();
        cur = ac.getSharedCursor();
        // If this activity wasnt loaded with a shared instance state
        if (savedInstanceState == null) {
            // Get the extras passed by the intent
            extras = getIntent().getExtras();
            if(extras == null) {
                // If the extras was null then set the boolean to false (Defaults)
                firstTimeUser = false;
            } else {
                // Else set the boolean to the value stored in the extras
                firstTimeUser = extras.getBoolean("firstTimeUser");
            }
        } else {
            // Get the value store inside the saved instance state
            firstTimeUser= (Boolean) savedInstanceState.getSerializable("firstTimeUser");
        }

        if(firstTimeUser){
            // If they are a first time user
            TextView view = (TextView) findViewById(R.id.text_view_heading);
            view.setText(getResources().getString(R.string.new_user_main_screen_title) + " " + cur.getString(cur.getColumnIndex(MMCDatabase.KEY_FIRSTNAME)));
            view = (TextView) findViewById(R.id.text_view_heading_description);
            view.setText(getResources().getString(R.string.new_user_main_screen_body));
        } else {
            // If they are a returning user
            TextView view = (TextView) findViewById(R.id.text_view_heading);
            view.setText(getResources().getString(R.string.existing_user_main_screen_title) + " " + cur.getString(cur.getColumnIndex(MMCDatabase.KEY_FIRSTNAME)));
            view = (TextView) findViewById(R.id.text_view_heading_description);
            view.setText(getResources().getString(R.string.existing_user_main_screen_body));
        }
        getBloodPressureInformation();
        getHeartRateInformation();
        getTemperatureInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
        return true;
    }

    /**
     * If this activity is ever resumed then call the three data retrieval methods again.
     */
    @Override
    protected void onResume() {
        super.onResume();
        getBloodPressureInformation();
        getHeartRateInformation();
        getTemperatureInformation();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainScreen.this, AppSettings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * This method gets the heart rate information stored in the database and then checks the values
     * On completion is changes the imageview on the main screen to be one of three colours denoting
     * high, medium and low.
     * @return True if it was able to retrieve information, false if not.
     */
    private boolean getHeartRateInformation(){
        Cursor HRCur = database.getHeartRate(cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID)));
        ImageView image = (ImageView) findViewById(R.id.image_view_heartrate);
        TextView text = (TextView) findViewById(R.id.text_view_heartrate_assessment);
        if(HRCur.moveToFirst()){
            HRCur.moveToFirst();
            int heartRate = HRCur.getInt(HRCur.getColumnIndex(MMCDatabase.KEY_HRMEASUREMENT));
            if(heartRate >= 0 && heartRate <= 120){
                // Normal
                image.setBackgroundColor(Color.GREEN);
                text.setText("NORMAL");
            } else if (heartRate > 120 && heartRate <= 160){
                // Low Risk
                image.setBackgroundColor(Color.YELLOW);
                text.setText("LOW RISK");
            } else if(heartRate > 160){
                // High Risk
                image.setBackgroundColor(Color.RED);
                text.setText("HIGH RISK");
                sendText("heart rate");
            }
            return true;
        } else {
            return false;
        }
    }
    /**
     * This method gets the blood pressure information stored in the database and then checks the values
     * On completion is changes the imageview on the main screen to be one of three colours denoting
     * high, medium and low.
     * @return True if it was able to retrieve information, false if not.
     */
    private boolean getBloodPressureInformation(){
        Cursor BPCur = database.getBloodPressure(cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID)));
        ImageView image = (ImageView) findViewById(R.id.image_view_blood_pressure);
        TextView text = (TextView) findViewById(R.id.text_view_blood_pressure_assessment);
        if(BPCur.moveToFirst()){
            BPCur.moveToFirst();
            int highBloodPressure = BPCur.getInt(BPCur.getColumnIndex(MMCDatabase.KEY_HIGHPRESSURE));
            int lowBloodPressure = BPCur.getInt(BPCur.getColumnIndex(MMCDatabase.KEY_LOWPRESSURE));
            if(lowBloodPressure < 80 && highBloodPressure <= 120){
                // Normal
                image.setBackgroundColor(Color.GREEN);
                text.setText("NORMAL");
            } else if (lowBloodPressure >= 80 && lowBloodPressure <= 110 && highBloodPressure >= 120 && highBloodPressure <=180){
                // Low Risk
                image.setBackgroundColor(Color.YELLOW);
                text.setText("LOW RISK");
            } else if(lowBloodPressure > 110 && highBloodPressure > 180){
                // High Risk
                image.setBackgroundColor(Color.RED);
                text.setText("HIGH RISK");
                sendText("blood pressure");
            }
            return true;
        } else {
            return false;
        }
    }
    /**
     * This method gets the temperature information stored in the database and then checks the values
     * On completion is changes the imageview on the main screen to be one of three colours denoting
     * high, medium and low.
     * @return True if it was able to retrieve information, false if not.
     */
    private boolean getTemperatureInformation(){
        Cursor tempCur = database.getTemperature(cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID)));
        ImageView image = (ImageView) findViewById(R.id.image_view_temperature);
        TextView text = (TextView) findViewById(R.id.text_view_temperature_assessment);
        if(tempCur.moveToFirst()){
            tempCur.moveToFirst();
            float temperature = tempCur.getFloat(tempCur.getColumnIndex(MMCDatabase.KEY_TEMPERATURE));
            if(temperature >= 37){
                // Normal
                image.setBackgroundColor(Color.GREEN);
                text.setText("NORMAL");
            } else if (temperature > 37 && temperature < 38){
                // Low Risk
                image.setBackgroundColor(Color.YELLOW);
                text.setText("LOW RISK");
            } else if(temperature >= 38){
                // High Risk
                image.setBackgroundColor(Color.RED);
                text.setText("HIGH RISK");
                sendText("temperature");
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method sends a text message to the GP telephone stored in the database
     * @param typeOfRisk A string value stating the type of risk the user is under
     */
    private void sendText(String typeOfRisk){
        String message = cur.getString(cur.getColumnIndex(MMCDatabase.KEY_FIRSTNAME)) + " has an abnormal " + typeOfRisk;
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(String.valueOf(cur.getLong(cur.getColumnIndex(MMCDatabase.KEY_GPTELEPHONE))), null, message, null, null);
    }

    /**
     * A method called by all three buttons on this screen which uses a switch statement to decide
     * between which screen to load.
     * @param v An object reference to the view which called it
     */
    public void gotoNextScreen(View v){
        Intent intent;
        int id = v.getId();
        switch(id){
            case R.id.button_blood_pressure :{
                intent = new Intent(MainScreen.this, BloodPressure.class);
                startActivity(intent);
                break;
            }

            case R.id.button_heart_rate :{
                intent = new Intent(MainScreen.this, HeartRate.class);
                startActivity(intent);
                break;
            }

            case R.id.button_temperature :{
                intent = new Intent(MainScreen.this, Temperature.class);
                startActivity(intent);
                break;
            }
        }
    }
}

package com.edgehill.mad.mymedicare.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.edgehill.mad.mymedicare.MMCDatabase;
import com.edgehill.mad.mymedicare.R;
import com.edgehill.mad.mymedicare.TypeFaces;
/*
* This activity class is the first screen the loads when the user opens the application
* it contains two buttons asking the user if they are a new or returning user. It then loads the
* appropriate activity next.
*/

public class WelcomeScreen extends Activity {
    // Field reference to the database class
    MMCDatabase databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        // Check API level on startup to change API level specific traits
        this.checkAPILevel();
        // Init the database and call the open method to open or create the database.
        databaseHelper = new MMCDatabase(this);
        databaseHelper.open();
        // Variable refernce to the button defined in XML, with an on click listener attached.
        Button newUserButton = (Button) findViewById(R.id.NewUser);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new intent object
                Intent intent = new Intent(WelcomeScreen.this, NewUser.class);
                // Start the activity defined within the intent
                startActivity(intent);
            }
        });
        // Same as above but for the returning user button
        Button returningUserButton = (Button) findViewById(R.id.ExistingUser);
        returningUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this, ExistingUser.class);
                startActivity(intent);
            }
        });
    }
    // This method checks the API level of the device that the application is running on
    private void checkAPILevel(){
        // If the API level is less than 16. ICS 4.0.3 or below
        if (android.os.Build.VERSION.SDK_INT < 16){
            this.setFontFamilyICS();
        } else {
            this.setFontFamilyICS();
        }
    }
    // Below API Level 16 the XML attribute font-family is not available
    // this is a workaround for android version 4.0.3 which is the lowest
    // one supported by this application.
    private void setFontFamilyICS(){
        Log.d("WelcomeScreen", "changing font styles");
        TextView welcomeScreenTopRow = (TextView) findViewById(R.id.welcomeScreenTopRow);
        TextView welcomeScreenBottomRow = (TextView) findViewById(R.id.welcomeScreenBottomRow);
        welcomeScreenBottomRow.setTypeface(TypeFaces.get(getApplicationContext(), "Champagne & Limousines"));
        welcomeScreenTopRow.setTypeface(TypeFaces.get(getApplicationContext(), "Champagne & Limousines"));
    }
}

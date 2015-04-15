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


public class WelcomeScreen extends Activity {
    MMCDatabase databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        this.checkAPILevel();
        databaseHelper = new MMCDatabase(this);
        databaseHelper.open();
        Button newUserButton = (Button) findViewById(R.id.NewUser);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this, NewUser.class);
                startActivity(intent);
            }
        });
        Button returningUserButton = (Button) findViewById(R.id.ExistingUser);
        returningUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this, ExistingUser.class);
                startActivity(intent);
            }
        });
    }

    private void checkAPILevel(){
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

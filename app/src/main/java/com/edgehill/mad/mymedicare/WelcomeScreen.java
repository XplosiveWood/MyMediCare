package com.edgehill.mad.mymedicare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.Button;
import android.widget.TextView;


public class WelcomeScreen extends ActionBarActivity {
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome_screen, menu);
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

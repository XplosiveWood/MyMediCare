package com.edgehill.mad.mymedicare;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainScreen extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Bundle extras = getIntent().getExtras();
        boolean firstTimeUser = extras.getBoolean("firstTimeUser");
        if(firstTimeUser){
            // If they are a first time user
            TextView view = (TextView) findViewById(R.id.text_view_heading);
            view.setText(getResources().getString(R.string.new_user_main_screen_title));
            view = (TextView) findViewById(R.id.text_view_heading_description);
            view.setText(getResources().getString(R.string.new_user_main_screen_body));
        } else {
            // If they are a returning user
            TextView view = (TextView) findViewById(R.id.text_view_heading);
            view.setText(getResources().getString(R.string.existing_user_main_screen_title));
            view = (TextView) findViewById(R.id.text_view_heading_description);
            view.setText(getResources().getString(R.string.existing_user_main_screen_body));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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
}

package com.edgehill.mad.mymedicare;

import android.app.Application;
import android.database.Cursor;

/**
 * Created by scottwhite on 12/04/15.
 */
public class ApplicationController extends Application {
    Cursor sharedCursor; // this cursor can be shared between different Activities

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public Cursor getSharedCursor()
    {
        return this.sharedCursor;
    }

    public void setSharedCursor(Cursor c)
    {
        this.sharedCursor = c;
    }
}

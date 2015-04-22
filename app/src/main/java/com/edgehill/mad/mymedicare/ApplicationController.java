package com.edgehill.mad.mymedicare;

import android.app.Application;
import android.database.Cursor;

/**
 * This class can be accessed throughout the application using the getApplication method and is
 * useful for storing information that has to be accessed over multiple screens. In this case it is
 * being used to store a cursor pointing to the database row of the current user.
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

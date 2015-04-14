package com.edgehill.mad.mymedicare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MMCDatabase{
    public static final String KEY_ROWID = "_id";
    public static final String KEY_USERID = "userid";
    public static final String KEY_FIRSTNAME = "firstname";
    public static final String KEY_LASTNAME = "lastname";
    public static final String KEY_DOB = "dob";
    public static final String KEY_HEIGHT = "height";
    public static final String KEY_GPNAME = "gpname";
    public static final String KEY_GPTELEPHONE = "gptelephone";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_HRDATE = "hrdate";
    public static final String KEY_HRTIME = "hrtime";
    public static final String KEY_HRMEASUREMENT = "hrmeasurement";
    public static final String KEY_WEIGHTTIME = "weighttime";
    public static final String KEY_WEIGHTDATE = "weightdate";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_EXERTIME = "exertime";
    public static final String KEY_EXERDATE = "exerdate";
    public static final String KEY_EXDONE = "exdone";
    public static final String KEY_CALBURNED = "calburned";
    public static final String KEY_SLEEPDATE = "sleepdate";
    public static final String KEY_TIMESLEEP = "timesleep";
    public static final String KEY_TIMEAWAKE = "timeawake";
    public static final String KEY_LOWPRESSURE = "lowpressure";
    public static final String KEY_HIGHPRESSURE = "highpressure";
    public static final String KEY_PRESSUREDATE = "pressuredate";
    public static final String KEY_TEMPERATURE = "temperature";
    public static final String KEY_TEMPDATE = "tempdate";
    public static final String TABLE_PERSONALDETAILS = "personaldetails";
    public static final String TABLE_HEARTRATE = "heartrate";
    public static final String TABLE_WEIGHT = "weight";
    public static final String TABLE_EXERCISE = "exercise";
    public static final String TABLE_SLEEP = "sleep";
    public static final String TABLE_BLOODPRESSURE = "bloodpressure";
    public static final String TABLE_TEMPERATURE = "temperature";
    private static final String TAG = "MMCDatabse";
    private static final String DATABASE_NAME = "mmcdatabase";
    private static final int DATABASE_VERSION = 1;
    public final Context context;

    private DatabaseHelper DBHelper;
    public SQLiteDatabase db;

    public MMCDatabase(Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME,  null, DATABASE_VERSION);
        }
        // CREATE TABLE personaldetails  (userid INTEGER NOT NULL PRIMARY KEY, firstname varchar NOT NULL, lastname varchar NOT NULL, dob date NOT NULL, height float(2) NOT NULL, gpname varchar NOT NULL, gptelephone long NOT NULL, password varchar NOT NULL)
        // CREATE TABLE heartrate (hrdate date NOT NULL, hrtime time NOT NULL, measurement int NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE weighttable (weightdate date NOT NULL, weighttime time NOT NULL, weight float(2) NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE exercise (exerdate date NOT NULL, exertime time NOT NULL, exercisedone varchar, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE sleep (sleepdate date NOT NULL, timesleep time NOT NULL, timeawake time NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE bloodpressure (lowpressure int NOT NULL, highpressure int NOT NULL, pressuredate long NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE temperature (temperature long NOT NULL, tempdate long NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL("CREATE TABLE "+TABLE_PERSONALDETAILS+" ("+KEY_USERID+
                        " INTEGER NOT NULL PRIMARY KEY, "+KEY_FIRSTNAME +" varchar NOT NULL, "
                        +KEY_LASTNAME +" varchar NOT NULL, "+KEY_DOB +" long NOT NULL, "+KEY_HEIGHT+
                        " float(2) NOT NULL, "+KEY_GPNAME+" varchar NOT NULL, "+KEY_GPTELEPHONE+" long NOT NULL, "
                        +KEY_PASSWORD+" varchar NOT NULL)");
                db.execSQL("CREATE TABLE "+TABLE_HEARTRATE+" ("+KEY_HRDATE+" long NOT NULL, "
                        +KEY_HRTIME+" time NOT NULL, "+KEY_HRMEASUREMENT+" int NOT NULL, "+KEY_USERID+
                        " int, FOREIGN KEY("+KEY_USERID+") REFERENCES "+TABLE_PERSONALDETAILS+"("
                        +KEY_USERID+"))");
                db.execSQL("CREATE TABLE "+TABLE_WEIGHT+" ("+KEY_WEIGHTDATE+" long NOT NULL, "
                        +KEY_WEIGHTTIME+" time NOT NULL, "+KEY_WEIGHT+" float(2) NOT NULL, "
                        +KEY_USERID+" int, FOREIGN KEY("+KEY_USERID+") REFERENCES "
                        +TABLE_PERSONALDETAILS+"("+KEY_USERID+"))");

                db.execSQL("CREATE TABLE "+TABLE_EXERCISE+" ("+KEY_EXERDATE+" long NOT NULL, "
                        +KEY_EXERTIME+" time NOT NULL, "+KEY_EXDONE+" varchar, "+KEY_USERID+" int, " +
                        "FOREIGN KEY("+KEY_USERID+") REFERENCES "+TABLE_PERSONALDETAILS+"("
                        +KEY_USERID+"))");

                db.execSQL("CREATE TABLE "+TABLE_SLEEP+"("+KEY_SLEEPDATE+" long NOT NULL, "
                        +KEY_TIMESLEEP+" time NOT NULL, "+KEY_TIMEAWAKE+" time NOT NULL, "
                        +KEY_USERID+", FOREIGN KEY("+KEY_USERID+") REFERENCES "
                        +TABLE_PERSONALDETAILS+"("+KEY_USERID+"))");

                db.execSQL("CREATE TABLE "+TABLE_BLOODPRESSURE+" ("+KEY_LOWPRESSURE+" int NOT NULL, "
                        +KEY_HIGHPRESSURE+" int NOT NULL, "+KEY_PRESSUREDATE+" long NOT NULL, "+KEY_USERID+" int, " +
                        "FOREIGN KEY("+KEY_USERID+") REFERENCES "+TABLE_PERSONALDETAILS+"("
                        +KEY_USERID+"))");

                db.execSQL("CREATE TABLE "+TABLE_TEMPERATURE+" ("+KEY_TEMPERATURE+" long NOT NULL, "
                        +KEY_TEMPDATE+" long NOT NULL, "+KEY_USERID+" int, " +
                        "FOREIGN KEY("+KEY_USERID+") REFERENCES "+TABLE_PERSONALDETAILS+"("
                        +KEY_USERID+"))");
            } catch (SQLException e){
                Log.e("Database Helper", "SQL Exception: ", e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public MMCDatabase open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DBHelper.close();
    }

    public long insertNewPerson(String name, String lastname, long dob, float height, String gpname, long gptelephone, String password){
        ContentValues personValues = new ContentValues();
        personValues.putNull(KEY_USERID);
        personValues.put(KEY_FIRSTNAME, name);
        personValues.put(KEY_LASTNAME, lastname);
        personValues.put(KEY_DOB, dob);
        personValues.put(KEY_HEIGHT, height);
        personValues.put(KEY_GPNAME, gpname);
        personValues.put(KEY_GPTELEPHONE, gptelephone);
        personValues.put(KEY_PASSWORD, password);
        return db.insert(TABLE_PERSONALDETAILS, null, personValues);
    }

    public long insertHeartRate(long date, int time, int HRMeasurement, int userid){
        ContentValues heartRateValues = new ContentValues();
        heartRateValues.put(KEY_HRDATE, date);
        heartRateValues.put(KEY_HRTIME, time);
        heartRateValues.put(KEY_HRMEASUREMENT, HRMeasurement);
        heartRateValues.put(KEY_USERID, userid);
        return db.insert(TABLE_HEARTRATE, null, heartRateValues);
    }

    public long insertWeight(long date, int time, float weight, int userid){
        ContentValues weightValues = new ContentValues();
        weightValues.put(KEY_WEIGHTDATE, date);
        weightValues.put(KEY_WEIGHTTIME, time);
        weightValues.put(KEY_WEIGHT, weight);
        weightValues.put(KEY_USERID, userid);
        return db.insert(TABLE_WEIGHT, null, weightValues);
    }

    public long insertExercise(long date, int time, String exerciseDone, int caloriesBurned, int userid){
        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put(KEY_EXERDATE, date);
        exerciseValues.put(KEY_EXERTIME, time);
        exerciseValues.put(KEY_EXDONE, exerciseDone);
        exerciseValues.put(KEY_CALBURNED, caloriesBurned);
        exerciseValues.put(KEY_USERID, userid);
        return db.insert(TABLE_EXERCISE, null, exerciseValues);
    }

    public long insertSleep(long date, int timesleep, int timeawake, int userid){
        ContentValues sleepValues = new ContentValues();
        sleepValues.put(KEY_SLEEPDATE, date);
        sleepValues.put(KEY_TIMESLEEP, timesleep);
        sleepValues.put(KEY_TIMEAWAKE, timeawake);
        sleepValues.put(KEY_USERID, userid);
        return db.insert(TABLE_SLEEP, null, sleepValues);
    }

    public long insertBloodpressure(int lowpressure, int highpressure, long pressuredate, int userid){
        ContentValues bloodpressureValues = new ContentValues();
        bloodpressureValues.put(KEY_LOWPRESSURE, lowpressure);
        bloodpressureValues.put(KEY_HIGHPRESSURE, highpressure);
        bloodpressureValues.put(KEY_TEMPDATE, pressuredate);
        bloodpressureValues.put(KEY_USERID, userid);
        return db.insert(TABLE_BLOODPRESSURE, null, bloodpressureValues);
    }

    public long insertTemperature(long temperature, long tempdate, int userid){
        ContentValues temperatureValues = new ContentValues();
        temperatureValues.put(KEY_TEMPERATURE, temperature);
        temperatureValues.put(KEY_TEMPDATE, tempdate);
        temperatureValues.put(KEY_USERID, userid);
        return db.insert(TABLE_BLOODPRESSURE, null, temperatureValues);
    }

    public Boolean checkIfUserExists(String name, String surname){
        Cursor cur = db.query(TABLE_PERSONALDETAILS, new String[] {KEY_USERID},KEY_FIRSTNAME + " like '" + name + "' AND " +KEY_LASTNAME+ " like '" +surname+"'", null, null, null, null);
        if (cur.moveToFirst()){
            cur.close();
            return true;
        } else {
            cur.close();
            return false;
        }
    }

    public Cursor checkPassword(String name, String surname, String pass){
        Cursor cur = db.query(TABLE_PERSONALDETAILS, new String[] {KEY_USERID},KEY_FIRSTNAME + " like '" + name + "' AND " +KEY_LASTNAME+ " like '" +surname+"' AND " +KEY_PASSWORD+" like '"+pass+"'", null, null, null, null);
        return cur;
    }
}

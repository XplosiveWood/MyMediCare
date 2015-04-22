package com.edgehill.mad.mymedicare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * This class controls everything to do with the SQLite database backing store, making the tables,
 * storing the data and querying the data all happens in this class.
 */
public class MMCDatabase{
    // Fields used to store table and row names.
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
    public static final String SELECT_ALL = "*";
    private static final String TAG = "MMCDatabse";
    private static final String DATABASE_NAME = "mmcdatabase";
    private static final int DATABASE_VERSION = 1;
    public final Context context;

    private DatabaseHelper DBHelper;
    public SQLiteDatabase db;

    /**
     * Public constructor for the database object.
     * @param ctx The context from where the database was called.
     */
    public MMCDatabase(Context ctx){
        this.context = ctx;
        // Init a new object of DBHelper
        DBHelper = new DatabaseHelper(context);
    }

    /**
     * This inner class controls the opening, updating and creating of the database.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(Context context){
            super(context, DATABASE_NAME,  null, DATABASE_VERSION);
        }
        // CREATE TABLE personaldetails  (userid INTEGER NOT NULL PRIMARY KEY, firstname varchar NOT NULL, lastname varchar NOT NULL, dob date NOT NULL, height float(2) NOT NULL, gpname varchar NOT NULL, gptelephone long NOT NULL, password varchar NOT NULL)
        // CREATE TABLE heartrate (hrdate date NOT NULL, hrtime long NOT NULL, measurement int NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE weighttable (weightdate date NOT NULL, weighttime time NOT NULL, weight float(2) NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE exercise (exerdate date NOT NULL, exertime time NOT NULL, exercisedone varchar, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE sleep (sleepdate date NOT NULL, timesleep time NOT NULL, timeawake time NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE bloodpressure (lowpressure int NOT NULL, highpressure int NOT NULL, pressuredate long NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))
        // CREATE TABLE temperature (temperature float(2) NOT NULL, tempdate long NOT NULL, userid int, FOREIGN KEY(userid) REFERENCES personaldetails(userid))

        /**
         * When the open method of this class is called it uses a method called getWritableDatabase
         * this first checks to see if the database exists. If it does then it passes the database
         * object back, if not then it first calls this class onCreate.
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                // Try to build the tables of the database.
                db.execSQL( "PRAGMA foreign_keys=ON;" );
                db.execSQL("CREATE TABLE "+TABLE_PERSONALDETAILS+" ("+KEY_USERID+
                        " INTEGER NOT NULL PRIMARY KEY, "+KEY_FIRSTNAME +" varchar NOT NULL, "
                        +KEY_LASTNAME +" varchar NOT NULL, "+KEY_DOB +" long NOT NULL, "+KEY_HEIGHT+
                        " float(2) NOT NULL, "+KEY_GPNAME+" varchar NOT NULL, "+KEY_GPTELEPHONE+" long NOT NULL, "
                        +KEY_PASSWORD+" varchar NOT NULL)");
                db.execSQL("CREATE TABLE "+TABLE_HEARTRATE+" ("+KEY_HRDATE+" long NOT NULL, "
                        +KEY_HRTIME+" long NOT NULL, "+KEY_HRMEASUREMENT+" int NOT NULL, "+KEY_USERID+
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

                db.execSQL("CREATE TABLE "+TABLE_TEMPERATURE+" ("+KEY_TEMPERATURE+" float(2) NOT NULL, "
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

        @Override
        public void onOpen(SQLiteDatabase db) {
            db.execSQL( "PRAGMA foreign_keys=ON;" );
        }
    }

    /**
     * Method used to open the database.
     * @return MMCDatabase object
     * @throws SQLException
     */
    public MMCDatabase open() throws SQLException{
        db = DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * Close the database.
     */
    public void close(){
        DBHelper.close();
    }

    /**
     * This method inserts all the data associated with the user into the database.
     * @param name First name of the user
     * @param lastname Last name of the user
     * @param dob The users date of birth
     * @param height Height of the user
     * @param gpname The users GPs name
     * @param gptelephone The users GPs telephone
     * @param password Password of the user
     * @return A long value with the row id of the newly inserted row, -1 if it could not be inserted
     */
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

    /**
     * This method updates the data stored about the user
     * @param name First name of the user
     * @param lastname Last name of the user
     * @param dob The users date of birth
     * @param height Height of the user
     * @param gpname The users GPs name
     * @param gptelephone The users GPs telephone
     * @param password Password of the user
     * @param cur A cursor object of the user
     * @return A long value with the amount of rows changed, -1 if no rows were changed
     */
    public long updatePerson(String name, String lastname, long dob, float height, String gpname, long gptelephone, String password, Cursor cur){
        ContentValues personValues = new ContentValues();
        personValues.put(KEY_FIRSTNAME, name);
        personValues.put(KEY_LASTNAME, lastname);
        personValues.put(KEY_DOB, dob);
        personValues.put(KEY_HEIGHT, height);
        personValues.put(KEY_GPNAME, gpname);
        personValues.put(KEY_GPTELEPHONE, gptelephone);
        personValues.put(KEY_PASSWORD, password);
        return db.update(TABLE_PERSONALDETAILS, personValues, "userid = " + cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID)), null);
    }

    /**
     * A method to insert heart rate information into the database
     * @param date The date the measurement was taken
     * @param time The time the measurement was taken
     * @param HRMeasurement The heart rate measurement
     * @param userid The userID of the current user
     * @return A long value with the row id of the newly inserted row, -1 if it could not be inserted
     */
    public long insertHeartRate(long date, long time, int HRMeasurement, int userid){
        ContentValues heartRateValues = new ContentValues();
        heartRateValues.put(KEY_HRDATE, date);
        heartRateValues.put(KEY_HRTIME, time);
        heartRateValues.put(KEY_HRMEASUREMENT, HRMeasurement);
        heartRateValues.put(KEY_USERID, userid);
        return db.insert(TABLE_HEARTRATE, null, heartRateValues);
    }

    /**
     * A method to insert weight information in the database
     * @param date The date the measurement was taken
     * @param time Thetime the measurement was taken
     * @param weight The weight measurement itseld
     * @param userid The userID of the current user
     * @return A long value with the row id of the newly inserted row, -1 if it could not be inserted
     */
    public long insertWeight(long date, int time, float weight, int userid){
        ContentValues weightValues = new ContentValues();
        weightValues.put(KEY_WEIGHTDATE, date);
        weightValues.put(KEY_WEIGHTTIME, time);
        weightValues.put(KEY_WEIGHT, weight);
        weightValues.put(KEY_USERID, userid);
        return db.insert(TABLE_WEIGHT, null, weightValues);
    }

    /**
     * A method to insert weight information in the database
     * @param date The date of the measurement
     * @param time The time of the measurement
     * @param exerciseDone The exercise undertaken
     * @param caloriesBurned The amount of calories burned
     * @param userid The userID of the current user
     * @return A long value with the row id of the newly inserted row, -1 if it could not be inserted
     */
    public long insertExercise(long date, int time, String exerciseDone, int caloriesBurned, int userid){
        ContentValues exerciseValues = new ContentValues();
        exerciseValues.put(KEY_EXERDATE, date);
        exerciseValues.put(KEY_EXERTIME, time);
        exerciseValues.put(KEY_EXDONE, exerciseDone);
        exerciseValues.put(KEY_CALBURNED, caloriesBurned);
        exerciseValues.put(KEY_USERID, userid);
        return db.insert(TABLE_EXERCISE, null, exerciseValues);
    }

    /**
     * A method used to insert sleep information into the database
     * @param date The date of the sleep
     * @param timesleep The amount of time the user slept
     * @param timeawake The time the user awoke
     * @param userid The userID of the current user
     * @return A long value with the row id of the newly inserted row, -1 if it could not be inserted
     */
    public long insertSleep(long date, int timesleep, int timeawake, int userid){
        ContentValues sleepValues = new ContentValues();
        sleepValues.put(KEY_SLEEPDATE, date);
        sleepValues.put(KEY_TIMESLEEP, timesleep);
        sleepValues.put(KEY_TIMEAWAKE, timeawake);
        sleepValues.put(KEY_USERID, userid);
        return db.insert(TABLE_SLEEP, null, sleepValues);
    }

    /**
     * A method used to insert blood pressure information into the database
     * @param lowpressure The diastolic measurement
     * @param highpressure The systolic measurement
     * @param pressuredate The date of the measurement
     * @param userid The userID of the current user
     * @return A long value with the row id of the newly inserted row, -1 if it could not be inserted
     */
    public long insertBloodpressure(int lowpressure, int highpressure, long pressuredate, int userid){
        ContentValues bloodpressureValues = new ContentValues();
        bloodpressureValues.put(KEY_LOWPRESSURE, lowpressure);
        bloodpressureValues.put(KEY_HIGHPRESSURE, highpressure);
        bloodpressureValues.put(KEY_PRESSUREDATE, pressuredate);
        bloodpressureValues.put(KEY_USERID, userid);
        return db.insert(TABLE_BLOODPRESSURE, null, bloodpressureValues);
    }

    /**
     * A method used to insert temperature information into the database
     * @param temperature The temperature measurement
     * @param tempdate The date of the measurement
     * @param userid The userID of the current user
     * @return A long value with the row id of the newly inserted row, -1 if it could not be inserted
     */
    public long insertTemperature(float temperature, long tempdate, int userid){
        ContentValues temperatureValues = new ContentValues();
        temperatureValues.put(KEY_TEMPERATURE, temperature);
        temperatureValues.put(KEY_TEMPDATE, tempdate);
        temperatureValues.put(KEY_USERID, userid);
        return db.insert(TABLE_TEMPERATURE, null, temperatureValues);
    }

    /**
     * Check if the user exists within the database
     * @param name The first name of the user
     * @param surname The surname of the user
     * @return Return true if they exist, false if they don't
     */
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

    /**
     * Check if the password the user has entered is correct
     * @param name The first name of the user
     * @param surname The last name of the user
     * @param pass The password the user entered
     * @return A cursor object containing the results of the query
     */
    public Cursor checkPassword(String name, String surname, String pass){
        Cursor cur = db.query(TABLE_PERSONALDETAILS, new String[] {KEY_USERID},KEY_FIRSTNAME + " like '" + name + "' AND " +KEY_LASTNAME+ " like '" +surname+"' AND " +KEY_PASSWORD+" like '"+pass+"'", null, null, null, null);
        return cur;
    }

    /**
     * Returns the data from the personal details table of the currentuser
     * @param name The first name of the user
     * @param surname The last name of the user
     * @param pass The password the user entered
     * @return A cursor object containing the results of the query
     */
    public Cursor getPersonalDetails(String name, String surname, String pass){
        Cursor cur = db.query(TABLE_PERSONALDETAILS, new String[] {SELECT_ALL},KEY_FIRSTNAME + " like '" + name + "' AND " +KEY_LASTNAME+ " like '" +surname+"' AND " +KEY_PASSWORD+" like '"+pass+"'", null, null, null, null);
        return cur;
    }

    /**
     * Retrieves heart rate information of the current user
     * @param userId The userID of the logged in user
     * @return A cursor object containing the results of the query
     */
    public Cursor getHeartRate(int userId){
        Cursor cur = db.query(TABLE_HEARTRATE, new String[] {SELECT_ALL},KEY_USERID + " = " + userId, null, null, null, KEY_HRDATE + " DESC");
        return cur;
    }

    /**
     * Retrieves blood pressure information of the current user
     * @param userId The userID of the logged in user
     * @return A cursor object containing the results of the query
     */
    public Cursor getBloodPressure(int userId){
        Cursor cur = db.query(TABLE_BLOODPRESSURE, new String[] {SELECT_ALL},KEY_USERID + " = " + userId, null, null, null, KEY_PRESSUREDATE + " DESC");
        return cur;
    }

    /**
     * Retrieves temperature information of the current user
     * @param userId The userID of the logged in user
     * @return A cursor object containing the results of the query
     */
    public Cursor getTemperature(int userId){
        Cursor cur = db.query(TABLE_TEMPERATURE, new String[] {SELECT_ALL},KEY_USERID + " = " + userId, null, null, null, KEY_TEMPDATE + " DESC");
        return cur;
    }

    /**
     * Deletes the user from the database along with any other data about them in other tables
     * @param cur The userID of the logged in user
     * @return Returns true if the user could be deleted
     */
    public boolean deleteUser(Cursor cur){

        db.delete(TABLE_PERSONALDETAILS, "userid = " + cur.getInt(cur.getColumnIndex(MMCDatabase.KEY_USERID)), null);
        return true;
    }
}

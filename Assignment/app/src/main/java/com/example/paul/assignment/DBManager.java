package com.example.paul.assignment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

public class DBManager {

    public static final String KEY_ROWID = "_id";
    public static final String CLASSNAME = "className";
    public static final String CLASSLOCATION = "classLocation";
    public static final String CLASSTYPE = "classType";
    public static final String CLASSDAYOFWEEK = "classDayOfWeek";
    public static final String CLASSSTARTTIME = "classStartTime";
    public static final String CLASSENDTIME = "classEndTime";

    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE =
            "create table Class(_id integer primary key autoincrement, " +
                    "className text not null,"+
                    "classLocation text not null,"+
                    "classType text not null,"+
                    "classDayOfWeek text not null,"+
                    "classStartTime time not null,"+
                    "classEndTime time not null)";

    private static String DATABASE_TABLE="Class";
    private final Context context;
    private MyDatabaseHelper DBHelper;
    private SQLiteDatabase db;
    private static String DBName ="TimeTableDB";

    public DBManager(Context ctx) {
        this.context = ctx;
        DBHelper = new MyDatabaseHelper(context);
    }

    public static class MyDatabaseHelper extends SQLiteOpenHelper {

        MyDatabaseHelper(Context context) {
            super(context, DBName, null, DATABASE_VERSION);
        }


        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //db.execSQL(DATABASE_UPDATE);
        }
    }

    public DBManager open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long insertSomething(String name, String location, String classDayOfWeek ,String classType, int classStartTime, int classEndTime)
    {
        ContentValues initialValues = new ContentValues();

        initialValues.put(CLASSNAME, name);
        initialValues.put(CLASSLOCATION, location);
        initialValues.put(CLASSDAYOFWEEK, classDayOfWeek);
        initialValues.put(CLASSTYPE, classType);
        initialValues.put(CLASSSTARTTIME, classStartTime);
        initialValues.put(CLASSENDTIME, classEndTime);

        return db.insert(DATABASE_TABLE, null, initialValues); //inserts values passed into database
    }

    public Cursor getAll()
    {
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        Cursor myCursor= db.rawQuery("SELECT * FROM "+ DATABASE_TABLE +";", null); //gets everyting from database
        return myCursor;
    }

    public boolean removeById(int id)
    {
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        try {
            db.execSQL("DELETE FROM "+ DATABASE_TABLE +" WHERE "+ KEY_ROWID + " = " + id + ";"); //remove specific thing from database
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateEverything(int _id, String name, String location, String classDayOfWeek ,String classType, int classStartTime, int classEndTime)
    {
        SQLiteDatabase db = DBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ROWID, _id);
        values.put(CLASSNAME, name);
        values.put(CLASSLOCATION, location);
        values.put(CLASSDAYOFWEEK, classDayOfWeek);
        values.put(CLASSTYPE, classType);
        values.put(CLASSSTARTTIME, classStartTime);
        values.put(CLASSENDTIME, classEndTime);

        try {
            db.update(DATABASE_TABLE, values, KEY_ROWID + " = '" + _id + "'", null); //updates everything in database with values passed
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Cursor getSomething(int id)
    {
        /*Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{
                KEY_ROWID,
                CLASSNAME,
                CLASSLOCATION,
                CLASSDAYOFWEEK,
                CLASSTYPE,
                CLASSSTARTTIME,
                CLASSENDTIME
        }, KEY_ROWID + "=" + Id, null, null, null, null, null);*/
        SQLiteDatabase db = DBHelper.getWritableDatabase();
        Cursor mCursor =db.rawQuery("SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_ROWID + " = " + id + ";", null); //get specific row
        return mCursor;
    }

    public Cursor selectSomethingByDay(String dayOfWeek) throws SQLException
    {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{
                KEY_ROWID,
                CLASSNAME,
                CLASSLOCATION,
                CLASSDAYOFWEEK,
                CLASSTYPE,
                CLASSSTARTTIME,
                CLASSENDTIME
        }, CLASSDAYOFWEEK+ "='"+ dayOfWeek +"'", null, null, null, CLASSSTARTTIME+" ASC", null); //get a row where dayofweek = passed value where startime is in ascending order

        return mCursor;
    }

}



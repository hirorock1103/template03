package com.design_phantom.mokuhyou.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

    private final static int DBVERSION = 1;
    private final static String DBNAME = "Mokuhyou.db";

    protected final static String TABLE_NAME = "Member";
    protected final static String MEMBER_COLUMN_ID = "id";
    protected final static String MEMBER_COLUMN_NAME = "name";
    protected final static String MEMBER_COLUMN_AGE = "age";
    protected final static String MEMBER_COLUMN_PROFILE_IMAGE = "profile_image";
    protected final static String MEMBER_COLUMN_CREATEDATE = "createdate";

    protected final static String TABLE_GOAL = "Goal";
    protected final static String GOAL_COLUMN_ID = "goal_id";
    protected final static String GOAL_COLUMN_TITLE = "goal_title";
    protected final static String GOAL_COLUMN_EXPIRED_DATE = "goal_expired_date";
    protected final static String GOAL_COLUMN_CREATEDATE = "goal_createdate";
    protected final static String GOAL_COLUMN_UPDATEDATE = "goal_updatedate";


    protected final static String TABLE_MEASURE = "GoalMeasure";
    protected final static String MEASURE_COLUMN_ID = "measure_id";
    protected final static String MEASURE_COLUMN_TITLE = "measure_title";
    protected final static String MEASURE_COLUMN_PARENT_ID = "goal_id";
    protected final static String MEASURE_COLUMN_TYPE = "measure_type";
    protected final static String MEASURE_COLUMN_INT_VALUE = "measure_int_value";
    protected final static String MEASURE_COLUMN_IMAGE_VALUE = "measure_image_value";
    protected final static String MEASURE_COLUMN_MEASURE_DATE = "measure_date";
    protected final static String MEASURE_COLUMN_CREATEDATE = "measure_createdate";
    protected final static String MEASURE_COLUMN_UPDATEDATE = "measure_updatedate";

    protected final static String TABLE_MEASURE_HISTORY = "GoalMeasureHistory";
    protected final static String HISTORY_COLUMN_ID = "history_id";
    protected final static String HISTORY_COLUMN_PARENT_MEASURE_ID = "parent_measure_id";
    protected final static String HISTORY_COLUMN_INT_VALUE = "measure_int_value";
    protected final static String HISTORY_COLUMN_IMAGE_VALUE = "measure_image_value";
    protected final static String HISTORY_COLUMN_MEASURE_DATE = "measure_date";
    protected final static String HISTORY_COLUMN_CREATEDATE = "history_createdate";
    protected final static String HISTORY_COLUMN_UPDATEDATE = "history_updatedate";


    public MyDbHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                MEMBER_COLUMN_ID + " integer primary key autoincrement ," +
                MEMBER_COLUMN_NAME + " text ," +
                MEMBER_COLUMN_AGE + " intger," +
                MEMBER_COLUMN_PROFILE_IMAGE + " blob," +
                MEMBER_COLUMN_CREATEDATE + " text" +
                ")" ;

        sqLiteDatabase.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS " + TABLE_GOAL + " (" +
                GOAL_COLUMN_ID + " integer primary key autoincrement ," +
                GOAL_COLUMN_TITLE + " text ," +
                GOAL_COLUMN_EXPIRED_DATE + " text," +
                GOAL_COLUMN_CREATEDATE + " text," +
                GOAL_COLUMN_UPDATEDATE + " text" +
                ")" ;

        sqLiteDatabase.execSQL(query);



        query = "CREATE TABLE IF NOT EXISTS " + TABLE_MEASURE + " (" +
                MEASURE_COLUMN_ID + " integer primary key autoincrement ," +
                MEASURE_COLUMN_TITLE + " text ," +
                MEASURE_COLUMN_PARENT_ID + " integer," +
                MEASURE_COLUMN_TYPE + " text," +
                MEASURE_COLUMN_INT_VALUE + " integer," +
                MEASURE_COLUMN_IMAGE_VALUE + " blob," +
                MEASURE_COLUMN_MEASURE_DATE + " text," +
                MEASURE_COLUMN_CREATEDATE + " text," +
                MEASURE_COLUMN_UPDATEDATE + " text" +
                ")" ;

        sqLiteDatabase.execSQL(query);


        query = "CREATE TABLE IF NOT EXISTS " + TABLE_MEASURE_HISTORY + " (" +
                HISTORY_COLUMN_ID + " integer primary key autoincrement ," +
                HISTORY_COLUMN_PARENT_MEASURE_ID + " integer ," +
                HISTORY_COLUMN_INT_VALUE + " integer," +
                HISTORY_COLUMN_IMAGE_VALUE + " blob," +
                HISTORY_COLUMN_MEASURE_DATE + " text," +
                HISTORY_COLUMN_CREATEDATE + " text," +
                HISTORY_COLUMN_UPDATEDATE + " text" +
                ")" ;

        sqLiteDatabase.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);

        query = "DROP TABLE IF EXISTS " + TABLE_GOAL;
        sqLiteDatabase.execSQL(query);

        query = "DROP TABLE IF EXISTS " + TABLE_MEASURE;
        sqLiteDatabase.execSQL(query);

        query = "DROP TABLE IF EXISTS " + TABLE_MEASURE_HISTORY;
        sqLiteDatabase.execSQL(query);

        onCreate(sqLiteDatabase);
    }
}

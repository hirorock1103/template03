package com.design_phantom.template03.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper extends SQLiteOpenHelper {

    private final static int DBVERSION = 1;
    private final static String DBNAME = "TipsApp.db";

    protected final static String TABLE_NAME = "Member";
    protected final static String MEMBER_COLUMN_ID = "id";
    protected final static String MEMBER_COLUMN_NAME = "name";
    protected final static String MEMBER_COLUMN_AGE = "age";
    protected final static String MEMBER_COLUMN_PROFILE_IMAGE = "profile_image";
    protected final static String MEMBER_COLUMN_CREATEDATE = "createdate";


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



    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        sqLiteDatabase.execSQL(query);


        onCreate(sqLiteDatabase);
    }
}

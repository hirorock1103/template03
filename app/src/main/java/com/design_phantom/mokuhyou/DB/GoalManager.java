package com.design_phantom.mokuhyou.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.Master.Goal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GoalManager extends MyDbHelper {
    public GoalManager(Context context) {
        super(context);
    }

    //select
    public Goal getListById(int id){

        Goal goal = new Goal();

        String query = "SELECT * FROM " + TABLE_GOAL + " WHERE " + GOAL_COLUMN_ID + " = " + id;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()){

            goal.setGoalId(c.getInt(c.getColumnIndex(GOAL_COLUMN_ID)));
            goal.setGoalTitle(c.getString(c.getColumnIndex(GOAL_COLUMN_TITLE)));
            goal.setGoalExpiredDate(c.getString(c.getColumnIndex(GOAL_COLUMN_EXPIRED_DATE)));
            goal.setCreatedate(c.getString(c.getColumnIndex(GOAL_COLUMN_CREATEDATE)));
            goal.setUpdatedate(c.getString(c.getColumnIndex(GOAL_COLUMN_UPDATEDATE)));


            c.moveToNext();
        }

        return goal;

    }

    public List<Goal> getList(){
        List<Goal> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_GOAL + " ORDER BY " + GOAL_COLUMN_ID + " DESC ";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()){

            Goal goal = new Goal();

            goal.setGoalId(c.getInt(c.getColumnIndex(GOAL_COLUMN_ID)));
            goal.setGoalTitle(c.getString(c.getColumnIndex(GOAL_COLUMN_TITLE)));
            goal.setGoalExpiredDate(c.getString(c.getColumnIndex(GOAL_COLUMN_EXPIRED_DATE)));
            goal.setCreatedate(c.getString(c.getColumnIndex(GOAL_COLUMN_CREATEDATE)));
            goal.setUpdatedate(c.getString(c.getColumnIndex(GOAL_COLUMN_UPDATEDATE)));

            list.add(goal);

            c.moveToNext();
        }

        return list;
    }


    //add
    public long addGoal(Goal goal){

        long resultId = 0;

        ContentValues values = new ContentValues();
        values.put(GOAL_COLUMN_TITLE,goal.getGoalTitle());
        values.put(GOAL_COLUMN_EXPIRED_DATE,goal.getGoalExpiredDate());
        values.put(GOAL_COLUMN_UPDATEDATE, Common.formatDate(new Date(),Common.DB_DATE_FORMAT));
        values.put(GOAL_COLUMN_CREATEDATE,Common.formatDate(new Date(),Common.DB_DATE_FORMAT));

        SQLiteDatabase db = getWritableDatabase();

        resultId = db.insert(TABLE_GOAL, null, values);


        return resultId;

    }


    //update
    public long updateGoal(Goal goal){

        long resultId = 0;

        ContentValues values = new ContentValues();
        values.put(GOAL_COLUMN_TITLE,goal.getGoalTitle());
        values.put(GOAL_COLUMN_EXPIRED_DATE,goal.getGoalExpiredDate());
        values.put(GOAL_COLUMN_UPDATEDATE, Common.formatDate(new Date(),Common.DB_DATE_FORMAT));

        SQLiteDatabase db = getWritableDatabase();

        String where = GOAL_COLUMN_ID + " = ?";
        String[] args = {String.valueOf(goal.getGoalId())};

        resultId = db.update(TABLE_GOAL, values, where, args);

        return resultId;

    }

    //delete

    public void deleteGoal(int id){
        String query = "DELETE FROM " + TABLE_GOAL + " WHERE " + GOAL_COLUMN_ID + " = " + id;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }


}

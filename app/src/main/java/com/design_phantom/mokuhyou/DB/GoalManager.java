package com.design_phantom.mokuhyou.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.design_phantom.mokuhyou.Common.Common;
import com.design_phantom.mokuhyou.Master.Goal;
import com.design_phantom.mokuhyou.Master.GoalMeasure;
import com.design_phantom.mokuhyou.Master.MeasureHistory;

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
            goal.setGoalMeasureDate(c.getString(c.getColumnIndex(GOAL_COLUMN_MEASURE_DATE)));
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
            goal.setGoalMeasureDate(c.getString(c.getColumnIndex(GOAL_COLUMN_MEASURE_DATE)));
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
        values.put(GOAL_COLUMN_MEASURE_DATE,goal.getGoalMeasureDate());
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
        values.put(GOAL_COLUMN_MEASURE_DATE,goal.getGoalMeasureDate());
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


    //addMeasure
    public long addMeasure(GoalMeasure measure){

        long resultId = 0;

        ContentValues values = new ContentValues();

        values.put(MEASURE_COLUMN_TITLE, measure.getMeasureTitle());
        values.put(MEASURE_COLUMN_PARENT_ID, measure.getParentGoalId());
        values.put(MEASURE_COLUMN_MEASURE_DATE, measure.getMeasureDate());
        values.put(MEASURE_COLUMN_TYPE, measure.getMeasureType());
        values.put(MEASURE_COLUMN_INT_VALUE, measure.getMeasureIntValue());
        values.put(MEASURE_COLUMN_IMAGE_VALUE, measure.getMeasureImageValue());
        values.put(MEASURE_COLUMN_UPDATEDATE, Common.formatDate(new Date(), Common.DB_DATE_FORMAT));
        values.put(MEASURE_COLUMN_CREATEDATE, Common.formatDate(new Date(), Common.DB_DATE_FORMAT));

        SQLiteDatabase db = getWritableDatabase();

        resultId = db.insert(TABLE_MEASURE, null, values);


        return resultId;

    }

    //list
    public List<GoalMeasure> getMeasureByGoalId(int goalId){

        List<GoalMeasure> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_MEASURE
                + " WHERE " + MEASURE_COLUMN_PARENT_ID + " = " + goalId
                + " ORDER BY " + MEASURE_COLUMN_ID + " DESC ";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()){

            GoalMeasure measure = new GoalMeasure();

            measure.setMeasureId(c.getInt(c.getColumnIndex(MEASURE_COLUMN_ID)));
            measure.setMeasureTitle(c.getString(c.getColumnIndex(MEASURE_COLUMN_TITLE)));
            measure.setParentGoalId(c.getInt(c.getColumnIndex(MEASURE_COLUMN_PARENT_ID)));
            measure.setMeasureType(c.getString(c.getColumnIndex(MEASURE_COLUMN_TYPE)));
            measure.setMeasureIntValue(c.getInt(c.getColumnIndex(MEASURE_COLUMN_INT_VALUE)));
            measure.setMeasureImageValue(c.getBlob(c.getColumnIndex(MEASURE_COLUMN_IMAGE_VALUE)));
            measure.setMeasureDate(c.getString(c.getColumnIndex(MEASURE_COLUMN_MEASURE_DATE)));
            measure.setUpdatedate(c.getString(c.getColumnIndex(MEASURE_COLUMN_UPDATEDATE)));
            measure.setCreatedate(c.getString(c.getColumnIndex(MEASURE_COLUMN_CREATEDATE)));

            list.add(measure);

            c.moveToNext();
        }

        return list;

    }

    //get Goalmeasure
    public GoalMeasure getMeasureById(int measureId){

        GoalMeasure measure = new GoalMeasure();

        String query = "SELECT * FROM " + TABLE_MEASURE
                + " WHERE " + MEASURE_COLUMN_ID + " = " + measureId;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()){

            measure.setMeasureId(c.getInt(c.getColumnIndex(MEASURE_COLUMN_ID)));
            measure.setMeasureTitle(c.getString(c.getColumnIndex(MEASURE_COLUMN_TITLE)));
            measure.setParentGoalId(c.getInt(c.getColumnIndex(MEASURE_COLUMN_PARENT_ID)));
            measure.setMeasureType(c.getString(c.getColumnIndex(MEASURE_COLUMN_TYPE)));
            measure.setMeasureIntValue(c.getInt(c.getColumnIndex(MEASURE_COLUMN_INT_VALUE)));
            measure.setMeasureImageValue(c.getBlob(c.getColumnIndex(MEASURE_COLUMN_IMAGE_VALUE)));
            measure.setMeasureDate(c.getString(c.getColumnIndex(MEASURE_COLUMN_MEASURE_DATE)));
            measure.setUpdatedate(c.getString(c.getColumnIndex(MEASURE_COLUMN_UPDATEDATE)));
            measure.setCreatedate(c.getString(c.getColumnIndex(MEASURE_COLUMN_CREATEDATE)));

            c.moveToNext();
        }

        return measure;

    }


    /**
     * measure History
     */
    public long addMeasureHistory(MeasureHistory history){

        long resultId = 0;

        ContentValues values = new ContentValues();

        values.put(HISTORY_COLUMN_PARENT_MEASURE_ID, history.getParentMeasureId());
        values.put(HISTORY_COLUMN_MEASURE_DATE, history.getMeasureDate());
        values.put(HISTORY_COLUMN_INT_VALUE, history.getMeasureIntValue());
        values.put(HISTORY_COLUMN_IMAGE_VALUE, history.getMeasureImageValue());

        values.put(HISTORY_COLUMN_CREATEDATE, Common.formatDate(new Date(), Common.DB_DATE_FORMAT));
        values.put(HISTORY_COLUMN_UPDATEDATE, Common.formatDate(new Date(), Common.DB_DATE_FORMAT));

        SQLiteDatabase db = getWritableDatabase();

        resultId = db.insert(TABLE_MEASURE_HISTORY, null, values);

        return resultId;

    }


    //list -- 1 get by measureId, string date
    public List<MeasureHistory> getMeasureHistoryByMeasureId(int measureId, String targetDate){

        List<MeasureHistory> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_MEASURE_HISTORY
                + " WHERE " + HISTORY_COLUMN_PARENT_MEASURE_ID + " = " + measureId ;

        if(targetDate == null || targetDate.isEmpty()){
            query += " ORDER BY " + HISTORY_COLUMN_ID + " DESC ";
        }else{
            query += " AND " + HISTORY_COLUMN_MEASURE_DATE + " = " + "'"+ targetDate +"'";
        }

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()){

            MeasureHistory history = new MeasureHistory();

            history.setHistoryId(c.getInt(c.getColumnIndex(HISTORY_COLUMN_ID)));
            history.setParentMeasureId(c.getInt(c.getColumnIndex(HISTORY_COLUMN_PARENT_MEASURE_ID)));
            history.setMeasureDate(c.getString(c.getColumnIndex(HISTORY_COLUMN_MEASURE_DATE)));
            history.setMeasureIntValue(c.getInt(c.getColumnIndex(HISTORY_COLUMN_INT_VALUE)));
            history.setMeasureImageValue(c.getBlob(c.getColumnIndex(HISTORY_COLUMN_IMAGE_VALUE)));

            history.setUpdatedate(c.getString(c.getColumnIndex(HISTORY_COLUMN_UPDATEDATE)));
            history.setCreatedate(c.getString(c.getColumnIndex(HISTORY_COLUMN_CREATEDATE)));

            list.add(history);

            c.moveToNext();
        }

        return list;

    }

}

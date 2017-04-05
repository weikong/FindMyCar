package com.king.smart.car.mysmartcarapp.db.helper;

import android.content.ContentValues;
import android.database.Cursor;

import com.king.smart.car.mysmartcarapp.bean.RunBean;
import com.king.smart.car.mysmartcarapp.db.table.RunTable;
import com.king.smart.car.mysmartcarapp.manager.Logger;

import java.util.ArrayList;

/**
 * Created by jian.cao on 2016/3/9.
 */
public class RunDBHelper {
    private static final String TAG = "RunDBHelper";

    public static boolean insert(RunBean bean) {
        if (bean == null)
            return false;
        ContentValues values = new ContentValues();
        values.put(RunTable.Columns.RUN_LATITUDE, bean.getmLat());
        values.put(RunTable.Columns.RUN_LONGITUDE, bean.getmLon());
        values.put(RunTable.Columns.RUN_ADDRESS, bean.getmAddress());
        values.put(RunTable.Columns.RUN_NEARBY, bean.getmNearBy());
        values.put(RunTable.Columns.RUN_CONTINUE_DAYS, bean.getmRunContinueDays());
        values.put(RunTable.Columns.RUN_DATE, bean.getmRunDate());
        values.put(RunTable.Columns.RUN_DISTANCE, bean.getmRunDistance());
        values.put(RunTable.Columns.RUN_HEAT, bean.getmRunHeat());
        values.put(RunTable.Columns.RUN_SPEED, bean.getmRunSpeed());
        values.put(RunTable.Columns.RUN_USE_TIME, bean.getmUseTime());
        values.put(RunTable.Columns.RUN_COORDINATE_LIST, bean.getmRunCoordinateList());
        long mInsert = 0;
        mInsert = DatabaseHelper.openDataBase().insert(RunTable.TABLE_NAME, null, values);
        Logger.e("mInsert = " + mInsert);
        DatabaseHelper.closeDataBase();
        return mInsert > 0;
    }

    public static boolean update(RunBean bean) {
        if (bean == null && bean.getmID() == 0)
            return false;
        ContentValues values = new ContentValues();
        values.put(RunTable.Columns.RUN_LATITUDE, bean.getmLat());
        values.put(RunTable.Columns.RUN_LONGITUDE, bean.getmLon());
        values.put(RunTable.Columns.RUN_ADDRESS, bean.getmAddress());
        values.put(RunTable.Columns.RUN_NEARBY, bean.getmNearBy());
        values.put(RunTable.Columns.RUN_CONTINUE_DAYS, bean.getmRunContinueDays());
        values.put(RunTable.Columns.RUN_DATE, bean.getmRunDate());
        values.put(RunTable.Columns.RUN_DISTANCE, bean.getmRunDistance());
        values.put(RunTable.Columns.RUN_HEAT, bean.getmRunHeat());
        values.put(RunTable.Columns.RUN_SPEED, bean.getmRunSpeed());
        values.put(RunTable.Columns.RUN_USE_TIME, bean.getmUseTime());
        values.put(RunTable.Columns.RUN_COORDINATE_LIST, bean.getmRunCoordinateList());
        int mUpdate = 0;
        mUpdate = DatabaseHelper.openDataBase().update(RunTable.TABLE_NAME, values, "run_id = ?", new String[]{String.valueOf(bean.getmID())});
        Logger.e("mUpdate = " + mUpdate);
        DatabaseHelper.closeDataBase();
        return mUpdate > 0;
    }

    public static ArrayList<RunBean> query() {
        String select = "";
        Cursor cursor = null;
        String projection[] = {RunTable.Columns.RUN_ID,//
                RunTable.Columns.RUN_ADDRESS,//
                RunTable.Columns.RUN_NEARBY,//
                RunTable.Columns.RUN_LATITUDE,//
                RunTable.Columns.RUN_LONGITUDE,//
                RunTable.Columns.RUN_CONTINUE_DAYS,//
                RunTable.Columns.RUN_DATE,//
                RunTable.Columns.RUN_DISTANCE,//
                RunTable.Columns.RUN_HEAT,//
                RunTable.Columns.RUN_SPEED,//
                RunTable.Columns.RUN_USE_TIME,//
                RunTable.Columns.RUN_COORDINATE_LIST
        };

        /**
         * 排序
         * */
        StringBuffer order = new StringBuffer();
        order.append(RunTable.Columns.RUN_ID).append(" desc");//按时间排序

        cursor = DatabaseHelper.openDataBase().query(RunTable.TABLE_NAME, projection, null, null, null, null, order.toString(), null);
        ArrayList<RunBean> runList = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0)
            return runList;
        if (cursor.moveToLast()) {
            RunBean bean = new RunBean();
            bean.setmID(cursor.getInt(0));
            bean.setmAddress(cursor.getString(1));
            bean.setmNearBy(cursor.getString(2));
            bean.setmLat(cursor.getDouble(3));
            bean.setmLon(cursor.getDouble(4));
            bean.setmRunContinueDays(cursor.getString(5));
            bean.setmRunDate(cursor.getString(6));
            bean.setmRunDistance(cursor.getString(7));
            bean.setmRunHeat(cursor.getString(8));
            bean.setmRunSpeed(cursor.getString(9));
            bean.setmUseTime(cursor.getString(10));
            bean.setmRunCoordinateList(cursor.getString(11));
            runList.add(bean);
        }

        while (cursor.moveToPrevious()) {
            RunBean bean = new RunBean();
            bean.setmID(cursor.getInt(0));
            bean.setmAddress(cursor.getString(1));
            bean.setmNearBy(cursor.getString(2));
            bean.setmLat(cursor.getDouble(3));
            bean.setmLon(cursor.getDouble(4));
            bean.setmRunContinueDays(cursor.getString(5));
            bean.setmRunDate(cursor.getString(6));
            bean.setmRunDistance(cursor.getString(7));
            bean.setmRunHeat(cursor.getString(8));
            bean.setmRunSpeed(cursor.getString(9));
            bean.setmUseTime(cursor.getString(10));
            bean.setmRunCoordinateList(cursor.getString(11));
            runList.add(bean);
        }
        Logger.e("lockList = " + runList.toString());
        //回收
        cursor.close();
        DatabaseHelper.closeDataBase();
        return runList;
    }

    public static boolean delete(RunBean bean) {
        if (bean == null && bean.getmID() == 0)
            return false;
        int mDelete = 0;
        mDelete = DatabaseHelper.openDataBase().delete(RunTable.TABLE_NAME, "run_id = ?", new String[]{String.valueOf(bean.getmID())});
        Logger.e("mDelete = " + mDelete + "  _id = " + bean.getmID());
        DatabaseHelper.closeDataBase();
        return mDelete > 0;
    }

}

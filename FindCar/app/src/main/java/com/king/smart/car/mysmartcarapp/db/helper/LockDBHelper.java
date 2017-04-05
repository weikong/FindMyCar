package com.king.smart.car.mysmartcarapp.db.helper;

import android.content.ContentValues;
import android.database.Cursor;

import com.king.smart.car.mysmartcarapp.bean.LockBean;
import com.king.smart.car.mysmartcarapp.db.table.LockTable;
import com.king.smart.car.mysmartcarapp.manager.Logger;

import java.util.ArrayList;

/**
 * Created by jian.cao on 2016/3/9.
 */
public class LockDBHelper {
    private static final String TAG = "LockDBHelper";

    public static boolean insert(LockBean bean) {
        if (bean == null)
            return false;
        ContentValues values = new ContentValues();
        values.put(LockTable.Columns.LOCK_ADDRESS, bean.getmAddress());
        values.put(LockTable.Columns.LOCK_NEARBY, bean.getmNearBy());
        values.put(LockTable.Columns.LOCK_TIME, bean.getmTime());
        values.put(LockTable.Columns.LOCK_LATITUDE, bean.getmLat());
        values.put(LockTable.Columns.LOCK_LONGITUDE, bean.getmLon());
        long mInsert = 0;
        mInsert = DatabaseHelper.openDataBase().insert(LockTable.TABLE_NAME, null, values);
        Logger.e("mInsert = " + mInsert);
        DatabaseHelper.closeDataBase();
        return mInsert > 0;
    }

    public static boolean update(LockBean bean) {
        if (bean == null && bean.getmID() == 0)
            return false;
        ContentValues values = new ContentValues();
        values.put(LockTable.Columns.LOCK_ADDRESS, bean.getmAddress());
        values.put(LockTable.Columns.LOCK_NEARBY, bean.getmNearBy());
        values.put(LockTable.Columns.LOCK_TIME, bean.getmTime());
        values.put(LockTable.Columns.LOCK_LATITUDE, bean.getmLat());
        values.put(LockTable.Columns.LOCK_LONGITUDE, bean.getmLon());
        int mUpdate = 0;
        mUpdate = DatabaseHelper.openDataBase().update(LockTable.TABLE_NAME, values, "lock_id = ?", new String[]{String.valueOf(bean.getmID())});
        Logger.e("mUpdate = " + mUpdate);
        DatabaseHelper.closeDataBase();
        return mUpdate > 0;
    }

    public static ArrayList<LockBean> query() {
        String select = "";
        Cursor cursor = null;
        String projection[] = {LockTable.Columns.LOCK_ID,//
                LockTable.Columns.LOCK_ADDRESS,//
                LockTable.Columns.LOCK_TIME,//
                LockTable.Columns.LOCK_LATITUDE,//
                LockTable.Columns.LOCK_LONGITUDE,//
                LockTable.Columns.LOCK_NEARBY

        };
        cursor = DatabaseHelper.openDataBase().query(LockTable.TABLE_NAME, projection, null, null, null, null, null, null);
        ArrayList<LockBean> lockList = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0)
            return lockList;
        if (cursor.moveToLast()) {
            LockBean bean = new LockBean();
            bean.setmID(cursor.getInt(0));
            bean.setmAddress(cursor.getString(1));
            bean.setmTime(cursor.getString(2));
            bean.setmLat(cursor.getDouble(3));
            bean.setmLon(cursor.getDouble(4));
            bean.setmNearBy(cursor.getString(5));
            lockList.add(bean);
        }

        while (cursor.moveToPrevious()) {
            LockBean bean = new LockBean();
            bean.setmID(cursor.getInt(0));
            bean.setmAddress(cursor.getString(1));
            bean.setmTime(cursor.getString(2));
            bean.setmLat(cursor.getDouble(3));
            bean.setmLon(cursor.getDouble(4));
            bean.setmNearBy(cursor.getString(5));
            lockList.add(bean);
        }
        Logger.e("lockList = " + lockList.toString());
        //回收
        cursor.close();
        DatabaseHelper.closeDataBase();
        return lockList;
    }

    public static boolean delete(LockBean bean) {
        if (bean == null && bean.getmID() == 0)
            return false;
        int mDelete = 0;
        mDelete = DatabaseHelper.openDataBase().delete(LockTable.TABLE_NAME, "lock_id = ?", new String[]{String.valueOf(bean.getmID())});
        Logger.e("mDelete = " + mDelete + "  _id = " + bean.getmID());
        DatabaseHelper.closeDataBase();
        return mDelete > 0;
    }

}

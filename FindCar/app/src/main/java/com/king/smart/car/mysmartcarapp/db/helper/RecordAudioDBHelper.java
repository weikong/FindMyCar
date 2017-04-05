package com.king.smart.car.mysmartcarapp.db.helper;

import android.content.ContentValues;
import android.database.Cursor;

import com.king.smart.car.mysmartcarapp.bean.RecordAudioBean;
import com.king.smart.car.mysmartcarapp.db.table.RecordAudioTable;
import com.king.smart.car.mysmartcarapp.manager.Logger;

import java.util.ArrayList;

/**
 * Created by jian.cao on 2016/3/9.
 */
public class RecordAudioDBHelper {
    private static final String TAG = "LockDBHelper";

    public static boolean insert(RecordAudioBean bean) {
        if (bean == null)
            return false;
        ContentValues values = new ContentValues();
//        values.put(RecordAudioTable.Columns._ID, bean.get_id());
        values.put(RecordAudioTable.Columns._NAME, bean.getName());
        values.put(RecordAudioTable.Columns._TIME, bean.getTime());
        values.put(RecordAudioTable.Columns._DURATION, bean.getDuration());
        values.put(RecordAudioTable.Columns._PATH, bean.getPath());
        long mInsert = 0;
        mInsert = DatabaseHelper.openDataBase().insert(RecordAudioTable.TABLE_NAME, null, values);
        Logger.e("mInsert = " + mInsert);
        DatabaseHelper.closeDataBase();
        return mInsert > 0;
    }

    public static boolean update(RecordAudioBean bean) {
        if (bean == null && bean.get_id() == 0)
            return false;
        ContentValues values = new ContentValues();
        values.put(RecordAudioTable.Columns._NAME, bean.getName());
        values.put(RecordAudioTable.Columns._TIME, bean.getTime());
        values.put(RecordAudioTable.Columns._DURATION, bean.getDuration());
        values.put(RecordAudioTable.Columns._PATH, bean.getPath());
        int mUpdate = 0;
        mUpdate = DatabaseHelper.openDataBase().update(RecordAudioTable.TABLE_NAME, values, "record_audio_id = ?", new String[]{String.valueOf(bean.get_id())});
        Logger.e("mUpdate = " + mUpdate);
        DatabaseHelper.closeDataBase();
        return mUpdate > 0;
    }

    public static ArrayList<RecordAudioBean> query() {
        String select = "";
        Cursor cursor = null;
        String projection[] = {RecordAudioTable.Columns._ID,//
                RecordAudioTable.Columns._NAME,//
                RecordAudioTable.Columns._TIME,//
                RecordAudioTable.Columns._DURATION,//
                RecordAudioTable.Columns._PATH

        };
        cursor = DatabaseHelper.openDataBase().query(RecordAudioTable.TABLE_NAME, projection, null, null, null, null, null, null);
        ArrayList<RecordAudioBean> lockList = new ArrayList<>();
        if (cursor == null || cursor.getCount() == 0)
            return lockList;
        if (cursor.moveToLast()) {
            RecordAudioBean bean = new RecordAudioBean();
            bean.set_id(cursor.getInt(0));
            bean.setName(cursor.getString(1));
            bean.setTime(cursor.getString(2));
            bean.setDuration(cursor.getString(3));
            bean.setPath(cursor.getString(4));
            lockList.add(bean);
        }

        while (cursor.moveToPrevious()) {
            RecordAudioBean bean = new RecordAudioBean();
            bean.set_id(cursor.getInt(0));
            bean.setName(cursor.getString(1));
            bean.setTime(cursor.getString(2));
            bean.setDuration(cursor.getString(3));
            bean.setPath(cursor.getString(4));
            lockList.add(bean);
        }
        Logger.e("lockList = " + lockList.toString());
        //回收
        cursor.close();
        DatabaseHelper.closeDataBase();
        return lockList;
    }

    public static boolean delete(RecordAudioBean bean) {
        if (bean == null && bean.get_id() == 0)
            return false;
        int mDelete = 0;
        mDelete = DatabaseHelper.openDataBase().delete(RecordAudioTable.TABLE_NAME, "record_audio_id = ?", new String[]{String.valueOf(bean.get_id())});
        Logger.e("mDelete = " + mDelete);
        DatabaseHelper.closeDataBase();
        return mDelete > 0;
    }

}

package com.king.smart.car.mysmartcarapp.db.helper;

import android.content.ContentValues;
import android.database.Cursor;

import com.king.smart.car.mysmartcarapp.bean.Alarm;
import com.king.smart.car.mysmartcarapp.db.table.AlarmTable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/11/28.
 */
public class AlarmDBHelper {

    public static long create(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(AlarmTable.Columns.COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(AlarmTable.Columns.COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
        }

        cv.put(AlarmTable.Columns.COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_NAME, alarm.getAlarmName());

        return DatabaseHelper.openDataBase().insert(AlarmTable.TABLE_NAME, null, cv);
    }

    public static int update(Alarm alarm) {
        ContentValues cv = new ContentValues();
        cv.put(AlarmTable.Columns.COLUMN_ALARM_ACTIVE, alarm.getAlarmActive());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_TIME, alarm.getAlarmTimeString());

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = null;
            oos = new ObjectOutputStream(bos);
            oos.writeObject(alarm.getDays());
            byte[] buff = bos.toByteArray();

            cv.put(AlarmTable.Columns.COLUMN_ALARM_DAYS, buff);

        } catch (Exception e) {
        }

        cv.put(AlarmTable.Columns.COLUMN_ALARM_DIFFICULTY, alarm.getDifficulty().ordinal());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_TONE, alarm.getAlarmTonePath());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_VIBRATE, alarm.getVibrate());
        cv.put(AlarmTable.Columns.COLUMN_ALARM_NAME, alarm.getAlarmName());

        return DatabaseHelper.openDataBase().update(AlarmTable.TABLE_NAME, cv, "_id=" + alarm.getId(), null);
    }

    public static Cursor getCursor() {
        // TODO Auto-generated method stub
        String[] columns = new String[]{
                AlarmTable.Columns.COLUMN_ALARM_ID,
                AlarmTable.Columns.COLUMN_ALARM_ACTIVE,
                AlarmTable.Columns.COLUMN_ALARM_TIME,
                AlarmTable.Columns.COLUMN_ALARM_DAYS,
                AlarmTable.Columns.COLUMN_ALARM_DIFFICULTY,
                AlarmTable.Columns.COLUMN_ALARM_TONE,
                AlarmTable.Columns.COLUMN_ALARM_VIBRATE,
                AlarmTable.Columns.COLUMN_ALARM_NAME
        };
        return DatabaseHelper.openDataBase().query(AlarmTable.TABLE_NAME, columns, null, null, null, null,
                null);
    }

    public static Alarm getAlarm(int id) {
        // TODO Auto-generated method stub
        String[] columns = new String[]{
                AlarmTable.Columns.COLUMN_ALARM_ID,
                AlarmTable.Columns.COLUMN_ALARM_ACTIVE,
                AlarmTable.Columns.COLUMN_ALARM_TIME,
                AlarmTable.Columns.COLUMN_ALARM_DAYS,
                AlarmTable.Columns.COLUMN_ALARM_DIFFICULTY,
                AlarmTable.Columns.COLUMN_ALARM_TONE,
                AlarmTable.Columns.COLUMN_ALARM_VIBRATE,
                AlarmTable.Columns.COLUMN_ALARM_NAME
        };
        Cursor c = DatabaseHelper.openDataBase().query(AlarmTable.TABLE_NAME, columns, AlarmTable.Columns.COLUMN_ALARM_ID + "=" + id, null, null, null,
                null);
        Alarm alarm = null;

        if (c.moveToFirst()) {

            alarm = new Alarm();
            alarm.setId(c.getInt(1));
            alarm.setAlarmActive(c.getInt(2) == 1);
            alarm.setAlarmTime(c.getString(3));
            byte[] repeatDaysBytes = c.getBlob(4);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(repeatDaysBytes);
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                Alarm.Day[] repeatDays;
                Object object = objectInputStream.readObject();
                if (object instanceof Alarm.Day[]) {
                    repeatDays = (Alarm.Day[]) object;
                    alarm.setDays(repeatDays);
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            alarm.setDifficulty(Alarm.Difficulty.values()[c.getInt(5)]);
            alarm.setAlarmTonePath(c.getString(6));
            alarm.setVibrate(c.getInt(7) == 1);
            alarm.setAlarmName(c.getString(8));
        }
        c.close();
        return alarm;
    }

    public static List<Alarm> getAll() {
        List<Alarm> alarms = new ArrayList<Alarm>();
        Cursor cursor = getCursor();
        if (cursor.moveToFirst()) {

            do {
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setAlarmActive(cursor.getInt(1) == 1);
                alarm.setAlarmTime(cursor.getString(2));
                byte[] repeatDaysBytes = cursor.getBlob(3);

                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                        repeatDaysBytes);
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(
                            byteArrayInputStream);
                    Alarm.Day[] repeatDays;
                    Object object = objectInputStream.readObject();
                    if (object instanceof Alarm.Day[]) {
                        repeatDays = (Alarm.Day[]) object;
                        alarm.setDays(repeatDays);
                    }
                } catch (StreamCorruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                alarm.setDifficulty(Alarm.Difficulty.values()[cursor.getInt(4)]);
                alarm.setAlarmTonePath(cursor.getString(5));
                alarm.setVibrate(cursor.getInt(6) == 1);
                alarm.setAlarmName(cursor.getString(7));

                alarms.add(alarm);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return alarms;
    }

    public static int deleteEntry(Alarm alarm) {
        return deleteEntry(alarm.getId());
    }

    public static int deleteEntry(int id) {
        return DatabaseHelper.openDataBase().delete(AlarmTable.TABLE_NAME, AlarmTable.Columns.COLUMN_ALARM_ID + "=" + id, null);
    }

    public static int deleteAll() {
        return DatabaseHelper.openDataBase().delete(AlarmTable.TABLE_NAME, "1", null);
    }

}

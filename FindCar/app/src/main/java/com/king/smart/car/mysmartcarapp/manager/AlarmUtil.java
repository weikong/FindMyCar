package com.king.smart.car.mysmartcarapp.manager;

import android.database.Cursor;
import android.media.RingtoneManager;

import com.king.smart.car.mysmartcarapp.app.App;

/**
 * Created by xinzhendi-031 on 2016/11/28.
 */
public class AlarmUtil {

    public static String[] alarmTones;
    public static String[] alarmTonePaths;

    public static void queryAlarmTones(){
        RingtoneManager ringtoneMgr = new RingtoneManager(App.getInstance());
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        alarmTones = new String[alarmsCursor.getCount() + 1];
        alarmTones[0] = "Silent";
        alarmTonePaths = new String[alarmsCursor.getCount() + 1];
        alarmTonePaths[0] = "";
        if (alarmsCursor.moveToFirst()) {
            do {
                alarmTones[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(App.getInstance());
                alarmTonePaths[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
            } while (alarmsCursor.moveToNext());
        }
        alarmsCursor.close();
    }
}

/*
 * Copyright 2014-2024 setNone. All rights reserved. 
 */
package com.king.smart.car.mysmartcarapp.db.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.king.smart.car.mysmartcarapp.app.App;
import com.king.smart.car.mysmartcarapp.db.table.AlarmTable;
import com.king.smart.car.mysmartcarapp.db.table.LockTable;
import com.king.smart.car.mysmartcarapp.db.table.RecordAudioTable;
import com.king.smart.car.mysmartcarapp.db.table.RunTable;
import com.king.smart.car.mysmartcarapp.manager.Logger;

/**
 * DatabaseHelper.java - 数据库操作帮助类
 *
 * @author Kevin.Zhang
 *         <p/>
 *         2014-7-17 下午5:18:56
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 调试TAG
     */
    private static final String TAG = "DatabaseHelper";

    /**
     * 数据库名
     */
    private static final String DATABASE_NAME = "KingSmartCar";

    private static final long mUserId = 20160813L;

    /**
     * 数据库版本号(必须大于等于1)
     */
    private static final int DATABASE_VERSION = 5;

    public static DatabaseHelper databaseHelper = null;

    public static DatabaseHelper getInstace() {
        initHelper();
        return databaseHelper;
    }

    public static void initHelper() {
        if (databaseHelper == null)
            databaseHelper = new DatabaseHelper(App.getInstance());
    }

    public DatabaseHelper(Context context) {
        super(context, buildUserDB(mUserId), null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, long userId) {
        super(context, buildUserDB(userId), null, DATABASE_VERSION);
    }

    // 得到根据用户名而变化的db文件（默认ChatRecord_0.db）
    private static String buildUserDB(long userId) {
        Logger.i("--->DB DatabaseHelper init!");
        return new StringBuffer(DATABASE_NAME).append("_").append(userId).append(".db").toString();

    }

    public static SQLiteDatabase mSQLiteDataBase;

    public static SQLiteDatabase openDataBase() {
        if (mSQLiteDataBase == null) {
            mSQLiteDataBase = DatabaseHelper.getInstace().getWritableDatabase();
        }
        return mSQLiteDataBase;
    }

    public static void closeDataBase() {
        if (mSQLiteDataBase != null) {
            mSQLiteDataBase.close();
            mSQLiteDataBase = null;
        }
    }

    // 首次创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.i(TAG, "onCreate SQLiteDatabase = " + db);
        createLockInfoTable(db);
        createRecordAudioTable(db);
        createAlarmTable(db);
        createRunTable(db);
    }

    // 当前版本大于系统版本
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.i(TAG, "onUpgrade newVersion = " + newVersion + " oldVersion = " + oldVersion);
        for (int currentVersion = oldVersion + 1; currentVersion <= newVersion; currentVersion++) {
            switch (currentVersion) {
                case 2:
                    upgradeDB2Version2(db);
                    break;
                case 3:
                    createRecordAudioTable(db);
                    break;
                case 4:
                    createAlarmTable(db);
                    break;
                case 5:
                    createRunTable(db);
                    break;
            }
        }
    }

    /**
     * 创建LockTable
     */
    private void createLockInfoTable(SQLiteDatabase db) {
        Logger.i(TAG, "createLockInfoTable SQLiteDatabase = " + db);
        LockTable table = new LockTable();
        table.createTable(db);
    }

    /**
     * 创建LockTable
     */
    private void createRecordAudioTable(SQLiteDatabase db) {
        Logger.i(TAG, "createRecordAudioTable SQLiteDatabase = " + db);
        RecordAudioTable table = new RecordAudioTable();
        table.createTable(db);
    }

    /**
     * db version = 2
     * 在LockTable中增加NearBy
     */
    private void upgradeDB2Version2(SQLiteDatabase db) {
        // 单聊与群聊表增加header字段
        LockTable table = new LockTable();
        String updateSql = table.addColumnNearBySql(db);
        if (!TextUtils.isEmpty(updateSql))
            db.execSQL(updateSql);
    }

    /**
     * 创建AlarmTable
     */
    private void createAlarmTable(SQLiteDatabase db) {
        Logger.i(TAG, "createAlarmTable SQLiteDatabase = " + db);
        AlarmTable table = new AlarmTable();
        table.createTable(db);
    }

    /**
     * 创建RunTable
     */
    private void createRunTable(SQLiteDatabase db) {
        Logger.i(TAG, "RunTable SQLiteDatabase = " + db);
        RunTable table = new RunTable();
        table.createTable(db);
    }
}
package com.king.smart.car.mysmartcarapp.db.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by xinzhendi-031 on 2015/11/26.
 */
public class AlarmTable extends AbsBaseTable {
    public static final String TABLE_NAME = "table_alarm";

    /**
     * 表中所有的列名
     */

    public interface Columns extends BaseColumns {
        public static final String COLUMN_ALARM_ID = "_id";
        public static final String COLUMN_ALARM_ACTIVE = "alarm_active";
        public static final String COLUMN_ALARM_TIME = "alarm_time";
        public static final String COLUMN_ALARM_DAYS = "alarm_days";
        public static final String COLUMN_ALARM_DIFFICULTY = "alarm_difficulty";
        public static final String COLUMN_ALARM_TONE = "alarm_tone";
        public static final String COLUMN_ALARM_VIBRATE = "alarm_vibrate";
        public static final String COLUMN_ALARM_NAME = "alarm_name";
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    protected String getCreateTableSql() {
        StringBuffer sqlStatement = new StringBuffer();
        sqlStatement.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME);
        sqlStatement.append(" (");
        sqlStatement.append(Columns.COLUMN_ALARM_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sqlStatement.append(Columns.COLUMN_ALARM_ACTIVE).append(" INTEGER NOT NULL, ");
        sqlStatement.append(Columns.COLUMN_ALARM_TIME).append(" TEXT NOT NULL, ");
        sqlStatement.append(Columns.COLUMN_ALARM_DAYS).append(" BLOB NOT NULL, ");
        sqlStatement.append(Columns.COLUMN_ALARM_DIFFICULTY).append(" INTEGER NOT NULL, ");
        sqlStatement.append(Columns.COLUMN_ALARM_TONE).append(" TEXT NOT NULL, ");
        sqlStatement.append(Columns.COLUMN_ALARM_VIBRATE).append(" INTEGER NOT NULL, ");
        sqlStatement.append(Columns.COLUMN_ALARM_NAME).append(" TEXT NOT NULL");
        sqlStatement.append(");");
        return sqlStatement.toString();
    }

    /**
     * 方法2：检查表中某列是否存在
     *
     * @param db
     * @param tableName  表名
     * @param columnName 列名
     * @return
     */
    private boolean checkColumnExists(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }
}

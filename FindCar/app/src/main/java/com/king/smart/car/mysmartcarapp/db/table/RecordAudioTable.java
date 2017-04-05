package com.king.smart.car.mysmartcarapp.db.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by xinzhendi-031 on 2015/11/26.
 */
public class RecordAudioTable extends AbsBaseTable {
    public static final String TABLE_NAME = "table_record_audio";

    /**
     * 表中所有的列名
     */

    public interface Columns extends BaseColumns {
        String _ID = "record_audio_id";//
        String _NAME = "record_audio_name";
        String _TIME = "record_audio_time";
        String _DURATION = "record_audio_duration";
        String _PATH = "record_audio_path";
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
        sqlStatement.append(Columns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlStatement.append(Columns._NAME).append(" TEXT,");
        sqlStatement.append(Columns._TIME).append(" TEXT,");
        sqlStatement.append(Columns._DURATION).append(" TEXT,");
        sqlStatement.append(Columns._PATH).append(" TEXT");
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

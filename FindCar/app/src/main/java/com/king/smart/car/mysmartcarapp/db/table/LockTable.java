package com.king.smart.car.mysmartcarapp.db.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by xinzhendi-031 on 2015/11/26.
 */
public class LockTable extends AbsBaseTable {
    public static final String TABLE_NAME = "table_lock";

    /**
     * 表中所有的列名
     */

    public interface Columns extends BaseColumns {
        String LOCK_ID = "lock_id";//
        String LOCK_LONGITUDE = "lock_longitude";//经度
        String LOCK_LATITUDE = "lock_latitude";//纬度
        String LOCK_ADDRESS = "lock_address";//地点
        String LOCK_COUNTRY = "lock_country";//国家
        String LOCK_PROVINCE = "lock_province";//省
        String LOCK_CITY = "lock_city";//城市
        String LOCK_COUNTY = "lock_county";//区县
        String LOCK_TIME = "lock_time";//时间
        //version 2,新增nearby字段
        String LOCK_NEARBY = "lock_nearby";//附近
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
        sqlStatement.append(Columns.LOCK_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sqlStatement.append(Columns.LOCK_ADDRESS).append(" TEXT,");
        sqlStatement.append(Columns.LOCK_COUNTRY).append(" TEXT,");
        sqlStatement.append(Columns.LOCK_PROVINCE).append(" TEXT,");
        sqlStatement.append(Columns.LOCK_CITY).append(" TEXT,");
        sqlStatement.append(Columns.LOCK_COUNTY).append(" TEXT,");
        sqlStatement.append(Columns.LOCK_LONGITUDE).append(" DOUBLE,");
        sqlStatement.append(Columns.LOCK_LATITUDE).append(" DOUBLE,");
        sqlStatement.append(Columns.LOCK_TIME).append(" TEXT,");
        sqlStatement.append(Columns.LOCK_NEARBY).append(" TEXT");
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

    public String addColumnNearBySql(SQLiteDatabase db) {
        if (checkColumnExists(db, TABLE_NAME, Columns.LOCK_NEARBY))
            return "";
        String sqlStatement = "ALTER TABLE " + TABLE_NAME + " ADD " + Columns.LOCK_NEARBY + " TEXT";
        return sqlStatement;
    }
}

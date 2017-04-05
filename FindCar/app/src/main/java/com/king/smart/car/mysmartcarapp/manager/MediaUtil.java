package com.king.smart.car.mysmartcarapp.manager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.king.smart.car.mysmartcarapp.bean.MediaEntity;
import com.king.smart.car.mysmartcarapp.bean.MediaStateEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinzhendi-031 on 2016/11/16.
 */
public class MediaUtil {
    public static final String TAG = "MediaUtil";
    public static List<MediaEntity> mediaEntityList;

    public static List<MediaEntity> getAllMediaList(Context context, String selection) {
        Cursor cursor = null;
        List<MediaEntity> mediaList = new ArrayList<MediaEntity>();
        try {
            cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.DISPLAY_NAME,
                            MediaStore.Audio.Media.DURATION,
                            MediaStore.Audio.Media.ARTIST,
                            MediaStore.Audio.Media.DATA,
                            MediaStore.Audio.Media.SIZE},
                    selection, null, MediaStore.Audio.Media.DATE_ADDED + " DESC");
            if (cursor == null) {
                Log.d(TAG, "The getMediaList cursor is null.");
                return mediaList;
            }
            int count = cursor.getCount();
            if (count <= 0) {
                Log.d(TAG, "The getMediaList cursor count is 0.");
                return mediaList;
            }
            mediaList = new ArrayList<MediaEntity>();
            MediaEntity mediaEntity = null;
//			String[] columns = cursor.getColumnNames();
            while (cursor.moveToNext()) {
                mediaEntity = new MediaEntity();
                mediaEntity.id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                mediaEntity.title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                mediaEntity.display_name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                mediaEntity.duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                mediaEntity.size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
//                mediaEntity.durationStr = longToStrTime(mediaEntity.duration);

                if (!checkIsMusic(mediaEntity.duration, mediaEntity.size)) {
                    continue;
                }
                mediaEntity.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                mediaEntity.singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                mediaEntity.albums = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                mediaEntity.path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                mediaList.add(mediaEntity);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return mediaList;
    }


    /**
     * 根据时间和大小，来判断所筛选的media 是否为音乐文件，具体规则为筛选小于30秒和1m一下的
     */
    public static boolean checkIsMusic(int time, long size) {
        if (time <= 0 || size <= 0) {
            return false;
        }

        time /= 1000;
        int minute = time / 60;
        //	int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        if (minute <= 0 && second <= 30) {
            return false;
        }
        if (size <= 1024 * 1024) {
            return false;
        }
        return true;
    }

    private void scanSdCard(Context context) {
        context.sendBroadcast(new Intent(/*Intent.ACTION_MEDIA_SCANNER_SCAN_FILE*/Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
    }

    public static void setMediaState(MediaStateEntity entity){
        SharePreferceTool tool = SharePreferceTool.getInstance();
        tool.setCache("id",entity.getId());
        tool.setCache("duration",entity.getDuration());
        tool.setCache("current_time",entity.getCurrentTime());
        tool.setCache("loop_type",entity.getLoopType());
        tool.setCache("select_index",entity.getSelectIndex());
        tool.setCache("state_play",entity.getStatePlay());
    }

    public static MediaStateEntity getMediaState(){
        SharePreferceTool tool = SharePreferceTool.getInstance();
        int id = tool.getInt("id",-1);
        int duration = tool.getInt("duration",0);
        int current_time = tool.getInt("current_time",0);
        int loop_type = tool.getInt("loop_type",1);
        int select_index = tool.getInt("select_index",-1);
        int state_play = tool.getInt("state_play",0);
        MediaStateEntity entity = new MediaStateEntity();
        entity.setId(id)
                .setDuration(duration)
                .setCurrentTime(current_time)
                .setLoopType(loop_type)
                .setSelectIndex(select_index)
                .setStatePlay(state_play);
        return entity;
    }
}

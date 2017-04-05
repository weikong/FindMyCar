package com.king.smart.car.mysmartcarapp.manager;

import android.content.Context;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.Random;

/**
 * Created by xinzhendi-031 on 2016/11/17.
 */
public class MediaPlayerControll implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener {

    private Context context;
    private static MediaPlayer mediaPlayer;
    private String prePath, nextPath;
    private String mediaPath;

    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = 0;
    private final int TYPE_LOOP_LIST = 1; //列表循环
    private final int TYPE_LOOP_SINGLE = 2; //单曲循环
    private final int TYPE_LOOP_RANDOM = 3; //随机
    private int mTypeLoop = TYPE_LOOP_LIST;
    private int selectIndex = -1;

    public static MediaPlayerControll controll;

    public String getMediaPath() {
        return mediaPath;
    }

    public MediaPlayerControll setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
        return this;
    }

    public static MediaPlayerControll getInstance() {
        if (controll == null)
            controll = new MediaPlayerControll();
        return controll;
    }

    public MediaPlayerControll() {
    }

    public MediaPlayerControll(Context context) {
        this.context = context;
    }

    public MediaPlayer initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
        }
        return mediaPlayer;
    }

    public void setMediaSource(String path) {
        try {
            resetMedia();
            mediaPath = path;
            initMediaPlayer().setDataSource(path);
            initMediaPlayer().prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playMedia() {
        if (mediaPlayer != null && !initMediaPlayer().isPlaying())
            initMediaPlayer().start();
    }

    public void pauseMedia() {
        if (mediaPlayer != null && initMediaPlayer().isPlaying()) {
            initMediaPlayer().pause();
        }
    }

    public void resetMedia() {
        if (mediaPlayer != null)
            initMediaPlayer().reset();
    }

    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            initMediaPlayer().release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        if (playerListener != null)
            playerListener.onPrepared(mp);
        Logger.e("onPrepared");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Logger.e("play over");
        if (playerListener != null)
            playerListener.onCompletion(mp);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        ToastUtil.show("play error");
        Logger.e("play error");
        resetMedia();
        if (playerListener != null)
            playerListener.onError(mp, what, extra);
        return false;
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (playerListener != null)
            playerListener.onSeekComplete(mp);
    }

    private PlayerListener playerListener;

    public PlayerListener getPlayerListener() {
        return playerListener;
    }

    public void setPlayerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    public interface PlayerListener {
        public void onPrepared(MediaPlayer mp);

        public void onCompletion(MediaPlayer mp);

        public boolean onError(MediaPlayer mp, int what, int extra);

        public void onSeekComplete(MediaPlayer mp);
    }

    private void playNextOne() {
        if (MediaUtil.mediaEntityList == null || MediaUtil.mediaEntityList.size() == 0)
            return;
        mTypeLoop = SharePreferceTool.getInstance().getInt("TYPE_LOOP", TYPE_LOOP_LIST);
        if (mTypeLoop == TYPE_LOOP_LIST) {
            if (selectIndex >= (MediaUtil.mediaEntityList.size() - 1))
                selectIndex = 0;
            else
                selectIndex++;
        } else if (mTypeLoop == TYPE_LOOP_RANDOM) {
            try {
                Random random = new Random();
                selectIndex = random.nextInt(MediaUtil.mediaEntityList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            setMediaSource(MediaUtil.mediaEntityList.get(selectIndex).path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.king.smart.car.mysmartcarapp.ui.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.MediaEntity;
import com.king.smart.car.mysmartcarapp.bean.MediaStateEntity;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.manager.MediaPlayerControll;
import com.king.smart.car.mysmartcarapp.manager.MediaUtil;
import com.king.smart.car.mysmartcarapp.manager.SharePreferceTool;
import com.king.smart.car.mysmartcarapp.ui.base.ABaseUIActivity;
import com.king.smart.car.mysmartcarapp.ui.view.MediaControlView;
import com.king.smart.car.mysmartcarapp.ui.view.MediaDiskView;
import com.king.smart.car.mysmartcarapp.ui.view.MediaSeekBarView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class MusicPlayActivity extends ABaseUIActivity {

    private TextView tvMediaTitle, tvMediaSinger;
    private MediaControlView mediaControlView;
    private MediaSeekBarView mediaSeekBarView;
    private MediaDiskView mediaDiskView;
    private int selectIndex = -1;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = 0;
    private MediaPlayerControll mediaPlayerControll;
    private String strMediaTitle, strMediaSinger;
    private MediaEntity mediaEntity = null;

    private final int TYPE_LOOP_LIST = 1; //列表循环
    private final int TYPE_LOOP_SINGLE = 2; //单曲循环
    private final int TYPE_LOOP_RANDOM = 3; //随机
    private int mTypeLoop = TYPE_LOOP_LIST;
    private String mediaPath = "";

    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_play);
        getBundleData();
        initView();
        initData();
        startTimer();
    }

    @Override
    public void onBackPressed() {
        savePlayingState();
        mediaPlayerControll.setPlayerListener(null);
        closeTimer();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        Logger.e("Activity","onDestroy");
        super.onDestroy();
    }

    private void savePlayingState(){
        if (mPlay > 0){
            MediaStateEntity entity = new MediaStateEntity();
            entity.setId(MediaUtil.mediaEntityList.get(selectIndex).id)
                    .setSelectIndex(selectIndex)
                    .setStatePlay(mPlay)
                    .setLoopType(mTypeLoop)
                    .setCurrentTime(mediaPlayerControll.initMediaPlayer().getCurrentPosition())
                    .setDuration(mediaPlayerControll.initMediaPlayer().getDuration());
            MediaUtil.setMediaState(entity);
        }
    }

    private void getBundleData() {
//        Bundle bundle = this.getIntent().getExtras();
//        if (bundle != null && MediaUtil.mediaEntityList.size() > 0) {
//            selectIndex = (int) bundle.getInt("SelectIndex", -1);
//            mPlay = (int) bundle.getInt("PlayState", 0);
//            if (selectIndex != -1) {
//                try {
//                    mediaEntity = MediaUtil.mediaEntityList.get(selectIndex);
//                } catch (Exception e) {
//                    mediaEntity = MediaUtil.mediaEntityList.get(0);
//                }
//            }
//        }

        if (MediaUtil.mediaEntityList != null) {
            MediaStateEntity entity = MediaUtil.getMediaState();
            mPlay = entity.getStatePlay();
            selectIndex = entity.getSelectIndex();
            mediaPath = selectIndex == -1 ? "" : MediaUtil.mediaEntityList.get(selectIndex).path;
            mTypeLoop = SharePreferceTool.getInstance().getInt("TYPE_LOOP", TYPE_LOOP_LIST);
            if (selectIndex != -1) {
                try {
                    mediaEntity = MediaUtil.mediaEntityList.get(selectIndex);
                } catch (Exception e) {
                    mediaEntity = MediaUtil.mediaEntityList.get(0);
                }
            }
        }
    }

    private void initView() {
        tvMediaTitle = (TextView) findViewById(R.id.tv_media_title);
        tvMediaSinger = (TextView) findViewById(R.id.tv_media_singer);
        mediaControlView = (MediaControlView) findViewById(R.id.view_media_control);
        mediaControlView.setClickListener(clickListener);
        mediaSeekBarView = (MediaSeekBarView) findViewById(R.id.view_media_seek_bar);
        mediaSeekBarView.setSeekListener(seekListener);
        mediaDiskView = (MediaDiskView) findViewById(R.id.view_media_disk);
        mediaPlayerControll = MediaPlayerControll.getInstance();
        mediaPlayerControll.initMediaPlayer();
        mediaPlayerControll.setPlayerListener(playerListener);
    }

    private void initData() {
        if (mediaEntity != null) {
            strMediaTitle = mediaEntity.title;
            strMediaSinger = mediaEntity.singer;
            tvMediaTitle.setText(strMediaTitle);
            tvMediaSinger.setText(strMediaSinger);
            if (mPlay == 0)
                return;
            mediaControlView.setPlayStateIcon(mPlay);
            if (mediaPlayerControll.initMediaPlayer().getDuration() > 0 && mediaPlayerControll.initMediaPlayer().getCurrentPosition() >= 0) {
                mediaSeekBarView.setTvTimeRight(mediaPlayerControll.initMediaPlayer().getDuration());
                mediaSeekBarView.setTvTimeLeft(mediaPlayerControll.initMediaPlayer().getCurrentPosition());
            }
            if (mPlay == TYPE_PLAY)
                mediaDiskView.playAnim();
            else
                mediaDiskView.stopAnim();
        }
    }

    private void startTimer() {
        if (timerTask == null)
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (mPlay == TYPE_PLAY) {
                        handler.sendEmptyMessage(1);
                    }
                }
            };
        if (timer == null) {
            timer = new Timer();
            timer.schedule(timerTask, 100, 50);
        }
    }

    private void closeTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mediaSeekBarView.setTvTimeLeft(mediaPlayerControll.initMediaPlayer().getCurrentPosition());
                    break;
            }
        }
    };

    private void playNextOne() {
        if (MediaUtil.mediaEntityList == null || MediaUtil.mediaEntityList.size() == 0)
            return;
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
        playOrPauseMedia(TYPE_PLAY);
    }

    private void playPreOne() {
        if (MediaUtil.mediaEntityList == null || MediaUtil.mediaEntityList.size() == 0)
            return;
        if (mTypeLoop == TYPE_LOOP_LIST) {
            if (selectIndex <= 0)
                selectIndex = MediaUtil.mediaEntityList.size() - 1;
            else
                selectIndex--;
        } else if (mTypeLoop == TYPE_LOOP_RANDOM) {
            try {
                Random random = new Random();
                selectIndex = random.nextInt(MediaUtil.mediaEntityList.size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        playOrPauseMedia(TYPE_PLAY);
    }

    MediaPlayerControll.PlayerListener playerListener = new MediaPlayerControll.PlayerListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaEntity = MediaUtil.mediaEntityList.get(selectIndex);
            initData();
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            Logger.e("MusicPlayActivity", "onCompletion");
            playNextOne();
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            return false;
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {
        }
    };

    private void playOrPauseMedia(int play) {
        if (play == TYPE_PLAY) {
            if (selectIndex >= (MediaUtil.mediaEntityList.size() - 1) || selectIndex < 0)
                selectIndex = 0;
            String path = MediaUtil.mediaEntityList.get(selectIndex).path;
            if (!TextUtils.isEmpty(path)) {
                if (mPlay == 0 || !path.equals(mediaPath))
                    mediaPlayerControll.setMediaSource(path);
                else
                    mediaPlayerControll.playMedia();
                mediaPath = path;
            }
        } else {
            mediaPlayerControll.pauseMedia();
        }
        mPlay = play;
        if (mPlay == TYPE_PLAY)
            mediaDiskView.playAnim();
        else
            mediaDiskView.stopAnim();
    }

    MediaControlView.ClickListener clickListener = new MediaControlView.ClickListener() {
        @Override
        public void actionLoopType(int type) {
            mTypeLoop = type;
            SharePreferceTool.getInstance().setCache("TYPE_LOOP", mTypeLoop);
        }

        @Override
        public void actionMenu(int index) {

        }

        @Override
        public void actionPlay(int play) {
            playOrPauseMedia(play);
        }

        @Override
        public void actionNext() {
            playNextOne();
        }

        @Override
        public void actionPre() {
            playPreOne();
        }
    };

    MediaSeekBarView.SeekListener seekListener = new MediaSeekBarView.SeekListener() {
        @Override
        public void actionSeek(int currentTime) {
            mediaPlayerControll.initMediaPlayer().seekTo(currentTime);
        }
    };
}

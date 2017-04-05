package com.king.smart.car.mysmartcarapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.manager.TimeFormatUtils;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class MediaSeekBarView extends LinearLayout {

    private TextView tvTimeLeft, tvTimeRight;
    private SeekBar seekBar;

    private long mCurrentTime = 0;
    private long mDuration = 0;

    public MediaSeekBarView(Context context) {
        super(context);
        init();
    }

    public MediaSeekBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_media_progress_time, this);
        tvTimeLeft = (TextView) view.findViewById(R.id.tv_time_left);
        tvTimeRight = (TextView) view.findViewById(R.id.tv_time_right);
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar_media);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (seekListener != null)
                    seekListener.actionSeek(seekBar.getProgress());
            }
        });
    }

    public void setTvTimeLeft(long currentTime) {
        if (this.tvTimeLeft != null) {
            mCurrentTime = currentTime;
            if (currentTime >= 0 && currentTime <= mDuration) {
                this.tvTimeLeft.setText(TimeFormatUtils.formatDurationMMss(currentTime));
                this.seekBar.setProgress((int) currentTime);
            }
        }
    }

    public void setTvTimeRight(long duration) {
        if (this.tvTimeRight != null && duration > 0) {
            mDuration = duration;
            try {
                this.tvTimeRight.setText(TimeFormatUtils.formatDurationMMss(duration));
                this.seekBar.setMax((int) duration);
            } catch (Exception e) {

            }
        }
    }

    private SeekListener seekListener;

    public void setSeekListener(SeekListener seekListener) {
        this.seekListener = seekListener;
    }

    public interface SeekListener {
        public void actionSeek(int currentTime);
    }
}

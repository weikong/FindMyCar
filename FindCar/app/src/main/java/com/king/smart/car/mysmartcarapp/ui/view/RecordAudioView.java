package com.king.smart.car.mysmartcarapp.ui.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.RecordAudioBean;
import com.king.smart.car.mysmartcarapp.manager.RecordManager;
import com.king.smart.car.mysmartcarapp.manager.SDCardUtil;
import com.king.smart.car.mysmartcarapp.manager.TimeFormatUtils;
import com.king.smart.car.mysmartcarapp.manager.ToastUtil;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class RecordAudioView extends LinearLayout implements View.OnTouchListener {

    private RelativeLayout layoutCircle;
    private ImageView ivCircleIn;
    private TextView tvTime;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = TYPE_PAUSE;
    private RecordManager recordManager;
    private Animation animSmall, animBig;
    private String fileName = "";
    private RecordAudioBean recordAudioBean;
    private Handler mHandler = new Handler();

    public RecordAudioView(Context context) {
        super(context);
        init();
    }

    public RecordAudioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_record_audio, this);
        layoutCircle = (RelativeLayout) view.findViewById(R.id.layout_circle);
        ivCircleIn = (ImageView) view.findViewById(R.id.iv_circle_in);
        tvTime = (TextView) view.findViewById(R.id.tv_record_time);
        layoutCircle.setOnTouchListener(this);
        animSmall = AnimationUtils.loadAnimation(getContext(), R.anim.anim_video_scale_small);
        animBig = AnimationUtils.loadAnimation(getContext(), R.anim.anim_video_scale_big);
        recordManager = new RecordManager(handler);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long duration = (long) msg.obj;
                    tvTime.setText(TimeFormatUtils.formatDurationH(duration));
                    break;
                case 2:
                    recordAudio();
                    break;
            }
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                ivCircleIn.clearAnimation();
                ivCircleIn.startAnimation(animSmall);
                long time = System.currentTimeMillis();
                fileName = "REC_" + TimeFormatUtils.getRecordFormatDate(time) + ".mp3";
                recordAudioBean = new RecordAudioBean();
                recordAudioBean.setName(fileName);
                recordAudioBean.setTime("" + time);
                recordAudioBean.setPath(SDCardUtil.createTmpFile(fileName).getAbsolutePath());
                recordManager.setAudioPath(SDCardUtil.createTmpFile(fileName));
                recordManager.startRecord();
                return true;
            case MotionEvent.ACTION_UP:
                ivCircleIn.clearAnimation();
                ivCircleIn.startAnimation(animBig);
                if (recordManager != null) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            long duration = 0;
                            try {
                                duration = recordManager.stopRecord();
                                if (duration > 1000) {
                                    recordAudioBean.setDuration("" + duration);
                                    handler.sendEmptyMessage(2);
                                } else {
                                    tvTime.setText("0:00:00");
                                    recordAudioBean = null;
                                    ToastUtil.show("录制时间太短");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                tvTime.setText("0:00:00");
                                recordAudioBean = null;
                                String message = "音频录制错误";
                                if (e != null && !TextUtils.isEmpty(e.getMessage())){
                                    message = e.getMessage();
                                }
                                ToastUtil.show(message);
                            }
                        }
                    },220);
                } else {
                    tvTime.setText("0:00:00");
                    recordAudioBean = null;
                }
                break;
        }
        return false;
    }

    public void recordAudio() {
        if (recordListener != null) {
            recordListener.actionRecord(recordAudioBean);
            tvTime.setText("0:00:00");
            recordAudioBean = null;
        }
    }

    private RecordListener recordListener;

    public void setRecordListener(RecordListener recordListener) {
        this.recordListener = recordListener;
    }

    public interface RecordListener {
        public void actionRecord(RecordAudioBean bean);
    }
}

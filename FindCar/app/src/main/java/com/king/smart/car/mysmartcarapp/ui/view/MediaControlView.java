package com.king.smart.car.mysmartcarapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.manager.SharePreferceTool;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class MediaControlView extends LinearLayout implements View.OnClickListener {

    private ImageView ivMenu, ivLoopType, ivPlay, ivNext, ivPre;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = TYPE_PAUSE;

    private final int TYPE_LOOP_LIST = 1; //列表循环
    private final int TYPE_LOOP_SINGLE = 2; //单曲循环
    private final int TYPE_LOOP_RANDOM = 3; //随机
    private int mTypeLoop = TYPE_LOOP_LIST;

    public MediaControlView(Context context) {
        super(context);
        init();
    }

    public MediaControlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_media_control, this);
        ivLoopType = (ImageView) view.findViewById(R.id.iv_loop_type);
        ivPre = (ImageView) view.findViewById(R.id.iv_play_pre);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        ivNext = (ImageView) view.findViewById(R.id.iv_play_next);
        ivMenu = (ImageView) view.findViewById(R.id.iv_menu);
        ivLoopType.setOnClickListener(this);
        ivPre.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        int type = SharePreferceTool.getInstance().getInt("TYPE_LOOP", TYPE_LOOP_LIST);
        setLoopIcon(type);
    }

    public void setPlayStateIcon(int play) {
        this.mPlay = play;
        if (mPlay == TYPE_PAUSE) {
            ivPlay.setImageResource(R.drawable.btn_media_play_selector);
        } else {
            ivPlay.setImageResource(R.drawable.btn_media_pause_selector);
        }
    }

    public void setLoopIcon(int type) {
        mTypeLoop = type;
        int d = R.drawable.btn_media_loop_list_selector;
        if (mTypeLoop == TYPE_LOOP_LIST) {
            d = R.drawable.btn_media_loop_list_selector;
        } else if (mTypeLoop == TYPE_LOOP_SINGLE) {
            d = R.drawable.btn_media_loop_single_selector;
        } else {
            d = R.drawable.btn_media_loop_random_selector;
        }
        ivLoopType.setImageResource(d);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_loop_type:
                if (clickListener != null) {
                    int d = R.drawable.btn_media_loop_list_selector;
                    if (mTypeLoop == TYPE_LOOP_LIST) {
                        mTypeLoop = TYPE_LOOP_SINGLE;
                        d = R.drawable.btn_media_loop_single_selector;
                    } else if (mTypeLoop == TYPE_LOOP_SINGLE) {
                        mTypeLoop = TYPE_LOOP_RANDOM;
                        d = R.drawable.btn_media_loop_random_selector;
                    } else {
                        mTypeLoop = TYPE_LOOP_LIST;
                        d = R.drawable.btn_media_loop_list_selector;
                    }
                    ivLoopType.setImageResource(d);
                    clickListener.actionLoopType(mTypeLoop);
                }
                break;
            case R.id.iv_play:
                if (clickListener != null) {
                    if (mPlay == TYPE_PAUSE) {
                        mPlay = TYPE_PLAY;
                        ivPlay.setImageResource(R.drawable.btn_media_pause_selector);
                    } else {
                        mPlay = TYPE_PAUSE;
                        ivPlay.setImageResource(R.drawable.btn_media_play_selector);
                    }
                    clickListener.actionPlay(mPlay);
                }
                break;
            case R.id.iv_play_next:
                if (clickListener != null)
                    clickListener.actionNext();
                break;
            case R.id.iv_play_pre:
                if (clickListener != null)
                    clickListener.actionPre();
                break;
            case R.id.iv_menu:
                if (clickListener != null)
                    clickListener.actionMenu(-1);
                break;
        }
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void actionLoopType(int type);

        public void actionMenu(int index);

        public void actionPlay(int play);

        public void actionNext();

        public void actionPre();
    }
}

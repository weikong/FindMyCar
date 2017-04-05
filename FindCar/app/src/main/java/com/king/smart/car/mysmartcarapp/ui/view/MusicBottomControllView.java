package com.king.smart.car.mysmartcarapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.manager.SharePreferceTool;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class MusicBottomControllView extends LinearLayout implements View.OnClickListener {

    private ImageView ivIcon, ivMenu, ivPlay, ivNext;
    private TextView tvTitle;
    private TextView tvName;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = TYPE_PAUSE;

    private final int TYPE_LOOP_LIST = 1; //列表循环
    private final int TYPE_LOOP_SINGLE = 2; //单曲循环
    private final int TYPE_LOOP_RANDOM = 3; //随机
    private int mTypeLoop = TYPE_LOOP_LIST;

    public MusicBottomControllView(Context context) {
        super(context);
        init();
    }

    public MusicBottomControllView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_music_bottom_controll, this);
        ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        ivMenu = (ImageView) view.findViewById(R.id.iv_menu);
        ivPlay = (ImageView) view.findViewById(R.id.iv_play);
        ivNext = (ImageView) view.findViewById(R.id.iv_next);
        tvTitle = (TextView) view.findViewById(R.id.item_title);
        tvName = (TextView) view.findViewById(R.id.item_singer);
        ivMenu.setColorFilter(getResources().getColor(R.color.color_CD3D3A));
        ivMenu.setOnClickListener(this);
        ivPlay.setOnClickListener(this);
        ivNext.setOnClickListener(this);
        int type = SharePreferceTool.getInstance().getInt("TYPE_LOOP",TYPE_LOOP_LIST);
        setMenuIcon(type);
    }


    public void setTvActionTitleText(String s) {
        tvTitle.setText(s);
    }

    public void setTvActionNameText(String s) {
        tvName.setText(s);
    }

    public void playMusic(int play) {
        if (clickListener != null) {
            mPlay = play;
            clickListener.actionPlay(play);
            if (mPlay == TYPE_PLAY)
                ivPlay.setImageResource(R.drawable.playbar_btn_pause);
            else
                ivPlay.setImageResource(R.drawable.playbar_btn_play);
        }
    }

    public void setMenuIcon(int type){
        mTypeLoop = type;
        int d = R.drawable.lockscreen_player_btn_repeat_normal;
        if (mTypeLoop == TYPE_LOOP_LIST){
            d = R.drawable.lockscreen_player_btn_repeat_normal;
        } else if (mTypeLoop == TYPE_LOOP_SINGLE){
            d = R.drawable.lockscreen_player_btn_repeat_once;
        } else{
            d = R.drawable.lockscreen_player_btn_random;
        }
        ivMenu.setImageResource(d);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_menu:
                if (clickListener != null){
                    int d = R.drawable.lockscreen_player_btn_repeat_normal;
                    if (mTypeLoop == TYPE_LOOP_LIST){
                        mTypeLoop = TYPE_LOOP_SINGLE;
                        d = R.drawable.lockscreen_player_btn_repeat_once;
                    } else if (mTypeLoop == TYPE_LOOP_SINGLE){
                        mTypeLoop = TYPE_LOOP_RANDOM;
                        d = R.drawable.lockscreen_player_btn_random;
                    } else{
                        mTypeLoop = TYPE_LOOP_LIST;
                        d = R.drawable.lockscreen_player_btn_repeat_normal;
                    }
                    ivMenu.setImageResource(d);
                    clickListener.actionMenu(mTypeLoop);
                }
                break;
            case R.id.iv_play:
                if (clickListener != null) {
                    if (mPlay == TYPE_PAUSE) {
                        mPlay = TYPE_PLAY;
                        ivPlay.setImageResource(R.drawable.playbar_btn_pause);
                    } else {
                        mPlay = TYPE_PAUSE;
                        ivPlay.setImageResource(R.drawable.playbar_btn_play);
                    }
                    clickListener.actionPlay(mPlay);
                }
                break;
            case R.id.iv_next:
                if (clickListener != null)
                    clickListener.actionNext();
                break;
        }
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void actionMenu(int type);

        public void actionPlay(int play);

        public void actionNext();
    }
}

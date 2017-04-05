package com.king.smart.car.mysmartcarapp.ui.run.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.manager.Logger;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class CircleAnimView extends RelativeLayout implements View.OnTouchListener {

    private final int TYPE_RUN_PAUSE = 1;
    private final int TYPE_RUN_KEEP = 2;
    private final int TYPE_RUN_OVER = 3;
    private final int TYPE_RUN_NOMAL = 0;

    private RelativeLayout layoutRingBg;
    private View viewRing;
    private View viewRingBg;
    private TextView tvR;
    private Handler handler;
    private Animation animBig2, animSmall2;
    private Animation animBig1, animSmall1;
    private Handler mHandler = new Handler();

    public CircleAnimView(Context context) {
        super(context);
        init();
    }

    public CircleAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_circle_anim, this);
        layoutRingBg = (RelativeLayout) view.findViewById(R.id.layout_ring_bg);
        viewRingBg = (View) view.findViewById(R.id.view_ring_bg);
        viewRing = (View) view.findViewById(R.id.view_ring);
        tvR = (TextView) view.findViewById(R.id.tv_R);
        animBig2 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_big_2);
        animSmall2 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_scale_small_2);
        animBig1 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_video_scale_big);
        animSmall1 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_video_scale_small);
        animSmall1.setAnimationListener(animSmall1Listener);
        viewRingBg.setOnTouchListener(this);
    }

    public CircleAnimView setHandler(Handler handler) {
        this.handler = handler;
        return this;
    }

    public void setTvRText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvR.setText("R");
        } else {
            tvR.setText(s);
        }
    }

    public void setViewRingBgEnable(boolean enable){
        viewRingBg.setEnabled(enable);
    }

    private void startAnimDown() {
        Logger.e("CircleAnimView", "down");
        viewRingBg.clearAnimation();
        viewRingBg.startAnimation(animBig2);
    }

    private void startAnimUp() {
        Logger.e("CircleAnimView", "up");
        viewRing.clearAnimation();
        viewRing.startAnimation(animSmall1);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startAnimDown();
                return true;
            case MotionEvent.ACTION_UP:
                startAnimUp();
                return true;
        }
        return false;
    }

    private Animation.AnimationListener animSmall1Listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewRingBg.clearAnimation();
                    viewRingBg.startAnimation(animSmall2);
                    viewRing.clearAnimation();
                    viewRing.startAnimation(animBig1);
                    if (handler != null){
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                handler.sendEmptyMessage(1);
                            }
                        },200);
                    }
                    if (clickListener != null)
                        clickListener.doAction(TYPE_RUN_KEEP);

                }
            }, 50);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void doAction(int action);
    }
}

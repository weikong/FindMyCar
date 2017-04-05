package com.king.smart.car.mysmartcarapp.ui.run;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.ui.base.ABaseUIActivity;

public class WelRunActivity extends ABaseUIActivity {

    private TextView tvRun;
    private Animation animBig, animSmall;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel_run);
        tvRun = (TextView) findViewById(R.id.tv_run);
        animBig = AnimationUtils.loadAnimation(this, R.anim.anim_scale_big);
        animBig.setAnimationListener(animBigListener);
        animSmall = AnimationUtils.loadAnimation(this, R.anim.anim_scale_small);
        animSmall.setAnimationListener(animSmallListener);
        handler.sendEmptyMessageDelayed(2, 1500);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    IntentActivity(WelRunActivity.this, RunMainActivity.class);
                    finish();
                    break;
                case 2:
                    count++;
                    tvRun.clearAnimation();
                    tvRun.startAnimation(animBig);
                    break;
                case 3:
                    tvRun.clearAnimation();
                    tvRun.startAnimation(animSmall);
                    break;
            }
        }
    };

    private Animation.AnimationListener animBigListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            handler.sendEmptyMessageDelayed(3, 20);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener animSmallListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            handler.sendEmptyMessageDelayed(1, 500);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}

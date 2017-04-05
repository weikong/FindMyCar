package com.king.smart.car.mysmartcarapp.ui.run.view;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class RunMainView extends RelativeLayout implements View.OnClickListener {

    private TextView tvDistance, tvSpeed, tvTime, tvHeat;
    //    private TextView tvRun;
    private CircleAnimView circleAnimView;
    private Handler handler;

    public RunMainView(Context context) {
        super(context);
        init();
    }

    public RunMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_main_run, this);
        tvDistance = (TextView) view.findViewById(R.id.tv_distance);
        tvSpeed = (TextView) view.findViewById(R.id.tv_speed);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvHeat = (TextView) view.findViewById(R.id.tv_heat);
        circleAnimView = (CircleAnimView) view.findViewById(R.id.view_circle_anim);
        initData();
    }

    public RunMainView setHandler(Handler handler) {
        if (handler != null) {
            this.handler = handler;
            circleAnimView.setHandler(handler);
        }
        return this;
    }


    private void initData() {
        setTvDistanceText("");
        setTvSpeedText("");
        setTvTimeText("");
        setTvHeatText("");
    }

    public void setTvDistanceText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvDistance.setText("00.00");
        } else {
            tvDistance.setText(s);
        }
    }

    public void setTvSpeedText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvSpeed.setText("--");
        } else {
            tvSpeed.setText(s);
        }
    }

    public void setTvTimeText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvTime.setText("00:00:00");
        } else {
            tvTime.setText(s);
        }
    }

    public void setTvHeatText(String s) {
        if (TextUtils.isEmpty(s)) {
            tvHeat.setText("0");
        } else {
            tvHeat.setText(s);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_run:
                if (clickListener != null)
                    clickListener.run();
                break;
        }
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void run();
    }
}

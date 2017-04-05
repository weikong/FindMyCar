package com.king.smart.car.mysmartcarapp.ui.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class NavigationBarView extends LinearLayout implements View.OnClickListener {

    private TextView tvActionTips;
    private TextView tvActionNav;
    private TextView tvActionLock;
    private TextView tvActionMusic;
    private TextView tvActionTool;
    private int nomalColor, pressColor;

    public NavigationBarView(Context context) {
        super(context);
        init();
    }

    public NavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.layout_navigation_bar, this);
        tvActionTips = (TextView) view.findViewById(R.id.tv_navigation_tips);
        tvActionNav = (TextView) view.findViewById(R.id.tv_navigation_nav);
        tvActionLock = (TextView) view.findViewById(R.id.tv_navigation_lock);
        tvActionMusic = (TextView) view.findViewById(R.id.tv_navigation_music);
        tvActionTool = (TextView) view.findViewById(R.id.tv_navigation_tool);

        tvActionTips.setOnClickListener(this);
        tvActionNav.setOnClickListener(this);
        tvActionLock.setOnClickListener(this);
        tvActionMusic.setOnClickListener(this);
        tvActionTool.setOnClickListener(this);

        nomalColor = ContextCompat.getColor(getContext(), R.color.color_666666);
        pressColor = ContextCompat.getColor(getContext(), R.color.color_ffffff);
        tvActionTips.setTextColor(pressColor);
    }

    public void setNavigationUnSelect() {
        tvActionTips.setTextColor(nomalColor);
        tvActionNav.setTextColor(nomalColor);
        tvActionLock.setTextColor(nomalColor);
        tvActionMusic.setTextColor(nomalColor);
        tvActionTool.setTextColor(nomalColor);
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.tv_navigation_tips:
                    if (checkPageListener != null)
                        checkPageListener.junpIndex(0);
                    break;
                case R.id.tv_navigation_lock:
                    if (checkPageListener != null)
                        checkPageListener.junpIndex(1);
                    break;
                case R.id.tv_navigation_nav:
                    if (checkPageListener != null)
                        checkPageListener.junpIndex(2);
                    break;
                case R.id.tv_navigation_music:
                    if (checkPageListener != null)
                        checkPageListener.junpIndex(3);
                    break;
                case R.id.tv_navigation_tool:
                    if (checkPageListener != null)
                        checkPageListener.junpIndex(4);
                    break;
            }
        } finally {
            setNavigationUnSelect();
            ((TextView) view).setTextColor(pressColor);
        }
    }

    public void checkItemView(int position) {
        setNavigationUnSelect();
        switch (position) {
            case 0:
                tvActionTips.setTextColor(pressColor);
                break;
            case 1:
                tvActionLock.setTextColor(pressColor);
                break;
            case 2:
                tvActionNav.setTextColor(pressColor);
                break;
            case 3:
                tvActionMusic.setTextColor(pressColor);
                break;
            case 4:
                tvActionTool.setTextColor(pressColor);
                break;
            default:
                tvActionTips.setTextColor(pressColor);
                break;
        }
    }

    private CheckPageListener checkPageListener;

    public void setCheckPageListener(CheckPageListener listener) {
        this.checkPageListener = listener;
    }

    public interface CheckPageListener {
        public void junpIndex(int index);
    }
}

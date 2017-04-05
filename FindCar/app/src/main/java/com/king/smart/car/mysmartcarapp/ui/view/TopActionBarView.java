package com.king.smart.car.mysmartcarapp.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class TopActionBarView extends RelativeLayout implements View.OnClickListener {

    private TextView tvActionLeft;
    private TextView tvActionTitle;
    private TextView tvActionRight;

    public TopActionBarView(Context context) {
        super(context);
        init();
    }

    public TopActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_top_action_bar, this);
        tvActionLeft = (TextView) view.findViewById(R.id.tv_action_left);
        tvActionTitle = (TextView) view.findViewById(R.id.tv_action_title);
        tvActionRight = (TextView) view.findViewById(R.id.tv_action_right);
        tvActionLeft.setOnClickListener(this);
        tvActionRight.setOnClickListener(this);
    }

    public void setTvActionLeftText(String s) {
        tvActionLeft.setText(s);
    }

    public void setTvActionTitleText(String s) {
        tvActionTitle.setText(s);
    }

    public void setTvActionRightText(String s) {
        tvActionRight.setText(s);
    }

    public void setTvActionLeftVisiable(int visiable) {
        tvActionLeft.setVisibility(visiable);
    }

    public void setTvActionTitleVisiable(int visiable) {
        tvActionTitle.setVisibility(visiable);
    }

    public void setTvActionRightVisiable(int visiable) {
        tvActionRight.setVisibility(visiable);
    }

    public void setTvActionRightTextColor(int color) {
        tvActionRight.setTextColor(color);
    }

    /**
     * left:0;
     * top:1;
     * right:2;
     * bottom:3;
     */
    public void setTvActionRightDrawable(int d, int location) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), d);
        if (drawable == null)
            return;
        tvActionRight.setText("");
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        switch (location) {
            case 0:
                tvActionRight.setCompoundDrawables(drawable, null, null, null);
                break;
            case 1:
                tvActionRight.setCompoundDrawables(null, drawable, null, null);
                break;
            case 2:
                tvActionRight.setCompoundDrawables(null, null, drawable, null);
                break;
            case 3:
                tvActionRight.setCompoundDrawables(null, null, null, drawable);
                break;
            default:
                tvActionRight.setCompoundDrawables(drawable, null, null, null);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_action_left:
                if (clickListener != null)
                    clickListener.close();
                break;
            case R.id.tv_action_right:
                if (clickListener != null)
                    clickListener.setting();
                break;
        }
    }

    private ClickListener clickListener;

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void close();

        public void setting();
    }
}

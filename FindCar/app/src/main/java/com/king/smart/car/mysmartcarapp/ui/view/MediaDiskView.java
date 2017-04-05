package com.king.smart.car.mysmartcarapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.king.smart.car.mysmartcarapp.R;

/**
 * Created by xinzhendi-031 on 2016/10/26.
 */
public class MediaDiskView extends RelativeLayout {

    private RelativeLayout layoutAnimDisk;
    private ImageView ivDisk;
    private Animation animRotate;
    private boolean isPlayAnim = false;

    public MediaDiskView(Context context) {
        super(context);
        init();
    }

    public MediaDiskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(R.layout.view_media_disk, this);
        layoutAnimDisk = (RelativeLayout) view.findViewById(R.id.layout_disk_anim);
        ivDisk = (ImageView) view.findViewById(R.id.iv_disk);
        animRotate = AnimationUtils.loadAnimation(getContext(), R.anim.anim_media_disk);
    }

    public void playAnim() {
        if (!isPlayAnim) {
            isPlayAnim = true;
            layoutAnimDisk.clearAnimation();
            layoutAnimDisk.startAnimation(animRotate);
        }
    }

    public void stopAnim() {
        isPlayAnim = false;
        layoutAnimDisk.clearAnimation();
    }

}

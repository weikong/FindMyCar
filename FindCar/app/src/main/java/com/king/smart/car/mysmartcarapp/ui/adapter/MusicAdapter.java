/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.king.smart.car.mysmartcarapp.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.MediaEntity;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends BaseAdapter {

    private Context mContext;
    private List<MediaEntity> datas = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private int selectIndex = -1;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = 0;

    public MusicAdapter(Context context) {
        this.mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setDatas(List<MediaEntity> list) {
        if (list == null)
            return;
        this.datas.clear();
        this.datas.addAll(list);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NodeViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_music, null);
            holder = new NodeViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_singer);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.viewSelect = (View) convertView.findViewById(R.id.view_select);
            holder.iv_anim = (ImageView) convertView.findViewById(R.id.iv_anim);
            convertView.setTag(holder);
        } else {
            holder = (NodeViewHolder) convertView.getTag();
        }
        MediaEntity entity = (MediaEntity) getItem(position);
        holder.title.setText(entity.title);
        holder.name.setText(entity.singer);
        if (getSelectIndex() == position) {
            holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            holder.viewSelect.setVisibility(View.VISIBLE);
            holder.iv_anim.setVisibility(View.VISIBLE);
            AnimationDrawable animationDrawable = (AnimationDrawable) holder.iv_anim.getDrawable();
            if (mPlay == TYPE_PLAY)
                animationDrawable.start();
            else if (mPlay == TYPE_PAUSE)
                animationDrawable.stop();
        } else {
            holder.title.setTextColor(ContextCompat.getColor(mContext, R.color.color_f4f4f4));
            holder.viewSelect.setVisibility(View.INVISIBLE);
            holder.iv_anim.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    private class NodeViewHolder {
        private TextView name;
        private TextView title;
        private View viewSelect;
        private ImageView iv_anim;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    public int getmPlay() {
        return mPlay;
    }

    public void setmPlay(int mPlay) {
        this.mPlay = mPlay;
    }
}

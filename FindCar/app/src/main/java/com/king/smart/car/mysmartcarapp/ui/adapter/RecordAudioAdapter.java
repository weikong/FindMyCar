package com.king.smart.car.mysmartcarapp.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.RecordAudioBean;
import com.king.smart.car.mysmartcarapp.manager.TimeFormatUtils;
import com.king.smart.car.mysmartcarapp.ui.view.SwipeListView.BaseSwipListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xinzhendi-031 on 2016/11/22.
 */
public class RecordAudioAdapter extends BaseSwipListAdapter {

    private Context mContext;
    private ArrayList<RecordAudioBean> datas = new ArrayList<>();
    private Map<String,RecordAudioBean> dateBeanMap = new HashMap<>();
    private int selectIndex = -1;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private int mPlay = 0;

    public RecordAudioAdapter(Context context) {
        this.mContext = context;
    }

    public RecordAudioAdapter(Context context, ArrayList<RecordAudioBean> datas) {
        this.mContext = context;
        setDatas(datas);
    }

    public void setDatas(ArrayList<RecordAudioBean> datas) {
        if (datas != null) {
            this.datas.clear();
            this.datas.addAll(freshData(datas));
        }
    }

    public void addData(RecordAudioBean item) {
        if (item != null)
            this.datas.add(0, item);
    }

    public ArrayList<RecordAudioBean> freshData(ArrayList<RecordAudioBean> datas){
        if (datas == null)
            return null;
        ArrayList<RecordAudioBean> list = new ArrayList<>();
        dateBeanMap.clear();
        for (RecordAudioBean bean : datas){
            String strTime = bean.getTime();
            if (!TextUtils.isEmpty(strTime)) {
                long time = Long.parseLong(strTime);
                String date = TimeFormatUtils.getFormatDate4(time);
                if (!TextUtils.isEmpty(date) && !dateBeanMap.containsKey(date)){
                    RecordAudioBean headItem = new RecordAudioBean();
                    headItem.setShowDate(true).setTime(strTime);
//                    bean.setShowDate(true);
                    dateBeanMap.put(date,headItem);
                    list.add(headItem);
                }
                list.add(bean);
            }
        }
        return list;
    }

    public void removeData(RecordAudioBean item) {
        this.datas.remove(item);
    }

    public void removeData(int position) {
        this.datas.remove(position);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public RecordAudioBean getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        RecordAudioBean bean = getItem(position);
        return bean.isShowDate() ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final RecordAudioBean item = getItem(position);
        if (item.isShowDate()){
            if (convertView == null) {
                convertView = View.inflate(mContext,
                        R.layout.adapter_record_audio_head, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String strTime = item.getTime();
            if (!TextUtils.isEmpty(strTime)) {
                long time = Long.parseLong(strTime);
                viewHolder.tv_date.setText(TimeFormatUtils.getFormatDate4(time));
            }
            return convertView;
        }

        if (convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
            if (viewHolder.tv_name == null){
                convertView = View.inflate(mContext,
                        R.layout.adapter_record_audio, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
        } else {
            convertView = View.inflate(mContext,
                    R.layout.adapter_record_audio, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        String strTime = item.getTime();
        if (!TextUtils.isEmpty(strTime)){
            long time = Long.parseLong(strTime);
            viewHolder.tv_time.setText(TimeFormatUtils.getFormatDate5(time));
//            String date = TimeFormatUtils.getFormatDate4(time);
//            if (item.isShowDate()){
//                viewHolder.layout_date.setVisibility(View.VISIBLE);
//                viewHolder.tv_date.setText(TimeFormatUtils.getFormatDate4(time));
//            } else {
//                viewHolder.layout_date.setVisibility(View.GONE);
//            }
        } else {
            viewHolder.tv_time.setText("");
//            viewHolder.layout_date.setVisibility(View.GONE);
        }

        viewHolder.tv_name.setText(item.getName());
        final String duration = item.getDuration();
        if (TextUtils.isEmpty(duration))
            viewHolder.tv_duration.setText("0:00:00");
        else {
            if (getSelectIndex() == position && mPlay == TYPE_PLAY) {
                viewHolder.tv_duration.setVisibility(View.INVISIBLE);
                viewHolder.iv_anim.setVisibility(View.VISIBLE);
                AnimationDrawable animationDrawable = (AnimationDrawable) viewHolder.iv_anim.getDrawable();
                animationDrawable.start();
            } else {
                long d = Long.parseLong(duration);
                viewHolder.iv_anim.setVisibility(View.INVISIBLE);
                viewHolder.tv_duration.setVisibility(View.VISIBLE);
                viewHolder.tv_duration.setText(TimeFormatUtils.formatDurationH(d));
            }
        }
        return convertView;
    }

    class ViewHolder {
        RelativeLayout layout_date;
        TextView tv_date;
        TextView tv_name, tv_time, tv_duration;
        ImageView iv_anim;

        public ViewHolder(View view) {
            layout_date = (RelativeLayout) view.findViewById(R.id.layout_date);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_duration = (TextView) view.findViewById(R.id.tv_duration);
            iv_anim = (ImageView) view.findViewById(R.id.iv_anim);
            view.setTag(this);
        }
    }

    @Override
    public boolean getSwipEnableByPosition(int position) {
        RecordAudioBean item = getItem(position);
        if (item.isShowDate())
            return false;
        return true;
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

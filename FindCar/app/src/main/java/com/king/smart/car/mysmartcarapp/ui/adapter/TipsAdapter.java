package com.king.smart.car.mysmartcarapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.WeatherBean;
import com.king.smart.car.mysmartcarapp.bean.WeatherItemBean;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder> {
    private Context mContext;
    //    private ArrayList<WeatherBean> dataList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private WeatherBean tipsBean;

    public TipsAdapter(Context context) {
        mContext = context;
    }

    public void setData(WeatherBean tipsBean) {
        if (tipsBean != null) {
            this.tipsBean = tipsBean;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tips, parent, false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (tipsBean == null)
            return;
        holder.pollutionIndex.setText(tipsBean.getPollutionIndex());
        holder.airCondition.setText(tipsBean.getAirCondition());
        holder.dressingIndex.setText(tipsBean.getDressingIndex());
        holder.coldIndex.setText(tipsBean.getColdIndex());
        holder.exerciseIndex.setText(tipsBean.getExerciseIndex());
        holder.sunRiseSet.setText(tipsBean.getSunRise() + " ~ " + tipsBean.getSunSet());
        holder.week.setText(tipsBean.getWeek());
        for (int i = 0; i < 6; i++) {
            try {
                WeatherItemBean futureBean = tipsBean.getItemWeatherList().get(i);
                TextView future = (TextView) holder.layoutFuture.getChildAt(i);
                String temp = futureBean.getTemperature().replace("/", "\n");
                future.setText(futureBean.getWeek() + "\n" + temp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return 1;
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView pollutionIndex, airCondition, dressingIndex, coldIndex, exerciseIndex, sunRiseSet, week;
        //        private TextView future1, future2, future3, future4, future5, future6, future7;
        private LinearLayout rootView;
        private LinearLayout layoutFuture;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = (LinearLayout) itemView.findViewById(R.id.rootView);
            pollutionIndex = (TextView) itemView.findViewById(R.id.item_air_index);
            airCondition = (TextView) itemView.findViewById(R.id.item_air_condition);
            dressingIndex = (TextView) itemView.findViewById(R.id.item_dress_index);
            coldIndex = (TextView) itemView.findViewById(R.id.item_cold_index);
            exerciseIndex = (TextView) itemView.findViewById(R.id.item_exercise_index);
            sunRiseSet = (TextView) itemView.findViewById(R.id.item_sun_rise_set);
            week = (TextView) itemView.findViewById(R.id.item_car_forbid);
            layoutFuture = (LinearLayout) itemView.findViewById(R.id.layout_future);
//            future1 = (TextView) itemView.findViewById(R.id.tv_future_1);
//            future2 = (TextView) itemView.findViewById(R.id.tv_future_2);
//            future3 = (TextView) itemView.findViewById(R.id.tv_future_3);
//            future4 = (TextView) itemView.findViewById(R.id.tv_future_4);
//            future5 = (TextView) itemView.findViewById(R.id.tv_future_5);
//            future6 = (TextView) itemView.findViewById(R.id.tv_future_6);
//            future7 = (TextView) itemView.findViewById(R.id.tv_future_7);
        }
    }
}

package com.king.smart.car.mysmartcarapp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.RowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class RecordAudioRecyclerAdapter extends RecyclerView.Adapter<RecordAudioRecyclerAdapter.MainViewHolder> {
    LayoutInflater inflater;
    List<RowModel> modelList;

    public RecordAudioRecyclerAdapter(Context context, List<RowModel> list) {
        inflater = LayoutInflater.from(context);
        modelList = new ArrayList<>(list);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_record_audio_recycler, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.bindData(modelList.get(position));
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MainViewHolder extends RecyclerView.ViewHolder {

        TextView mainText, subText;

        public MainViewHolder(View itemView) {
            super(itemView);
            mainText = (TextView) itemView.findViewById(R.id.mainText);
            subText = (TextView) itemView.findViewById(R.id.subText);
        }

        public void bindData(RowModel rowModel) {
            mainText.setText(rowModel.getMainText());
            subText.setText(rowModel.getSubText());
        }
    }
}

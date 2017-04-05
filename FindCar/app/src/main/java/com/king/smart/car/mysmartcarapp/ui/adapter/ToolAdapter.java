package com.king.smart.car.mysmartcarapp.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.LockBean;
import com.king.smart.car.mysmartcarapp.manager.BluetoothManager;
import com.king.smart.car.mysmartcarapp.manager.ToolManager;

import java.util.ArrayList;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class ToolAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private ArrayList<String> dataList = new ArrayList<>();
    private boolean blueToothOpen = false;

    public ToolAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public ArrayList<String> getDataList() {
        return dataList;
    }

    public void setDatas(ArrayList<String> dataList) {
        if (dataList != null)
            this.dataList = dataList;
    }

    public void addData(String bean) {
        if (bean != null)
            this.dataList.add(0, bean);
    }

    public void removeData(LockBean bean) {
        if (bean != null)
            this.dataList.remove(bean);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToolViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_tool_single, null);
            holder = new ToolViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_tool);
            holder.iv_arrow = (ImageView) convertView.findViewById(R.id.iv_arrow);
            holder.iv_off_on = (ImageView) convertView.findViewById(R.id.iv_off_on);
            convertView.setTag(holder);
        } else {
            holder = (ToolViewHolder) convertView.getTag();
        }
        if (position == 1) {
            holder.iv_arrow.setVisibility(View.INVISIBLE);
            holder.iv_off_on.setVisibility(View.VISIBLE);
            holder.iv_off_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToolManager.flashLightOffOn();
                    if (ToolManager.checkFlashLightOpen(null))
                        ((ImageView)v).setImageResource(R.drawable.switch_on_clicked);
                    else
                        ((ImageView)v).setImageResource(R.drawable.switching_off);
                }
            });
            if (ToolManager.checkFlashLightOpen(null))
                holder.iv_off_on.setImageResource(R.drawable.switch_on_clicked);
            else
                holder.iv_off_on.setImageResource(R.drawable.switching_off);
        } else if (position == 2) {
            holder.iv_arrow.setVisibility(View.INVISIBLE);
            holder.iv_off_on.setVisibility(View.VISIBLE);
            holder.iv_off_on.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (BluetoothManager.isBluetoothSupported()) {
                        if (!BluetoothManager.isBluetoothEnabled()){
                            boolean open = BluetoothManager.turnOnBluetooth();
                            if (open)
                                blueToothOpen = true;
                            else
                                blueToothOpen = false;
                        } else {
                            boolean off = BluetoothManager.turnOffBluetooth();
                            if (off)
                                blueToothOpen = false;
                        }
                        if (BluetoothManager.isBluetoothSupported()) {
                            if (blueToothOpen)
                                ((ImageView)v).setImageResource(R.drawable.switch_on_clicked);
                            else
                                ((ImageView)v).setImageResource(R.drawable.switching_off);
                        }
                    }
                }
            });
            if (BluetoothManager.isBluetoothSupported()) {
                if (BluetoothManager.isBluetoothEnabled() || blueToothOpen)
                    holder.iv_off_on.setImageResource(R.drawable.switch_on_clicked);
                else
                    holder.iv_off_on.setImageResource(R.drawable.switching_off);
            }
        } else {
            holder.iv_arrow.setVisibility(View.VISIBLE);
            holder.iv_off_on.setVisibility(View.INVISIBLE);
        }
        holder.name.setText(dataList.get(position));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    private class ToolViewHolder {
        private TextView name;
        private ImageView iv_arrow;
        private ImageView iv_off_on;
    }

    public void onItemClick(View view, int position) {
        if (position == 1) {
            ToolManager.flashLightOffOn();
        } else if (position == 2) {
            if (BluetoothManager.isBluetoothSupported()) {
                if (!BluetoothManager.isBluetoothEnabled())
                    BluetoothManager.turnOnBluetooth();
                else
                    BluetoothManager.turnOffBluetooth();
            }
        }
        notifyDataSetInvalidated();
    }

}

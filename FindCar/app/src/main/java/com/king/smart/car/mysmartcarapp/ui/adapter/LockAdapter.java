package com.king.smart.car.mysmartcarapp.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.LockBean;

import java.util.ArrayList;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class LockAdapter extends RecyclerView.Adapter<LockAdapter.CityViewHolder> {
    private Context mContext;
    private ArrayList<LockBean> dataList = new ArrayList<>();
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private boolean isLongClick = false;
    private Handler handler;
    String mStrChina;
    String mStrSC;

    public ArrayList<LockBean> getDataList() {
        return dataList;
    }

    public boolean isLongClick() {
        return isLongClick;
    }

    public void setLongClick(boolean longClick) {
        this.isLongClick = longClick;
    }

    public LockAdapter(Context context,Handler handler) {
        this.mContext = context;
        this.handler = handler;
        mStrChina = mContext.getString(R.string.txt_china);
        mStrSC = mContext.getString(R.string.txt_sc);
    }

    public void setDatas(ArrayList<LockBean> dataList) {
        if (dataList != null)
            this.dataList = dataList;
    }

    public void addData(LockBean bean) {
        if (bean != null)
            this.dataList.add(0, bean);
    }

    public void removeData(LockBean bean) {
        if (bean != null)
            this.dataList.remove(bean);
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_lock_new, parent, false));
    }


    @Override
    public void onBindViewHolder(final CityViewHolder holder, final int position) {
        final LockBean bean = dataList.get(position);
        String mStrAddress = bean.getmAddress();
        if (!TextUtils.isEmpty(mStrAddress)){
            if (mStrAddress.startsWith(mStrChina)){
                mStrAddress = mStrAddress.substring(mStrChina.length());
            }
            if (mStrAddress.startsWith(mStrSC)){
                mStrAddress = mStrAddress.substring(mStrSC.length());
            }
            holder.itemAddress.setText(mStrAddress);
        }
        String nearby = bean.getmNearBy();
        if (!TextUtils.isEmpty(nearby)) {
            if (nearby.startsWith(mContext.getResources().getString(R.string.txt_zai))) {
                nearby = nearby.substring(1);
            }
            holder.itemNearBy.setText("(" + nearby + ")");
        }
        holder.itemTime.setText(String.valueOf(bean.getmTime()));
        if (isLongClick) {
            holder.ivArrow.setVisibility(View.INVISIBLE);
            holder.ivCheck.setVisibility(View.VISIBLE);
            if (bean.isCheck()) {
                holder.ivCheck.setImageResource(R.drawable.icon_checkbox_hover);
            } else {
                holder.ivCheck.setImageResource(R.drawable.icon_checkbox);
            }
        } else {
            bean.setCheck(false);
            holder.ivArrow.setVisibility(View.VISIBLE);
            holder.ivCheck.setVisibility(View.INVISIBLE);
            holder.ivCheck.setImageResource(R.drawable.icon_checkbox);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v, position);
                if (!isLongClick) {
                    mOnItemClickListener.onItemClick(v, position);
                } else {
                    bean.setCheck(!bean.isCheck());
                    notifyDataSetChanged();
                }
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                mOnItemClickListener.onItemLongClick(v, position);
                if (!isLongClick) {
                    isLongClick = true;
                    bean.setCheck(true);
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = isLongClick;
                    handler.sendMessage(message);
                    notifyDataSetChanged();
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }


    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int pos);

        void onItemLongClick(View view, int pos);
    }

    class CityViewHolder extends RecyclerView.ViewHolder {
        private TextView itemAddress, itemNearBy, itemTime;
        private RelativeLayout cardView;
        private ImageView ivCheck, ivArrow;


        public CityViewHolder(View itemView) {
            super(itemView);
            itemAddress = (TextView) itemView.findViewById(R.id.tv_address);
            itemNearBy = (TextView) itemView.findViewById(R.id.tv_nearby);
            itemTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivCheck = (ImageView) itemView.findViewById(R.id.ivCheck);
            ivArrow = (ImageView) itemView.findViewById(R.id.iv_arrow);
            cardView = (RelativeLayout) itemView.findViewById(R.id.cardView);
        }
    }
}

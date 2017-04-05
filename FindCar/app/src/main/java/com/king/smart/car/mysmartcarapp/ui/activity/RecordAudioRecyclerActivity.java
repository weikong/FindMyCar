package com.king.smart.car.mysmartcarapp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.RowModel;
import com.king.smart.car.mysmartcarapp.manager.ToastUtil;
import com.king.smart.car.mysmartcarapp.ui.adapter.RecordAudioRecyclerAdapter;
import com.king.smart.car.mysmartcarapp.ui.base.ABaseUIActivity;
import com.king.smart.car.mysmartcarapp.ui.listener.OnActivityTouchListener;
import com.king.smart.car.mysmartcarapp.ui.listener.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class RecordAudioRecyclerActivity extends ABaseUIActivity implements RecyclerTouchListener.RecyclerTouchListenerHelper {

    private RecyclerView mRecyclerView;
    private RecordAudioRecyclerAdapter mAdapter;
    private RecyclerTouchListener onTouchListener;
    private int openOptionsPosition;
    private OnActivityTouchListener touchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio_recycler);
        initView();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new RecordAudioRecyclerAdapter(this, getData());
        mRecyclerView.setAdapter(mAdapter);

        onTouchListener = new RecyclerTouchListener(this, mRecyclerView);
        onTouchListener
                .setIndependentViews(R.id.rowButton)
                .setViewsToFade(R.id.rowButton)
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        ToastUtil.show("Row " + (position + 1) + " clicked!");
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {
                        ToastUtil.show("Button in row " + (position + 1) + " clicked!");
                    }
                })
                .setLongClickable(true, new RecyclerTouchListener.OnRowLongClickListener() {
                    @Override
                    public void onRowLongClicked(int position) {
                        ToastUtil.show("Row " + (position + 1) + " long clicked!");
                    }
                })
                .setSwipeOptionViews(R.id.add, R.id.edit, R.id.change)
                .setSwipeable(R.id.rowFG, R.id.rowBG, new RecyclerTouchListener.OnSwipeOptionsClickListener() {
                    @Override
                    public void onSwipeOptionClicked(int viewID, int position) {
                        String message = "";
                        if (viewID == R.id.add) {
                            message += "Add";
                        } else if (viewID == R.id.edit) {
                            message += "Edit";
                        } else if (viewID == R.id.change) {
                            message += "Change";
                        }
                        message += " clicked for row " + (position + 1);
                        ToastUtil.show(message);
                    }
                });
    }

    private List<RowModel> getData() {
        List<RowModel> list = new ArrayList<>(25);
        for (int i = 0; i < 25; i++) {
            list.add(new RowModel("Row " + (i + 1), "Some Text... "));
        }
        return list;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchListener != null) touchListener.getTouchCoordinates(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void setOnActivityTouchListener(OnActivityTouchListener listener) {
        this.touchListener = listener;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRecyclerView.removeOnItemTouchListener(onTouchListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecyclerView.addOnItemTouchListener(onTouchListener);
    }
}

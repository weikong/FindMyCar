/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 baoyongzhang <baoyz94@gmail.com>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.king.smart.car.mysmartcarapp.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.RecordAudioBean;
import com.king.smart.car.mysmartcarapp.db.helper.RecordAudioDBHelper;
import com.king.smart.car.mysmartcarapp.manager.DisplayUtil;
import com.king.smart.car.mysmartcarapp.manager.MediaPlayerControll;
import com.king.smart.car.mysmartcarapp.manager.ToastUtil;
import com.king.smart.car.mysmartcarapp.ui.adapter.RecordAudioAdapter;
import com.king.smart.car.mysmartcarapp.ui.view.RecordAudioView;
import com.king.smart.car.mysmartcarapp.ui.view.SwipeListView.SwipeMenu;
import com.king.smart.car.mysmartcarapp.ui.view.SwipeListView.SwipeMenuCreator;
import com.king.smart.car.mysmartcarapp.ui.view.SwipeListView.SwipeMenuItem;
import com.king.smart.car.mysmartcarapp.ui.view.SwipeListView.SwipeMenuListView;
import com.king.smart.car.mysmartcarapp.ui.view.TopActionBarView;
import com.king.smart.car.mysmartcarapp.ui.view.dialog.MyInputDialog;
import com.king.smart.car.mysmartcarapp.ui.view.dialog.MyInputFixNameDialog;

import java.util.ArrayList;

/**
 * SwipeMenuListView
 * Created by baoyz on 15/6/29.
 */
public class RecordAudioActivity extends Activity {

    private ArrayList<RecordAudioBean> dataList = new ArrayList<>();
    private RecordAudioAdapter mAdapter;
    private SwipeMenuListView mListView;
    private RecordAudioView recordAudioView;
    private MediaPlayerControll mediaPlayerControll;
    private final int TYPE_PLAY = 1;
    private final int TYPE_PAUSE = 2;
    private MyInputFixNameDialog myInputDialog;
    private RecordAudioBean fixBean;
    private TopActionBarView actionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        getData();
        initActionBar();
        initView();
        //        mediaPlayerControll = new MediaPlayerControll(this);
        mediaPlayerControll = MediaPlayerControll.getInstance();
        mediaPlayerControll.initMediaPlayer();
        mediaPlayerControll.setPlayerListener(playerListener);
    }

    @Override
    protected void onDestroy() {
        mediaPlayerControll.setPlayerListener(null);
        super.onDestroy();
    }

    private void initActionBar() {
        actionBarView = (TopActionBarView) findViewById(R.id.view_top_action_bar);
        actionBarView.setTvActionTitleText(getString(R.string.item_tool_voice_record));
        actionBarView.setClickListener(actionListener);
    }

    private void initView() {
        recordAudioView = (RecordAudioView) findViewById(R.id.recordAudioView);
        recordAudioView.setRecordListener(recordListener);
        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        mAdapter = new RecordAudioAdapter(this, getData());
        mListView.setAdapter(mAdapter);
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x53, 0xbc,
                        0x68)));
                // set item width
                openItem.setWidth(DisplayUtil.dp2px(80));
                // set item title
                openItem.setTitle("编辑");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                SwipeMenuItem delItem = new SwipeMenuItem(
                        getApplicationContext());
                // create "delete" item
                delItem.setBackground(new ColorDrawable(Color.rgb(0xD9, 0x26,
                        0x1B)));
                // set item width
                delItem.setWidth(DisplayUtil.dp2px(80));
                // set item title
                delItem.setTitle("删除");
                // set item title fontsize
                delItem.setTitleSize(18);
                // set item title font color
                delItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(delItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                fixBean = mAdapter.getItem(position);
                switch (index) {
                    case 0:
                        // edit
                        popDialog();
                        break;
                    case 1:
                        // delete
                        delData(fixBean);
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mediaPlayerControll.setMediaSource(mAdapter.getItem(position).getPath());
                updateUIOver(position, TYPE_PLAY);
            }
        });
    }

    private ArrayList<RecordAudioBean> getData() {
        ArrayList<RecordAudioBean> list = RecordAudioDBHelper.query();
        dataList.clear();
        dataList.addAll(list);
        return dataList;
    }

    private void delData(RecordAudioBean bean) {
        boolean isDel = RecordAudioDBHelper.delete(bean);
        if (isDel) {
            mAdapter.removeData(bean);
            mAdapter.notifyDataSetChanged();
        }
    }

    private RecordAudioView.RecordListener recordListener = new RecordAudioView.RecordListener() {
        @Override
        public void actionRecord(RecordAudioBean bean) {
            boolean isInsert = RecordAudioDBHelper.insert(bean);
            if (isInsert) {
                mAdapter.setDatas(getData());
                mAdapter.notifyDataSetChanged();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.smoothScrollToPositionFromTop(0, -1, 400);
                    }
                }, 100);
            }
        }
    };

    Handler handler = new Handler();

    private void updateUIOver(int select, int state) {
        if (mAdapter != null) {
            mAdapter.setSelectIndex(select);
            mAdapter.setmPlay(state);
            mAdapter.notifyDataSetChanged();
        }

    }

    MediaPlayerControll.PlayerListener playerListener = new MediaPlayerControll.PlayerListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            updateUIOver(-1, -1);
        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            updateUIOver(-1, -1);
            return false;
        }

        @Override
        public void onSeekComplete(MediaPlayer mp) {

        }
    };

    private void updateItemData(RecordAudioBean bean) {
        boolean isUpdate = RecordAudioDBHelper.update(bean);
        if (isUpdate) {
            mAdapter.setDatas(getData());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void popDialog() {
        if (myInputDialog == null) {
            myInputDialog = new MyInputFixNameDialog(this);
            myInputDialog.setOnItemInDlgClickLinster(dlgClickListener);
        }
        myInputDialog.show();
    }

    MyInputFixNameDialog.OnItemInDlgClickListener dlgClickListener = new MyInputFixNameDialog.OnItemInDlgClickListener() {
        @Override
        public void onItemClick(int type, String input) {
            switch (type) {
                case MyInputDialog.TypeConfirm:
                    if (fixBean == null)
                        return;
                    if (TextUtils.isEmpty(input)) {
                        ToastUtil.show("文件名不能为空");
                        return;
                    }
                    fixBean.setName(input);
                    updateItemData(fixBean);
                    break;
                case MyInputDialog.TypeCancle:
                    break;
            }
        }
    };

    TopActionBarView.ClickListener actionListener = new TopActionBarView.ClickListener() {
        @Override
        public void close() {
            finish();
        }

        @Override
        public void setting() {
        }
    };
}

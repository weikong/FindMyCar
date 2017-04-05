/* Copyright 2014 Sheldon Neilson www.neilson.co.za
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.king.smart.car.mysmartcarapp.ui.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.Alarm;
import com.king.smart.car.mysmartcarapp.db.helper.AlarmDBHelper;
import com.king.smart.car.mysmartcarapp.ui.alarm.preferences.AlarmPreferencesActivity;
import com.king.smart.car.mysmartcarapp.ui.view.TopActionBarView;

import java.util.List;

public class AlarmActivity extends AlarmBaseActivity {

    private ImageView ivAddAlarm;
    private ListView mathAlarmListView;
    private AlarmListAdapter alarmListAdapter;
    private TopActionBarView actionBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm_activity);
        initActionBar();
        ivAddAlarm = (ImageView) findViewById(R.id.iv_add_alarm);
        ivAddAlarm.setOnClickListener(this);
        mathAlarmListView = (ListView) findViewById(android.R.id.list);
        mathAlarmListView.setLongClickable(true);
        mathAlarmListView
                .setOnItemLongClickListener(new OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView,
                                                   View view, int position, long id) {
                        return true;
                    }
                });

        callMathAlarmScheduleService();

        alarmListAdapter = new AlarmListAdapter(this);
        this.mathAlarmListView.setAdapter(alarmListAdapter);
        mathAlarmListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position,
                                    long id) {
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Alarm alarm = (Alarm) alarmListAdapter.getItem(position);
                Intent intent = new Intent(AlarmActivity.this,
                        AlarmPreferencesActivity.class);
                intent.putExtra("alarm", alarm);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.findItem(R.id.menu_item_save).setVisible(false);
        menu.findItem(R.id.menu_item_delete).setVisible(false);
        return result;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAlarmList();
    }

    private void initActionBar() {
        actionBarView = (TopActionBarView) findViewById(R.id.view_top_action_bar);
        actionBarView.setTvActionTitleText(getString(R.string.item_tool_alarm));
        actionBarView.setTvActionRightText(getString(R.string.action_edit));
        actionBarView.setClickListener(actionListener);
    }

    public void updateAlarmList() {
        final List<Alarm> alarms = AlarmDBHelper.getAll();
        alarmListAdapter.setMathAlarms(alarms);

        runOnUiThread(new Runnable() {
            public void run() {
                // reload content
                AlarmActivity.this.alarmListAdapter.notifyDataSetChanged();
                if (alarms.size() > 0) {
                    findViewById(android.R.id.empty).setVisibility(
                            View.INVISIBLE);
                } else {
                    findViewById(android.R.id.empty)
                            .setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox_alarm_active:
                updateCheckAlarm(v);
                break;
            case R.id.iv_add_alarm:
                Intent newAlarmIntent = new Intent(this,
                        AlarmPreferencesActivity.class);
                startActivity(newAlarmIntent);
                break;
        }
    }

    private void updateCheckAlarm(View v) {
        if (v instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) v;
            Alarm alarm = (Alarm) alarmListAdapter.getItem((Integer) checkBox
                    .getTag());
            alarm.setAlarmActive(checkBox.isChecked());
            AlarmDBHelper.update(alarm);
            AlarmActivity.this.callMathAlarmScheduleService();
            if (checkBox.isChecked()) {
                Toast.makeText(AlarmActivity.this,
                        alarm.getTimeUntilNextAlarmMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

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
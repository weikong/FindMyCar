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
package com.king.smart.car.mysmartcarapp.ui.alarm.preferences;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.Alarm;
import com.king.smart.car.mysmartcarapp.manager.AlarmUtil;
import com.king.smart.car.mysmartcarapp.manager.DisplayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AlarmPreferenceListAdapter extends BaseAdapter implements Serializable {

    private Context context;
    private Alarm alarm;
    private List<AlarmPreference> preferences = new ArrayList<AlarmPreference>();
    private final String[] repeatDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private final String[] alarmDifficulties = {"Easy", "Medium", "Hard"};

//    private String[] alarmTones;
//    private String[] alarmTonePaths;

    public AlarmPreferenceListAdapter(Context context, final Alarm alarm) {
        setContext(context);
//        RingtoneManager ringtoneMgr = new RingtoneManager(getContext());
//        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
//        Cursor alarmsCursor = ringtoneMgr.getCursor();
//        AlarmUtil.alarmTones = new String[alarmsCursor.getCount() + 1];
//        AlarmUtil.alarmTones[0] = "Silent";
//        AlarmUtil.alarmTonePaths = new String[alarmsCursor.getCount() + 1];
//        AlarmUtil.alarmTonePaths[0] = "";
//        if (alarmsCursor.moveToFirst()) {
//            do {
//                AlarmUtil.alarmTones[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtone(alarmsCursor.getPosition()).getTitle(getContext());
//                AlarmUtil.alarmTonePaths[alarmsCursor.getPosition() + 1] = ringtoneMgr.getRingtoneUri(alarmsCursor.getPosition()).toString();
//            } while (alarmsCursor.moveToNext());
//        }
//        alarmsCursor.close();
        setMathAlarm(alarm);
    }

    @Override
    public int getCount() {
        return preferences.size();
    }

    @Override
    public Object getItem(int position) {
        return preferences.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressWarnings("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlarmPreference alarmPreference = (AlarmPreference) getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        ViewHolder viewHolder = null;
        switch (alarmPreference.getType()) {
            case BOOLEAN:
//                if (null == convertView || (convertView.getId() != R.id.item_alarm_bool)){
//                    convertView = layoutInflater.inflate(R.layout.item_alarm_bool, null);
//                    viewHolder = new ViewHolder(convertView,true);
//                    convertView.setTag(viewHolder);
//                }else {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                }
//                viewHolder.tvTitle.setText(alarmPreference.getTitle());
//                viewHolder.checkedTextView.setChecked((Boolean) alarmPreference.getValue());

                if (null == convertView || (convertView.getId() != android.R.layout.simple_list_item_checked))
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_checked, null);
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(60)));
                CheckedTextView checkedTextView = (CheckedTextView) convertView.findViewById(android.R.id.text1);
                checkedTextView.setCheckMarkDrawable(R.drawable.btn_check_alarm_selector);
                checkedTextView.setText(alarmPreference.getTitle());
                checkedTextView.setChecked((Boolean) alarmPreference.getValue());
                break;
            case INTEGER:
            case STRING:
            case LIST:
            case MULTIPLE_LIST:
            case TIME:
            default:
//                if (null == convertView || (convertView.getId() != R.id.item_alarm_text)){
//                    convertView = layoutInflater.inflate(R.layout.item_alarm_text, null);
//                    viewHolder = new ViewHolder(convertView);
//                    convertView.setTag(viewHolder);
//                }else {
//                    viewHolder = (ViewHolder) convertView.getTag();
//                }
//                viewHolder.tvTitle.setText(alarmPreference.getTitle());
//                viewHolder.tvDesc.setText(alarmPreference.getSummary());
                if (null == convertView || convertView.getId() != android.R.layout.simple_list_item_2)
                    convertView = layoutInflater.inflate(android.R.layout.simple_list_item_2, null);
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(60)));
                TextView text1 = (TextView) convertView.findViewById(android.R.id.text1);
                text1.setTextSize(18);
                text1.setText(alarmPreference.getTitle());

                TextView text2 = (TextView) convertView.findViewById(android.R.id.text2);
                text2.setText(alarmPreference.getSummary());
                break;
        }

        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        TextView tvDesc;
        ImageView ivCheck;

        public ViewHolder(View view, boolean type) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            ivCheck = (ImageView) view.findViewById(R.id.iv_check);
        }

        public ViewHolder(View view) {
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
        }
    }

    public Alarm getMathAlarm() {
        for (AlarmPreference preference : preferences) {
            switch (preference.getKey()) {
                case ALARM_ACTIVE:
                    alarm.setAlarmActive((Boolean) preference.getValue());
                    break;
                case ALARM_NAME:
                    alarm.setAlarmName((String) preference.getValue());
                    break;
                case ALARM_TIME:
                    alarm.setAlarmTime((String) preference.getValue());
                    break;
                case ALARM_DIFFICULTY:
                    alarm.setDifficulty(Alarm.Difficulty.valueOf((String) preference.getValue()));
                    break;
                case ALARM_TONE:
                    alarm.setAlarmTonePath((String) preference.getValue());
                    break;
                case ALARM_VIBRATE:
                    alarm.setVibrate((Boolean) preference.getValue());
                    break;
                case ALARM_REPEAT:
                    alarm.setDays((Alarm.Day[]) preference.getValue());
                    break;
            }
        }

        return alarm;
    }

    public void setMathAlarm(Alarm alarm) {
        this.alarm = alarm;
        preferences.clear();
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_ACTIVE, "Active", null, null, alarm.getAlarmActive(), AlarmPreference.Type.BOOLEAN));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_NAME, "Label", alarm.getAlarmName(), null, alarm.getAlarmName(), AlarmPreference.Type.STRING));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TIME, "Set time", alarm.getAlarmTimeString(), null, alarm.getAlarmTime(), AlarmPreference.Type.TIME));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_REPEAT, "Repeat", alarm.getRepeatDaysString(), repeatDays, alarm.getDays(), AlarmPreference.Type.MULTIPLE_LIST));
        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_DIFFICULTY, "Difficulty", alarm.getDifficulty().toString(), alarmDifficulties, alarm.getDifficulty(), AlarmPreference.Type.LIST));

        Uri alarmToneUri = Uri.parse(alarm.getAlarmTonePath());
        Ringtone alarmTone = RingtoneManager.getRingtone(getContext(), alarmToneUri);

        if (alarmTone instanceof Ringtone && !alarm.getAlarmTonePath().equalsIgnoreCase("")) {
            preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TONE, "Ringtone", alarmTone.getTitle(getContext()), AlarmUtil.alarmTones, alarm.getAlarmTonePath(), AlarmPreference.Type.LIST));
        } else {
            preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_TONE, "Ringtone", getAlarmTones()[0], AlarmUtil.alarmTones, null, AlarmPreference.Type.LIST));
        }

        preferences.add(new AlarmPreference(AlarmPreference.Key.ALARM_VIBRATE, "Vibrate", null, null, alarm.getVibrate(), AlarmPreference.Type.BOOLEAN));
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String[] getRepeatDays() {
        return repeatDays;
    }

    public String[] getAlarmDifficulties() {
        return alarmDifficulties;
    }

    public String[] getAlarmTones() {
        return AlarmUtil.alarmTones;
    }

    public String[] getAlarmTonePaths() {
        return AlarmUtil.alarmTonePaths;
    }

}

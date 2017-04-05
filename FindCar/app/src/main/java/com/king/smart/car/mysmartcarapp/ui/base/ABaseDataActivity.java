package com.king.smart.car.mysmartcarapp.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.king.smart.car.mysmartcarapp.ui.alarm.service.AlarmServiceBroadcastReciever;

public class ABaseDataActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void callMathAlarmScheduleService() {
        Intent mathAlarmServiceIntent = new Intent(this,
                AlarmServiceBroadcastReciever.class);
        sendBroadcast(mathAlarmServiceIntent, null);
    }
}

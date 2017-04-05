package com.king.smart.car.mysmartcarapp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.manager.AlarmUtil;
import com.king.smart.car.mysmartcarapp.manager.BluetoothManager;
import com.king.smart.car.mysmartcarapp.manager.DisplayUtil;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.manager.MediaUtil;
import com.king.smart.car.mysmartcarapp.manager.NetWorkUtil;
import com.king.smart.car.mysmartcarapp.manager.OkHttpClientManager;
import com.king.smart.car.mysmartcarapp.manager.ToastUtil;
import com.king.smart.car.mysmartcarapp.manager.WeatherUtil;
import com.king.smart.car.mysmartcarapp.ui.base.ABaseUIActivity;
import com.squareup.okhttp.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class WelActivity extends ABaseUIActivity {

    private long startTime = 0;
    private Timer timer;
    private TimerTask timerTask;
    public static boolean isJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wel);
        isJump = false;
        startTime = System.currentTimeMillis();
        startTimer();
        DisplayUtil.displayScreen(this);
        if (MediaUtil.mediaEntityList == null)
            MediaUtil.mediaEntityList = MediaUtil.getAllMediaList(WelActivity.this, null);
        if (BluetoothManager.isBluetoothSupported() && !BluetoothManager.isBluetoothEnabled())
            BluetoothManager.turnOnBluetooth();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (AlarmUtil.alarmTones == null || AlarmUtil.alarmTonePaths == null)
                    AlarmUtil.queryAlarmTones();
            }
        }).start();
        httpWeatherTask();
        httpLocalTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        closeTimer();
        super.onDestroy();
    }

    private void startTimer() {
        closeTimer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - startTime > 8 * 1000) {
                    if (!isJump) {
                        isJump = true;
                        handler.sendEmptyMessage(2);
                    }
                }
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 200);
    }

    private void closeTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void httpWeatherTask() {
        if (!NetWorkUtil.haveNetWork(this)) {
            ToastUtil.show("请检查网络");
            handler.sendEmptyMessage(1);
            return;
        }
        OkHttpClientManager.getInstance()._getAsyn(WeatherUtil.getWeatherUrl("四川", "成都"), new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response) {
                if (TextUtils.isEmpty(response)) {
                    handler.sendEmptyMessage(1);
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String msg = jsonObject.optString("msg");
                    String retCode = jsonObject.optString("retCode");
                    if (!TextUtils.isEmpty(retCode) && retCode.equals("200")) {
                        JSONArray jsonArray = jsonObject.optJSONArray("result");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            JSONObject jsonData = jsonArray.optJSONObject(0);
                            WeatherUtil.weatherData = jsonData;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    handler.sendEmptyMessage(1);
                }
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (isJump)
                        return;
                    long time = System.currentTimeMillis() - startTime;
                    if (time < 2000)
                        this.sendEmptyMessageDelayed(2, 2000 - time);
                    else
                        this.sendEmptyMessage(2);
                    isJump = true;
                    break;
                case 2:
                    JumpFinish();
                    break;
            }
        }
    };

    private void JumpFinish() {
        IntentActivity(WelActivity.this, SmartCarActivity.class);
        finish();
    }

    private void httpLocalTask(){
        OkHttpClientManager.getInstance()._getAsyn("http://172.16.0.138/MyMethod", new OkHttpClientManager.StringCallback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e("httpLocalTask","onFailure");
            }

            @Override
            public void onResponse(String response) {
                Logger.e("httpLocalTask","response = "+response);
            }
        });
    }

}

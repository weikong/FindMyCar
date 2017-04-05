package com.king.smart.car.mysmartcarapp.ui.run;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.king.smart.car.mysmartcarapp.R;
import com.king.smart.car.mysmartcarapp.bean.CoordinateBean;
import com.king.smart.car.mysmartcarapp.manager.LocationUtil;
import com.king.smart.car.mysmartcarapp.manager.Logger;
import com.king.smart.car.mysmartcarapp.manager.TimeFormatUtils;
import com.king.smart.car.mysmartcarapp.ui.base.ABaseUIActivity;
import com.king.smart.car.mysmartcarapp.ui.run.view.CircleAnimView;
import com.king.smart.car.mysmartcarapp.ui.run.view.RunTrainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class RunTrainActivity extends ABaseUIActivity {
    private RunTrainView runTrainView;
    private LocationUtil locationUtil;
    private CircleAnimView circleAnimView;

    private List<CoordinateBean> listLatLon = new ArrayList<>();
    private double mAllDistance = 0;
    private Timer mTimer = null;
    private TimerTask timerTask = null;
    private long startLocationTime = 0;

    private int mStartCount = 4;
    private final int TYPE_RUN_PAUSE = 1;
    private final int TYPE_RUN_KEEP = 2;
    private final int TYPE_RUN_OVER = 3;
    private final int TYPE_RUN_NOMAL = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_train);
        runTrainView = (RunTrainView) findViewById(R.id.view_run_train);
        circleAnimView = (CircleAnimView) findViewById(R.id.view_circle_anim);
        circleAnimView.setClickListener(clickListener);
        locationUtil = new LocationUtil(true);
        locationUtil.setReceiveListener(receiveListener);
        locationUtil.startLocationClient(this);
        startTimer();
        calculateTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        stopTimer();
        if (locationUtil != null)
            locationUtil.destoryLocationClient();
        super.onDestroy();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x2000:
                    circleAnimView.setTvRText("暂停");
                    runTrainView.setTvTimeText("" + TimeFormatUtils.formatDurationHHMMss(System.currentTimeMillis() - startLocationTime));
                    break;
                case 0x2001:
                    Logger.e("RunTrainActivity", "TimeFormatUtils distance = " + TimeFormatUtils.formatDistance(mAllDistance));
                    runTrainView.setTvDistanceText("" + TimeFormatUtils.formatDistance(mAllDistance));
                    break;
                case 0x2002:
                    runTrainView.setTvHeatText("" + msg.arg1);
                    break;
                case 0x2003:
                    if (mStartCount > 0)
                        circleAnimView.setTvRText("" + mStartCount);
                    else if (mStartCount == 0){
                        circleAnimView.setTvRText("GO");
                        startLocationTime = System.currentTimeMillis();
                    }
                    break;

            }
        }
    };

    LocationUtil.ReceiveListener receiveListener = new LocationUtil.ReceiveListener() {
        @Override
        public void setLocation(BDLocation location) {
            collectCoordinate(location);
        }
    };

    private void collectCoordinate(BDLocation location) {
        CoordinateBean bean = new CoordinateBean();
        if (location != null) {
            bean.setState(1).setLat(location.getLatitude()).setLon(location.getLongitude()).setType(location.getLocType());
        } else {
            bean.setState(0);
        }
        listLatLon.add(bean);
        if (listLatLon.size() > 1) {
            isCalculate = true;
        }
    }

    private double calculateDistance(CoordinateBean c1, CoordinateBean c2) {
        double distance = 0;
        if (listLatLon.size() <= 1 || c1.getState() == 0 || c2.getState() == 0)
            return distance;
        LatLng ll1 = new LatLng(c1.getLat(), c1.getLon());
        LatLng ll2 = new LatLng(c2.getLat(), c2.getLon());
        distance = DistanceUtil.getDistance(ll1, ll2);
        distance = TimeFormatUtils.retainOne(distance);
        Logger.e("RunTrainActivity", "distance = " + distance + "  type = " + c1.getType()+"  lat = "+c1.getLat()+"  lon = "+c1.getLon());
        Message msg = Message.obtain();
        msg.what = 0x2002;
        msg.arg1 = c1.getType();
        handler.sendMessage(msg);
        return distance;
    }

    private boolean isCalculate = false;

    private void calculateTask() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isCalculate) {
                        isCalculate = false;
                        CoordinateBean lastBean = listLatLon.get(listLatLon.size() - 1);
                        CoordinateBean lastPreBean = listLatLon.get(listLatLon.size() - 2);
                        if (lastBean == null || lastBean.getLat() == 0){
                            listLatLon.remove(lastBean);
                            return;
                        }
                        if (lastPreBean == null|| lastPreBean.getLat() == 0){
                            listLatLon.remove(lastPreBean);
                            return;
                        }
                        mAllDistance += calculateDistance(lastBean, lastPreBean);
                        if (mAllDistance > 8){
                            listLatLon.remove(lastBean);
                            return;
                        }
                        handler.sendEmptyMessage(0x2001);
                    }
                }
            }
        }).start();
    }

    private void startTimer() {
        stopTimer();
        mTimer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (mStartCount > 0) {
                    mStartCount--;
                    handler.sendEmptyMessage(0x2003);
                    return;
                }
                handler.sendEmptyMessage(0x2000);
            }
        };
        mTimer.schedule(timerTask, 100, 1000);
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    CircleAnimView.ClickListener clickListener = new CircleAnimView.ClickListener() {
        @Override
        public void doAction(int action) {

        }
    };
}

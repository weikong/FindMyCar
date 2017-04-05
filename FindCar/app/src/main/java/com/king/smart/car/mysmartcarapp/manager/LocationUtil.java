package com.king.smart.car.mysmartcarapp.manager;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by xinzhendi-031 on 2016/11/9.
 */
public class LocationUtil {
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private boolean isNever = false;

    public LocationUtil(boolean isNever) {
        this.isNever = isNever;
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location != null && listener != null)
                listener.setLocation(location);
            if (!isNever)
                stopLocationClient();
        }
    }

    public void startLocationClient(Context context) {
        // 定位初始化
        if (mLocClient == null) {
            mLocClient = new LocationClient(context);
            mLocClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1001);
            option.setIsNeedAddress(true);
//            option.setAddrType("all");
            option.setIsNeedLocationDescribe(true);
            option.setLocationNotify(true);
            option.setNeedDeviceDirect(true);
            mLocClient.setLocOption(option);
        }
        mLocClient.start();
    }

    public void stopLocationClient() {
        // 退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
    }

    public void destoryLocationClient() {
        // 退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
        mLocClient = null;
    }

    ReceiveListener listener;

    public void setReceiveListener(ReceiveListener listener) {
        this.listener = listener;
    }

    public interface ReceiveListener {
        public void setLocation(BDLocation location);
    }
}

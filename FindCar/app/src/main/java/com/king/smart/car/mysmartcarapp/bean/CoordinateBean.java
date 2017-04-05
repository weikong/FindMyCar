package com.king.smart.car.mysmartcarapp.bean;

/**
 * Created by xinzhendi-031 on 2016/12/5.
 */
public class CoordinateBean {

    private Double lat;
    private Double lon;

    /**
     * 1:GPS;
     * 2:WIFI
     * */
    private int type;

    /**
     * 0:定位失败；
     * 1:定位成功；
     * */
    private int state;

    public Double getLat() {
        return lat;
    }

    public CoordinateBean setLat(Double lat) {
        this.lat = lat;
        return this;
    }

    public Double getLon() {
        return lon;
    }

    public CoordinateBean setLon(Double lon) {
        this.lon = lon;
        return this;
    }

    public int getType() {
        return type;
    }

    public CoordinateBean setType(int type) {
        this.type = type;
        return this;
    }

    public int getState() {
        return state;
    }

    public CoordinateBean setState(int state) {
        this.state = state;
        return this;
    }
}

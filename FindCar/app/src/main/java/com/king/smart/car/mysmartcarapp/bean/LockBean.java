package com.king.smart.car.mysmartcarapp.bean;

import java.io.Serializable;

/**
 * Created by hugo on 2015/9/30 0030.
 */
public class LockBean implements Serializable {

    public int mID;
    public String mAddress;
    public String mNearBy;
    public String mTime;
    public double mLat;
    public double mLon;
    public boolean isCheck;

    public int getmID() {
        return mID;
    }

    public LockBean setmID(int mID) {
        this.mID = mID;
        return this;
    }

    public String getmAddress() {
        return mAddress;
    }

    public LockBean setmAddress(String mAddress) {
        this.mAddress = mAddress;
        return this;
    }

    public String getmNearBy() {
        return mNearBy;
    }

    public LockBean setmNearBy(String mNearBy) {
        this.mNearBy = mNearBy;
        return this;
    }

    public String getmTime() {
        return mTime;
    }

    public LockBean setmTime(String mTime) {
        this.mTime = mTime;
        return this;
    }

    public double getmLat() {
        return mLat;
    }

    public LockBean setmLat(double mLat) {
        this.mLat = mLat;
        return this;
    }

    public double getmLon() {
        return mLon;
    }

    public LockBean setmLon(double mLon) {
        this.mLon = mLon;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public LockBean setCheck(boolean check) {
        isCheck = check;
        return this;
    }
}

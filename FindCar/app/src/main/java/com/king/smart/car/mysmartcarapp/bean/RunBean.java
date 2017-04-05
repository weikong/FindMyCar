package com.king.smart.car.mysmartcarapp.bean;

import java.io.Serializable;

/**
 * Created by hugo on 2015/9/30 0030.
 */
public class RunBean implements Serializable {

    public int mID;
    public double mLat;
    public double mLon;
    public String mAddress;
    public String mNearBy;
    public String mRunContinueDays;
    public String mRunDate;
    public String mRunDistance;
    public String mRunSpeed;
    public String mUseTime;
    public String mRunHeat;
    public String mRunCoordinateList;
    public boolean isCheck;

    public int getmID() {
        return mID;
    }

    public RunBean setmID(int mID) {
        this.mID = mID;
        return this;
    }

    public double getmLat() {
        return mLat;
    }

    public RunBean setmLat(double mLat) {
        this.mLat = mLat;
        return this;
    }

    public double getmLon() {
        return mLon;
    }

    public RunBean setmLon(double mLon) {
        this.mLon = mLon;
        return this;
    }

    public String getmAddress() {
        return mAddress;
    }

    public RunBean setmAddress(String mAddress) {
        this.mAddress = mAddress;
        return this;
    }

    public String getmNearBy() {
        return mNearBy;
    }

    public RunBean setmNearBy(String mNearBy) {
        this.mNearBy = mNearBy;
        return this;
    }

    public String getmRunContinueDays() {
        return mRunContinueDays;
    }

    public RunBean setmRunContinueDays(String mRunContinueDays) {
        this.mRunContinueDays = mRunContinueDays;
        return this;
    }

    public String getmRunDate() {
        return mRunDate;
    }

    public RunBean setmRunDate(String mRunDate) {
        this.mRunDate = mRunDate;
        return this;
    }

    public String getmRunDistance() {
        return mRunDistance;
    }

    public RunBean setmRunDistance(String mRunDistance) {
        this.mRunDistance = mRunDistance;
        return this;
    }

    public String getmRunSpeed() {
        return mRunSpeed;
    }

    public RunBean setmRunSpeed(String mRunSpeed) {
        this.mRunSpeed = mRunSpeed;
        return this;
    }

    public String getmUseTime() {
        return mUseTime;
    }

    public RunBean setmUseTime(String mUseTime) {
        this.mUseTime = mUseTime;
        return this;
    }

    public String getmRunHeat() {
        return mRunHeat;
    }

    public RunBean setmRunHeat(String mRunHeat) {
        this.mRunHeat = mRunHeat;
        return this;
    }

    public String getmRunCoordinateList() {
        return mRunCoordinateList;
    }

    public RunBean setmRunCoordinateList(String mRunCoordinateList) {
        this.mRunCoordinateList = mRunCoordinateList;
        return this;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public RunBean setCheck(boolean check) {
        isCheck = check;
        return this;
    }
}

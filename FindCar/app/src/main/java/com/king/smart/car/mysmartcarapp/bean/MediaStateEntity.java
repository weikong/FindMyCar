package com.king.smart.car.mysmartcarapp.bean;

import java.io.Serializable;

/**
 * Created by xinzhendi-031 on 2016/11/16.
 */
public class MediaStateEntity implements Serializable {

    public int id; //id标识
    public int duration; // 媒体播放总时间
    public int currentTime;// 媒体播放时间
    public int selectIndex;
    public int loopType;
    public int statePlay;

    public int getId() {
        return id;
    }

    public MediaStateEntity setId(int id) {
        this.id = id;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public MediaStateEntity setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public MediaStateEntity setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public MediaStateEntity setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        return this;
    }

    public int getLoopType() {
        return loopType;
    }

    public MediaStateEntity setLoopType(int loopType) {
        this.loopType = loopType;
        return this;
    }

    public int getStatePlay() {
        return statePlay;
    }

    public MediaStateEntity setStatePlay(int statePlay) {
        this.statePlay = statePlay;
        return this;
    }
}
package com.king.smart.car.mysmartcarapp.bean;

public class RecordAudioBean {
    private int _id;
    private String name, time, duration, path;
    private boolean isShowDate = false;

    public int get_id() {
        return _id;
    }

    public RecordAudioBean set_id(int _id) {
        this._id = _id;
        return this;
    }

    public String getName() {
        return name;
    }

    public RecordAudioBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getTime() {
        return time;
    }

    public RecordAudioBean setTime(String time) {
        this.time = time;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public RecordAudioBean setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getPath() {
        return path;
    }

    public RecordAudioBean setPath(String path) {
        this.path = path;
        return this;
    }

    public boolean isShowDate() {
        return isShowDate;
    }

    public RecordAudioBean setShowDate(boolean showDate) {
        isShowDate = showDate;
        return this;
    }
}

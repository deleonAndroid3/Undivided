package com.training.android.undivided.CallLog;

import java.util.Date;

/**
 * Created by Maouusama on 12/18/2017.
 */

public class Log {

    String num;
    String type;
    String date;
    Date time;
    String duration;

    public Log(String num, String type, String date, Date time, String duration) {
        this.num = num;
        this.type = type;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}

package com.training.android.undivided;


public class TimeTicker {
    public TimeTicker(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    private int milliseconds = 0;

    public String formatHours() {
        return String.format("%02d", hours());
    }

    public String formatMinutes() {
        return String.format("%02d", minutes());
    }

    public String formatSeconds() {
        return String.format("%02d", seconds());
    }

    public int hours() {
        return (this.milliseconds / (1000*60*60)) % 24;
    }

    public int minutes() {
        return (this.milliseconds / (1000*60)) % 60;
    }

    public int seconds() {
        return (this.milliseconds / 1000) % 60 ;
    }
}

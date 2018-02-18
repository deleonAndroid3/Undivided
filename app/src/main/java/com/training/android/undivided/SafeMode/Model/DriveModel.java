package com.training.android.undivided.SafeMode.Model;

/**
 * Created by Maouusama on 2/18/2018.
 */

public class DriveModel {
    private String driveType;
    private String start_time;
    private String end_time;

    public DriveModel() {
    }

    public DriveModel(String driveType, String start_time, String end_time) {
        this.driveType = driveType;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}

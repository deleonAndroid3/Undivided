package com.training.android.undivided.SmsGroup.Interface;

import com.training.android.undivided.SmsGroup.Model.SmsTask;

import java.util.List;

/**
 * Created by Hillary Briones on 2/7/2018.
 */

public interface ISmsStatusListener {
    void onStatusChange(List<SmsTask> task);
}

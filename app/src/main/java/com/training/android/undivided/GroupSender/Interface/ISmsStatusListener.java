package com.training.android.undivided.GroupSender.Interface;

import com.training.android.undivided.GroupSender.Model.SmsTask;

import java.util.List;

/**
 * Created by Hillary Briones on 11/1/2017.
 */

public interface ISmsStatusListener {
    void onStatusChange(List<SmsTask> task);
}

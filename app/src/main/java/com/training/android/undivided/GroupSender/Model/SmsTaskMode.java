package com.training.android.undivided.GroupSender.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Hillary Briones on 9/15/2017.
 */

public enum SmsTaskMode {
    @SerializedName("ALL_TO_ALL")
    ALL_TO_ALL,

    @SerializedName("ONE_TO_ONE")
    ONE_TO_ONE
}

package com.training.android.undivided.GroupSender.Model;

/**
 * Created by Hillary Briones on 9/15/2017.
 */

public enum MessageStatus {
    SMS_DELIVERED,
    SMS_CANCELED,
    SMS_SENT,
    SMS_FAILURE,
    SMS_ERROR_SERVICE,
    SMS_ERROR_NULL_PDU,
    SMS_TIMEOUT,
    IDLE, PENDING,
    SMS_ERROR_RADIO_OFF
}

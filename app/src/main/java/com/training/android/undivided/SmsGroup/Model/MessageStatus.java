package com.training.android.undivided.SmsGroup.Model;

/**
 * Created by Hillary Briones on 2/7/2018.
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

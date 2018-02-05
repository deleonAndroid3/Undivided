package com.training.android.undivided.GroupSender.DB.message;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.training.android.undivided.GroupSender.Objects.Message;

import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.PHONE_NUMBERS;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.SMS;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.TITLE;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.UUID;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageCursorWrapper extends CursorWrapper {

    public MessageCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Message getMessage() {
        return new Message
                .Builder(java.util.UUID.fromString(getString(getColumnIndex(UUID))))
                .setTitle(getString(getColumnIndex(TITLE)))
                .setSms(getString(getColumnIndex(SMS)))
                .setPhoneNumbers(getString(getColumnIndex(PHONE_NUMBERS)))
                .build();
    }
}

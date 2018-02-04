package com.training.android.undivided.GroupSender.DB.message;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.training.android.undivided.GroupSender.App;
import com.training.android.undivided.GroupSender.DB.MessageDbSchema;
import com.training.android.undivided.GroupSender.Objects.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.DATE;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.PHONE_NUMBERS;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.SMS;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.TIME;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.TITLE;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageManager {
    private SQLiteDatabase mDatabase;

    private MessageManager() {
        mDatabase = new MessageBaseHelper(App.getInstance().getApplicationContext())
                .getWritableDatabase();
    }

    public static MessageManager getInstance() {
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final MessageManager sInstance = new MessageManager();
    }

    public void addMessage(Message message) {
        ContentValues values = getContentValues(message);
        mDatabase.insert(MessageDbSchema.MessageTable.NAME, null, values);
    }

    public void delMessage(Message message) {
        String uuid = message.getId().toString();
        mDatabase.delete(MessageDbSchema.MessageTable.NAME, MessageDbSchema.MessageTable.Cols.UUID + " = ?", new String[]{uuid});
    }

    public void updateMessage(Message message) {
        String uuid = message.getId().toString();
        ContentValues values = getContentValues(message);
        mDatabase.update(MessageDbSchema.MessageTable.NAME, values, MessageDbSchema.MessageTable.Cols.UUID + " = ?", new String[]{uuid});
    }

    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        MessageCursorWrapper cursor = queryMessages();

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                messages.add(cursor.getMessage());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return messages;
    }

    public Message getMessage(UUID id) {
        MessageCursorWrapper cursor = queryMessages(MessageDbSchema.MessageTable.Cols.UUID + " = ?",
                new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getMessage();
        } finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Message message) {
        ContentValues values = new ContentValues();
        values.put(MessageDbSchema.MessageTable.Cols.UUID, message.getId().toString());
        values.put(DATE, message.getDate());
        values.put(TIME, message.getTime());
        values.put(TITLE, message.getTitle());
        values.put(SMS, message.getSms());
        values.put(PHONE_NUMBERS, message.getPhoneNumbers());
        return values;
    }

    private MessageCursorWrapper queryMessages(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                MessageDbSchema.MessageTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new MessageCursorWrapper(cursor);
    }

    private MessageCursorWrapper queryMessages() {
        Cursor cursor = mDatabase.rawQuery("select * from " + MessageDbSchema.MessageTable.NAME, null);
        return new MessageCursorWrapper(cursor);
    }
}

package com.training.android.undivided.GroupSender.DB.message;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.training.android.undivided.GroupSender.DB.MessageDbSchema;
import com.training.android.undivided.GroupSender.Utils.SQL;

import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.Cols.*;
import static com.training.android.undivided.GroupSender.DB.MessageDbSchema.MessageTable.NAME;

/**
 * Created by Hillary Briones on 1/28/2018.
 */

public class MessageBaseHelper extends SQLiteOpenHelper {

    public MessageBaseHelper(Context context) {
        super(context, MessageDbSchema.DATABASE_NAME, null, MessageDbSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL.createTable(NAME)
                .addIntegerColsWithPrimaryKey("_id", true)
                .addTextCols(UUID)
                .addTextCols(DATE)
                .addTextCols(TIME)
                .addTextCols(TITLE)
                .addTextCols(SMS)
                .addTextCols(PHONE_NUMBERS)
                .create());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

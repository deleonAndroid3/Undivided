package com.training.android.undivided.Group.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.Group.Model.GroupModel;

public class DBHandler extends SQLiteOpenHelper {

    public static final String TABLE_CREATE_GROUP = "groups";
    public static final String COLUMN_GROUPID = "groupid";
    public static final String COLUMN_GROUPNAME = "groupname";
    public static final String COLUMN_GROUPDESC = "groupdesc";
    public static final String COLUMN_GROUPMESSAGE = "groupmessage";
    public static final String COLUMN_DECLINECALL = "declinecall";
    public static final String COLUMN_AUTOREPLYSMS = "autoreplysms";
    public static final String COLUMN_AUTOREPYCALLS = "autoreplycalls";
    public static final String COLUMN_REPLYSMS = "replysms";
    public static final String COLUMN_READSMS = "readsms";
    public static final String COLUMN_VOICEMAIL = "voicemail";
    public static final String COLUMN_NOTIFYIFCALL = "notifyifcallrecieved";

    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_CONTACTID = "contactid";
    public static final String COLUMN_CONTACTNUM = "contactnum";
    public static final String COLUMN_CONTACTNAME = "contactname";
    public static final String FK_COLUMN_GROUPID = "group_id";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UndividedDB.db";

    String GroupQuery = "CREATE TABLE IF NOT EXISTS " +
            TABLE_CREATE_GROUP + " (" +
            COLUMN_GROUPID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_GROUPNAME + " TEXT," +
            COLUMN_GROUPDESC + " TEXT," +
            COLUMN_GROUPMESSAGE + " TEXT," +
            COLUMN_DECLINECALL + " INTEGER," +
            COLUMN_AUTOREPLYSMS + " INTEGER," +
            COLUMN_AUTOREPYCALLS + " INTEGER," +
            COLUMN_REPLYSMS + " INTEGER," +
            COLUMN_READSMS + " INTEGER," +
            COLUMN_VOICEMAIL + " INTEGER," +
            COLUMN_NOTIFYIFCALL + " INTEGER)";

    String ContactQuery = "CREATE TABLE IF NOT EXISTS " +
            TABLE_CONTACTS + "(" +
            COLUMN_CONTACTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CONTACTNUM + " TEXT, " +
            COLUMN_CONTACTNAME + " TEXT, " +
            FK_COLUMN_GROUPID + " INTEGER, " +
            "FOREIGN KEY (" + FK_COLUMN_GROUPID + ") REFERENCES " +
            TABLE_CREATE_GROUP + " (" + COLUMN_GROUPID + "));";

    SQLiteDatabase db;


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i("TAG", ContactQuery);
        Log.i("TAG", GroupQuery);

        sqLiteDatabase.execSQL(GroupQuery);
        sqLiteDatabase.execSQL(ContactQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_GROUP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(sqLiteDatabase);
    }

    public void addGroup(GroupModel groupModel) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GROUPNAME, groupModel.getGroupName());
        contentValues.put(COLUMN_GROUPDESC, groupModel.getGroupDesc());
        contentValues.put(COLUMN_GROUPMESSAGE, groupModel.getGroupMessage());
        contentValues.put(COLUMN_DECLINECALL, groupModel.getRule1());
        contentValues.put(COLUMN_AUTOREPLYSMS, groupModel.getRule2());
        contentValues.put(COLUMN_AUTOREPYCALLS, groupModel.getRule3());
        contentValues.put(COLUMN_REPLYSMS, groupModel.getRule4());
        contentValues.put(COLUMN_READSMS, groupModel.getRule5());
        contentValues.put(COLUMN_VOICEMAIL, groupModel.getRule6());
        contentValues.put(COLUMN_NOTIFYIFCALL, groupModel.getRule7());

        db.insert(TABLE_CREATE_GROUP, null, contentValues);
        db.close();
    }

    public void addContact(ContactsModel contactsModel, String name) {

        db.execSQL("INSERT INTO " + TABLE_CONTACTS +" VALUES (null,'" +
                contactsModel.getContactNumber() + "', '" +
                contactsModel.getContactName() + "', (SELECT " +
                COLUMN_GROUPID + " FROM " + TABLE_CREATE_GROUP +
                " WHERE " + COLUMN_GROUPNAME + " = '" + name + "'));"
        );

    }

}

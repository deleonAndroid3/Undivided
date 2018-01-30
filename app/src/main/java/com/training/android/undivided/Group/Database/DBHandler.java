package com.training.android.undivided.Group.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.Group.Model.GroupModel;

import java.util.ArrayList;

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

        db.execSQL("INSERT INTO " + TABLE_CONTACTS + " VALUES (null,'" +
                contactsModel.getContactNumber() + "', '" +
                contactsModel.getContactName() + "', (SELECT " +
                COLUMN_GROUPID + " FROM " + TABLE_CREATE_GROUP +
                " WHERE " + COLUMN_GROUPNAME + " = '" + name + "'));"
        );

    }

    public ArrayList<GroupModel> getAllGroups() {
        SQLiteDatabase rdb = getReadableDatabase();
        Cursor c = rdb.rawQuery("SELECT * FROM " + TABLE_CREATE_GROUP, null);
        ArrayList<GroupModel> list = new ArrayList<>();
        GroupModel gm;


        while (c.moveToNext()) {
            gm = new GroupModel();
            gm.setGroupName(c.getString(1));
            gm.setGroupDesc(c.getString(2));
            gm.setGroupMessage(c.getString(3));
            gm.setRule1(Integer.parseInt(c.getString(4)));
            gm.setRule2(Integer.parseInt(c.getString(5)));
            gm.setRule3(Integer.parseInt(c.getString(6)));
            gm.setRule4(Integer.parseInt(c.getString(7)));
            gm.setRule5(Integer.parseInt(c.getString(8)));
            gm.setRule6(Integer.parseInt(c.getString(9)));
            gm.setRule7(Integer.parseInt(c.getString(10)));

            list.add(gm);
        }

        c.close();
        rdb.close();

        return list;
    }

    public boolean EmergencyGroupExists() {

        Cursor cursor = null;

        try {
            String sql = "SELECT groupid FROM " + TABLE_CREATE_GROUP + " WHERE groupname = 'Emergency'";
            cursor = db.rawQuery(sql, null);

            return (cursor.getCount() > 0);
        }finally {
            if (cursor != null)
                cursor.close();
        }

    }

    public boolean numberExists(String Phonenum){

        Cursor cursor = null;

        try {
            String sql = "SELECT contactid FROM " + TABLE_CONTACTS + " WHERE contactnum = '" + Phonenum + "' AND NOT group_id = 1 " ;
            cursor = db.rawQuery(sql, null);

            return (cursor.getCount() > 0);
        }finally {
            if (cursor != null)
                cursor.close();
        }

    }

    public boolean GroupNameExists(String name){

        Cursor cursor = null;

        try {
            String sql = "SELECT groupid FROM " + TABLE_CREATE_GROUP + " WHERE groupname = '" + name + "'"  ;
            cursor = db.rawQuery(sql, null);

            return (cursor.getCount() > 0);
        }finally {
            if (cursor != null)
                cursor.close();
        }
    }

}

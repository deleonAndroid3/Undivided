package com.training.android.undivided.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.Group.Model.GroupModel;
import com.training.android.undivided.Models.TowingServicesModel;
import com.training.android.undivided.Models.TowingServicesModel;
import com.training.android.undivided.DriveHistory.Model.DriveModel;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    public static final String TABLE_CREATE_GROUP = "groups";
    public static final String COLUMN_GROUPID = "groupid";
    public static final String COLUMN_GROUPNAME = "groupname";
    public static final String COLUMN_GROUPDESC = "groupdesc";
    public static final String COLUMN_GROUPMESSAGE = "groupmessage";
    public static final String COLUMN_AUTOREPLYSMS = "autoreplysms";
    public static final String COLUMN_AUTOREPYCALLS = "autoreplycalls";
    public static final String COLUMN_READREPLYSMS = "readsms";
    public static final String COLUMN_NOTIFYIFCALL = "notifyifcallrecieved";

    public static final String DRIVE_HISTORY = "drivehistory";
    public static final String DRIVE_ID = "driveid";
    public static final String DRIVE_TYPE = "drivetype";
    public static final String DRIVE_START = "starttime";
    public static final String DRIVE_END = "endtime";

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
            COLUMN_AUTOREPLYSMS + " INTEGER," +
            COLUMN_AUTOREPYCALLS + " INTEGER," +
            COLUMN_READREPLYSMS + " INTEGER," +
            COLUMN_NOTIFYIFCALL + " INTEGER)";

    String ContactQuery = "CREATE TABLE IF NOT EXISTS " +
            TABLE_CONTACTS + "(" +
            COLUMN_CONTACTID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_CONTACTNUM + " TEXT, " +
            COLUMN_CONTACTNAME + " TEXT, " +
            FK_COLUMN_GROUPID + " INTEGER, " +
            "FOREIGN KEY (" + FK_COLUMN_GROUPID + ") REFERENCES " +
            TABLE_CREATE_GROUP + " (" + COLUMN_GROUPID + ")" +
            " ON DELETE CASCADE);";

    String TowingQuery = "CREATE TABLE IF NOT EXISTS towing (t_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "towing_name TEXT," +
            "towing_address TEXT," +
            "towing_latlong TEXT," +
            "towing_contact TEXT)";

    String EmergencyQuery = "CREATE TABLE IF NOT EXISTS emergency_contacts (t_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "emer_name TEXT," +
            "emer_address TEXT," +
            "emer_contact TEXT," +
            "emer_type TEXT)";

    String DriveQuery = "CREATE TABLE IF NOT EXISTS "+ DRIVE_HISTORY +
            " (" + DRIVE_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DRIVE_TYPE +" TEXT, " +
             DRIVE_START +" TEXT, " +
            DRIVE_END +" TEXT)";

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
        sqLiteDatabase.execSQL(TowingQuery);
        sqLiteDatabase.execSQL(EmergencyQuery);
        sqLiteDatabase.execSQL(DriveQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CREATE_GROUP);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS towing");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS emergency_contacts");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DRIVE_HISTORY);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.execSQL("PRAGMA foreign_keys = 1");
    }

    public void addDrive(DriveModel driveModel) {

        if(!db.isOpen())
            db = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DRIVE_TYPE, driveModel.getDriveType());
            contentValues.put(DRIVE_START, driveModel.getStart_time());
            contentValues.put(DRIVE_END, driveModel.getEnd_time());

            db.insert(DRIVE_HISTORY, null, contentValues);
            db.close();

    }

    public void addGroup(GroupModel groupModel) {

        if (!db.isOpen())
            db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_GROUPNAME, groupModel.getGroupName());
        contentValues.put(COLUMN_GROUPDESC, groupModel.getGroupDesc());
        contentValues.put(COLUMN_GROUPMESSAGE, groupModel.getGroupMessage());
        contentValues.put(COLUMN_AUTOREPLYSMS, groupModel.getRule1());
        contentValues.put(COLUMN_AUTOREPYCALLS, groupModel.getRule2());
        contentValues.put(COLUMN_READREPLYSMS, groupModel.getRule3());
        contentValues.put(COLUMN_NOTIFYIFCALL, groupModel.getRule4());

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

    public void AddTowingServices(TowingServicesModel tsm){

        if (!db.isOpen())
            db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("towing_name", tsm.getName());
        contentValues.put("towing_address", tsm.getAddress());
        contentValues.put("towing_latlong", tsm.getLatlng());
        contentValues.put("towing_contact", tsm.getContactNumber());

        db.insert("towing", null, contentValues);
        db.close();

    }

    public void deleteGroup(String name) {

        SQLiteDatabase dbd = getWritableDatabase();
        dbd.delete(TABLE_CREATE_GROUP, COLUMN_GROUPNAME + " = '" + name + "'", null);
        dbd.close();
    }

    public void DeleteContacts(String  num) {
        if (!db.isOpen())
            db = getWritableDatabase();

        db.delete(TABLE_CONTACTS, COLUMN_CONTACTNUM + " = '" + num + "' AND " + FK_COLUMN_GROUPID + " != 1", null);
        db.close();
    }

    public void DeleteContactsifEmergency(String num) {
        if (!db.isOpen())
            db = getWritableDatabase();

        db.delete(TABLE_CONTACTS, COLUMN_CONTACTNUM + " = '" + num + "' AND " + FK_COLUMN_GROUPID + " = 1", null);
        db.close();
    }

    public void UpdateGroup(String name, GroupModel gm) {

        if (!db.isOpen())
            db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROUPNAME, gm.getGroupName());
        cv.put(COLUMN_GROUPDESC, gm.getGroupDesc());
        cv.put(COLUMN_GROUPMESSAGE, gm.getGroupMessage());
        cv.put(COLUMN_AUTOREPLYSMS, gm.getRule1());
        cv.put(COLUMN_AUTOREPYCALLS, gm.getRule2());
        cv.put(COLUMN_READREPLYSMS, gm.getRule3());
        cv.put(COLUMN_NOTIFYIFCALL, gm.getRule4());

        db.update(TABLE_CREATE_GROUP, cv, COLUMN_GROUPNAME + " = '" + name + "'", null);

    }

    public ArrayList<DriveModel> getDriveHistory() {
        SQLiteDatabase rdb = getReadableDatabase();
        Cursor c = rdb.rawQuery("SELECT * FROM " + DRIVE_HISTORY, null);
        ArrayList<DriveModel> list = new ArrayList<>();
        DriveModel dm;


        while (c.moveToNext()) {
            dm = new DriveModel();
            dm.setDriveType(c.getString(1));
            dm.setStart_time(c.getString(2));
            dm.setEnd_time(c.getString(3));

            list.add(dm);
        }

        c.close();
        rdb.close();

        return list;
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

            list.add(gm);
        }

        c.close();
        rdb.close();

        return list;
    }

    public ArrayList<ContactsModel> getContactsofGroup(String name) {

        SQLiteDatabase rdb = getReadableDatabase();
        Cursor c = rdb.rawQuery("SELECT * FROM " + TABLE_CONTACTS + " WHERE " + FK_COLUMN_GROUPID
                + " IN (SELECT " + COLUMN_GROUPID + " FROM " + TABLE_CREATE_GROUP + " WHERE " + COLUMN_GROUPNAME + " = '" + name + "')", null);
        ArrayList<ContactsModel> cm = new ArrayList<>();
        ContactsModel cModel;

        while (c.moveToNext()) {

            cModel = new ContactsModel();
            cModel.setContactName(c.getString(2));
            cModel.setContactNumber(c.getString(1));

            cm.add(cModel);
        }

        c.close();
        rdb.close();

        return cm;
    }

    public ArrayList<ContactsModel> getEmergencyContacts() {
        if (!db.isOpen())
            db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + FK_COLUMN_GROUPID + " = 1";
        Cursor c = db.rawQuery(query, null);

        ArrayList<ContactsModel> cm = new ArrayList<>();
        ContactsModel cModel;

        while (c.moveToNext()) {

            cModel = new ContactsModel();
            cModel.setContactName(c.getString(2));
            cModel.setContactNumber(c.getString(1));

            cm.add(cModel);
        }
        c.close();
        db.close();

        return cm;
    }

    public ArrayList<TowingServicesModel> getServices(){

        if (!db.isOpen())
            db = getReadableDatabase();

        String query = "SELECT * FROM towing" ;
        Cursor c = db.rawQuery(query, null);

        ArrayList<TowingServicesModel> tsmList = new ArrayList<>();
        TowingServicesModel tsm;

        while (c.moveToNext()) {

            tsm = new TowingServicesModel();
            tsm.setName(c.getString(1));
            tsm.setAddress(c.getString(2));
            tsm.setLatlng(c.getString(3));
            tsm.setContactNumber(c.getString(4));

            tsmList.add(tsm);
        }
        c.close();
        db.close();

        return tsmList;

    }

    public boolean EmergencyGroupExists() {

        Cursor cursor = null;

        try {
            String sql = "SELECT groupid FROM " + TABLE_CREATE_GROUP + " WHERE groupname = 'Emergency'";
            cursor = db.rawQuery(sql, null);

            return (cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }

    }

    public boolean numberExists(String Phonenum) {

        if (!db.isOpen())
            db = getReadableDatabase();

        Cursor cursor = null;

        try {
            String sql = "SELECT contactid FROM " + TABLE_CONTACTS + " WHERE contactnum = '" + Phonenum + "' AND "+ FK_COLUMN_GROUPID + " != 1 ";
            cursor = db.rawQuery(sql, null);

            return (cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public boolean GroupNameExists(String name) {

        Cursor cursor = null;

        if (!db.isOpen())
            db = getReadableDatabase();
        try {
            String sql = "SELECT groupid FROM " + TABLE_CREATE_GROUP + " WHERE lower(groupname) = '" + name + "'";
            cursor = db.rawQuery(sql, null);

            return (cursor.getCount() > 0);
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }
    }

    public boolean checkTowingifEmpty(){
        if (!db.isOpen())
            db = getReadableDatabase();

        Cursor cursor = null;

        try {
            String sql = "SELECT * FROM towing";
            cursor = db.rawQuery(sql, null);

            return (cursor.getCount() > 0);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    public GroupModel getMessage(String name) {

        if (!db.isOpen())
            db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CREATE_GROUP + " WHERE " + COLUMN_GROUPNAME + " = '" + name + "'";

        Cursor c = db.rawQuery(query, null);
        GroupModel groupModel = new GroupModel();

        try {
            if (c != null && c.moveToFirst()) {
                groupModel.setGroupName(c.getString(1));
                groupModel.setGroupDesc(c.getString(2));
                groupModel.setGroupMessage(c.getString(3));
                groupModel.setRule1(Integer.parseInt(c.getString(4)));
                groupModel.setRule2(Integer.parseInt(c.getString(5)));
                groupModel.setRule3(Integer.parseInt(c.getString(6)));
                groupModel.setRule4(Integer.parseInt(c.getString(7)));

            }
        } finally {
            c.close();
            db.close();
        }


        return groupModel;
    }

    public GroupModel getGroup(String contactnum) {

        if (!db.isOpen())
            db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CREATE_GROUP + " WHERE " + COLUMN_GROUPID + " = (SELECT "
                + FK_COLUMN_GROUPID + " FROM "+ TABLE_CONTACTS +" WHERE " + COLUMN_CONTACTNUM + " = '" + contactnum +"' AND " + FK_COLUMN_GROUPID + " != '1') ";

        Cursor c = db.rawQuery(query, null);
        GroupModel groupModel = new GroupModel();

        try {
            if (c != null && c.moveToFirst()) {
                groupModel.setGroupName(c.getString(1));
                groupModel.setGroupDesc(c.getString(2));
                groupModel.setGroupMessage(c.getString(3));
                groupModel.setRule1(Integer.parseInt(c.getString(4)));
                groupModel.setRule2(Integer.parseInt(c.getString(5)));
                groupModel.setRule3(Integer.parseInt(c.getString(6)));
                groupModel.setRule4(Integer.parseInt(c.getString(7)));


            }
        } finally {
            c.close();
            db.close();
        }

        return groupModel;
    }


}

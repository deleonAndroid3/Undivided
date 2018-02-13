package com.training.android.undivided.AutoReply;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.training.android.undivided.R;

import java.util.ArrayList;

/***\
 *  Created by Hillary Briones
 *  */
public class ContactPicker extends AppCompatActivity {
    static ContentResolver cr;
    ArrayList<String> phoneNos; //array holding phone nos, from which the selected ones go into selectedContacts
    String[] listContacts; //array holding Strings that will be displayed in the listview
    Button selectButton;
    Activity thisActivity;
    String logTag = "GroupContactPicker";


    SparseBooleanArray checked;
    ListView listView;
    RadioGroup radioFilterType;

    private static String incomingExtraTag = "selected_contacts";
    private static String outgoingExtraTag = "selected_contacts_string";
    private static String outgoingFilterTypeTag = "contact_filter_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        thisActivity = this;

        // Set result the canceled incase user bails
        setResult(RESULT_CANCELED);

        listView = (ListView)findViewById(R.id.contactpicker_contactsList);
        radioFilterType = (RadioGroup) findViewById(R.id.radioGroup_filterType);

        // TODO progress bar

        long startTime = System.nanoTime();
        // store contacts + nos in listContacts
        getContactList();

        // populate the listview with listContacts
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listContacts);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        if (getIntent().hasExtra(incomingExtraTag)) { // if opened from an edit
            String[] savedNos = getIntent().getStringExtra(incomingExtraTag).split(","); // get already existing nos
            for (int i = 0; i < savedNos.length; i++) { //  look thru already existing nos
                int n = phoneNos.indexOf(savedNos[i]);
                if (n != -1) { //                           if they exist in the current contacts list
                    listView.setItemChecked(n, true);//     check their corresponding checkbox
                }
            }

            long endTime = System.nanoTime();
            Log.i(logTag, "population took " + (endTime - startTime) + " secs"); // TODO delete
        }
    }


    private void getContactList(){
        Uri myContacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(myContacts,
                projection, // only number and name columns
                null, null, // all rows
                ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"  // sort by ascending name
        );
        phoneNos = new ArrayList<String>(cursor.getCount());
        listContacts = new String[cursor.getCount()];

        int i =0;
        if(cursor.moveToFirst())
        {
            do
            {
                String phoneNo = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)); // get phone no in raw format
                phoneNos.add(i, phoneNo.replaceAll("[()\\-\\s]", "")); // Trim from extra chars then add into phone no arraylist
                listContacts[i] = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.Data.DISPLAY_NAME)) + "\n" +
                        phoneNo; // name \n raw phone no
                i++;
            }
            while(cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * Called when the done actionbar button is selected.
     * Finishes the activity with the selected phoneNos as the extra of the result intent
     */
    private void doneSelected(){

        // 0 = include / 1 = exclude
        int filterType = radioFilterType.indexOfChild(findViewById(radioFilterType.getCheckedRadioButtonId()));

        checked = listView.getCheckedItemPositions();
        ArrayList<String> selectedContacts = new ArrayList<String>();
        String selectedContactsString = "";
        for (int i = 0; i < listView.getCount(); i++)
            if (checked.get(i)) {
                selectedContactsString += phoneNos.get(i) + ",";
            }
        // Put the array as an extra and finish activity
        Intent contactIntent = new Intent();
        contactIntent.putExtra(outgoingFilterTypeTag, filterType);
        contactIntent.putExtra(outgoingExtraTag, selectedContactsString);
        setResult(RESULT_OK, contactIntent);
        thisActivity.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contact_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.contactpicker_action_done:
                doneSelected();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Sets all the checkboxes in the listView to the given value
     * @param value Value to set
     */
    private void setAll(boolean value) {
        for (int i = 0; i < listView.getCount(); i++) {
            listView.setItemChecked(i, value);
        }
    }
}
//package com.training.android.undivided.AutoReply;


//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
//import android.util.SparseBooleanArray;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//
//import com.training.android.undivided.Database.DBHandler;
//import com.training.android.undivided.Group.Model.ContactsModel;
//import com.training.android.undivided.R;
//
//import java.util.ArrayList;
//
///***\
// *  Created by Hillary Briones
// *  */
//public class ContactPicker extends AppCompatActivity {
//
//    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
//    public static final String logTag = "ContactPicker";
//    private static String incomingExtraTag = "selected_contacts";
//
//    private ListView listView;
//    private ArrayList<String> phoneNos; //array holding phone nos, from which the selected ones go into selectedContacts
//    private ArrayList<String> contactNames;
//    private String[] listContacts; //array holding Strings that will be displayed in the listview
//    private SparseBooleanArray checked;
//    private Intent name;
//    private DBHandler db;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_picker);
//
//         Set result the canceled incase user bails
//        setResult(RESULT_CANCELED);
//
//        listView = (ListView) findViewById(R.id.contactpicker_contactsList);
//
//        name = getIntent();
//        db = new DBHandler(this);
//
//         store contacts + nos in listContacts
//        getContactList();
//
//         populate the listview with listContacts
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listContacts);
//        listView.setAdapter(adapter);
//        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//
//    }
//
//
//    private void getContactList() {
//        Uri myContacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Data.DISPLAY_NAME};
//        Cursor cursor = getContentResolver().query(myContacts,
//                projection, // only number and name columns
//                null, null, // all rows
//                ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"  // sort by ascending name
//        );
//        phoneNos = new ArrayList<String>(cursor.getCount());
//        contactNames = new ArrayList<>(cursor.getCount());
//        listContacts = new String[cursor.getCount()];
//
//        int i = 0;
//        if (cursor.moveToFirst()) {
//            do {
//                String phoneNo = cursor.getString(cursor
//                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)); // get phone no in raw format
//                String contactname = cursor.getString(cursor
//                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//
//                contactNames.add(contactname);
//                phoneNos.add(phoneNo);
//                listContacts[i] = contactname + "\n" + phoneNo; // name \n raw phone no
//                i++;
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//    }
//
//    /**
//     * Called when the done actionbar button is selected.
//     * Finishes the activity with the selected phoneNos as the extra of the result intent
//     */
//    private void doneSelected() {
//        checked = listView.getCheckedItemPositions();
//        ContactsModel cm = new ContactsModel();
//
//        for (int i = 0; i < listView.getCount(); i++) {
//            if (checked.get(i)) {
//                cm.setContactName(contactNames.get(i));
//                cm.setContactNumber(phoneNos.get(i));
//                db.addContact(cm, name.getStringExtra("groupname"));
//            }
//        }
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent gotoViewGroup = new Intent(ContactPicker.this, ViewGroup.class);
//                startActivity(gotoViewGroup);
//                finish();
//            }
//        }, 1500);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_contact_picker, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.contactpicker_action_done:
//                doneSelected();
//                return true;
//            case R.id.contactpicker_action_selectAll:
//                setAll(true);
//                return true;
//            case R.id.contactpicker_action_deselectAll:
//                setAll(false);
//                return true;
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    /**
//     * Sets all the checkboxes in the listView to the given value
//     *
//     * @param value Value to set
//     */
//    private void setAll(boolean value) {
//        for (int i = 0; i < listView.getCount(); i++) {
//            listView.setItemChecked(i, value);
//        }
//    }
//}
//

package com.training.android.undivided.Group;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.training.android.undivided.Group.Database.DBHandler;
import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.R;

import java.util.ArrayList;

/***\
 *  Created by Hillary Briones
 *  */

public class GroupContactPicker extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 11;
    public static final String logTag = "GroupContactPicker";
    private static String incomingExtraTag = "selected_contacts";

    private ListView listView;
    private ArrayList<String> phoneNos; //array holding phone nos, from which the selected ones go into selectedContacts
    private ArrayList<String> contactNames;
    private String[] listContacts; //array holding Strings that will be displayed in the listview
    private SparseBooleanArray checked;
    private Intent name;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupcontact_picker);

        // Set result the canceled incase user bails
        setResult(RESULT_CANCELED);

        listView = (ListView) findViewById(R.id.contactpicker_contactsList);

        name = getIntent();
        db = new DBHandler(this);

        // store contacts + nos in listContacts
        getContactList();

        // populate the listview with listContacts
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, listContacts);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }

    private void getContactList() {
        Uri myContacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(myContacts,
                projection, // only number and name columns
                null, null, // all rows
                ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC"  // sort by ascending name
        );
        phoneNos = new ArrayList<String>(cursor.getCount());
        contactNames = new ArrayList<>(cursor.getCount());
        listContacts = new String[cursor.getCount()];

        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                String phoneNo = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)); // get phone no in raw format
                String contactname = cursor.getString(cursor
                        .getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                contactNames.add(contactname);
                phoneNos.add(phoneNo);
                listContacts[i] = contactname + "\n" + phoneNo; // name \n raw phone no
                i++;
            }
            while (cursor.moveToNext());
        }
        cursor.close();
    }

    /**
     * Called when the done actionbar button is selected.
     * Finishes the activity with the selected phoneNos as the extra of the result intent
     */
    private void doneSelected() {
        checked = listView.getCheckedItemPositions();
        ContactsModel cm = new ContactsModel();
        if (checked.size() == 0) {
            Toast.makeText(this, "Group must at least have 1 contact", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < listView.getCount(); i++) {

                if (checked.get(i)) {
                    cm.setContactName(contactNames.get(i));
                    cm.setContactNumber(phoneNos.get(i));
                    if (db.numberExists(phoneNos.get(i))) {
                        //TODO: create an alert dialog if a number exists in another group
                        Toast.makeText(this, "Phone Number " + phoneNos.get(i) + " already exists in another group and won't be added to this group", Toast.LENGTH_SHORT).show();
                    } else {
                        db.addContact(cm, name.getStringExtra("groupname"));
                    }
                }

            }


            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent gotoViewGroup = new Intent(GroupContactPicker.this, ViewGroup.class);
                    startActivity(gotoViewGroup);
                    finish();
                }
            }, 1500);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

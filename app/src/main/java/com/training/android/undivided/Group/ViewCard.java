package com.training.android.undivided.Group;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.training.android.undivided.Group.Adapter.ContactsAdapter;
import com.training.android.undivided.Group.Database.DBHandler;
import com.training.android.undivided.Group.Model.ContactsModel;
import com.training.android.undivided.Group.Model.GroupModel;
import com.training.android.undivided.R;

public class ViewCard extends AppCompatActivity {

    DBHandler dbHandler;
    int counter = 0;
    String name = "";
    private ListView mContactsList;
    private ContactsAdapter adapter;
    private EditText mEtGroupName;
    private EditText mEtGroupDescription;
    private EditText mEtGroupMessage;
    private CheckBox cbRule1;
    private CheckBox cbRule2;
    private CheckBox cbRule3;
    private CheckBox cbRule4;
    private CheckBox cbRule5;
    private CheckBox cbRule6;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_card);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Undivided");

        Toolbar toolbar = findViewById(R.id.card_toolbar);
        toolbar.setTitle("Contacts");
        if (toolbar != null) {
            // inflate your menu
            toolbar.inflateMenu(R.menu.contacts_menu);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    return true;
                }
            });
        }

        dbHandler = new DBHandler(this);

        mEtGroupName = findViewById(R.id.etGroupName);
        mEtGroupDescription = findViewById(R.id.etGroupDesc);
        mEtGroupMessage = findViewById(R.id.etGroupMessage);
        cbRule1 = findViewById(R.id.declinecall);
        cbRule2 = findViewById(R.id.autoreplysms);
        cbRule3 = findViewById(R.id.autoreplycall);
        cbRule4 = findViewById(R.id.replysms);
        cbRule5 = findViewById(R.id.readsms);
        cbRule6 = findViewById(R.id.answercall);
        mContactsList = findViewById(R.id.lvContactsList);

        mEtGroupDescription.setMaxLines(Integer.MAX_VALUE);
        mEtGroupDescription.setHorizontallyScrolling(false);
        mEtGroupMessage.setMaxLines(Integer.MAX_VALUE);
        mEtGroupMessage.setHorizontallyScrolling(false);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            name = b.getString("name");
            mEtGroupName.setText(name);
            mEtGroupDescription.setText(b.getString("desc"));
            mEtGroupMessage.setText(b.getString("message"));

            if (b.getInt("1") == 1)
                cbRule1.setChecked(true);
            if (b.getInt("2") == 1)
                cbRule2.setChecked(true);
            if (b.getInt("3") == 1)
                cbRule3.setChecked(true);
            if (b.getInt("4") == 1)
                cbRule4.setChecked(true);
            if (b.getInt("5") == 1)
                cbRule5.setChecked(true);
            if (b.getInt("6") == 1)
                cbRule6.setChecked(true);

            adapter = new ContactsAdapter(this, R.layout.contacts_listview, dbHandler.getContactsofGroup(b.getString("name")));
            mContactsList.setAdapter(adapter);
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewcard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_card:
                if (counter == 1) {
                    if (dbHandler.GroupNameExists(mEtGroupName.getText().toString().toLowerCase())
                            && !name.equals(mEtGroupName.getText().toString())) {
                        Snackbar.make(findViewById(R.id.activity_viewcard)
                                , "Group Name " + mEtGroupName.getText() + " already exists", Snackbar.LENGTH_SHORT).show();
                    }
                    if (mEtGroupName.getText().toString().trim().isEmpty()) {
                        mEtGroupName.setError("Please provide group name");
                    }
                    if (mEtGroupDescription.getText().toString().trim().isEmpty()) {
                        mEtGroupDescription.setError("Please provide group description");
                    }
                    if (mEtGroupMessage.getText().toString().trim().isEmpty()) {
                        mEtGroupMessage.setError("Please provide group message");
                    }
                    if (!mEtGroupName.getText().toString().trim().isEmpty()
                            && !mEtGroupDescription.getText().toString().trim().isEmpty()
                            && !mEtGroupMessage.getText().toString().trim().isEmpty()) {
                        dbHandler.UpdateGroup(name, getData());
                        Snackbar.make(findViewById(R.id.activity_viewcard), "Update Successful", Snackbar.LENGTH_SHORT).show();
                        item.setIcon(R.drawable.ic_mode_edit);
                        returnCard();
                        counter = 0;
                    }

                } else {

                    AlertDialog.Builder builder;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(this);
                    }

                    builder.setTitle("Edit Group")
                            .setMessage("Are you sure you want to Edit this group?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    item.setIcon(R.drawable.action_save);
                                    update();
                                    counter = 1;
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();

                }
                break;
            case android.R.id.home:
                onBackPressed();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void update() {

        mEtGroupName.setFocusable(true);
        mEtGroupName.setFocusableInTouchMode(true);
        mEtGroupDescription.setFocusable(true);
        mEtGroupDescription.setFocusableInTouchMode(true);
        mEtGroupMessage.setFocusable(true);
        mEtGroupMessage.setFocusableInTouchMode(true);
        cbRule1.setClickable(true);
        cbRule2.setClickable(true);
        cbRule3.setClickable(true);
        cbRule4.setClickable(true);
        cbRule5.setClickable(true);
        cbRule6.setClickable(true);

        if (mEtGroupDescription.getText().toString().equals("Emergency")) {
            mEtGroupName.setFocusable(false);
            cbRule1.setClickable(false);
            cbRule2.setClickable(false);
            cbRule3.setClickable(false);
            cbRule4.setClickable(false);
            cbRule5.setClickable(false);
            cbRule6.setClickable(false);
        }

    }

    public void returnCard() {
        mEtGroupName.setFocusable(false);
        mEtGroupDescription.setFocusable(false);
        mEtGroupMessage.setFocusable(false);
        cbRule1.setClickable(false);
        cbRule2.setClickable(false);
        cbRule3.setClickable(false);
        cbRule4.setClickable(false);
        cbRule5.setClickable(false);
        cbRule6.setClickable(false);

    }

    public void deleteContact(int pos) {

        ContactsModel cm = (ContactsModel) mContactsList.getItemAtPosition(pos);

        dbHandler.DeleteContacts(Integer.parseInt(cm.getContactNumber()));
    }

    public GroupModel getData() {
        GroupModel gm = new GroupModel();
        gm.setGroupName(mEtGroupName.getText().toString());
        gm.setGroupDesc(mEtGroupDescription.getText().toString());
        gm.setGroupMessage(mEtGroupMessage.getText().toString());
        gm.setRule1(cbRule1.isChecked() ? 1 : 0);
        gm.setRule2(cbRule2.isChecked() ? 1 : 0);
        gm.setRule3(cbRule3.isChecked() ? 1 : 0);
        gm.setRule4(cbRule4.isChecked() ? 1 : 0);
        gm.setRule5(cbRule5.isChecked() ? 1 : 0);
        gm.setRule6(cbRule6.isChecked() ? 1 : 0);

        return gm;
    }
}

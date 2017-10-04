package com.training.android.undivided.Group;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.training.android.undivided.Group.Database.DBHandler;
import com.training.android.undivided.Group.Model.GroupModel;
import com.training.android.undivided.R;

public class AddGroup extends AppCompatActivity {

    DBHandler dbHandler;
    private EditText mEtGroupName;
    private EditText mEtGroupDescription;
    private EditText mEtGroupMessage;
    private CheckBox cbRule1;
    private CheckBox cbRule2;
    private CheckBox cbRule3;
    private CheckBox cbRule4;
    private CheckBox cbRule5;
    private CheckBox cbRule6;
    private CheckBox cbRule7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Group");

        dbHandler = new DBHandler(this);

        mEtGroupName = (EditText) findViewById(R.id.etGroupName);
        mEtGroupDescription = (EditText) findViewById(R.id.etGroupDesc);
        mEtGroupMessage = (EditText) findViewById(R.id.etGroupMessage);
        cbRule1 = (CheckBox) findViewById(R.id.declinecall);
        cbRule2 = (CheckBox) findViewById(R.id.autoreplysms);
        cbRule3 = (CheckBox) findViewById(R.id.autoreplycall);
        cbRule4 = (CheckBox) findViewById(R.id.replysms);
        cbRule5 = (CheckBox) findViewById(R.id.readsms);
        cbRule6 = (CheckBox) findViewById(R.id.voicemail);
        cbRule7 = (CheckBox) findViewById(R.id.answercall);

        mEtGroupDescription.setMaxLines(Integer.MAX_VALUE);
        mEtGroupDescription.setHorizontallyScrolling(false);
        mEtGroupMessage.setMaxLines(Integer.MAX_VALUE);
        mEtGroupMessage.setHorizontallyScrolling(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save_group:
                showAlert();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_group, menu);
        return true;
    }

    public void getData() {
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
        gm.setRule7(cbRule7.isChecked() ? 1 : 0);
        dbHandler.addGroup(gm);

    }

    public void showAlert() {

        AlertDialog.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Create Group")
                .setMessage("Are you sure you want to create this group?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getData();
                        Toast.makeText(AddGroup.this, "Group Created", Toast.LENGTH_SHORT).show();
                        Toast.makeText(AddGroup.this, "Add Contacts to this Group", Toast.LENGTH_SHORT).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent contactpick = new Intent(AddGroup.this, ContactPicker.class);
                                contactpick.putExtra("groupname", mEtGroupName.getText().toString());
                                startActivity(contactpick);

                                finish();
                            }
                        }, 1000);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();

    }

}

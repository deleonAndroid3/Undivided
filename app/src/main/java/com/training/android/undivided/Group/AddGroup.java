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

import com.training.android.undivided.Database.DBHandler;
import com.training.android.undivided.Group.Model.GroupModel;
import com.training.android.undivided.R;

public class AddGroup extends AppCompatActivity {

    DBHandler dbHandler;
    AlertDialog ModeDialog;
    private EditText mEtGroupName;
    private EditText mEtGroupDescription;
    private EditText mEtGroupMessage;
    private CheckBox cbRule1;
    private CheckBox cbRule2;
    private CheckBox cbRule3;
    private CheckBox cbRule4;
    private CheckBox cbRule5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Add Group");

        dbHandler = new DBHandler(this);

        mEtGroupName = findViewById(R.id.etGroupName);
        mEtGroupDescription = findViewById(R.id.etGroupDesc);
        mEtGroupMessage = findViewById(R.id.etGroupMessage);

        cbRule1 = findViewById(R.id.autoreplysms);
        cbRule2 = findViewById(R.id.autoreplycall);
        cbRule3 = findViewById(R.id.replysms);
        cbRule4 = findViewById(R.id.readsms);
        cbRule5 = findViewById(R.id.answercall);

        mEtGroupDescription.setMaxLines(Integer.MAX_VALUE);
        mEtGroupDescription.setHorizontallyScrolling(false);
        mEtGroupMessage.setMaxLines(Integer.MAX_VALUE);
        mEtGroupMessage.setHorizontallyScrolling(false);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mEtGroupName.setText(bundle.getString("Title"));
            mEtGroupDescription.setText(bundle.getString("Desc"));
            mEtGroupMessage.setText(bundle.getString("Message"));
            mEtGroupName.setEnabled(false);
            cbRule1.setEnabled(false);
            cbRule2.setEnabled(false);
            cbRule3.setEnabled(false);
            cbRule4.setEnabled(false);
            cbRule5.setEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save_group:
                if (dbHandler.GroupNameExists(mEtGroupName.getText().toString().toLowerCase())) {
                    Toast.makeText(this, "Group Name " + mEtGroupName.getText() + " already exists", Toast.LENGTH_SHORT).show();
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
                        && !mEtGroupMessage.getText().toString().trim().isEmpty()
                        && !dbHandler.GroupNameExists(mEtGroupName.getText().toString().toLowerCase())) {
                    showAlert();
                }
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
        dbHandler.addGroup(gm);

    }

    public void showAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
//        } else {
//
//        }

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
                                Intent contactpick = new Intent(AddGroup.this, GroupContactPicker.class);
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

package com.training.android.undivided;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class EditReplyMessage extends AppCompatActivity {

    private AutoReply auto_reply;
    private EditText edit_reply_message;
    private ArrayAdapter<String> adapter;
    private RecentMessage recent_messages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_reply_message);
        auto_reply = AutoReply.getInstance();
        recent_messages = RecentMessage.getInstance();
        recent_messages.loadList(this);
        edit_reply_message = (EditText) findViewById(R.id.edit_reply_message);
        edit_reply_message.setText(auto_reply.currentMessage());
    }
    protected void onResume() {
        super.onResume();
        updateState();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recent_messages.list);
        ListView recent_messages_list_view = (ListView) findViewById(R.id.recent_messages_list_view);
        recent_messages_list_view.setOnItemClickListener(on_recent_message_click);
        recent_messages_list_view.setAdapter(adapter);
    }

    protected void onDestroy() {
        super.onDestroy();
        recent_messages.saveList(this);
    }

    private AdapterView.OnItemClickListener on_recent_message_click = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Get the new and old messages
            String selected_message = recent_messages.list.get(i);
            String current_message = auto_reply.currentMessage();

            // Reorder the recent_messages
            recent_messages.list.remove(i);
            recent_messages.push(current_message);

            // Save the new current message
            auto_reply.setCurrentMessage(selected_message);
            saveMessageToPreference(selected_message);

            // Close the activity
            finish();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_reply_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.reply_message_shadow) {
            // Get the new message
            String new_message = edit_reply_message.getText().toString();

            // Save the current message to recent messages if it is different
            if (!auto_reply.currentMessage().equals(new_message)) {
                recent_messages.push(auto_reply.currentMessage());
            }

            // Set the new message to the auto reply
            auto_reply.setCurrentMessage(new_message);

            // Set the string to the shared preferences
            saveMessageToPreference(new_message);

            // Close this activity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void clearEditMessage(View v) {
        edit_reply_message.setText("");
    }

    public void clearRecentMessages(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Clear Recent");
        builder.setMessage("Do you wish to remove recent messages?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                recent_messages.list.clear();
                adapter.notifyDataSetChanged();
                dialogInterface.dismiss();
                updateState();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog confirm = builder.create();
        confirm.show();
    }

    public boolean dismissKeyboard(View v) {
        EditText myEditText = (EditText) findViewById(R.id.edit_reply_message);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                this.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        return false;
    }

    private void saveMessageToPreference(String new_message) {
        // Set the string to the shared preferences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pref_message", new_message);
        editor.commit();
    }

    private void updateState() {
        if (recent_messages.list.isEmpty()) {
            findViewById(R.id.recent_messages_label).setVisibility(View.INVISIBLE);
            findViewById(R.id.recent_messages_list_view).setVisibility(View.INVISIBLE);
            findViewById(R.id.button).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.recent_messages_label).setVisibility(View.VISIBLE);
            findViewById(R.id.recent_messages_list_view).setVisibility(View.VISIBLE);
            findViewById(R.id.button).setVisibility(View.VISIBLE);
        }

    }
    }


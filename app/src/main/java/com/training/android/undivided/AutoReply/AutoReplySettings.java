package com.training.android.undivided.AutoReply;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.training.android.undivided.R;

public class AutoReplySettings extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor sPrefEditor;
    private String logTag = "Settings";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_reply_settings);

        Spinner muteSpinner = (Spinner) findViewById(R.id.settings_spinner_mute);


        sharedPref = getSharedPreferences(getString(R.string.shared_preferences_key), Context.MODE_PRIVATE);
        sPrefEditor = sharedPref.edit();


        ArrayAdapter<CharSequence> muteAdapter = ArrayAdapter.createFromResource(this,
                R.array.settings_mute_spinner_array, android.R.layout.simple_spinner_item);

        muteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        muteSpinner.setAdapter(muteAdapter);


        try {
            muteSpinner.setSelection(sharedPref.getInt(getString(R.string.settings_mute_position_key), -1));
        } catch (IndexOutOfBoundsException e) {}



        muteSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.i(logTag, "Mute spinner - onItemSelected called with position: " + position);
                if (position !=0) {

                    sPrefEditor.putInt(getString(R.string.settings_mute_position_key), position);
                    if (!sPrefEditor.commit()) {
                        Log.e(logTag, "Error while committing mute delay position into shared preferences");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(logTag, "Mute spinner - onNothingSelected called");
            }
        });
    }
}

package com.training.android.undivided.LivetoText;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.training.android.undivided.R;

/**
 * Created by Hillary Briones on 2/13/2018.
 */

public class ConfigureScreen extends PreferenceActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.configure);
    }
}

package com.training.android.undivided.NavigationMode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.github.ppamorim.dragger.DraggerActivity;
import com.github.ppamorim.dragger.DraggerPosition;
import com.github.ppamorim.dragger.DraggerView;
import com.training.android.undivided.R;

public class EmergencyContactsList extends AppCompatActivity {

    private DraggerView draggerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts_list);

        draggerView = findViewById(R.id.dragger_view);

        configIntents();

    }

    @Override public void onBackPressed() {
        draggerView.closeActivity();
    }

    @Override
    protected void onPause() {
        overridePendingTransition(0, 0);
        super.onPause();
    }

    private void configIntents() {
        draggerView.setDraggerPosition((DraggerPosition) getIntent().getSerializableExtra("Drag"));
    }
}

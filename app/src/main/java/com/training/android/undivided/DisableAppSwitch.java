package com.training.android.undivided;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DisableAppSwitch extends AppCompatActivity {

    private Button mbtnSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_app_switch);

        mbtnSwitch = (Button) findViewById(R.id.btnSwitch);
        startLockTask();

        mbtnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DisableAppSwitch.this, Functions.class);
                stopLockTask();
                startActivity(i);
            }
        });


    }
}

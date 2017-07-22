package com.training.android.undivided;

import android.app.job.JobScheduler;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mbtnMaps;
    Button mbtnService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mbtnMaps = (Button) findViewById(R.id.btnMaps);
        mbtnService = (Button) findViewById(R.id.btnService);

        mbtnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Navigation.class);
                startActivity(i);
            }
        });

        mbtnService.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                startService(intent);
            }
        });
    }

    protected void onDestroy(){
//        Intent intent = new Intent(this, BackgroundService.class);
//        startService(intent);
        super.onDestroy();


    }
}

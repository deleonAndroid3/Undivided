package com.training.android.undivided;

import android.Manifest;
import android.app.job.JobScheduler;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mbtnMaps;
    Button mbtnService;
    Button mbtnMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mbtnMaps = (Button) findViewById(R.id.btnMaps);
        mbtnService = (Button) findViewById(R.id.btnService);
        mbtnMessage = (Button) findViewById(R.id.btnMessage);

        mbtnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Navigation.class);
                startActivity(i);
            }
        });

        /**
         * BACKGROUND FUNCTION WITH AUTO START ( INCLUDING ON DESTROY )
         * (IMPLEMENTED WITH BUTTON RIGHT NOW)
         */
        mbtnService.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                startService(intent);
            }
        });

        /**
         *  SEND SMS FUNCTION FOR SPEECH TO TEXT REPLY.
         *  (IMPLEMENTED WITH BUTTON RIGHT NOW)
         */
        mbtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent textToSpeech = new Intent(MainActivity.this, TextToSpeech.class);
                startActivity(textToSpeech);

            }
        });
    }



    protected void onDestroy(){
//        Intent intent = new Intent(this, BackgroundService.class);
//        startService(intent);
        super.onDestroy();


    }
}

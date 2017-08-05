package com.training.android.undivided;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mbtnMaps;
    Button mbtnService;
    Button mbtnSTT;
    Button mbtnMessage;
   Button mbtnAR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mbtnMaps = (Button) findViewById(R.id.btnMaps);
        mbtnService = (Button) findViewById(R.id.btnService);
        mbtnMessage = (Button) findViewById(R.id.btnMessage);
        mbtnSTT = (Button) findViewById(R.id.btnSTT);
        mbtnAR=(Button)findViewById(R.id.btnAR);

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
        mbtnService.setOnClickListener(new View.OnClickListener() {
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

        mbtnSTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent speechToSpeech = new Intent(MainActivity.this, SpeechToText.class);
                startActivity(speechToSpeech);
            }
        });
        mbtnAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent autoReply=new Intent(MainActivity.this,AutoReplyActivity.class);
                startActivity(autoReply);
            }
        });

    }

    protected void onDestroy() {
//        Intent intent = new Intent(this, BackgroundService.class);
//        startService(intent);
        super.onDestroy();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent settings = new Intent(MainActivity.this, Settings.class);
                startActivity(settings);
                break;

        }


        return super.onOptionsItemSelected(item);
    }


}

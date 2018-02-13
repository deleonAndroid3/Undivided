package com.training.android.undivided;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.training.android.undivided.AutoReply.AutoReplyActivity;
import com.training.android.undivided.CallLog.CallLogActivity;


import com.training.android.undivided.SmsGroup.Activity.SmsGroupActivity;

public class Functions extends AppCompatActivity {

    Button mbtnSTT;
    Button mbtnMessage;
    Button mbtnAR;
    Button mbtnEmergency;
    Button mbtnDAS;
    Button mbtnCallLog;
    Button mbtnCreateGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);

        mbtnMessage = (Button) findViewById(R.id.btnMessage);
        mbtnSTT = (Button) findViewById(R.id.btnSTT);
        mbtnAR = (Button) findViewById(R.id.btnAR);
        mbtnEmergency = (Button) findViewById(R.id.btnEmergency);
        mbtnDAS = (Button) findViewById(R.id.btnDisableAppSwitch);
        mbtnCallLog = findViewById(R.id.btnCallLog);
        mbtnCreateGroup=findViewById(R.id.btnCreateGroup);

        mbtnCallLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callLog = new Intent(Functions.this, CallLogActivity.class);
                startActivity(callLog);
            }
        });

        mbtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent textToSpeech = new Intent(Functions.this, TextToSpeech.class);
                startActivity(textToSpeech);
            }
        });

        mbtnSTT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent speechToSpeech = new Intent(Functions.this, SpeechToText.class);
                startActivity(speechToSpeech);
            }
        });
        mbtnAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent autoReply = new Intent(Functions.this, AutoReplyActivity.class);
                startActivity(autoReply);
            }
        });
        mbtnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emergency = new Intent(Functions.this, Emergency.class);
                startActivity(emergency);
            }
        });
        mbtnDAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent DAS = new Intent(Functions.this, DisableAppSwitch.class);
                startActivity(DAS);
            }
        });
        mbtnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent GS = new Intent(Functions.this, SmsGroupActivity.class);
                startActivity(GS);
            }
        });

    }
}

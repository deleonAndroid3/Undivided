package com.training.android.undivided;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.training.android.undivided.AutoReply.AutoReplyActivity;

public class Functions extends AppCompatActivity {

    Button mbtnSTT;
    Button mbtnMessage;
    Button mbtnAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);

        mbtnMessage = (Button) findViewById(R.id.btnMessage);
        mbtnSTT = (Button) findViewById(R.id.btnSTT);
        mbtnAR = (Button) findViewById(R.id.btnAR);

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
    }
}

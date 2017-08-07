package com.training.android.undivided;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.training.android.undivided.NavigationMode.Navigation;

public class MainActivity extends AppCompatActivity {

    Button mbtnSTT;
    Button mbtnMessage;
    Button mbtnAR;
    AlertDialog ModeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chooseMode();

        mbtnMessage = (Button) findViewById(R.id.btnMessage);
        mbtnSTT = (Button) findViewById(R.id.btnSTT);
        mbtnAR = (Button) findViewById(R.id.btnAR);

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
                Intent autoReply = new Intent(MainActivity.this, AutoReplyActivity.class);
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

    public void chooseMode() {

        final CharSequence[] modes = {"Safe Mode", "Navigation Mode", "Passenger Mode"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Driving Mode");
        builder.setSingleChoiceItems(modes, -1, null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                int position = ModeDialog.getListView().getCheckedItemPosition();

                switch (position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "Safe Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Intent navi = new Intent(MainActivity.this, Navigation.class);
                        startActivity(navi);
                        Toast.makeText(MainActivity.this, "Navigation Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "Passenger Mode Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });

        ModeDialog = builder.create();
        ModeDialog.show();

    }

}

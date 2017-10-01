package com.training.android.undivided;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Emergency extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        int i = 0;
        String[] contact = {"09053274403", "09952635512"};
        String message = "EMERGENCY SOS HELP";
        while (i != contact.length) {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contact[i], null, message, null, null);

                Toast.makeText(this, "Sent!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Failed.", Toast.LENGTH_SHORT).show();
            }
            i++;
        }

       /* Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:09053274403"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "No Permission.", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(callIntent);*/
    }
}

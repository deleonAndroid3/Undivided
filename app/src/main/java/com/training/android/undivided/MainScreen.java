package com.training.android.undivided;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class MainScreen extends AppCompatActivity {

    private ImageView mIvStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        mIvStart = (ImageView) findViewById(R.id.ivDriveStart);

        mIvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
                Intent settings = new Intent(MainScreen.this, Settings.class);
                startActivity(settings);
                break;

            case R.id.menu_functions:
                Intent functions = new Intent(MainScreen.this, MainActivity.class);
                startActivity(functions);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}

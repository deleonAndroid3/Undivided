package com.training.android.undivided.CallLog;

import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ListView;
import android.widget.TextView;

import com.training.android.undivided.R;

import java.util.ArrayList;
import java.util.Date;

public class CallLogActivity extends AppCompatActivity {

    ArrayList<Log> dataModels;
    ListView listView;
    private static LogAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_log);

        listView=(ListView)findViewById(R.id.lvCallLog);


//        mTvCallLog.setText(getCallDetails());
        getCallDetails();
//        mTvCallLog.setMovementMethod(new ScrollingMovementMethod());
    }

    private void getCallDetails() {

        dataModels= new ArrayList<>();

        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);


        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int name = (managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
//        sb.append("Call Details :");
        while (managedCursor.moveToNext()) {

//            String phNumber = managedCursor.getString(number);
//            String callType = managedCursor.getString(type);
//            String callDate = managedCursor.getString(date);
//            Date callDayTime = new Date(Long.valueOf(callDate));
//            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(managedCursor.getString(type));
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }

            if(dircode == CallLog.Calls.INCOMING_TYPE || dircode == CallLog.Calls.MISSED_TYPE) {
//                sb.append("\nPhone Number: " + phNumber + " \nCall Type: "
//                        + dir + " \nCall Date: " + callDayTime
//                        + " \nCall duration in sec : " + callDuration);
//                sb.append("\n----------------------------------");

//                if(managedCursor.getString(name).equals(" ")) {
//                    dataModels.add(new Log(managedCursor.getString(number),managedCursor.getString(type),
//                            managedCursor.getString(date), new Date(Long.valueOf(managedCursor.getString(date))),
//                            managedCursor.getString(duration), "Unknown Number"));
//
//                }   else {

                    dataModels.add(new Log(managedCursor.getString(number), dir,
                            managedCursor.getString(date), new Date(Long.valueOf(managedCursor.getString(date))),
                            managedCursor.getString(duration), managedCursor.getString(name)));
//                }
            }
        }
//        managedCursor.close();
        adapter= new LogAdapter(dataModels,getApplicationContext());
        listView.setAdapter(adapter);
        return;

    }
}

package com.example.no1.photongallery.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyAlarmReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 12345;
  //  public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, AutoChangeBackgroundImage.class);
     //   i.putExtra("foo", "bar");
        context.startService(i);


        Toast.makeText(context, "yeeeeeeeesssssssssssssss", Toast.LENGTH_SHORT).show();
    }

}




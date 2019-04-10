package com.example.smscatcher;

import android.Manifest;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView textCatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_SMS}, 1);

        textCatcher = findViewById(R.id.textCatcher);
        OutgoingSMSObserver outgoingSMSObserver = new OutgoingSMSObserver(new Handler(), this, textCatcher);
        ContentResolver contentResolver = this.getApplicationContext().getContentResolver();
        contentResolver.registerContentObserver(Uri.parse("content://sms/"), true, outgoingSMSObserver);
    }
}

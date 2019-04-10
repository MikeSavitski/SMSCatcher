package com.example.smscatcher;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class OutgoingSMSObserver extends ContentObserver {

    private TextView textView;
    private Context context;
    private String lastString;


    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public OutgoingSMSObserver(Handler handler) {
        super(handler);
    }

    public OutgoingSMSObserver(Handler handler, Context context, TextView textView) {
        super(handler);
        this.context = context;
        this.textView = textView;
        this.lastString = "";
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        Uri smsURI = Uri.parse("content://sms");
        Cursor cursor = context.getApplicationContext().getContentResolver().query(smsURI, null, null, null, null);
        cursor.moveToNext();
        String smsNumber = cursor.getString(cursor.getColumnIndex("address"));
        String message = cursor.getString(cursor.getColumnIndex("body"));
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        if (smsNumber == null || smsNumber.length() <= 0) {
            smsNumber = "Unknown";
        }
        String currentText = textView.getText().toString();
        String newString = currentText + "\n" + smsNumber + ": " + message;
        Log.d("OutgoingSMSObserver", "onChange() was called with type " + cursor.getInt(cursor.getColumnIndex("type")));
        if (cursor.getInt(cursor.getColumnIndex("type")) == 2 && !id.matches(lastString)) {
            Log.d("OutgoingSMSObserver", "Tried to post a sent message. with id " + id);
            textView.setText(newString);
            lastString = id;
        }
        cursor.close();
    }
}

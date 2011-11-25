package net.mootoh.TimeCard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TouchTrackerActivity extends Activity {
    private static final String PREFS_NAME = "org.deadbeaf.TouchTracker";
    private SharedPreferences prefs;
    private boolean isTracking;
    private ArrayList <String> tagHistory;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(PREFS_NAME, 0);
        isTracking = prefs.getBoolean("tracking", true);
        tagHistory = getTagHistory();
        handleIntent(getIntent());
    }

    private ArrayList<String> getTagHistory() {
        ArrayList<String> history = new ArrayList<String>();
        return history;
    }

    private void saveTagHistory() {
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (! NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            showMainWindow("");
            return;
        }
        Tag tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null)
            return;
        byte[] idBytes = tag.getId();
        if (idBytes == null)
            return;

        final String tagId = getHex(idBytes);
        Log.d(getClass().getSimpleName(), "tag id=" + tagId);

        if (isBrandnew(tagId)) {
            Log.d(getClass().getSimpleName(), "brand new");

            // show a form to input the name or something for this tag

            setContentView(R.layout.newtag);
            TextView textView = (TextView)findViewById(R.id.textView2);
            textView.setText(tagId);

            final EditText editText = (EditText)findViewById(R.id.tagName);

            final Button button = (Button)findViewById(R.id.saveName);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (editText.getText().equals(""))
                        return;
                    SharedPreferences.Editor editor = prefs.edit();
                    Log.d("aa", "text = " + editText.getText().toString());
                    editor.putString(tagId, editText.getText().toString());

                    editor.putBoolean("tracking", true);
                    editor.commit();

                    finish();
                }
            });
        } else {
            Log.d(getClass().getSimpleName(), "already exist");
            String tagName = getTagName(tagId);

            if (tagId.equals(getLastTagId())) {
                SharedPreferences.Editor editor = prefs.edit();
                boolean isTracking = ! prefs.getBoolean("tracking", true);
                editor.putBoolean("tracking", isTracking);
                if (isTracking) {

                }
            }

            // does not have to show the view.
            // enough to display the notification.
            showMainWindow(tagName);
        }
    }

    private String getLastTagId() {
        return null;
    }

    private void showMainWindow(String name) {
        setContentView(R.layout.main);
        final TextView textView = (TextView)findViewById(R.id.textView1);
        textView.setText(name);
        final Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();

                textView.setText("");
            }
        });
    }

    // from http://rgagnon.com/javadetails/java-0596.html
    private static final String HEXES = "0123456789ABCDEF";
    private String getHex(byte[] raw) {
        if (raw == null)
            return null;
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw)
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        return hex.toString();
    }

    private boolean isBrandnew(final String tagName) {
        String stored = prefs.getString(tagName, null);
        return stored == null;
    }

    private String getTagName(final String tagId) {
        String tagName = prefs.getString(tagId, null);
        return tagName;
    }
}
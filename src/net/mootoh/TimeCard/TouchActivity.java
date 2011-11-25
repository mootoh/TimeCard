package net.mootoh.TimeCard;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TouchActivity extends NavigationActivity {
    final String TAG;
    private static final String PREFS_NAME = "net.mootoh.TouchTracker";
    private SharedPreferences prefs;
    boolean isTracking;
    ArrayList <String> tagHistory;

    public TouchActivity() {
        TAG = getClass().getSimpleName();
    }
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

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (! NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            showMainWindow("");
            return;
        }

        final String tagId = getTagId(intent);
        if (tagId == null) return;

        if (isBrandnew(tagId)) {
            Log.d(TAG, "brand new");
            Intent newTagIntent = new Intent();
            newTagIntent.putExtra("tagId", tagId);
            newTagIntent.setClass(this, NewTagActivity.class);
            startActivity(newTagIntent);
        } else {
            Log.d(TAG, "already exist");
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

    private String getTagId(Intent intent) {
        Tag tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            Log.d(TAG, "Tag not found");
            return null;
        }

        byte[] idBytes = tag.getId();
        if (idBytes == null)
            return null;

        final String tagId = getHex(idBytes);
        Log.d(TAG, "tag id=" + tagId);
        return tagId;
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
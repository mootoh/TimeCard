package org.deadbeaf.TouchTracker;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TouchTrackerActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (! NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Log.e(getClass().getSimpleName(), "Unknown intent " + intent);
            return;
        }
        Tag tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null)
            return;
        byte[] idBytes = tag.getId();
        if (idBytes == null)
            return;
        String tagId = getHex(idBytes);
        Log.d(getClass().getSimpleName(), "tag id=" + tagId);

        if (isBrandnew(tagId)) {
            setContentView(R.layout.main);
            TextView textView = (TextView)findViewById(R.id.textView1);
            textView.setText(tagId);
        } else {
            setContentView(R.layout.main);
            String tagName = getTagName(tagId);
            TextView textView = (TextView)findViewById(R.id.textView1);
            textView.setText(tagName);
        }
    }

    // from http://rgagnon.com/javadetails/java-0596.html
    static final String HEXES = "0123456789ABCDEF";
    private String getHex(byte[] raw) {
        if (raw == null)
            return null;
        final StringBuilder hex = new StringBuilder(2 * raw.length);
        for (final byte b : raw)
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
        return hex.toString();
    }

    private boolean isBrandnew(final String tagName) {
        return true;
    }

    private String getTagName(final String tagId) {
        return tagId;
    }
}

package net.mootoh.TimeCard;

import java.sql.SQLException;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class TouchActivity extends android.app.Activity {
    final String TAG;

    public TouchActivity() {
        TAG = getClass().getSimpleName();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (! NfcAdapter.ACTION_TECH_DISCOVERED.equals(action))
            return;

        final String tagId = getTagId(intent);
        if (tagId == null)
            return;

        TagStore tagStore = new TagStore(this);
        if (tagStore.isBrandNewTag(tagId)) {
            Intent newTagIntent = new Intent();
            newTagIntent.putExtra("tagId", tagId);
            newTagIntent.setClass(this, NewTagActivity.class);
            startActivity(newTagIntent);
            finish();
            return;
        }

        Tag currentTag = tagStore.currentTag();
        try {
            if (currentTag != null && currentTag.id.equals(tagId)) {
                tagStore.stopCurrentTag();
                Toast.makeText(this, currentTag.name + " end.", Toast.LENGTH_SHORT).show();
            } else {
                tagStore.startTag(tagId);
                Toast.makeText(this, tagStore.getTagName(tagId) + " start.", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Log.e(TAG, "Cannot start/stop the tag:" + tagId);
            return;
        }

        finish();
    }

    private String getTagId(Intent intent) {
        android.nfc.Tag tag = (android.nfc.Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) {
            Log.w(TAG, "Tag not found");
            return null;
        }

        byte[] idBytes = tag.getId();
        if (idBytes == null)
            return null;

        final String tagId = getHex(idBytes);
        Log.w(TAG, "tag id=" + tagId);
        return tagId;
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
}
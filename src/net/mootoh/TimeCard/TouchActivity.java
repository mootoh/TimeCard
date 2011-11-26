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
        if (! NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            finish();
        }

        final String tagId = getTagId(intent);
        if (tagId == null) finish();

        TagStore tagStore = new TagStore(this);
        Tag currentTag = tagStore.currentTag();
        if (currentTag != null && currentTag.id.equals(tagId)) {
            try {
                tagStore.stopTag(currentTag.id);
            } catch (SQLException e) {
                Log.e(TAG, "cannot stop the tag:" + currentTag.id);
                finish();
            }
            Toast.makeText(this, currentTag.name + " end.", Toast.LENGTH_SHORT).show();
        } else { // different tag
            if (tagStore.isBrandNewTag(tagId)) {
                Intent newTagIntent = new Intent();
                newTagIntent.putExtra("tagId", tagId);
                newTagIntent.setClass(this, NewTagActivity.class);
                startActivity(newTagIntent);
                finish();
            }
            if (currentTag != null) {
                try {
                    tagStore.stopTag(currentTag.id);
                } catch (SQLException e) {
                    Log.e(TAG, "Cannot stop the current tag:" + currentTag.id);
                    finish();
                }
                Toast.makeText(this, currentTag.name + " end.", Toast.LENGTH_SHORT).show();
            }
            try {
                tagStore.startTag(tagId);
            } catch (SQLException e) {
                Log.e(TAG, "Cannot start the tag:" + tagId);
                finish();
            }
            Toast.makeText(this, tagStore.getTagName(tagId) + " start.", Toast.LENGTH_SHORT).show();
        }

        finish();
    }

    private String getTagId(Intent intent) {
        android.nfc.Tag tag = (android.nfc.Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
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
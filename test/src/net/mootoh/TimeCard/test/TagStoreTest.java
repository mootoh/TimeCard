package net.mootoh.TimeCard.test;

import java.sql.SQLException;

import net.mootoh.TimeCard.Tag;
import net.mootoh.TimeCard.TagStore;
import android.test.AndroidTestCase;

public final class TagStoreTest extends AndroidTestCase {
    TagStore tagStore;
    final String TAG_ID = "777";

    public void setUp() {
        tagStore = new TagStore(getContext());
        tagStore.reset();
    }

    public void testAddDeleteTag() throws Exception {
        assertEquals(0, tagStore.getTags().length);
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");

        Tag[] tags = tagStore.getTags();
        assertEquals(1, tags.length);

        assertEquals(TAG_ID, tags[0].id);
        tagStore.deleteTag(TAG_ID);
        assertEquals(0, tagStore.getTags().length);
    }

    public void testCurrentTagId() throws SQLException {
        String currentTagId = tagStore.currentTagId();
        assertNull(currentTagId);
    }

    public void testStartTag() throws SQLException {
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        tagStore.startTag(TAG_ID);
        String currentTagId = tagStore.currentTagId();
        assertEquals(TAG_ID, currentTagId);
    }

    public void testStoptTag() throws SQLException {
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        tagStore.stopTag(TAG_ID);
        String currentTagId = tagStore.currentTagId();
        assertNotSame(TAG_ID, currentTagId);
    }
}
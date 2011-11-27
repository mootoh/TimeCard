package net.mootoh.TimeCard.test;

import java.sql.SQLException;

import net.mootoh.TimeCard.Tag;
import net.mootoh.TimeCard.TagStore;
import android.test.AndroidTestCase;

public final class TagStoreTest extends AndroidTestCase {
    TagStore tagStore;
    String TAG = "TagStoreTest";
    final String TAG_ID = "777";

    public void setUp() {
        tagStore = new TagStore(getContext());
        try {
            tagStore.reset();
        } catch (SQLException e) {
            android.util.Log.e(getClass().getSimpleName(), "failed in resetting database");
        }
    }

    public void testAddDeleteTag() throws Exception {
        assertEquals(1, tagStore.getTags().length);

        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        Tag[] tags = tagStore.getTags();
        assertEquals(2, tags.length);
        assertEquals(TAG_ID, tags[1].id);

        tagStore.addTag("another tag", "another", "#00ff00");
        tags = tagStore.getTags();
        assertEquals(3, tags.length);
        assertEquals("another tag", tags[2].id);

        tagStore.deleteTag(TAG_ID);
        assertEquals(2, tagStore.getTags().length);
    }

    public void testCurrentTag() throws SQLException {
        Tag currentTag = tagStore.currentTag();
        assertEquals("VOID", currentTag.id);
    }

    public void testStartTag() throws SQLException {
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        tagStore.startTag(TAG_ID);
        Tag currentTag = tagStore.currentTag();
        assertEquals(TAG_ID, currentTag.id);
        assertEquals("My Tag", currentTag.name);
    }

    public void testStoptTag() throws SQLException, InterruptedException {
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        Thread.sleep(1000);
        tagStore.stopCurrentTag();
        Tag currentTag = tagStore.currentTag();
        assertEquals("VOID", currentTag.id);
    }

    public void testIsBrandNew() throws Exception {
        assertTrue(tagStore.isBrandNewTag("000"));
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        assertTrue(tagStore.isBrandNewTag("000"));
        assertFalse(tagStore.isBrandNewTag(TAG_ID));

        tagStore.deleteTag(TAG_ID);
        assertTrue(tagStore.isBrandNewTag(TAG_ID));
    }

    public void testTagName() throws SQLException {
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        assertEquals("My Tag",tagStore.getTagName(TAG_ID));
    }
}
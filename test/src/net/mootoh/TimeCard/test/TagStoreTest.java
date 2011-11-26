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

    public void testCurrentTag() throws SQLException {
        Tag currentTag = tagStore.currentTag();
        assertNull(currentTag);
    }

    public void testStartTag() throws SQLException {
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        tagStore.startTag(TAG_ID);
        Tag currentTag = tagStore.currentTag();
        assertEquals(TAG_ID, currentTag.id);
        assertEquals("My Tag", currentTag.name);
    }

    public void testStoptTag() throws SQLException {
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        tagStore.stopTag(TAG_ID);
        Tag currentTag = tagStore.currentTag();
        assertNull(currentTag);
    }

    public void testIsBrandNew() throws Exception {
        assertTrue(tagStore.isBrandNewTag("000"));
        tagStore.addTag(TAG_ID, "My Tag", "#ff0000");
        assertTrue(tagStore.isBrandNewTag("000"));
        assertFalse(tagStore.isBrandNewTag(TAG_ID));

        tagStore.deleteTag(TAG_ID);
        assertTrue(tagStore.isBrandNewTag(TAG_ID));
    }
}
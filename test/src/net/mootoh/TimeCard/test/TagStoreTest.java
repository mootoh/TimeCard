package net.mootoh.TimeCard.test;

import java.sql.SQLException;

import net.mootoh.TimeCard.Tag;
import net.mootoh.TimeCard.TagStore;
import android.test.AndroidTestCase;

public final class TagStoreTest extends AndroidTestCase {
    TagStore tagStore;

    public void setUp() {
        tagStore = new TagStore(getContext());
        tagStore.reset();
    }

    public void testAddDeleteTag() throws Exception {
        assertEquals(0, tagStore.getTags().length);
        tagStore.addTag("12", "My Tag", "#ff0000");

        Tag[] tags = tagStore.getTags();
        assertEquals(1, tags.length);

        assertEquals("12", tags[0].id);
        tagStore.deleteTag("12");
        assertEquals(0, tagStore.getTags().length);
    }

    public void testCurrentTagId() throws SQLException {
        String currentTagId = tagStore.currentTagId();
        assertNull(currentTagId);
    }
}
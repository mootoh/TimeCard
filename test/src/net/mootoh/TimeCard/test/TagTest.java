package net.mootoh.TimeCard.test;

import java.text.ParseException;

import net.mootoh.TimeCard.Tag;
import android.test.AndroidTestCase;

public final class TagTest extends AndroidTestCase {
    public void testParseDate() throws ParseException {
        Tag tag = new Tag("1", "My Tag", "black", "2011-11-26 07:24:07", true);
        assertNotNull(tag);
    }
}

package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class WebsiteTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Website(null));
    }

    @Test
    public void constructor_invalidWebsite_throwsIllegalArgumentException() {
        String invalidWebsite = " ";
        assertThrows(IllegalArgumentException.class, () -> new Website(invalidWebsite));
    }

    @Test
    public void isValidWebsite() {
        // null website
        assertThrows(NullPointerException.class, () -> Website.isValidWebsite(null));

        // invalid websites
        assertFalse(Website.isValidWebsite(" ")); // spaces only
        assertFalse(Website.isValidWebsite("  abc")); // starts with whitespace

        // valid websites
        assertTrue(Website.isValidWebsite("")); // empty string
        assertTrue(Website.isValidWebsite("https://www.google.com"));
        assertTrue(Website.isValidWebsite("example.com"));
        assertFalse(Website.isValidWebsite("  example.com ")); // spaces in between (invalid)
    }

    @Test
    public void equals() {
        Website website = new Website("valid.com");

        // same object -> returns true
        assertTrue(website.equals(website));

        // same values -> returns true
        Website websiteCopy = new Website("valid.com");
        assertTrue(website.equals(websiteCopy));

        // different values -> returns false
        assertFalse(website.equals(new Website("other.com")));

        // different types -> returns false
        assertFalse(website.equals(1));

        // null -> returns false
        assertFalse(website.equals(null));
    }

    @Test
    public void hashCodeTest() {
        Website website = new Website("valid.com");

        // same value -> same hashcode
        Website websiteCopy = new Website("valid.com");
        assertTrue(website.hashCode() == websiteCopy.hashCode());

        // different value -> different hashcode
        assertFalse(website.hashCode() == new Website("other.com").hashCode());
    }
}

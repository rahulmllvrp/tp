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
    public void isValidWebsite() {
        // null website
        assertThrows(NullPointerException.class, () -> Website.isValidWebsite(null));

        // valid websites
        assertTrue(Website.isValidWebsite("")); // empty string
        assertFalse(Website.isValidWebsite(" ")); // spaces only
        assertTrue(Website.isValidWebsite("https://www.google.com"));
        assertTrue(Website.isValidWebsite("https://www.example.com"));
        assertTrue(Website.isValidWebsite("example.com"));
    }

    @Test
    public void equals() {
        Website website = new Website("https://www.example.com");

        // same values -> returns true
        assertTrue(website.equals(new Website("https://www.example.com")));

        // same object -> returns true
        assertTrue(website.equals(website));

        // null -> returns false
        assertFalse(website.equals(null));

        // different types -> returns false
        assertFalse(website.equals(5.0f));

        // different values -> returns false
        assertFalse(website.equals(new Website("https://www.google.com")));
    }
}

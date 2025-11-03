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

        // invalid websites - those with spaces or invalid format
        assertFalse(Website.isValidWebsite(" ")); // spaces only
        assertFalse(Website.isValidWebsite("  abc")); // starts with whitespace
        assertFalse(Website.isValidWebsite("  example.com")); // starts with space
        assertFalse(Website.isValidWebsite(" example.com ")); // starts and ends with space
        assertFalse(Website.isValidWebsite("   ")); // multiple spaces only
        assertFalse(Website.isValidWebsite("    a")); // multiple spaces before content
        assertFalse(Website.isValidWebsite("www.go.co www.jjij.co")); // multiple domains with space
        assertFalse(Website.isValidWebsite("example .com")); // space in domain
        assertFalse(Website.isValidWebsite("example. com")); // space after dot
        assertFalse(Website.isValidWebsite("example")); // no TLD
        assertFalse(Website.isValidWebsite("example.")); // ends with dot
        assertFalse(Website.isValidWebsite(".com")); // starts with dot
        assertFalse(Website.isValidWebsite("example.c")); // TLD too short

        // valid websites - empty string (for optional website)
        assertTrue(Website.isValidWebsite("")); // empty string is valid for optional field

        // valid websites - various URL formats
        assertTrue(Website.isValidWebsite("https://www.google.com"));
        assertTrue(Website.isValidWebsite("http://example.com"));
        assertTrue(Website.isValidWebsite("example.com"));
        assertTrue(Website.isValidWebsite("www.example.com"));
        assertTrue(Website.isValidWebsite("https://example.com/path"));
        assertTrue(Website.isValidWebsite("https://example.com/path?query=value"));
        assertTrue(Website.isValidWebsite("https://subdomain.example.com"));
        assertTrue(Website.isValidWebsite("subdomain.example.com"));
        assertTrue(Website.isValidWebsite("my-site.example.co"));
        assertTrue(Website.isValidWebsite("test123.example.org"));
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

        // empty string websites are equal
        Website emptyWebsite1 = new Website("");
        Website emptyWebsite2 = new Website("");
        assertTrue(emptyWebsite1.equals(emptyWebsite2));

        // empty vs non-empty -> returns false
        assertFalse(website.equals(emptyWebsite1));
        assertFalse(emptyWebsite1.equals(website));
    }

    @Test
    public void hashCodeTest() {
        Website website = new Website("valid.com");

        // same value -> same hashcode
        Website websiteCopy = new Website("valid.com");
        assertTrue(website.hashCode() == websiteCopy.hashCode());
        // different value -> different hashcode
        assertFalse(website.hashCode() == new Website("other.com").hashCode());

        // empty websites have same hashcode
        Website emptyWebsite1 = new Website("");
        Website emptyWebsite2 = new Website("");
        assertTrue(emptyWebsite1.hashCode() == emptyWebsite2.hashCode());
    }

    @Test
    public void constructor_emptyString_success() {
        // empty string is valid for optional website field
        Website emptyWebsite = new Website("");
        assertTrue(emptyWebsite.equals(new Website("")));
    }

    @Test
    public void toStringTest() {
        // non-empty website
        Website website = new Website("https://www.example.com");
        assertTrue(website.toString().equals("https://www.example.com"));

        // empty website
        Website emptyWebsite = new Website("");
        assertTrue(emptyWebsite.toString().equals(""));
    }
}

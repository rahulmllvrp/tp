package seedu.address.model.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EventNameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new EventName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new EventName(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> EventName.isValidName(null));

        // invalid name
        assertFalse(EventName.isValidName("")); // empty string
        assertFalse(EventName.isValidName(" ")); // spaces only
        assertFalse(EventName.isValidName("^")); // only non-alphanumeric characters
        assertFalse(EventName.isValidName("peter*")); // contains non-alphanumeric characters

        // valid name
        assertTrue(EventName.isValidName("peter jack")); // alphabets only
        assertTrue(EventName.isValidName("12345")); // numbers only
        assertTrue(EventName.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(EventName.isValidName("Capital Tan")); // with capital letters
        assertTrue(EventName.isValidName("David Roger Jackson Ray Jr 2nd")); // long names
        assertTrue(EventName.isValidName("O'Malley")); // with apostrophe
        assertTrue(EventName.isValidName("Event-Name")); // with hyphen
    }

    @Test
    public void equals() {
        EventName eventName = new EventName("Valid Event");

        // same object -> returns true
        assertTrue(eventName.equals(eventName));

        // same values -> returns true
        EventName eventNameCopy = new EventName("Valid Event");
        assertTrue(eventName.equals(eventNameCopy));

        // same values, different casing -> returns true
        EventName eventNameDifferentCase = new EventName("valid event");
        assertTrue(eventName.equals(eventNameDifferentCase));

        // different values -> returns false
        assertFalse(eventName.equals(new EventName("Other Event")));

        // null -> returns false
        assertFalse(eventName.equals(null));

        // different type -> returns false
        assertFalse(eventName.equals(5));
    }

    @Test
    public void hashCode_consistentWithEquals() {
        EventName eventName1 = new EventName("Valid Event");
        EventName eventName2 = new EventName("Valid Event");
        EventName eventName3 = new EventName("valid event"); // Same value, different case
        EventName eventName4 = new EventName("Other Event");

        // Consistent with equals for same values
        assertTrue(eventName1.hashCode() == eventName2.hashCode());
        // Consistent with equals for same values, different casing
        assertTrue(eventName1.hashCode() == eventName3.hashCode());
        // Inconsistent with equals for different values
        assertFalse(eventName1.hashCode() == eventName4.hashCode());
    }
}

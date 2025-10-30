package seedu.address.model.event;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EventDateTest {

    @Test
    public void isValidDate() {
        // null date
        assertThrows(NullPointerException.class, () -> EventDate.isValidDate(null));

        // invalid dates
        assertFalse(EventDate.isValidDate("")); // empty string
        assertFalse(EventDate.isValidDate(" ")); // spaces only
        assertFalse(EventDate.isValidDate("12-13-2024")); // invalid month
        assertFalse(EventDate.isValidDate("32-12-2024")); // invalid day
        assertFalse(EventDate.isValidDate("00-12-2024")); // day cannot be 0
        assertFalse(EventDate.isValidDate("12-00-2024")); // month cannot be 0
        assertFalse(EventDate.isValidDate("31-04-2024")); // April has 30 days
        assertFalse(EventDate.isValidDate("31-02-2024")); // February never has 31 days
        assertFalse(EventDate.isValidDate("29-02-2023")); // 2023 is not a leap year
        assertFalse(EventDate.isValidDate("29-02-2100")); // 2100 is not a leap year (divisible by 100 but not 400)

        // invalid dates (past dates)
        assertFalse(EventDate.isValidDate("01-01-2024")); // past date
        assertFalse(EventDate.isValidDate("29-02-2024")); // past leap year date
        assertFalse(EventDate.isValidDate("28-02-2023")); // past date

        // valid dates (today or future)
        assertTrue(EventDate.isValidDate("01-01-2030")); // today (example future date)
        assertTrue(EventDate.isValidDate("02-01-2030")); // tomorrow (example future date)
        assertTrue(EventDate.isValidDate("31-12-2030")); // end of year
        assertTrue(EventDate.isValidDate("29-02-2032")); // future leap year Feb 29 (2032 is divisible by 4)
        assertTrue(EventDate.isValidDate("30-04-2031")); // April has 30 days
        assertTrue(EventDate.isValidDate("31-05-2031")); // May has 31 days
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        String invalidDate = "29-02-2023"; // not a leap year
        assertThrows(IllegalArgumentException.class, EventDate.MESSAGE_CONSTRAINTS, () ->
                new EventDate(invalidDate));
    }

    @Test
    public void constructor_invalidLeapYearDate_throwsWithCorrectMessage() {
        String invalidDate = "29-02-2023"; // not a leap year
        try {
            new EventDate(invalidDate);
            throw new AssertionError("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("valid calendar date"));
        }
    }

    @Test
    public void constructor_validDate_success() {
        // This should not throw any exception
        new EventDate("31-10-2025"); // tomorrow
        new EventDate("28-02-2026"); // future date
        new EventDate("31-12-2025"); // end of year
    }
}

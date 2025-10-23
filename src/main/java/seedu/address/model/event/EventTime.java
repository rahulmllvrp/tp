package seedu.address.model.event;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an Event's time in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTime(String)}
 */
public class EventTime {

    public static final String MESSAGE_CONSTRAINTS =
            "Times should be in the format HH:mm";

    public static final String VALIDATION_REGEX = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";

    public final String value;

    /**
     * Constructs an {@code EventTime}.
     *
     * @param time A valid time.
     */
    public EventTime(String time) {
        requireNonNull(time);
        checkArgument(isValidTime(time), MESSAGE_CONSTRAINTS);
        value = time;
    }

    /**
     * Returns true if a given string is a valid time.
     */
    public static boolean isValidTime(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EventTime)) {
            return false;
        }

        EventTime otherTime = (EventTime) other;
        return value.equals(otherTime.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}

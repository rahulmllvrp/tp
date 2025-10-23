package seedu.address.model.event;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents an Event in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Event {

    // Identity fields
    private final EventName eventName;
    private final EventDate eventDate;
    private final EventTime eventTime;

    /**
     * Every field must be present and not null.
     */
    public Event(EventName eventName, EventDate eventDate, EventTime eventTime) {
        requireAllNonNull(eventName, eventDate, eventTime);
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public EventName getName() {
        return eventName;
    }

    public EventDate getDate() {
        return eventDate;
    }

    public EventTime getTime() {
        return eventTime;
    }

    /**
     * Returns true if both events have the same name.
     * This defines a weaker notion of equality between two events.
     */
    public boolean isSameEvent(Event otherEvent) {
        if (otherEvent == this) {
            return true;
        }

        return otherEvent != null
                && otherEvent.getName().equals(getName());
    }

    /**
     * Returns true if both events have the same identity and data fields.
     * This defines a stronger notion of equality between two events.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Event)) {
            return false;
        }

        Event otherEvent = (Event) other;
        return eventName.equals(otherEvent.eventName)
                && eventDate.equals(otherEvent.eventDate)
                && eventTime.equals(otherEvent.eventTime);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(eventName, eventDate, eventTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", eventName)
                .add("date", eventDate)
                .add("time", eventTime)
                .toString();
    }
}

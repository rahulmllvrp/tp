package seedu.address.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.model.person.PersonId;

/**
 * Represents an Event in the absolut sinema.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Event {
    // Identity fields
    private final EventName eventName;
    private final EventDate eventDate;
    private final EventTime eventTime;
    private final List<PersonId> participants;

    /**
     * Initialize with empty participants list
     */
    public Event(EventName eventName, EventDate eventDate, EventTime eventTime) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.participants = new ArrayList<>();
    }

    /**
     * Initialize with participants list
     */
    public Event(EventName eventName, EventDate eventDate, EventTime eventTime, List<PersonId> participants) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.participants = participants;
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

    public List<PersonId> getParticipants() {
        return participants;
    }

    /**
     * Checks if this Event is the same as another Event.
     * Events are the same if they have the same name.
     */
    public boolean isSameEvent(Event other) {
        if (this == other) {
            return true;
        }
        return this.eventName.equals(other.eventName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        Event other = (Event) o;
        return this.eventName.equals(other.eventName)
                && this.eventDate.equals(other.eventDate)
                && this.eventTime.equals(other.eventTime)
                && this.participants.equals(other.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, eventDate, eventTime, participants);
    }
}

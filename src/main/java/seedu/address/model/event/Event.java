package seedu.address.model.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import seedu.address.model.person.Budget;
import seedu.address.model.person.PersonId;

/**
 * Represents an Event in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Event {
    // Identity fields
    private final EventName eventName;
    private final EventDate eventDate;
    private final EventTime eventTime;
    private final List<PersonId> participants;
    private final Budget initialBudget;
    private final Budget remainingBudget;

    /**
     * Initialize with empty participants list
     */
    public Event(EventName eventName, EventDate eventDate, EventTime eventTime, Budget budget) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.participants = new ArrayList<>();
        this.initialBudget = budget;
        this.remainingBudget = budget;
    }

    /**
     * Initialize with participants list
     */
    public Event(EventName eventName, EventDate eventDate, EventTime eventTime, List<PersonId> participants,
            Budget initialBudget, Budget remainingBudget) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.participants = new ArrayList<>(participants); // Defensive copy
        this.initialBudget = initialBudget;
        this.remainingBudget = remainingBudget;
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

    public Budget getInitialBudget() {
        return initialBudget;
    }

    public Budget getRemainingBudget() {
        return remainingBudget;
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
                && this.participants.equals(other.participants)
                && this.initialBudget.equals(other.initialBudget)
                && this.remainingBudget.equals(other.remainingBudget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, eventDate, eventTime, participants, initialBudget, remainingBudget);
    }

    @Override
    public String toString() {
        return "Event{" + "eventName=" + eventName
                + ", eventDate=" + eventDate
                + ", eventTime=" + eventTime
                + ", participants=" + participants
                + ", initialBudget=" + initialBudget
                + ", remainingBudget=" + remainingBudget
                + '}';
    }
}

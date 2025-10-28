package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventTime;
import seedu.address.model.person.Budget;
import seedu.address.model.person.PersonId;

/**
 * A utility class to help build Event objects.
 */
public class EventBuilder {

    public static final String DEFAULT_NAME = "Birthday Party";
    public static final String DEFAULT_DATE = "12-12-2025";
    public static final String DEFAULT_TIME = "18:00";
    public static final String DEFAULT_BUDGET = "1000";

    private EventName name;
    private EventDate date;
    private EventTime time;
    private List<PersonId> participants;
    private Budget budget;
    private Budget remainingBudget;

    /**
     * Creates a {@code EventBuilder} with the default details.
     */
    public EventBuilder() {
        name = new EventName(DEFAULT_NAME);
        date = new EventDate(DEFAULT_DATE);
        time = new EventTime(DEFAULT_TIME);
        participants = new ArrayList<>();
        budget = new Budget(DEFAULT_BUDGET);
        remainingBudget = new Budget(DEFAULT_BUDGET);
    }

    /**
     * Initializes the EventBuilder with the data of {@code eventToCopy}.
     */
    public EventBuilder(Event eventToCopy) {
        name = eventToCopy.getName();
        date = eventToCopy.getDate();
        time = eventToCopy.getTime();
        participants = new ArrayList<>(eventToCopy.getParticipants());
        budget = eventToCopy.getInitialBudget(); // Use initial budget for copying
        remainingBudget = eventToCopy.getRemainingBudget();
    }

    /**
     * Sets the {@code EventName} of the {@code Event} that we are building.
     */
    public EventBuilder withName(String name) {
        this.name = new EventName(name);
        return this;
    }

    /**
     * Sets the {@code EventDate} of the {@code Event} that we are building.
     */
    public EventBuilder withDate(String date) {
        this.date = new EventDate(date);
        return this;
    }

    /**
     * Sets the {@code EventTime} of the {@code Event} that we are building.
     */
    public EventBuilder withTime(String time) {
        this.time = new EventTime(time);
        return this;
    }

    /**
     * Sets the {@code PersonId} of the {@code Event} that we are building.
     */
    public EventBuilder withParticipants(PersonId ... participants) {
        this.participants = new ArrayList<>(Arrays.asList(participants));
        return this;
    }

    /**
     * Sets the {@code Budget} of the {@code Event} that we are building.
     */
    public EventBuilder withBudget(String budget) {
        this.budget = new Budget(budget);
        return this;
    }

    /**
     * Sets the {@code remainingBudget} of the {@code Event} that we are building.
     */
    public EventBuilder withRemainingBudget(String remainingBudget) {
        this.remainingBudget = new Budget(remainingBudget);
        return this;
    }

    public Event build() {
        return new Event(name, date, time, participants, budget, remainingBudget);
    }
}

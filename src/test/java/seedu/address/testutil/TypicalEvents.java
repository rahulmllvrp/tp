package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.event.Event;

/**
 * A utility class containing a list of {@code Event} objects to be used in tests.
 */
public class TypicalEvents {

    public static final Event BIRTHDAY_PARTY = new EventBuilder().withName("Birthday Party")
            .withDate("12-12-2025").withTime("18:00").build();
    public static final Event MEETING = new EventBuilder().withName("Project Meeting")
            .withDate("01-01-2024").withTime("10:00").build();
    public static final Event CONFERENCE = new EventBuilder().withName("Annual Conference")
            .withDate("20-03-2025").withTime("09:00").build();

    // Manually added
    public static final Event WORKSHOP = new EventBuilder().withName("Workshop")
            .withDate("15-07-2024").withTime("14:00").build();
    public static final Event LUNCH = new EventBuilder().withName("Team Lunch")
            .withDate("05-02-2025").withTime("12:30").build();

    private TypicalEvents() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical events.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Event event : getTypicalEvents()) {
            ab.addEvent(event);
        }
        return ab;
    }

    public static List<Event> getTypicalEvents() {
        return new ArrayList<>(Arrays.asList(BIRTHDAY_PARTY, MEETING, CONFERENCE));
    }
}

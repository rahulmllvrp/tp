package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.event.Event;
import seedu.address.model.person.Person;

/**
 * Represents a snapshot of an AbsolutSinema at a specific point in time.
 * Used for implementing undo functionality.
 */
public class AbsolutSinemaSnapshot {
    private final List<Person> persons;
    private final List<Event> events;
    private final String operationDescription;

    /**
     * Creates a snapshot of the given AbsolutSinema.
     *
     * @param absolutSinema The AbsolutSinema to create a snapshot of.
     * @param operationDescription Description of the operation that was performed.
     */
    public AbsolutSinemaSnapshot(ReadOnlyAbsolutSinema absolutSinema, String operationDescription) {
        this.persons = new ArrayList<>(absolutSinema.getPersonList());
        this.events = new ArrayList<>(absolutSinema.getEventList());
        this.operationDescription = operationDescription;
    }

    /**
     * Restores an AbsolutSinema from this snapshot.
     *
     * @return A new AbsolutSinema with the data from this snapshot.
     */
    public AbsolutSinema restoreAbsolutSinema() {
        AbsolutSinema restoredBook = new AbsolutSinema();
        for (Person person : persons) {
            restoredBook.addPerson(person);
        }
        for (Event event : events) {
            restoredBook.addEvent(event);
        }
        return restoredBook;
    }

    /**
     * Returns the description of the operation that was performed.
     *
     * @return The operation description.
     */
    public String getOperationDescription() {
        return operationDescription;
    }
}

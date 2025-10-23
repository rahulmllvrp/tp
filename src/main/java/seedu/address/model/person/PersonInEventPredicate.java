package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.model.event.Event;

/**
 * Tests that a {@code Person} is assigned to a {@code Event}
 */
public class PersonInEventPredicate implements Predicate<Person> {
    private final Event event;

    public PersonInEventPredicate(Event event) {
        this.event = event;
    }

    @Override
    public boolean test(Person person) {
        PersonId personId = person.getId();
        return event.getParticipants().contains(personId);
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof PersonInEventPredicate
                && event.equals(((PersonInEventPredicate) other).event));
    }
}

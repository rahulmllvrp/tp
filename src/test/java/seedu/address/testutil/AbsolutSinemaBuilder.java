package seedu.address.testutil;

import seedu.address.model.AbsolutSinema;
import seedu.address.model.person.Person;

/**
 * A utility class to help with building AbsolutSinema objects.
 * Example usage: <br>
 *     {@code AbsolutSinema ab = new AbsolutSinemaBuilder().withPerson("John", "Doe").build();}
 */
public class AbsolutSinemaBuilder {

    private AbsolutSinema absolutSinema;

    public AbsolutSinemaBuilder() {
        absolutSinema = new AbsolutSinema();
    }

    public AbsolutSinemaBuilder(AbsolutSinema absolutSinema) {
        this.absolutSinema = absolutSinema;
    }

    /**
     * Adds a new {@code Person} to the {@code AbsolutSinema} that we are building.
     */
    public AbsolutSinemaBuilder withPerson(Person person) {
        absolutSinema.addPerson(person);
        return this;
    }

    public AbsolutSinema build() {
        return absolutSinema;
    }
}

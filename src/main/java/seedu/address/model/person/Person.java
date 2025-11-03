package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final PersonId id;

    // Data fields
    private final Website website;
    private final Set<Tag> tags = new HashSet<>();
    private final Budget budget;

    /**
     * Every field must be present and not null. Used for first creation of a person, where id is created.
     */
    public Person(Name name, Phone phone, Email email, Website website, Set<Tag> tags, Budget budget) {
        requireAllNonNull(name, phone, email, website, tags, budget);
        this.id = new PersonId();
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.tags.addAll(tags);
        this.budget = budget;
    }

    /**
     * Every field must be present and not null. Creates a person object with a given PersonId.
     */
    public Person(PersonId id, Name name, Phone phone, Email email, Website website, Set<Tag> tags, Budget budget) {
        requireAllNonNull(id, name, phone, email, website, tags, budget);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.tags.addAll(tags);
        this.budget = budget;
    }

    public PersonId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Website getWebsite() {
        return website;
    }

    public Budget getBudget() {
        return budget;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name (case-insensitive, no trailing spaces).
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && website.equals(otherPerson.website)
                && tags.equals(otherPerson.tags)
                && budget.equals(otherPerson.budget);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, website, tags, budget);
    }

    @Override
    public String toString() {
        return Person.class.getCanonicalName() + "{name=" + name
                + ", phone=" + phone
                + ", email=" + email
                + ", website=" + website
                + ", tags=" + tags
                + ", budget=" + budget + "}";
    }
}

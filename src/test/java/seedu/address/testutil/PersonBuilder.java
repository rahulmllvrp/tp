package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Budget;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Website;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_WEBSITE = "https://www.example.com";
    public static final String DEFAULT_BUDGET = "100";

    private Name name;
    private Phone phone;
    private Email email;
    private Website website;
    private Set<Tag> tags;
    private Budget budget;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        website = new Website(DEFAULT_WEBSITE);
        tags = new HashSet<>();
        budget = new Budget(DEFAULT_BUDGET);
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        website = personToCopy.getWebsite();
        tags = new HashSet<>(personToCopy.getTags());
        budget = personToCopy.getBudget();
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Website} of the {@code Person} that we are building.
     */
    public PersonBuilder withWebsite(String website) {
        this.website = new Website(website);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Sets the {@code Budget} of the {@code Person} that we are building.
     */
    public PersonBuilder withBudget(String budget) {
        this.budget = new Budget(budget);
        return this;
    }

    public Person build() {
        return new Person(name, phone, email, website, tags, budget);
    }

}

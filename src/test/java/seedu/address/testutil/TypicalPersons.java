package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_BUDGET_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_BUDGET_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEBSITE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_WEBSITE_BOB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000001")))
            .withName("Alice Pauline")
            .withWebsite("https://www.example.com")
            .withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends")
            .withBudget("100").build();
    public static final Person BENSON = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000002")))
            .withName("Benson Meier")
            .withWebsite("https://www.example.com")
            .withEmail("johnd@example.com")
            .withPhone("98765432")
            .withTags("owesMoney", "friends")
            .withBudget("200").build();
    public static final Person CARL = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000003")))
            .withName("Carl Kurz")
            .withPhone("95352563")
            .withEmail("heinz@example.com")
            .withWebsite("https://www.example.com")
            .withBudget("300").build();
    public static final Person DANIEL = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000004")))
            .withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withWebsite("https://www.example.com")
            .withTags("friends").withBudget("400").build();
    public static final Person ELLE = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000005")))
            .withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withWebsite("https://www.example.com")
            .withBudget("500").build();
    public static final Person FIONA = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000006")))
            .withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withWebsite("https://www.example.com")
            .withBudget("600").build();
    public static final Person GEORGE = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000007")))
            .withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withWebsite("https://www.example.com")
            .withBudget("700").build();

    // Manually added
    public static final Person HOON = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000008")))
            .withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withWebsite("https://www.example.com")
            .withBudget("800").build();
    public static final Person IDA = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000009")))
            .withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withWebsite("https://www.example.com")
            .withBudget("900").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000010")))
            .withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withWebsite(VALID_WEBSITE_AMY)
            .withTags(VALID_TAG_FRIEND).withBudget(VALID_BUDGET_AMY).build();
    public static final Person BOB = new PersonBuilder()
            .withId(new PersonId(UUID.fromString("00000000-0000-0000-0000-000000000011")))
            .withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withWebsite(VALID_WEBSITE_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).withBudget(VALID_BUDGET_BOB).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}

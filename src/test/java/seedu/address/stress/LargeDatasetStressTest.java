package seedu.address.stress;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Budget;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Website;
import seedu.address.model.tag.Tag;

/**
 * Stress test for testing the application with large datasets.
 * This test creates thousands of contacts to verify performance and stability.
 */
public class LargeDatasetStressTest {

    private AddressBook addressBook;

    @BeforeEach
    public void setUp() {
        addressBook = new AddressBook();
    }

    @Test
    public void stressTest_add1000Persons_success() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            Person person = createTestPerson(i);
            addressBook.addPerson(person);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(1000, addressBook.getPersonList().size());
        System.out.println("Time to add 1000 persons: " + duration + "ms");
        assertTrue(duration < 5000, "Adding 1000 persons should take less than 5 seconds");
    }

    @Test
    public void stressTest_add5000Persons_success() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 5000; i++) {
            Person person = createTestPerson(i);
            addressBook.addPerson(person);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(5000, addressBook.getPersonList().size());
        System.out.println("Time to add 5000 persons: " + duration + "ms");
        assertTrue(duration < 20000, "Adding 5000 persons should take less than 20 seconds");
    }

    @Test
    public void stressTest_add10000Persons_success() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            Person person = createTestPerson(i);
            addressBook.addPerson(person);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(10000, addressBook.getPersonList().size());
        System.out.println("Time to add 10000 persons: " + duration + "ms");
        assertTrue(duration < 40000, "Adding 10000 persons should take less than 40 seconds");
    }

    @Test
    public void stressTest_searchIn1000Persons_success() {
        // Populate with 1000 persons
        for (int i = 0; i < 1000; i++) {
            Person person = createTestPerson(i);
            addressBook.addPerson(person);
        }

        // Search for specific person
        long startTime = System.currentTimeMillis();
        boolean found = addressBook.getPersonList().stream()
                .anyMatch(p -> p.getName().fullName.equals("Test Person 500"));
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertTrue(found);
        System.out.println("Time to search in 1000 persons: " + duration + "ms");
        assertTrue(duration < 100, "Searching should take less than 100ms");
    }

    @Test
    public void stressTest_addPersonsWithManyTags_success() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            Person person = createTestPersonWithManyTags(i, 10);
            addressBook.addPerson(person);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(1000, addressBook.getPersonList().size());
        System.out.println("Time to add 1000 persons with 10 tags each: " + duration + "ms");
        assertTrue(duration < 10000, "Adding persons with many tags should complete reasonably fast");
    }

    @Test
    public void stressTest_repeatedAddAndRemove_success() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 500; i++) {
            Person person = createTestPerson(i);
            addressBook.addPerson(person);
        }

        for (int i = 0; i < 250; i++) {
            addressBook.removePerson(addressBook.getPersonList().get(0));
        }

        for (int i = 500; i < 750; i++) {
            Person person = createTestPerson(i);
            addressBook.addPerson(person);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(500, addressBook.getPersonList().size());
        System.out.println("Time for repeated add/remove operations: " + duration + "ms");
        assertTrue(duration < 5000, "Repeated operations should complete efficiently");
    }

    /**
     * Creates a test person with a unique ID.
     */
    private Person createTestPerson(int id) {
        Name name = new Name("Test Person " + id);
        Phone phone = new Phone(String.format("%08d", 90000000 + id));
        Email email = new Email("test" + id + "@example.com");
        Website website = new Website(id % 2 == 0 ? "https://example" + id + ".com" : "");
        Budget budget = new Budget(String.valueOf(100 + id));
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("stress"));
        tags.add(new Tag("test" + (id % 10)));

        return new Person(name, phone, email, website, tags, budget);
    }

    /**
     * Creates a test person with many tags.
     */
    private Person createTestPersonWithManyTags(int id, int tagCount) {
        Name name = new Name("Test Person " + id);
        Phone phone = new Phone(String.format("%08d", 90000000 + id));
        Email email = new Email("test" + id + "@example.com");
        Website website = new Website("");
        Budget budget = new Budget(String.valueOf(100 + id));
        Set<Tag> tags = new HashSet<>();

        for (int i = 0; i < tagCount; i++) {
            tags.add(new Tag("tag" + i + "id" + id));
        }

        return new Person(name, phone, email, website, tags, budget);
    }
}

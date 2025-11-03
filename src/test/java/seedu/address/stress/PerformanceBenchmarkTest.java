package seedu.address.stress;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Budget;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Website;
import seedu.address.model.tag.Tag;

/**
 * Performance benchmark tests to measure operation timing.
 * These tests measure the performance of core operations.
 */
public class PerformanceBenchmarkTest {

    private Model model;
    private AddressBook addressBook;

    @BeforeEach
    public void setUp() {
        addressBook = new AddressBook();
        model = new ModelManager(addressBook, new UserPrefs());
    }

    @Test
    public void benchmark_addPerson_measureTime() {
        long[] times = new long[1000];

        for (int i = 0; i < 1000; i++) {
            Person person = createTestPerson(i);
            long start = System.nanoTime();
            model.addPerson(person);
            long end = System.nanoTime();
            times[i] = end - start;
        }

        long avgTime = calculateAverage(times);
        long maxTime = findMax(times);
        long minTime = findMin(times);

        System.out.println("=== Add Person Benchmark ===");
        System.out.println("Average time: " + (avgTime / 1000) + " microseconds");
        System.out.println("Max time: " + (maxTime / 1000) + " microseconds");
        System.out.println("Min time: " + (minTime / 1000) + " microseconds");

        // Average add operation should be fast (less than 1ms)
        assertTrue(avgTime < 1_000_000, "Average add operation should be under 1ms");
    }

    @Test
    public void benchmark_deletePerson_measureTime() {
        // First populate with 1000 persons
        for (int i = 0; i < 1000; i++) {
            Person person = createTestPerson(i);
            model.addPerson(person);
        }

        long[] times = new long[500];

        for (int i = 0; i < 500; i++) {
            Person personToDelete = model.getFilteredPersonList().get(0);
            long start = System.nanoTime();
            model.deletePerson(personToDelete);
            long end = System.nanoTime();
            times[i] = end - start;
        }

        long avgTime = calculateAverage(times);
        long maxTime = findMax(times);
        long minTime = findMin(times);

        System.out.println("=== Delete Person Benchmark ===");
        System.out.println("Average time: " + (avgTime / 1000) + " microseconds");
        System.out.println("Max time: " + (maxTime / 1000) + " microseconds");
        System.out.println("Min time: " + (minTime / 1000) + " microseconds");

        assertTrue(avgTime < 1_000_000, "Average delete operation should be under 1ms");
    }

    @Test
    public void benchmark_hasPerson_measureTime() {
        // Populate with 5000 persons
        for (int i = 0; i < 5000; i++) {
            Person person = createTestPerson(i);
            model.addPerson(person);
        }

        Person testPerson = createTestPerson(2500);
        long[] times = new long[1000];

        for (int i = 0; i < 1000; i++) {
            long start = System.nanoTime();
            model.hasPerson(testPerson);
            long end = System.nanoTime();
            times[i] = end - start;
        }

        long avgTime = calculateAverage(times);

        System.out.println("=== Has Person (lookup) Benchmark in 5000 records ===");
        System.out.println("Average time: " + (avgTime / 1000) + " microseconds");

        assertTrue(avgTime < 500_000, "Average lookup should be under 500 microseconds");
    }

    @Test
    public void benchmark_updatePerson_measureTime() {
        // Populate with 1000 persons
        for (int i = 0; i < 1000; i++) {
            Person person = createTestPerson(i);
            model.addPerson(person);
        }

        long[] times = new long[500];

        for (int i = 0; i < 500; i++) {
            Person target = model.getFilteredPersonList().get(i);
            Person editedPerson = createTestPerson(i + 10000);
            long start = System.nanoTime();
            model.setPerson(target, editedPerson);
            long end = System.nanoTime();
            times[i] = end - start;
        }

        long avgTime = calculateAverage(times);

        System.out.println("=== Update Person Benchmark ===");
        System.out.println("Average time: " + (avgTime / 1000) + " microseconds");

        assertTrue(avgTime < 2_000_000, "Average update operation should be under 2ms");
    }

    @Test
    public void benchmark_getFilteredPersonList_measureTime() {
        // Populate with 10000 persons
        for (int i = 0; i < 10000; i++) {
            Person person = createTestPerson(i);
            model.addPerson(person);
        }

        long[] times = new long[1000];

        for (int i = 0; i < 1000; i++) {
            long start = System.nanoTime();
            int size = model.getFilteredPersonList().size();
            long end = System.nanoTime();
            times[i] = end - start;
        }

        long avgTime = calculateAverage(times);

        System.out.println("=== Get Filtered List Benchmark (10000 records) ===");
        System.out.println("Average time: " + (avgTime / 1000) + " microseconds");

        assertTrue(avgTime < 100_000, "Getting filtered list should be fast");
    }

    /**
     * Creates a test person with a unique ID.
     */
    private Person createTestPerson(int id) {
        Name name = new Name("Benchmark Person " + id);
        Phone phone = new Phone(String.format("%08d", 80000000 + id));
        Email email = new Email("bench" + id + "@test.com");
        Website website = new Website("");
        Budget budget = new Budget(String.valueOf(200 + id));
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("benchmark"));

        return new Person(name, phone, email, website, tags, budget);
    }

    private long calculateAverage(long[] times) {
        long sum = 0;
        for (long time : times) {
            sum += time;
        }
        return sum / times.length;
    }

    private long findMax(long[] times) {
        long max = times[0];
        for (long time : times) {
            if (time > max) {
                max = time;
            }
        }
        return max;
    }

    private long findMin(long[] times) {
        long min = times[0];
        for (long time : times) {
            if (time < min) {
                min = time;
            }
        }
        return min;
    }
}

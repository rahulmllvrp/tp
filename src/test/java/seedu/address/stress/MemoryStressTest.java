package seedu.address.stress;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
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
 * Memory stress tests to check memory consumption and potential memory leaks.
 */
public class MemoryStressTest {

    private AddressBook addressBook;
    private Runtime runtime;

    @BeforeEach
    public void setUp() {
        addressBook = new AddressBook();
        runtime = Runtime.getRuntime();
        // Force garbage collection before each test
        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    public void memoryTest_add10000Persons_checkMemoryUsage() {
        long memoryBefore = getUsedMemoryMB();
        System.out.println("Memory before adding 10000 persons: " + memoryBefore + " MB");

        for (int i = 0; i < 10000; i++) {
            Person person = createTestPerson(i);
            addressBook.addPerson(person);
        }

        long memoryAfter = getUsedMemoryMB();
        System.out.println("Memory after adding 10000 persons: " + memoryAfter + " MB");
        long memoryIncrease = memoryAfter - memoryBefore;
        System.out.println("Memory increase: " + memoryIncrease + " MB");

        // Memory increase should be reasonable (less than 100MB for 10000 persons)
        assertTrue(memoryIncrease < 100, "Memory usage should be reasonable");
    }

    @Test
    public void memoryTest_repeatedAddAndClear_checkForLeaks() {
        long initialMemory = getUsedMemoryMB();
        System.out.println("Initial memory: " + initialMemory + " MB");

        for (int cycle = 0; cycle < 5; cycle++) {
            // Add 2000 persons
            for (int i = 0; i < 2000; i++) {
                Person person = createTestPerson(i + cycle * 2000);
                addressBook.addPerson(person);
            }

            long memoryAfterAdd = getUsedMemoryMB();
            System.out.println("Cycle " + cycle + " - Memory after adding: " + memoryAfterAdd + " MB");

            // Clear all
            addressBook.setPersons(new ArrayList<>());

            // Force GC
            System.gc();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            long memoryAfterClear = getUsedMemoryMB();
            System.out.println("Cycle " + cycle + " - Memory after clearing: " + memoryAfterClear + " MB");
        }

        long finalMemory = getUsedMemoryMB();
        System.out.println("Final memory: " + finalMemory + " MB");
        long memoryLeaked = finalMemory - initialMemory;
        System.out.println("Potential memory leak: " + memoryLeaked + " MB");

        // After clearing and GC, memory should not have increased significantly
        assertTrue(memoryLeaked < 20, "Should not have significant memory leaks");
    }

    @Test
    public void memoryTest_largeNumberOfTags_checkMemory() {
        long memoryBefore = getUsedMemoryMB();
        System.out.println("Memory before adding persons with many tags: " + memoryBefore + " MB");

        for (int i = 0; i < 5000; i++) {
            Person person = createTestPersonWithManyTags(i, 20);
            addressBook.addPerson(person);
        }

        long memoryAfter = getUsedMemoryMB();
        System.out.println("Memory after adding 5000 persons with 20 tags each: " + memoryAfter + " MB");
        long memoryIncrease = memoryAfter - memoryBefore;
        System.out.println("Memory increase: " + memoryIncrease + " MB");

        assertTrue(memoryIncrease < 150, "Memory usage with many tags should be reasonable");
    }

    @Test
    public void memoryTest_reportDetailedMemoryStats() {
        System.out.println("\n=== Detailed Memory Statistics ===");
        System.out.println("Max Memory (heap): " + runtime.maxMemory() / (1024 * 1024) + " MB");
        System.out.println("Total Memory (heap): " + runtime.totalMemory() / (1024 * 1024) + " MB");
        System.out.println("Free Memory (heap): " + runtime.freeMemory() / (1024 * 1024) + " MB");
        System.out.println("Used Memory (heap): " + getUsedMemoryMB() + " MB");

        // Add different amounts of data and measure
        int[] testSizes = {100, 500, 1000, 2000, 5000};

        for (int size : testSizes) {
            AddressBook testBook = new AddressBook();
            long before = getUsedMemoryMB();

            for (int i = 0; i < size; i++) {
                testBook.addPerson(createTestPerson(i));
            }

            long after = getUsedMemoryMB();
            System.out.println(size + " persons use approximately " + (after - before) + " MB");

            testBook = null;
            System.gc();
        }
    }

    /**
     * Gets currently used memory in MB.
     */
    private long getUsedMemoryMB() {
        return (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024);
    }

    /**
     * Creates a test person with a unique ID.
     */
    private Person createTestPerson(int id) {
        Name name = new Name("Memory Test Person " + id);
        Phone phone = new Phone(String.format("%08d", 60000000 + id));
        Email email = new Email("memory" + id + "@test.com");
        Website website = new Website(id % 3 == 0 ? "https://memory" + id + ".com" : "");
        Budget budget = new Budget(String.valueOf(500 + id));
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("memory"));
        tags.add(new Tag("test"));

        return new Person(name, phone, email, website, tags, budget);
    }

    /**
     * Creates a test person with many tags.
     */
    private Person createTestPersonWithManyTags(int id, int tagCount) {
        Name name = new Name("Memory Test Person " + id);
        Phone phone = new Phone(String.format("%08d", 60000000 + id));
        Email email = new Email("memory" + id + "@test.com");
        Website website = new Website("");
        Budget budget = new Budget(String.valueOf(500 + id));
        Set<Tag> tags = new HashSet<>();

        for (int i = 0; i < tagCount; i++) {
            tags.add(new Tag("tag" + i + "person" + id));
        }

        return new Person(name, phone, email, website, tags, budget);
    }
}

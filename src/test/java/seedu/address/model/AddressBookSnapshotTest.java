package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;

/**
 * Contains unit tests for AddressBookSnapshot.
 */
public class AddressBookSnapshotTest {

    @Test
    public void constructor_validAddressBook_success() {
        AddressBook addressBook = getTypicalAddressBook();
        String operationDescription = "test operation";

        AddressBookSnapshot snapshot = new AddressBookSnapshot(addressBook, operationDescription);

        assertEquals(operationDescription, snapshot.getOperationDescription());
    }

    @Test
    public void restoreAddressBook_validSnapshot_success() {
        AddressBook originalAddressBook = new AddressBook();
        originalAddressBook.addPerson(ALICE);
        originalAddressBook.addPerson(BOB);

        AddressBookSnapshot snapshot = new AddressBookSnapshot(originalAddressBook, "add persons");

        AddressBook restoredAddressBook = snapshot.restoreAddressBook();

        // Check that the restored address book has the same persons
        List<Person> originalPersons = originalAddressBook.getPersonList();
        List<Person> restoredPersons = restoredAddressBook.getPersonList();

        assertEquals(originalPersons.size(), restoredPersons.size());
        assertTrue(restoredPersons.containsAll(originalPersons));

        // Ensure it's a new instance (deep copy)
        assertNotSame(originalAddressBook, restoredAddressBook);
    }

    @Test
    public void restoreAddressBook_emptyAddressBook_success() {
        AddressBook emptyAddressBook = new AddressBook();
        AddressBookSnapshot snapshot = new AddressBookSnapshot(emptyAddressBook, "clear");

        AddressBook restoredAddressBook = snapshot.restoreAddressBook();

        assertTrue(restoredAddressBook.getPersonList().isEmpty());
        assertEquals("clear", snapshot.getOperationDescription());
    }

    @Test
    public void getOperationDescription_validDescription_success() {
        AddressBook addressBook = new AddressBook();
        String expectedDescription = "delete person";

        AddressBookSnapshot snapshot = new AddressBookSnapshot(addressBook, expectedDescription);

        assertEquals(expectedDescription, snapshot.getOperationDescription());
    }

    @Test
    public void restoreAddressBook_independentModification_success() {
        AddressBook originalAddressBook = new AddressBook();
        originalAddressBook.addPerson(ALICE);

        AddressBookSnapshot snapshot = new AddressBookSnapshot(originalAddressBook, "test");

        // Modify original after snapshot
        originalAddressBook.addPerson(BOB);

        AddressBook restoredAddressBook = snapshot.restoreAddressBook();

        // Restored should only have ALICE, not BOB
        assertEquals(1, restoredAddressBook.getPersonList().size());
        assertTrue(restoredAddressBook.hasPerson(ALICE));
        assertTrue(!restoredAddressBook.hasPerson(BOB));
    }
}
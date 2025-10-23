package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.person.Person;

/**
 * Represents a snapshot of an AddressBook at a specific point in time.
 * Used for implementing undo functionality.
 */
public class AddressBookSnapshot {
    private final List<Person> persons;
    private final String operationDescription;

    /**
     * Creates a snapshot of the given AddressBook.
     *
     * @param addressBook The AddressBook to create a snapshot of.
     * @param operationDescription Description of the operation that was performed.
     */
    public AddressBookSnapshot(ReadOnlyAddressBook addressBook, String operationDescription) {
        this.persons = new ArrayList<>(addressBook.getPersonList());
        this.operationDescription = operationDescription;
    }

    /**
     * Restores an AddressBook from this snapshot.
     *
     * @return A new AddressBook with the data from this snapshot.
     */
    public AddressBook restoreAddressBook() {
        AddressBook restoredBook = new AddressBook();
        for (Person person : persons) {
            restoredBook.addPerson(person);
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

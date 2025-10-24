package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAbsolutSinema;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;

/**
 * Contains unit tests for AbsolutSinemaSnapshot.
 */
public class AbsolutSinemaSnapshotTest {

    @Test
    public void constructor_validAbsolutSinema_success() {
        AbsolutSinema absolutSinema = getTypicalAbsolutSinema();
        String operationDescription = "test operation";

        AbsolutSinemaSnapshot snapshot = new AbsolutSinemaSnapshot(absolutSinema, operationDescription);

        assertEquals(operationDescription, snapshot.getOperationDescription());
    }

    @Test
    public void restoreAbsolutSinema_validSnapshot_success() {
        AbsolutSinema originalAbsolutSinema = new AbsolutSinema();
        originalAbsolutSinema.addPerson(ALICE);
        originalAbsolutSinema.addPerson(BOB);

        AbsolutSinemaSnapshot snapshot = new AbsolutSinemaSnapshot(originalAbsolutSinema, "add persons");

        AbsolutSinema restoredAbsolutSinema = snapshot.restoreAbsolutSinema();

        // Check that the restored absolut sinema has the same persons
        List<Person> originalPersons = originalAbsolutSinema.getPersonList();
        List<Person> restoredPersons = restoredAbsolutSinema.getPersonList();

        assertEquals(originalPersons.size(), restoredPersons.size());
        assertTrue(restoredPersons.containsAll(originalPersons));

        // Ensure it's a new instance (deep copy)
        assertNotSame(originalAbsolutSinema, restoredAbsolutSinema);
    }

    @Test
    public void restoreAbsolutSinema_emptyAbsolutSinema_success() {
        AbsolutSinema emptyAbsolutSinema = new AbsolutSinema();
        AbsolutSinemaSnapshot snapshot = new AbsolutSinemaSnapshot(emptyAbsolutSinema, "clear");

        AbsolutSinema restoredAbsolutSinema = snapshot.restoreAbsolutSinema();

        assertTrue(restoredAbsolutSinema.getPersonList().isEmpty());
        assertEquals("clear", snapshot.getOperationDescription());
    }

    @Test
    public void getOperationDescription_validDescription_success() {
        AbsolutSinema absolutSinema = new AbsolutSinema();
        String expectedDescription = "delete person";

        AbsolutSinemaSnapshot snapshot = new AbsolutSinemaSnapshot(absolutSinema, expectedDescription);

        assertEquals(expectedDescription, snapshot.getOperationDescription());
    }

    @Test
    public void restoreAbsolutSinema_independentModification_success() {
        AbsolutSinema originalAbsolutSinema = new AbsolutSinema();
        originalAbsolutSinema.addPerson(ALICE);

        AbsolutSinemaSnapshot snapshot = new AbsolutSinemaSnapshot(originalAbsolutSinema, "test");

        // Modify original after snapshot
        originalAbsolutSinema.addPerson(BOB);

        AbsolutSinema restoredAbsolutSinema = snapshot.restoreAbsolutSinema();

        // Restored should only have ALICE, not BOB
        assertEquals(1, restoredAbsolutSinema.getPersonList().size());
        assertTrue(restoredAbsolutSinema.hasPerson(ALICE));
        assertTrue(!restoredAbsolutSinema.hasPerson(BOB));
    }
}

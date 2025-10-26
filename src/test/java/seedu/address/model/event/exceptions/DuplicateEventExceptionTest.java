package seedu.address.model.event.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DuplicateEventExceptionTest {

    @Test
    public void testConstructor_message() {
        assertEquals("Operation would result in duplicate events", new DuplicateEventException().getMessage());
    }
}

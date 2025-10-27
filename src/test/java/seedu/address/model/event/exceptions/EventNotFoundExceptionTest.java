package seedu.address.model.event.exceptions;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class EventNotFoundExceptionTest {

    @Test
    public void testConstructor() {
        assertNotNull(new EventNotFoundException());
    }
}

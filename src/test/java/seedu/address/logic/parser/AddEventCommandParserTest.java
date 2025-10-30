package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddEventCommand;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventTime;
import seedu.address.model.person.Budget;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        String userInput = " n/John's Birthday d/12-12-2025 t/18:00 b/500";
        Event expectedEvent = new Event(
                new EventName("John's Birthday"),
                new EventDate("12-12-2025"),
                new EventTime("18:00"),
                new Budget("500"));
        assertParseSuccess(parser, userInput, new AddEventCommand(expectedEvent, new HashSet<>()));
    }

    @Test
    public void parse_validLeapYearDate_success() {
        String userInput = " n/Leap Day Party d/29-02-2028 t/18:00 b/500";
        Event expectedEvent = new Event(
                new EventName("Leap Day Party"),
                new EventDate("29-02-2028"),
                new EventTime("18:00"),
                new Budget("500"));
        assertParseSuccess(parser, userInput, new AddEventCommand(expectedEvent, new HashSet<>()));
    }

    @Test
    public void parse_invalidLeapYearDate_failure() {
        // 2023 is not a leap year, so 29-02-2023 is invalid
        String userInput = " n/Invalid Party d/29-02-2023 t/18:00 b/500";
        assertParseFailure(parser, userInput, EventDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidDate_failure() {
        // April has only 30 days
        String userInput = " n/Invalid Party d/31-04-2026 t/18:00 b/500";
        assertParseFailure(parser, userInput, EventDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_pastDate_failure() {
        // Past dates should be rejected
        String userInput = " n/Past Party d/01-01-2024 t/18:00 b/500";
        assertParseFailure(parser, userInput, EventDate.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, " d/12-12-2025 t/18:00 b/500", expectedMessage);

        // missing date prefix
        assertParseFailure(parser, " n/John's Birthday t/18:00 b/500", expectedMessage);

        // missing time prefix
        assertParseFailure(parser, " n/John's Birthday d/12-12-2025 b/500", expectedMessage);

        // missing budget prefix
        assertParseFailure(parser, " n/John's Birthday d/12-12-2025 t/18:00", expectedMessage);
    }
}

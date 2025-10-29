package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListTagCommand;

public class ListTagCommandParserTest {

    private ListTagCommandParser parser = new ListTagCommandParser();

    @Test
    public void parse_validArgs_returnsListTagCommand() {
        assertParseSuccess(parser, "", new ListTagCommand());
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListTagCommand.MESSAGE_USAGE));
    }
}

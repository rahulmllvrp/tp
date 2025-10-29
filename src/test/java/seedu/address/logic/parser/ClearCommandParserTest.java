package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearTarget;

public class ClearCommandParserTest {

    private final ClearCommandParser parser = new ClearCommandParser();

    @Test
    public void parse_missingArg_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE);
        CommandParserTestUtil.assertParseFailure(parser, "", expectedMessage);
        CommandParserTestUtil.assertParseFailure(parser, "   ", expectedMessage);
    }

    @Test
    public void parse_invalidArg_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearCommand.MESSAGE_USAGE);
        CommandParserTestUtil.assertParseFailure(parser, "alll", expectedMessage);
        CommandParserTestUtil.assertParseFailure(parser, "party", expectedMessage);
        CommandParserTestUtil.assertParseFailure(parser, "contacts now", expectedMessage);
    }

    @Test
    public void parse_validAll_success() {
        ClearCommand expected = new ClearCommand(ClearTarget.ALL);
        CommandParserTestUtil.assertParseSuccess(parser, "all", expected);
        // uppercase/lowercase variations
        CommandParserTestUtil.assertParseSuccess(parser, "ALL", expected);
        CommandParserTestUtil.assertParseSuccess(parser, "All", expected);
    }

    @Test
    public void parse_validParties_success() {
        ClearCommand expected = new ClearCommand(ClearTarget.EVENTS);
        CommandParserTestUtil.assertParseSuccess(parser, "parties", expected);
        CommandParserTestUtil.assertParseSuccess(parser, "PARTIES", expected);
    }

    @Test
    public void parse_validContacts_success() {
        ClearCommand expected = new ClearCommand(ClearTarget.CONTACTS);
        CommandParserTestUtil.assertParseSuccess(parser, "contacts", expected);
        CommandParserTestUtil.assertParseSuccess(parser, "CONTACTS", expected);
    }

}

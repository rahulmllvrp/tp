package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearTarget;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ClearCommand object
 */
public class ClearCommandParser {

    /**
     * Parses the given {@code String} of arguments in the context of the ClearCommand
     * and returns a ClearCommand object for execution.
     *
     * Only the following arguments (case-insensitive) are allowed:
     * - "all"      -> ALL
     * - "parties"  -> EVENTS
     * - "contacts" -> CONTACTS
     *
     * If the argument is missing (i.e., user typed just "clear"), or the token is not one of
     * the allowed values, a ParseException is thrown with usage information.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ClearCommand parse(String args) throws ParseException {
        if (args == null || args.trim().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClearCommand.MESSAGE_USAGE));
        }
        String token = args.trim().toLowerCase();
        switch (token) {
        case "contacts":
            return new ClearCommand(ClearTarget.CONTACTS);
        case "parties":
            return new ClearCommand(ClearTarget.EVENTS);
        case "all":
            return new ClearCommand(ClearTarget.ALL);
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ClearCommand.MESSAGE_USAGE));
        }
    }
}

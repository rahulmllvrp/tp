package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ListTagCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new ListTagCommand object
 */
public class ListTagCommandParser implements Parser<ListTagCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ListTagCommand
     * and returns a ListTagCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ListTagCommand parse(String args) throws ParseException {
        if (!args.trim().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListTagCommand.MESSAGE_USAGE));
        }
        return new ListTagCommand();
    }

}

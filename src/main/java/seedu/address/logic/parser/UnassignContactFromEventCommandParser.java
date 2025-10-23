package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_NO_CONTACTS_SPECIFIED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.ParserUtil.parseContactIndexes;

import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnassignContactFromEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input for unassigning contacts from an event.
 */
public class UnassignContactFromEventCommandParser implements Parser<UnassignContactFromEventCommand> {
    @Override
    public UnassignContactFromEventCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap multimap = ArgumentTokenizer.tokenize(args, PREFIX_CONTACT);
        try {
            multimap.verifyNoDuplicatePrefixesFor(PREFIX_CONTACT);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    "Please only include one prefix c/ !"));
        }
        if (multimap.getValue(PREFIX_CONTACT).isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnassignContactFromEventCommand.MESSAGE_USAGE));
        }
        Index eventIndex;
        try {
            eventIndex = ParserUtil.parseIndex(multimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UnassignContactFromEventCommand.MESSAGE_USAGE));
        }
        String personIndexes = multimap.getValue(PREFIX_CONTACT).orElse("");
        if (personIndexes.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_NO_CONTACTS_SPECIFIED));
        }
        Set<Index> personIndexSet = parseContactIndexes(personIndexes);
        return new UnassignContactFromEventCommand(eventIndex, personIndexSet);
    }
}


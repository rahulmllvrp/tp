package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_BUDGET;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddEventCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.event.Event;
import seedu.address.model.event.EventDate;
import seedu.address.model.event.EventName;
import seedu.address.model.event.EventTime;
import seedu.address.model.person.Budget;

/**
 * Parses input arguments and creates a new AddEventCommand object
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddEventCommand
     * and returns an AddEventCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddEventCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_EVENT_NAME, PREFIX_DATE, PREFIX_TIME,
                PREFIX_CONTACT, PREFIX_BUDGET);

        if (!arePrefixesPresent(argMultimap, PREFIX_EVENT_NAME, PREFIX_DATE, PREFIX_TIME, PREFIX_BUDGET)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EVENT_NAME, PREFIX_DATE, PREFIX_TIME, PREFIX_CONTACT,
                PREFIX_BUDGET);
        Optional<String> eventNameOpt = argMultimap.getValue(PREFIX_EVENT_NAME);
        Optional<String> eventDateOpt = argMultimap.getValue(PREFIX_DATE);
        Optional<String> eventTimeOpt = argMultimap.getValue(PREFIX_TIME);
        Optional<String> budgetOpt = argMultimap.getValue(PREFIX_BUDGET);

        if (eventNameOpt.isEmpty() || eventDateOpt.isEmpty() || eventTimeOpt.isEmpty() || budgetOpt.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }

        EventName eventName = ParserUtil.parseEventName(eventNameOpt.get());
        EventDate eventDate = ParserUtil.parseEventDate(eventDateOpt.get());
        EventTime eventTime = ParserUtil.parseEventTime(eventTimeOpt.get());
        Budget budget = ParserUtil.parseBudget(budgetOpt.get());

        Set<Index> contactIndexes = new HashSet<>();
        Optional<String> contactIndexesOpt = argMultimap.getValue(PREFIX_CONTACT);
        if (contactIndexesOpt.isPresent()) {
            contactIndexes = ParserUtil.parseContactIndexes(contactIndexesOpt.get());
        }

        Event event = new Event(eventName, eventDate, eventTime, budget);
        return new AddEventCommand(event, contactIndexes);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}

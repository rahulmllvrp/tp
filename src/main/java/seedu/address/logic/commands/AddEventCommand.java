package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EVENT_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;

/**
 * Adds an event to the address book.
 */
public class AddEventCommand extends Command {

    public static final String COMMAND_WORD = "addp";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a party to the party list. "
            + "Parameters: "
            + PREFIX_EVENT_NAME + "NAME "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + "[" + PREFIX_CONTACT + "CONTACT_INDEX...]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_EVENT_NAME + "John's Birthday "
            + PREFIX_DATE + "12-12-2025 "
            + PREFIX_TIME + "18:00 "
            + PREFIX_CONTACT + "1,2";

    public static final String MESSAGE_SUCCESS = "New party added: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "This party already exists in the party list";

    private final Event toAdd;
    private final Set<Index> contactIndexes;

    /**
     * Creates an AddEventCommand to add the specified {@code Event}
     */
    public AddEventCommand(Event event, Set<Index> contactIndexes) {
        requireNonNull(event);
        toAdd = event;
        this.contactIndexes = contactIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        if (model.hasEvent(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }
        List<Person> lastShownPersonList = model.getFilteredPersonList();
        List<PersonId> assignedPersonIds = new ArrayList<>(toAdd.getParticipants());
        if (contactIndexes != null && !contactIndexes.isEmpty()) {
            for (Index index : contactIndexes) {
                if (index.getZeroBased() >= lastShownPersonList.size()) {
                    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
                }
                Person personToAssign = lastShownPersonList.get(index.getZeroBased());
                if (assignedPersonIds.contains(personToAssign.getId())) {
                    throw new CommandException(personToAssign.getName().toString()
                            + " has already been assigned to this party.");
                }
                assignedPersonIds.add(personToAssign.getId());
            }
        }
        Event newEvent = new Event(toAdd.getName(), toAdd.getDate(), toAdd.getTime(), new ArrayList<>(assignedPersonIds));
        model.addEvent(newEvent);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(newEvent)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddEventCommand)) {
            return false;
        }

        AddEventCommand otherAddEventCommand = (AddEventCommand) other;
        return toAdd.equals(otherAddEventCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}

package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.ParserUtil.parsePersonListToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.person.Person;
import seedu.address.model.person.PersonId;

/**
 * Unassigns contacts from a specific event (party).
 */
public class UnassignContactFromEventCommand extends Command {
    public static final String COMMAND_WORD = "unassign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Unassigns contacts from a specific party "
            + "where the party and contacts are identified by their index number. \n"
            + "Parameters: unassign PartyIndex (must be a positive integer) "
            + PREFIX_CONTACT + "(specify at least 1 person index to unassign)... \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CONTACT + "1,2,3";

    public static final String MESSAGE_UNASSIGN_FROM_EVENT_SUCCESS =
            "Unassigned the following people from %1$s's party: %2$s";

    private final Index targetEventIndex;
    private final Set<Index> unassignedPersonIndexList;

    /**
     * Constructs an UnassignContactFromEventCommand.
     *
     * @param targetEventIndex Index of the event to unassign contacts from.
     * @param unassignedPersonIndexList Set of person indexes to unassign.
     */
    public UnassignContactFromEventCommand(Index targetEventIndex, Set<Index> unassignedPersonIndexList) {
        this.targetEventIndex = targetEventIndex;
        this.unassignedPersonIndexList = unassignedPersonIndexList;
    }

    /**
     * Executes the unassignment of contacts from the specified event.
     *
     * @param model The model in which the command operates.
     * @return CommandResult of the unassignment.
     * @throws CommandException if any index is invalid or contact not assigned.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.saveStateForUndo("unassign from party " + targetEventIndex.getOneBased());
        List<Event> lastShownEventList = model.getFilteredEventList();
        if (targetEventIndex.getZeroBased() >= lastShownEventList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }
        Event eventToModify = lastShownEventList.get(targetEventIndex.getZeroBased());
        ArrayList<Person> contactsToUnassign = new ArrayList<>();
        List<Person> lastShownList = model.getFilteredPersonList();
        for (Index i : unassignedPersonIndexList) {
            if (i.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + " "
                        + Messages.MESSAGE_TRY_PERSON_LIST_MODE);
            } else {
                Person personToRemove = lastShownList.get(i.getZeroBased());
                contactsToUnassign.add(personToRemove);
            }
        }
        List<PersonId> existingPersonsInEvent = new ArrayList<>(eventToModify.getParticipants());
        for (Person person : contactsToUnassign) {
            if (!existingPersonsInEvent.contains(person.getId())) {
                throw new CommandException(person.getName().toString() + " is not assigned to this party.");
            } else {
                existingPersonsInEvent.remove(person.getId());
            }
        }
        Event newEvent = new Event(eventToModify.getName(), eventToModify.getDate(), eventToModify.getTime(),
                existingPersonsInEvent);
        model.setEvent(eventToModify, newEvent);
        String unassignedPersonNames = parsePersonListToString(contactsToUnassign);
        return new CommandResult(String.format(MESSAGE_UNASSIGN_FROM_EVENT_SUCCESS,
                eventToModify.getName().toString(), unassignedPersonNames));
    }
}

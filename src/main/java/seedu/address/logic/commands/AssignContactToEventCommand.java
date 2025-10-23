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
 * Assigns contacts to a specific event (party).
 */
public class AssignContactToEventCommand extends Command {
    public static final String COMMAND_WORD = "assign";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Assigns contacts to a specific party "
            + "where the party and contacts are identified by their index number. \n"
            + "Parameters: assign PartyIndex (must be a positive integer) "
            + PREFIX_CONTACT + "(specify at least 1 person index to assign)... \n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CONTACT + "1,2,3";

    public static final String MESSAGE_ASSIGN_TO_EVENT_SUCCESS =
            "Assigned the following people to %1$s's party: %2$s";

    private final Index targetEventIndex;
    private final Set<Index> assignedPersonIndexList;

    public AssignContactToEventCommand(Index targetEventIndex, Set<Index> assignedPersonIndexList) {
        this.targetEventIndex = targetEventIndex;
        this.assignedPersonIndexList = assignedPersonIndexList;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.saveStateForUndo("assign to party " + targetEventIndex.getOneBased());
        List<Event> lastShownEventList = model.getFilteredEventList();
        if (targetEventIndex.getZeroBased() >= lastShownEventList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }
        Event eventToModify = lastShownEventList.get(targetEventIndex.getZeroBased());
        ArrayList<Person> newContactsAssignedToEvent = new ArrayList<>();
        List<Person> lastShownList = model.getFilteredPersonList();
        for (Index i : assignedPersonIndexList) {
            if (i.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + " "
                        + Messages.MESSAGE_TRY_PERSON_LIST_MODE);
            } else {
                Person personToAdd = lastShownList.get(i.getZeroBased());
                newContactsAssignedToEvent.add(personToAdd);
            }
        }
        List<PersonId> existingPersonsInEvent = new ArrayList<>(eventToModify.getParticipants());
        for (Person person : newContactsAssignedToEvent) {
            if (existingPersonsInEvent.contains(person.getId())) {
                throw new CommandException(person.getName().toString() + " has already been assigned to this party.");
            } else {
                existingPersonsInEvent.add(person.getId());
            }
        }
        Event newEvent = new Event(eventToModify.getName(), eventToModify.getDate(), eventToModify.getTime(), existingPersonsInEvent);
        model.setEvent(eventToModify, newEvent);
        String assignedPersonNames = parsePersonListToString(newContactsAssignedToEvent);
        return new CommandResult(String.format(MESSAGE_ASSIGN_TO_EVENT_SUCCESS,
                eventToModify.getName().toString(), assignedPersonNames));
    }
}

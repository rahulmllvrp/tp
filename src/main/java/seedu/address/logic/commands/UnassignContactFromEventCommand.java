package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACT;
import static seedu.address.logic.parser.ParserUtil.parsePersonListToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.event.Event;
import seedu.address.model.person.Budget;
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

    private static final Logger logger = LogsCenter.getLogger(UnassignContactFromEventCommand.class);

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
        assert this.unassignedPersonIndexList != null : "unassignedPersonIndexList should not be null";
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
        logger.fine(() -> "Executing UnassignContactFromEventCommand for event index " + targetEventIndex);
        model.saveStateForUndo("unassign from party " + targetEventIndex.getOneBased());
        List<Event> lastShownEventList = model.getFilteredEventList();
        if (targetEventIndex.getZeroBased() >= lastShownEventList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }
        Event eventToModify = lastShownEventList.get(targetEventIndex.getZeroBased());
        double eventBudget = parseBudgetSafe(eventToModify.getRemainingBudget().value);
        List<Person> lastShownList = model.getFilteredPersonList();

        List<Person> contactsToUnassign = collectPersonsToUnassign(lastShownList);

        List<PersonId> updatedParticipantIds = new ArrayList<>(eventToModify.getParticipants());
        for (Person p : contactsToUnassign) {
            if (!updatedParticipantIds.contains(p.getId())) {
                throw new CommandException(p.getName().toString() + " is not assigned to this party.");
            }
            updatedParticipantIds.remove(p.getId());
            // add back to budget
            eventBudget += parseBudgetSafe(p.getBudget().value);
            // cap at initial budget
            eventBudget = Math.min(eventBudget, parseBudgetSafe(eventToModify.getInitialBudget().value));
        }

        Event newEvent = new Event(eventToModify.getName(), eventToModify.getDate(), eventToModify.getTime(),
                updatedParticipantIds, eventToModify.getInitialBudget(), new Budget(String.valueOf(eventBudget)));
        model.setEvent(eventToModify, newEvent);
        String unassignedPersonNames = parsePersonListToString(contactsToUnassign);
        logger.info(() -> String.format("Unassigned %s from event %s", unassignedPersonNames, eventToModify.getName()));
        return new CommandResult(String.format(MESSAGE_UNASSIGN_FROM_EVENT_SUCCESS,
                eventToModify.getName().toString(), unassignedPersonNames));
    }

    private double parseBudgetSafe(String budgetString) throws CommandException {
        try {
            return Double.parseDouble(budgetString);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid budget value encountered: " + budgetString);
        }
    }

    private List<Person> collectPersonsToUnassign(List<Person> lastShownList)
            throws CommandException {
        List<Person> result = new ArrayList<>();
        for (Index i : unassignedPersonIndexList) {
            if (i.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + " "
                        + Messages.MESSAGE_TRY_PERSON_LIST_MODE);
            }
            Person personToRemove = lastShownList.get(i.getZeroBased());
            result.add(personToRemove);
        }
        return result;
    }
}

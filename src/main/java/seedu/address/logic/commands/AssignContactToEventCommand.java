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

    private static final Logger logger = LogsCenter.getLogger(AssignContactToEventCommand.class);

    private final Index targetEventIndex;
    private final Set<Index> assignedPersonIndexList;

    /**
     * Constructs an AssignContactToEventCommand.
     *
     * @param targetEventIndex Index of the event to assign contacts to.
     * @param assignedPersonIndexList Set of person indexes to assign.
     */
    public AssignContactToEventCommand(Index targetEventIndex, Set<Index> assignedPersonIndexList) {
        this.targetEventIndex = targetEventIndex;
        this.assignedPersonIndexList = assignedPersonIndexList;
        assert this.assignedPersonIndexList != null : "assignedPersonIndexList should not be null";
    }

    /**
     * Executes the assignment of contacts to the specified event.
     *
     * @param model The model in which the command operates.
     * @return CommandResult of the assignment.
     * @throws CommandException if any index is invalid or contact already assigned.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        logger.fine(() -> "Executing AssignContactToEventCommand for event index " + targetEventIndex);
        model.saveStateForUndo("assign to party " + targetEventIndex.getOneBased());
        List<Event> lastShownEventList = model.getFilteredEventList();
        if (targetEventIndex.getZeroBased() >= lastShownEventList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }
        Event eventToModify = lastShownEventList.get(targetEventIndex.getZeroBased());
        double eventBudget = parseBudgetSafe(eventToModify.getRemainingBudget().value);
        List<Person> lastShownList = model.getFilteredPersonList();

        List<Person> newContactsAssignedToEvent = collectAndValidatePersons(lastShownList, eventToModify, eventBudget);

        // Build updated participant list
        List<PersonId> updatedParticipantIds = new ArrayList<>(eventToModify.getParticipants());
        for (Person p : newContactsAssignedToEvent) {
            if (updatedParticipantIds.contains(p.getId())) {
                throw new CommandException(p.getName().toString() + " has already been assigned to this party.");
            }
            updatedParticipantIds.add(p.getId());
        }

        // compute remaining budget after assignments
        double remainingBudget = eventBudget;
        for (Person p : newContactsAssignedToEvent) {
            remainingBudget -= parseBudgetSafe(p.getBudget().value);
        }

        Event newEvent = new Event(eventToModify.getName(), eventToModify.getDate(), eventToModify.getTime(),
                updatedParticipantIds, eventToModify.getInitialBudget(), new Budget(String.valueOf(remainingBudget)));
        model.setEvent(eventToModify, newEvent);
        String assignedPersonNames = parsePersonListToString(newContactsAssignedToEvent);
        logger.info(() -> String.format("Assigned %s to event %s", assignedPersonNames, eventToModify.getName()));
        return new CommandResult(String.format(MESSAGE_ASSIGN_TO_EVENT_SUCCESS,
                eventToModify.getName().toString(), assignedPersonNames));
    }

    private double parseBudgetSafe(String budgetString) throws CommandException {
        try {
            return Double.parseDouble(budgetString);
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid budget value encountered: " + budgetString);
        }
    }

    /**
     * Collects persons to be assigned and validates indexes and budget constraints.
     */
    private List<Person> collectAndValidatePersons(List<Person> lastShownList, Event eventToModify, double eventBudget)
            throws CommandException {
        List<Person> result = new ArrayList<>();
        for (Index i : assignedPersonIndexList) {
            if (i.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX + " "
                        + Messages.MESSAGE_TRY_PERSON_LIST_MODE);
            }
            Person personToAdd = lastShownList.get(i.getZeroBased());
            if (eventToModify.getParticipants().contains(personToAdd.getId())) {
                throw new CommandException(personToAdd.getName().toString()
                        + " has already been assigned to this party.");
            }
            double personBudget = parseBudgetSafe(personToAdd.getBudget().value);
            if (eventBudget < personBudget) {
                throw new CommandException("The budget of " + personToAdd.getName().toString()
                        + " exceeds the remaining budget of the party.");
            }
            eventBudget -= personBudget;
            result.add(personToAdd);
        }
        return result;
    }
}

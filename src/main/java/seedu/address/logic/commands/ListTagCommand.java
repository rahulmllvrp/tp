package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

/**
 * Lists all tags in the address book to the user.
 */
public class ListTagCommand extends Command {

    public static final String COMMAND_WORD = "listtags";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all tags in the address book.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Listed all tags:\n%1$s";
    public static final String MESSAGE_NO_TAGS = "No tags found in the address book.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        Set<Tag> tags = model.getAddressBook().getPersonList().stream()
                .flatMap(person -> person.getTags().stream())
                .collect(Collectors.toSet());

        if (tags.isEmpty()) {
            return new CommandResult(MESSAGE_NO_TAGS);
        }

        List<String> sortedTags = tags.stream()
                .map(tag -> tag.tagName)
                .sorted()
                .collect(Collectors.toList());

        return new CommandResult(String.format(MESSAGE_SUCCESS, String.join("\n", sortedTags)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ListTagCommand); // instanceof handles nulls
    }
}

package seedu.address.model.person;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;
import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} or {@code Tags} matches any of the keywords given.
 */
public class NameAndTagContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NameAndTagContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        return keywords.stream()
                .anyMatch(keyword -> {
                    // Check if name contains keyword
                    if (StringUtil.containsSubstringIgnoreCase(person.getName().fullName, keyword)) {
                        return true;
                    }
                    // Check if any tag contains keyword
                    return person.getTags().stream()
                            .anyMatch(tag -> StringUtil.containsSubstringIgnoreCase(tag.tagName, keyword));
                });
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof NameAndTagContainsKeywordsPredicate)) {
            return false;
        }

        NameAndTagContainsKeywordsPredicate otherPredicate = (NameAndTagContainsKeywordsPredicate) other;
        return keywords.equals(otherPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
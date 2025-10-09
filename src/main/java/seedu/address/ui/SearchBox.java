package seedu.address.ui;

import java.util.Arrays;
import java.util.function.Predicate;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.model.person.NameAndTagContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * The UI component that is responsible for receiving user search input and providing real-time filtering.
 */
public class SearchBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "SearchBox.fxml";

    /**
     * Enum representing different search types.
     */
    public enum SearchType {
        NAMES("Names"),
        TAGS("Tags"),
        BOTH("Names & Tags");

        private final String displayName;

        SearchType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private final Logic logic;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<SearchType> searchTypeComboBox;

    /**
     * Creates a {@code SearchBox} with the given {@code Logic}.
     */
    public SearchBox(Logic logic) {
        super(FXML);
        this.logic = logic;
        initializeSearchTypeComboBox();
        initializeSearchTextField();
    }

    /**
     * Initializes the search type combo box with available options.
     */
    private void initializeSearchTypeComboBox() {
        searchTypeComboBox.setItems(FXCollections.observableArrayList(SearchType.values()));
        searchTypeComboBox.setValue(SearchType.BOTH);
        searchTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchInput(searchTextField.getText());
        });
    }

    /**
     * Initializes the search text field with real-time filtering.
     */
    private void initializeSearchTextField() {
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchInput(newValue);
        });
    }

    /**
     * Handles the search input by directly filtering the model without executing commands.
     */
    private void handleSearchInput(String searchText) {
        try {
            if (searchText == null || searchText.trim().isEmpty()) {
                // Show all persons when search is empty
                logic.getModel().updateFilteredPersonList(person -> true);
            } else {
                // Create appropriate predicate based on search type
                Predicate<Person> predicate = createSearchPredicate(searchText.trim());
                logic.getModel().updateFilteredPersonList(predicate);
            }
            searchTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
        } catch (Exception e) {
            setStyleToIndicateSearchFailure();
        }
    }

    /**
     * Creates the appropriate search predicate based on the selected search type.
     */
    private Predicate<Person> createSearchPredicate(String searchText) {
        String[] keywords = searchText.split("\\s+");
        SearchType searchType = searchTypeComboBox.getValue();

        switch (searchType) {
        case NAMES:
            return new NameContainsKeywordsPredicate(Arrays.asList(keywords));
        case TAGS:
            return new TagContainsKeywordsPredicate(Arrays.asList(keywords));
        case BOTH:
        default:
            return new NameAndTagContainsKeywordsPredicate(Arrays.asList(keywords));
        }
    }

    /**
     * Sets the search box style to indicate a failed search.
     */
    private void setStyleToIndicateSearchFailure() {
        ObservableList<String> styleClass = searchTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }
}

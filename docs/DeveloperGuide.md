---
layout: page
title: Developer Guide
---

# AbsolutSin-ema Developer Guide

**AbsolutSin-ema** is a desktop application for party planners to manage contacts and events efficiently. It is optimized for use via a Command Line Interface (CLI) while still having the benefits of a Graphical User Interface (GUI). This guide provides comprehensive documentation for developers who wish to understand, maintain, or extend AbsolutSin-ema.

* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* This project is based on the AddressBook-Level3 project created by the [SE-EDU initiative](https://se-education.org)
* JavaFX library for GUI components
* Jackson library for JSON serialization/deserialization
* JUnit5 for testing framework

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of AbsolutSin-ema.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of AbsolutSin-ema's launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of AbsolutSin-ema's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of AbsolutSin-ema.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of AbsolutSin-ema in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `EventListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The UI displays two main lists:
* `PersonListPanel` - Shows all contacts (vendors/clients) with their details
* `EventListPanel` - Shows all events (parties) with their details including budgets

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` and `Event` objects residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object) and all `Event` objects (which are contained in a `UniqueEventList` object).
* each `Person` has the following fields: `Name`, `Phone`, `Email`, `Website`, `Budget`, `PersonId` (unique identifier), and a set of `Tag` objects.
* each `Event` has the following fields: `EventName`, `EventDate`, `EventTime`, `initialBudget`, `remainingBudget`, and a list of `PersonId` representing participants.
* stores the currently 'selected' `Person` and `Event` objects (e.g., results of a search query) as separate _filtered_ lists which are exposed to outsiders as unmodifiable `ObservableList<Person>` and `ObservableList<Event>` that can be 'observed' e.g. the UI can be bound to these lists so that the UI automatically updates when the data in the lists change.
* stores a `UserPref` object that represents the user's preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2526S1-CS2103T-T12-4/tp/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both AbsolutSin-ema's data (contacts and events) and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Undo feature

#### Implementation

The undo mechanism is facilitated by `AddressBookSnapshot`. The application stores snapshots of the address book state before each modifying command. The system implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface and used by commands that modify the address book data (e.g., `AddCommand`, `DeleteCommand`, `EditCommand`).

Given below is an example usage scenario and how the undo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The address book starts with no saved states for undo.

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. Before executing the deletion, the `delete` command calls `Model#saveStateForUndo("delete")`, saving a snapshot of the current address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#saveStateForUndo("add")` before adding, saving another snapshot.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#saveStateForUndo()`, so the address book state will not be saved.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command calls `Model#undo()`, which restores the address book to the state before the add command was executed.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If there are no saved states to restore, the `undo` command uses `Model#canUndo()` to check this. If no undo is possible, it will return an error to the user rather than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### Event Management System

#### Implementation

The event management system allows party planners to create and manage events (parties), track budgets, and assign contacts (vendors/clients) to specific events. This feature is fully integrated with the person management system.

**Key Components:**

1. **Event Entity** (`seedu.address.model.event.Event`)
   - Stores event details: `EventName`, `EventDate`, `EventTime`
   - Manages budgets: `initialBudget` and `remainingBudget`
   - Tracks participants via a list of `PersonId` references
   - Implements `isSameEvent()` method to prevent duplicate events

2. **Event Commands:**
   - `AddEventCommand` (`addp`) - Creates a new event with name, date, time, and budget
   - `EditEventCommand` (`editp`) - Modifies existing event details
   - `DeleteEventCommand` (`deletep`) - Removes an event from the system
   - `AssignContactToEventCommand` (`assign`) - Links one or more contacts to an event as participants (format: `assign EVENT_INDEX c/CONTACT_INDEXES`)
   - `UnassignContactFromEventCommand` (`unassign`) - Removes one or more contacts from an event (format: `unassign EVENT_INDEX c/CONTACT_INDEXES`)
   - `ViewCommand` (`view`) - Displays all participants for a specific event

3. **Budget Tracking:**
   - Each event tracks both initial and remaining budget
   - Each person (vendor) has an associated budget cost
   - When a person is assigned to an event, the remaining budget can be adjusted
   - Budget validation ensures sufficient funds are available

4. **UI Components:**
   - `EventListPanel` - Displays all events in the system
   - `EventListCard` - Shows individual event details including name, date, time, and budget

**How Event-Person Association Works:**

Events don't store full `Person` objects, but rather `PersonId` references. This design:
- Prevents data duplication
- Ensures person updates automatically reflect in associated events
- Allows efficient querying of participants via `Model#getPersonById(PersonId)`

**Example Usage Scenario:**

1. Party planner creates a new birthday party: `addp n/Birthday Party d/2024-12-25 t/18:00 b/5000`
2. System creates event with $5000 budget
3. Planner assigns caterer to event: `assign 1 c/1` (assigns contact at index 1 to event at index 1)
4. Planner can assign multiple contacts at once: `assign 1 c/2,3,4` (assigns contacts 2, 3, and 4 to event 1)
5. Planner can view all vendors/participants: `view 1`
6. If needed, planner can unassign: `unassign 1 c/1` (removes contact 1 from event 1)

### Confirmation System for Destructive Operations

#### Implementation

To prevent accidental data loss, the application implements a confirmation system for the `clear` command, which deletes all contacts and events.

**How it works:**

1. When user executes `clear`, the `ClearCommand` displays a warning message:
   ```
   Are you sure you want to clear the party planner? (Type 'y' to confirm, 'n' to cancel)
   ```
2. User must type `y` to confirm or `n` to cancel the operation
3. If confirmed, the `ConfirmClearCommand` performs the actual data deletion
4. If cancelled, the operation is aborted and no data is lost

**Design Rationale:**
- Destructive operations like `clear` are irreversible (even with undo, losing all data is risky)
- Interactive confirmation with explicit yes/no response provides a strong safety net
- The confirmation message clearly explains what will happen and how to proceed
- User-friendly prompts reduce the chance of accidental data loss from typos or misclicks
- Could be extended to other dangerous operations (e.g., bulk delete) in future versions

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope


**Target user profile**:

* Party planners who organize birthdays, anniversaries, and social gatherings
* Need to manage multiple vendor contacts (caterers, decorators, entertainers, venues)
* Track client information and party preferences
* Coordinate with various service providers for each party
* Handle multiple parties simultaneously
* Prefer desktop applications for quick data entry and retrieval
* Are reasonably comfortable with CLI commands for efficiency

**Value proposition**: **AbsolutSin-ema** helps party planners manage their contacts more efficiently than generic contact management apps by:

* Organizing vendor contacts by party type (birthday, anniversary, corporate, kids' parties)
* Quick filtering by vendor category (caterer, decorator, DJ, venue, photographer)
* Tracking vendor specialties (e.g., balloon artist, vegan caterer)
* Fast CLI-based access for busy planners juggling multiple events
* Tagging system for organizing by theme, budget tier, or reliability

---
### User stories

Priorities: High (must have) - `* * *`, Medium (should have) - `* *`, Low (nice to have) - `*`

| Priority | As a …​               | I want to …​                      | So that I can…​                                      |
| -------- | ---------------------- | --------------------------------- | ---------------------------------------------------- |
| `* * *`  | user                   | add new contacts                  | expand my event planner network                      |
| `* * *`  | user                   | delete contacts                   | remove unneeded or completed events                  |
| `* * *`  | user                   | tag/untag contacts                | organise my contacts effectively                     |
| `* * *`  | user                   | view a list of all contacts       | retrieve contact information quickly                 |
| `* *`    | user                   | edit contacts                     | update information upon changes                      |
| `* *`    | user                   | filter contacts by tag            | find specific profiles of interest                   |
| `* *`    | user                   | see a confirmation before delete  | avoid accidental data loss                           |
| `* *`    | first-time user        | see help messages                 | learn how to use the app                             |
| `*`      | user                   | archive contacts                  | prevent clutter without deleting                     |
| `*`      | user                   | sort contacts alphabetically      | organise and access easily                           |
| `*`      | user                   | undo latest action                | recover from mistakes                                |
| `*`      | user                   | import/export contacts (CSV)      | transfer or back up contacts                         |
| `*`      | user                   | add notes to contacts             | record extra details                                 |
| `*`      | user                   | set reminders                     | remember to communicate with vendors                 |
| `*`      | user                   | schedule calls                    | keep track of vendor follow-ups                      |
| `*`      | user                   | create events with associated contacts | manage all vendors for one event together        |
| `*`      | user                   | create todo lists for events      | manage tasks efficiently                             |
| `*`      | user                   | share contacts with colleagues    | allow team collaboration                             |
| `*`      | user                   | view statistics                   | monitor number of contacts and growth                |

---

### Use cases

(For all use cases below, the **System** is **AbsolutSin-ema** and the **Actor** is the `Party Planner`.)

**Use case UC01: Add a new caterer for kids' birthday parties**

**MSS**
1. Planner searches for existing caterer to avoid duplicates
2. System shows search results (none found)
3. Planner enters `add n/Happy Foods Catering p/91234567 e/info@happyfoods.sg a/123 Food Street t/caterer t/kids t/halal`
4. System validates all fields
5. System adds contact and shows success message

**Extensions**
* 4a. Duplicate detected → System shows error message, use case ends
* 4b. Invalid phone format → System shows error, user retries input



**Use case UC02: Find DJ for corporate party**

**MSS**
1. Planner enters `find dj`
2. System filters and shows all contacts tagged with `dj`
3. Planner reviews list
4. Planner enters `list` to return to full contact list

**Extensions**
* 2a. No DJs found → System shows "0 contacts listed", use case ends



**Use case UC03: Update decorator's service tags**

**MSS**
1. Planner views contact list
2. Planner identifies decorator at index 3
3. Planner enters `edit 3 t/decorator t/balloons t/elegant t/corporate`
4. System validates tags
5. System updates contact
6. System shows success message with updated info

---

### Non-Functional Requirements

**Performance**
1. System should respond to any command within 2 seconds
2. Contact list should load within 3 seconds even with 1000+ contacts

**Usability**
3. CLI users should perform basic operations without referring to docs
4. Error messages must clearly indicate fixes
5. Contact info should be easily readable in the GUI

**Scalability**
6. Handle at least 1000 vendor contacts without noticeable lag
7. Support at least 20 tags per contact

**Reliability**
8. Data auto-saved after each command
9. Recover gracefully from corrupted data files

**Portability**
10. Work on Windows, macOS, Linux
11. Distributed as a single JAR file

**Maintainability**
12. Code should follow OOP principles for easy extension
13. New vendor categories should be addable without code changes

---
### Glossary

| Term           | Definition                                                                 |
|----------------|----------------------------------------------------------------------------|
| AbsolutSin-ema | The name of this party planning contact and event management application   |
| Party Planner  | Professional who organizes and coordinates events                          |
| Vendor         | Service provider (caterer, decorator, entertainer, venue, etc.)            |
| Tag            | A label used to categorize contacts (e.g., `caterer`, `kids`, `halal`)     |
| Contact        | Vendor or client entry with name, phone, email, website, budget, and tags  |
| Event          | A party or gathering with date, time, budget, and assigned participants    |
| CLI            | Command Line Interface, text-based commands                                |
| GUI            | Graphical User Interface, displays contacts and events visually            |
| Index          | Numerical position of a contact or event in the displayed list             |
| Service Type   | Category of vendor service (caterer, DJ, venue, etc.)                      |
| Party Theme    | Style of a party (princess, superhero, elegant, tropical)                  |
| MVP            | Minimum Viable Product, core features needed for release                   |
--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test AbsolutSin-ema manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the AbsolutSin-ema GUI with a set of sample contacts and events. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_

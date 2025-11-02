package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;

public class JsonAddressBookCorruptionTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab.json"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs.json"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void readAddressBook_afterManualTamper_acceptsIfDataValid() throws Exception {
        // Save a normal address book to disk
        AddressBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        Path file = storageManager.getAddressBookFilePath();

        // Load contents and verify checksum is present
        String content = Files.readString(file, StandardCharsets.UTF_8);
        assertTrue(content.contains("\"checksum\""), "Saved JSON should contain a checksum field");

        // Corrupt the checksum value by flipping its first hex character (robust to whitespace)
        int keyPos = content.indexOf("\"checksum\"");
        if (keyPos >= 0) {
            int colonPos = content.indexOf(':', keyPos);
            if (colonPos > keyPos) {
                int firstQuote = content.indexOf('"', colonPos);
                if (firstQuote > colonPos) {
                    int secondQuote = content.indexOf('"', firstQuote + 1);
                    if (secondQuote > firstQuote + 1) {
                        int valueStart = firstQuote + 1;
                        int valueEnd = secondQuote;
                        String oldChecksum = content.substring(valueStart, valueEnd);
                        if (!oldChecksum.isEmpty()) {
                            char first = oldChecksum.charAt(0);
                            char flipped = (first == 'a') ? 'b' : 'a';
                            String newChecksum = flipped + oldChecksum.substring(1);
                            content = content.substring(0, valueStart) + newChecksum + content.substring(valueEnd);
                            Files.writeString(file, content, StandardCharsets.UTF_8);
                        }
                    }
                }
            }
        }

        assertDoesNotThrow(() -> storageManager.readAddressBook());
    }
}

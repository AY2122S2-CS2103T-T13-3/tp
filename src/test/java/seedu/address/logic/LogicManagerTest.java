package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_MEETING_DISPLAYED_INDEX;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.DATETIME_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.DURATION_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.MODULE_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.RECURRING_DESC_LECTURE;
import static seedu.address.logic.commands.CommandTestUtil.URL_DESC_LECTURE;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.typical.TypicalMeetings.CS2103;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.commands.meeting.AddMeetingCommand;
import seedu.address.logic.commands.meeting.ListMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.LinkyTime;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyLinkyTime;
import seedu.address.model.UserPrefs;
import seedu.address.model.meeting.Meeting;
import seedu.address.storage.JsonLinkyTimeStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.meeting.MeetingBuilder;

public class LogicManagerTest {
    private static final IOException DUMMY_IO_EXCEPTION = new IOException("dummy exception");

    @TempDir
    public Path temporaryFolder;

    private final Model model = new ModelManager();
    private Logic logic;

    @BeforeEach
    public void setUp() {
        final JsonLinkyTimeStorage linkyTimeStorage =
                new JsonLinkyTimeStorage(temporaryFolder.resolve("app.json"));
        final JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        final StorageManager storage = new StorageManager(linkyTimeStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void execute_invalidCommandFormat_throwsParseException() {
        final String invalidCommand = "uicfhmowqewca";
        assertParseException(invalidCommand, MESSAGE_UNKNOWN_COMMAND);
    }

    @Test
    public void execute_commandExecutionError_throwsCommandException() {
        final String deleteMeetingCommand = "delete 9";
        assertCommandException(deleteMeetingCommand, MESSAGE_INVALID_MEETING_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validCommand_success() throws Exception {
        final String listMeetingCommand = ListMeetingCommand.COMMAND_WORD;
        assertCommandSuccess(listMeetingCommand, ListMeetingCommand.MESSAGE_SUCCESS, model);
    }

    @Test
    public void execute_storageThrowsIoException_throwsCommandException() {
        // Setup LogicManager with JsonLinkyTimeIoExceptionThrowingStub
        final JsonLinkyTimeStorage linkyTimeStorage =
                new JsonLinkyTimeIoExceptionThrowingStub(temporaryFolder.resolve("ioExceptionLinkyTime.json"));
        final JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("ioExceptionUserPrefs.json"));
        final StorageManager storage = new StorageManager(linkyTimeStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);

        // Execute add command
        final String addMeetingCommand = AddMeetingCommand.COMMAND_WORD + NAME_DESC_LECTURE + URL_DESC_LECTURE
                + DATETIME_DESC_LECTURE + DURATION_DESC_LECTURE + MODULE_DESC_LECTURE + RECURRING_DESC_LECTURE;
        final Meeting expectedMeeting = new MeetingBuilder(CS2103).withTags().build();
        final ModelManager expectedModel = new ModelManager();
        expectedModel.addMeeting(expectedMeeting);
        final String expectedMessage = LogicManager.FILE_OPS_ERROR_MESSAGE + DUMMY_IO_EXCEPTION;
        assertCommandFailure(addMeetingCommand, CommandException.class, expectedMessage, expectedModel);
    }

    @Test
    public void getFilteredMeetingList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> logic.getFilteredMeetingList().remove(0));
    }

    /**
     * Executes the command and confirms that
     * - no exceptions are thrown <br>
     * - the feedback message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandSuccess(String inputCommand, String expectedMessage,
            Model expectedModel) throws CommandException, ParseException {
        final CommandResult result = logic.execute(inputCommand);
        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel, model);
    }

    /**
     * Executes the command, confirms that a ParseException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertParseException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, ParseException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that a CommandException is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandException(String inputCommand, String expectedMessage) {
        assertCommandFailure(inputCommand, CommandException.class, expectedMessage);
    }

    /**
     * Executes the command, confirms that the exception is thrown and that the result message is correct.
     * @see #assertCommandFailure(String, Class, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage) {
        final Model expectedModel = new ModelManager(new LinkyTime(), new UserPrefs());
        assertCommandFailure(inputCommand, expectedException, expectedMessage, expectedModel);
    }

    /**
     * Executes the command and confirms that
     * - the {@code expectedException} is thrown <br>
     * - the resulting error message is equal to {@code expectedMessage} <br>
     * - the internal model manager state is the same as that in {@code expectedModel} <br>
     * @see #assertCommandSuccess(String, String, Model)
     */
    private void assertCommandFailure(String inputCommand, Class<? extends Throwable> expectedException,
            String expectedMessage, Model expectedModel) {
        assertThrows(expectedException, expectedMessage, () -> logic.execute(inputCommand));
        assertEquals(expectedModel, model);
    }

    /**
     * A stub class to throw an {@code IOException} when the save method is called.
     */
    private static class JsonLinkyTimeIoExceptionThrowingStub extends JsonLinkyTimeStorage {
        private JsonLinkyTimeIoExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveLinkyTime(ReadOnlyLinkyTime linkyTime, Path filePath) throws IOException {
            throw DUMMY_IO_EXCEPTION;
        }
    }
}

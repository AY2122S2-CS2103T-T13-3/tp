package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.meetingentry.AddCommand;
import seedu.address.logic.commands.meetingentry.DeleteCommand;
import seedu.address.logic.commands.meetingentry.ListCommand;
import seedu.address.logic.commands.meetingentry.OpenCommand;
import seedu.address.logic.commands.module.ListModuleCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.meetingentry.AddCommandParser;
import seedu.address.logic.parser.meetingentry.DeleteCommandParser;
import seedu.address.logic.parser.meetingentry.OpenCommandParser;

/**
 * Parses user input.
 */
public class LinkyTimeParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        switch (commandWord) {
        // MeetingEntry Commands
        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);
        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);
        case ListCommand.COMMAND_WORD:
            return new ListCommand();
        case OpenCommand.COMMAND_WORD:
            return new OpenCommandParser().parse(arguments);

        // Module Commands
        case ListModuleCommand.COMMAND_WORD:
            return new ListModuleCommand();

        // System Commands
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();
        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();
        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();
        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}

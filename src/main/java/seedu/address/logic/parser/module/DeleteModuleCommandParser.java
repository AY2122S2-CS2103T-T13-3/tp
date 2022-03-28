package seedu.address.logic.parser.module;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.module.DeleteModuleCommand;
import seedu.address.logic.parser.Parser;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteModuleCommand object.
 */
public class DeleteModuleCommandParser implements Parser<DeleteModuleCommand> {

    /**
     * Parses the given string {@code String} of arguments in the context of the DeleteModuleCommand
     * and returns a DeleteModuleCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteModuleCommand parse(String args) throws ParseException {
        try {
            final Index index = ParserUtil.parseIndex(args);
            return new DeleteModuleCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteModuleCommand.MESSAGE_USAGE), pe);
        }
    }
}
package seedu.watson.logic.parser;

import static seedu.watson.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_STUDENTCLASS;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_SUBJECT;
import static seedu.watson.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.watson.logic.commands.AddCommand;
import seedu.watson.logic.parser.exceptions.ParseException;
import seedu.watson.model.person.Address;
import seedu.watson.model.person.Attendance;
import seedu.watson.model.person.Email;
import seedu.watson.model.person.Name;
import seedu.watson.model.person.Person;
import seedu.watson.model.person.Phone;
import seedu.watson.model.person.Remark;
import seedu.watson.model.person.StudentClass;
import seedu.watson.model.person.subject.Subject;
import seedu.watson.model.person.subject.SubjectHandler;
import seedu.watson.model.tag.Tag;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddCommandParser implements Parser<AddCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns an AddCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_TAG,
                                       PREFIX_STUDENTCLASS, PREFIX_REMARK, PREFIX_SUBJECT);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL,
                                PREFIX_STUDENTCLASS)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        // New fields
        StudentClass studentClass = ParserUtil.parseStudentClass(argMultimap.getValue(PREFIX_STUDENTCLASS).get());
        Attendance attendance = new Attendance(); // Adding a student results in empty attendance initially
        Set<Remark> remarksList = ParserUtil.parseRemarks(argMultimap.getAllValues(PREFIX_REMARK));
        Set<Subject> subjectList = ParserUtil.parseSubjects(argMultimap.getAllValues(PREFIX_SUBJECT));
        SubjectHandler subjectHandler = new SubjectHandler(subjectList);

        Person person = new Person(name, phone, email, address, studentClass,
            attendance, remarksList, subjectHandler, tagList);

        return new AddCommand(person);
    }

}

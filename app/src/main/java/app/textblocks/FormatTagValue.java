package app.textblocks;

import java.util.List;

import app.util.Tuple;

/**
 * Class for associating {@link FormatTag}s with a value.
 * <p>
 * Created by justusadam on 05/12/15.
 */
public abstract class FormatTagValue {

    private FormatTag formatTag;

    /**
     * This constructor is for creating empty FormatTagValues to be used as initial values in new FormatTags.
     *
     * Every subclass will require such an empty constructor.
     */
    public FormatTagValue() {}

    protected FormatTagValue(FormatTag tag) {
        formatTag = tag;
    }

    public String getName() {
        return formatTag.getName();
    }

    public String asInput(String identifier) {
        return formatTag.asInput(identifier);
    }

    public String asIdentifier(String baseIdentifier) {
        return formatTag.asIdentifier(baseIdentifier);
    }

    /**
     * Construct a value of the same kind filles with the (parsed) value of s
     *
     * @param tag associated tag
     * @param s value string
     * @return new FormatTagValue
     * @throws TypeError
     */
    protected abstract FormatTagValue fromValue(FormatTag tag, String s) throws TypeError;

    /**
     * This is the canonical function for constructing a {@link FormatTagValue} from the {@link String} out of a html
     * form
     * <p>
     * It verifies the input and either throws a type error or constructs the value.
     *
     * @param tag   tag to associate the constructed value with
     * @param value the string from the form
     * @return new FormatTagValue
     * @throws TypeError
     */
    public final FormatTagValue fromForm(FormatTag tag, String value) throws TypeError {
        if (verify(value))
            return fromValue(tag, value);
        else
            throw new TypeError();
    }

    /**
     * Verifies the structure of the input.
     * <p>
     * This should be overwritten in subclasses if the input needs a specific structure. See {@link DateFormatTagValue#verify(String)}
     *
     * @param value String from the html form
     * @return true if the structure is correct
     */
    public boolean verify(String value) {
        return true;
    }

    /**
     * Constructs a String representation of the stored value.
     *
     * @return value as string
     */
    public abstract String valueRepresentation();

    /**
     * Two strings which are the appropriate html input element for this type.
     *
     * @return Tuple(delimiter 1, delimiter 2)
     */
    public abstract Tuple<String, String> inputDelims();

    /**
     * Returns some classes that the generated input field should have.
     *
     * @return List of html classes
     */
    public abstract List<String> getInputClasses();
}

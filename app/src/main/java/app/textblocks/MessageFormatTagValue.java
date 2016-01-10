package app.textblocks;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;

import app.util.Tuple;

/**
 * A format tag suitable for a longer message.
 * <p>
 * Created by justusadam on 05/12/15.
 */

@Entity
public class MessageFormatTagValue extends FormatTagValue {

    /**
     * This is part of a bit of boilerplate code to make the input classes from {@link #getInputClasses()} static.
     */
    private static final List<String> inputClasses = mkInputClasses();
    private String value;

    public MessageFormatTagValue(FormatTag tag, String value) {
        super(tag);
        this.value = value;
    }

    public MessageFormatTagValue() {
    }

    /**
     * Generates the list for {@link #inputClasses}
     *
     * @return list of html classes
     */
    protected static List<String> mkInputClasses() {
        List<String> l = new LinkedList<>();
        l.add("message");
        return l;
    }

    @Override
    protected FormatTagValue fromValue(FormatTag tag, String s) {
        return new MessageFormatTagValue(tag, s);
    }

    @Override
    public String valueRepresentation() {
        return value;
    }

    @Override
    public Tuple<String, String> inputDelims() {
        return new Tuple<>("<textarea ", "></textarea>");
    }

    @Override
    public List<String> getInputClasses() {
        return inputClasses;
    }
}

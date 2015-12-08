package app.textblocks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.StrSubstitutor;

import app.util.SelectorFunctions;

/**
 * This is the value associated with a TextBlock.
 *
 * Created by justusadam on 05/12/15.
 */
public class TextBlockValue {

    /**
     * The text block for which this is a value
     */
    private TextBlock textBlock;
    /**
     * Values and tags for this text block.
     */
    private List<FormatTagValue> values;

    public TextBlockValue(TextBlock textBlock, List<FormatTagValue> values) {
        this.textBlock = textBlock;
        this.values = values;
    }

    /**
     * Constructs a map suitable for substituting the format string in {@link #textBlock} to create the original message.
     *
     * @return map
     */
    public Map<String, String> makeValueMap () {
        return values.stream().collect(Collectors.toMap(
                FormatTagValue::getName
                , FormatTagValue::valueRepresentation
                , SelectorFunctions.second()
        ));
    }

    /**
     * Substitute the format string of the associated {@link #textBlock} with the values, creating the actual message.
     *
     * @return message
     */
    public String toString() {
        return new StrSubstitutor(makeValueMap()).replace(textBlock.getFormatString());
    }

    public long getId() {
        return textBlock.getId();
    }

    public String getFormatString() {
        return textBlock.getFormatString();
    }

    public List<FormatTag> getTags() {
        return textBlock.getTags();
    }
}

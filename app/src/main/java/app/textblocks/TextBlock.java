package app.textblocks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.StrSubstitutor;

import app.util.SelectorFunctions;

/**
 * This is the source text block in the database.
 *
 * This class abstracts a block of text (Textbaustein) in it's raw form (before values have been supplied).
 * It is responsible for rendering a form with input fields appropriate for the expected values that are supplied to the fields.
 */
public class TextBlock {

    // @Id @GeneratedValue
    private long id;

    /**
     * A string that can be substituted with {@link StrSubstitutor}
     */
    private String formatString;


    /**
     * A list of replaceable tags in the format string.
     */
    private List<FormatTag> tags;

    public TextBlock(String formatString, List<FormatTag> tags) {
        this.formatString = formatString;
        this.tags = tags;
    }

    public TextBlock(long id, String formatString, List<FormatTag> tags) {
        this.id = id;
        this.formatString = formatString;
        this.tags = tags;
    }

    public TextBlockValue toValue(List<FormatTagValue> values) {
        return new TextBlockValue(this, values);
    }

    /**
     * Build a map where the keys are the names of the tags from the internal tag list and the values are html form
     * fields with types appropriate for the type of the respective tag.
     *
     * This map is suitable for substituting into the {@link #formatString} for rendering a form.
     *
     * @return the map
     */
    public Map<String, String> makeReplacementMap () {
        return tags.stream().collect(Collectors.toMap(
                FormatTag::getName
                , (FormatTag t) -> t.asInput(String.valueOf(id))
                , SelectorFunctions.second()));
    }

    /**
     * Build a unique form for this text block.
     * @return html text and inputs
     */
    public String asForm() {
        return  makeSelectedInput() +
                new StrSubstitutor(makeReplacementMap()).replace(formatString);
    }

    /**
     * Whether this block was selected.
     * @param requestValues data from the http request
     * @return true if this block was selected
     */
    public boolean wasSelected(Map<String, String> requestValues) {
        return requestValues.containsKey(makeSelectedFieldID());
    }

    private String makeSelectedFieldID() {
        return id + "-selected";
    }

    /**
     * Create a html checkbox to determine if this block was selected.
     *
     * @return html checkbox
     */
    public String makeSelectedInput() {
        return "<input type=\"checkbox\" name=\"" + makeSelectedFieldID() + "\" />";
    }

    /**
     * Create a value from the input html for values
     *
     * @param requestValues values from the htp request
     * @return value
     */
    public TextBlockValue fromForm (Map<String, String> requestValues) {
        return toValue(valuesFromForm(requestValues));
    }

    /**
     * Construct a list of {@link FormatTagValue}s from the incoming request suitable for creating a TextBlockValue.
     *
     * @param requestValues values from the request in map form
     * @return values associated with their tags
     */
    public List<FormatTagValue> valuesFromForm(Map<String, String> requestValues) {
        return tags.stream().map(t -> {
                    try {
                        return t.toValue(requestValues.get(t.asIdentifier(String.valueOf(id))));
                    } catch (TypeError typeError) {
                        typeError.printStackTrace();
                        // TODO handle this.
                        return null;
                    }
                }
            ).collect(Collectors.toList());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFormatString() {
        return formatString;
    }

    public void setFormatString(String formatString) {
        this.formatString = formatString;
    }

    public List<FormatTag> getTags() {
        return tags;
    }

    public void setTags(List<FormatTag> tags) {
        this.tags = tags;
    }
}

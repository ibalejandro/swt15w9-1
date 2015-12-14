package app.textblocks;

import app.util.Tuple;

public final class FormatTag {
    private FormatTagValue value;
    private String name;

    public FormatTag(FormatTagValue value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Create a new none empty value of the same type as the current internal one from a stringSinput
     *
     * @param s input string from the html form
     * @return non-empty {@link FormatTagValue}
     * @throws TypeError
     */
    public FormatTagValue toValue(String s) throws TypeError {
        return value.fromForm(this, s);
    }

    /**
     * Construct a HTML input tag using information from the internal {@link #value}
     *
     * @param identifier Identifier from the TextBlock
     * @return html input tag
     */
    public String asInput(String identifier) {
        String myIdentifier = asIdentifier(identifier);
        Tuple<String, String> delims = value.inputDelims();

        return delims.get1() + "name=\"" +
                myIdentifier +
                '-' +
                name +
                "\" " +
                "class=\"" +
                value.getInputClasses().stream().reduce(String::concat).orElse("") +
                "\" " +
                delims.get2();
    }

    /**
     * Create an identifier for this tag using a base identifier from a {@link TextBlock}
     *
     * @param baseIdentifier identifier for the text block
     * @return unique identifier
     */
    public String asIdentifier(String baseIdentifier) {
        return baseIdentifier + "-" + name;
    }
}

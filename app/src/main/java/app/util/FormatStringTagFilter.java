package app.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import app.textblocks.FormatTag;
import app.textblocks.TextFormatTagValue;

/**
 * The FormatStringTagFilter parses a formatString and retrieves all
 * {@link FormatTag}s as a list.
 *
 * @author Mario Henze
 */
public class FormatStringTagFilter {
	private final String formatString;

	public FormatStringTagFilter(String formatString) {
		if (formatString.isEmpty())
			throw new IllegalArgumentException();

		this.formatString = formatString;
	}

	/**
	 * Returns all found {@link FormatTag}s in the supplied formatString.
	 * 
	 * @return list of all {@link FormatTag}s
	 */
	public List<FormatTag> getTags() {
		List<FormatTag> l = new LinkedList<>();

		// The words in a format string are delimited by space
		String delim = "[ ]";
		String[] tokens = formatString.split(delim);
		List<String> tl = Arrays.asList(tokens);

		for (String st : tl) {
			if (st.startsWith("$")) {
				String s;
				s = st.replaceAll("\\p{Punct}", "");
				s = s.replace("${", "");
				s = s.replace("}", "");
				l.add(new FormatTag(new TextFormatTagValue(), s));
			}
		}

		return l;
	}
}

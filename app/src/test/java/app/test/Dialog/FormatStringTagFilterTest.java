/**
 * 
 */
package app.test.Dialog;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import app.textblocks.FormatTag;
import app.util.FormatStringTagFilter;

/**
 * @author Mario
 *
 */
public class FormatStringTagFilterTest {
	/**
	 * Test method for
	 * {@link app.util.FormatStringTagFilter#FormatStringTagFilter(java.lang.String)}
	 * .
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testFormatStringTagFilter() {
		new FormatStringTagFilter("");
	}

	/**
	 * Test method for {@link app.util.FormatStringTagFilter#getTags()}.
	 */
	@Test
	public void testGetTags() {
		FormatStringTagFilter f = new FormatStringTagFilter("The ${animal} jumped over the ${target}. Now that's ${adj}.");
		String[] ct = {"animal", "target", "adj"};
		List<String> checkTags = Arrays.asList(ct);
		List<FormatTag> l = f.getTags();
		
		for (FormatTag formatTag : l) {
			checkTags.contains(formatTag);
		}
	}
}

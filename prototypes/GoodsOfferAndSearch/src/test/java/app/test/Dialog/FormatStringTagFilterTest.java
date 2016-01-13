/**
 * 
 */
package app.test.Dialog;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import app.util.FormatStringTagFilter;

/**
 * @author Mario
 *
 */
public class FormatStringTagFilterTest {
	private static Iterable<String> validExamples;
	private static Iterable<String> invalidExamples;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Add valid examples here that should pass the test
		List<String> vl = new ArrayList<>();
		vl.add("The ${animal} jumped over the ${target}. Now that's ${adj}.");
		vl.add("${animal}");
		vl.add("animal");
		validExamples = vl;

		// Add invalid examples here that should not pass the test
		List<String> il = new ArrayList<>();
		il.add("The ${anim&$$al} jumpe$d over the ${target}. Now that's ${adj}.");
		il.add("$");
		invalidExamples = il;
	}

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
		/*FormatStringTagFilter f = new FormatStringTagFilter("The ${animal} jumped over the ${target}. Now that's ${adj}.");
		List<FormatTag> l = f.getTags();
		
		for (FormatTag formatTag : l) {
			
		}*/
		fail("Not implemented yet.");
	}

	/**
	 * Test method for
	 * {@link app.util.FormatStringTagFilter#isValidFormatString(java.lang.String)}
	 * .
	 */
	@Test
	public void testIsValidFormatString() {
		for (String st : validExamples) {
			if (!(FormatStringTagFilter.isValidFormatString(st)))
				fail("Not every valid example recognized: " + st);
		}
		
		for (String st : invalidExamples) {
			if ((FormatStringTagFilter.isValidFormatString(st)))
				fail("An invalid example was recognized: " + st);
		}
	}

}

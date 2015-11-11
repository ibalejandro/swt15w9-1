import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import dialog.TextBlock;
import dialog.Substitute;

public class TextBlockTests {
	private static List<Substitute> subs = new ArrayList<>();

	@BeforeClass
	public static void setUpBeforeClass() {
		subs.add(new Substitute(2, "E1"));
		subs.add(new Substitute(3, "E2"));
		subs.add(new Substitute(0, "E0-"));
		subs.add(new Substitute(4, "-E3"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNulledArguments() {
		new TextBlock(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsEmptySubstituteList() {
		new TextBlock("String", new ArrayList<Substitute>());
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsEmptyString() {
		new TextBlock("", subs);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsSamePositionSubstitutes() {
		List<Substitute> s = new ArrayList<Substitute>();
		s.add(new Substitute(1, "W"));
		s.add(new Substitute(1, "W"));

		new TextBlock("String", s);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsInvalidPositionSubstitutes() {
		List<Substitute> s = new ArrayList<Substitute>();
		s.add(new Substitute(Integer.MAX_VALUE, "W"));
		s.add(new Substitute(Integer.MIN_VALUE, "W"));
		s.add(new Substitute(-1, "W"));

		new TextBlock("String", s);
	}

	@Test
	public void testGetSubstitutedSentence() {
		TextBlock tb = new TextBlock("S:-!", subs);
		assertEquals("E0-S:E1-E2!-E3", tb.getSubstitutedSentence());
	}
}

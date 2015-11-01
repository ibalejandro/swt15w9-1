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
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNulledArguments() {
		new TextBlock(null, null);
	}
	
	@Test
	public void testGetSubstitutedSentence() {
		TextBlock tb = new TextBlock("S:-!", subs);
		assertEquals("S:E1-!", tb.getSubstitutedSentence());
	}

}

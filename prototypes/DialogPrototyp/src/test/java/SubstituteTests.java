import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import dialog.Substitute;

public class SubstituteTests {
	private static Substitute sub;

	@BeforeClass
	public static void setUpBeforeClass() {
		sub = new Substitute(1, "string");
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsNulledArguments() {
		new Substitute(0, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsEmptyString() {
		new Substitute(0, "");
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsInvalidPosition() {
		new Substitute(-1, "String");
	}
	
	@Test
	public void testGetSubstituteLength() {
		assertEquals(sub.getSubstituteLength(), 6);
	}

	@Test
	public void testGetPosition() {
		assertEquals(sub.getSubstitutePosition(), 1);
	}

	@Test
	public void testGetSubstitute() {
		assertEquals(sub.getSubstitute(), "string");
	}

	@Test
	public void testCompareTo() {
		Substitute s1, s2, s3;
		s1 = new Substitute(1, "string");
		s2 = new Substitute(2, "string");
		s3 = new Substitute(3, "string");

		assertEquals(0, s1.compareTo(s1));
		assertEquals(-1, s1.compareTo(s2));
		assertEquals(-1, s1.compareTo(s3));

		assertEquals(1, s2.compareTo(s1));
		assertEquals(0, s2.compareTo(s2));
		assertEquals(-1, s2.compareTo(s3));

		assertEquals(1, s3.compareTo(s1));
		assertEquals(1, s3.compareTo(s2));
		assertEquals(0, s3.compareTo(s3));
	}
}

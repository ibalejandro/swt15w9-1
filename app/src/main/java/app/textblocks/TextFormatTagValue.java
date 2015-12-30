package app.textblocks;

import java.util.LinkedList;
import java.util.List;

import app.util.Tuple;

/**
 * A tag value which represents a small bit of text.
 *
 * Created by justusadam on 05/12/15.
 */
public class TextFormatTagValue extends FormatTagValue {

	private static final List<String> inputClasses = mkInputClasses();

	private String value;

	public TextFormatTagValue() {
	}

	public TextFormatTagValue(FormatTag tag, String value) {
		super(tag);
		this.value = value;
	}

	/**
	 * @see MessageFormatTagValue#mkInputClasses()
	 */
	protected static List<String> mkInputClasses() {
		List<String> l = new LinkedList<>();
		l.add("text");
		return l;
	}

	@Override
	protected FormatTagValue fromValue(FormatTag tag, String s) throws TypeError {
		return new TextFormatTagValue(tag, s);
	}

	@Override
	public String valueRepresentation() {
		return value;
	}

	@Override
	public Tuple<String, String> inputDelims() {
		return new Tuple<>("<input type=\"text\" ", " />");
	}

	@Override
	public List<String> getInputClasses() {
		return inputClasses;
	}
}

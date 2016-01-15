package app.textblocks;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import app.util.Tuple;
import lombok.NonNull;

/**
 * A FormatTag which has a date inside it.
 * <p>
 * Created by justusadam on 05/12/15.
 */
public class DateFormatTagValue extends FormatTagValue {
	private static final List<String> inputClasses = mkInputClasses();
	private static final DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);
	private Date date;

	public DateFormatTagValue(@NonNull FormatTag tag, @NonNull Date date) {
		super(tag);
		this.date = date;
	}

	/**
	 * @see MessageFormatTagValue#mkInputClasses()
	 */
	protected static List<String> mkInputClasses() {
		List<String> l = new LinkedList<>();
		l.add("date");
		return l;
	}

	/**
	 * Try to parse the date
	 *
	 * @param value
	 *            String from the html form
	 * @return true if date can be parsed with {@link #format}
	 */
	@Override
	public boolean verify(String value) {
		try {
			format.parse(value);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	@Override
	protected FormatTagValue fromValue(@NonNull FormatTag tag, @NonNull String s) throws TypeError {
		try {
			return new DateFormatTagValue(tag, format.parse(s));
		} catch (ParseException e) {
			throw new TypeError();
		}
	}

	@Override
	public String valueRepresentation() {
		return date.toString();
	}

	@Override
	public Tuple<String, String> inputDelims() {
		return new Tuple<>("<input type=\"date\" ", " />");
	}

	@Override
	public List<String> getInputClasses() {
		return inputClasses;
	}
}

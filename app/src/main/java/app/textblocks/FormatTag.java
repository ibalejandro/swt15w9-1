package app.textblocks;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import app.util.Tuple;
import lombok.NonNull;

@Entity
public final class FormatTag {
	@Id
	@GeneratedValue
	private long id;

	@OneToOne(cascade = CascadeType.ALL)
	private FormatTagValue value;
	private String name;

	public FormatTag() {

	}

	public FormatTag(@NonNull FormatTagValue value, @NonNull String name) {
		this.value = value;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Create a new none empty value of the same type as the current internal
	 * one from a stringSinput
	 *
	 * @param s
	 *            input string from the html form
	 * @return non-empty {@link FormatTagValue}
	 * @throws TypeError
	 */
	public FormatTagValue toValue(@NonNull String s) throws TypeError {
		return value.fromForm(this, s);
	}

	/**
	 * Construct a HTML input tag using information from the internal
	 * {@link #value}
	 *
	 * @param identifier
	 *            Identifier from the TextBlock
	 * @return html input tag
	 */
	public String asInput(String identifier) {
		String myIdentifier = asIdentifier(identifier);
		Tuple<String, String> delims = value.inputDelims();

		return delims.get1() + "name=\"" + myIdentifier + "\" " + "class=\""
				+ value.getInputClasses().stream().reduce(String::concat).orElse("") + "\" " + delims.get2();
	}

	/**
	 * Create an identifier for this tag using a base identifier from a
	 * {@link TextBlock}
	 *
	 * @param baseIdentifier
	 *            identifier for the text block
	 * @return unique identifier
	 */
	public String asIdentifier(@NonNull String baseIdentifier) {
		return baseIdentifier + "-" + name;
	}
}

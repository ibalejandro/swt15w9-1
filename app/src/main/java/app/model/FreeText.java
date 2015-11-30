package app.model;

import org.springframework.util.Assert;

/**
 * <h1>FreeText</h1> The FreeText is an implementation of {@link MessageElement}
 * . It is used to represent and store simple text.
 * 
 * @author Mario Henze
 */
public class FreeText extends MessageElement {
	private final String text;

	/**
	 * Constructor.
	 * 
	 * @param text
	 *            The text to store
	 */
	public FreeText(String text) {
		super();

		Assert.hasText(text);

		this.text = text;
	}

	@Override
	public String getContent() {
		return this.text;
	}
}

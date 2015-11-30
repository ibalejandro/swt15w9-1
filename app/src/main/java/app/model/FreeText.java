package app.model;

import org.springframework.util.Assert;

public class FreeText extends MessageElement {
	private final String text;

	public FreeText() {
		super();
		this.text = null;
	}

	public FreeText(String text) {
		super();

		Assert.hasText(text, "Textfield must not be null or empty!");

		this.text = text;
	}

	@Override
	public String getContent() {
		return this.text;
	}
}

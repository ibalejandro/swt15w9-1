package prototyp.model;

import javax.persistence.Entity;

import org.springframework.util.Assert;

@Entity
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

	public String getText() {
		return this.text;
	}
}

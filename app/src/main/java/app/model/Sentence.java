package app.model;

import org.springframework.util.Assert;

public class Sentence extends MessageElement {
	private String sentence;
	private int placeholderCount = 0;

	public Sentence(String sentence, int placeholderCount) {
		Assert.hasText(sentence);
		Assert.isTrue(placeholderCount > 0, "Amount of placeholders must be greater than 0!");
		
		this.sentence = sentence;
		this.placeholderCount = placeholderCount;
	}

	public String getFormattedSentence(Object... s) {
		Assert.isTrue(placeholderCount == s.length, "Amount of given Placeholders doesn't match needed amount!");

		return String.format(sentence, s);
	}
}

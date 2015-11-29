package prototyp.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.util.Assert;

public class TextBlock extends MessageElement {
	private List<Substitute> substitutes;
	private String sentence;
	private String substitutedSentence;

	public TextBlock(String sentence, List<Substitute> substitutes) {
		Assert.hasLength(sentence);
		Assert.notNull(substitutes);

		this.sentence = sentence;

		if (substitutes.size() == 0) {
			throw new IllegalArgumentException("List of Substitutes must not be empty!");
		}

		Set<Integer> posList = new LinkedHashSet<>();
		for (Substitute substitute : substitutes) {
			if (!posList.add(substitute.getSubstitutePosition())) {
				throw new IllegalArgumentException(
						"List of Substitutes must not contain subtitutes with the same position!");
			}

			if ((substitute.getSubstitutePosition() < 0) || (substitute.getSubstitutePosition() > sentence.length())) {
				throw new IllegalArgumentException(
						"List of Substitutes must only contain substitutes with a position in the range of 0 to sentence.length() + 1!");
			}
		}

		/**
		 * Prepare the list in such a way that the substitutes order corresponds
		 * to their actual substitution-position.
		 */
		Collections.sort(substitutes);

		/**
		 * Every insertion into the original string shifts the position of the
		 * other substitutes for the length of the substitute. This offset gets
		 * accumulated in the lengthAccumulator.
		 */
		StringBuilder sb = new StringBuilder(sentence);
		int lengthAccumulator = 0;
		for (Substitute substitute : substitutes) {
			sb.insert(substitute.getSubstitutePosition() + lengthAccumulator, substitute.getSubstitute());
			lengthAccumulator += substitute.getSubstituteLength();
		}

		this.substitutedSentence = sb.toString();

		this.substitutes = substitutes;
	}

	public List<Substitute> getSubstitutes() {
		return this.substitutes;
	}

	public String getSentence() {
		return this.sentence;
	}

	public String getSubstitutedSentence() {
		return this.substitutedSentence;
	}
}

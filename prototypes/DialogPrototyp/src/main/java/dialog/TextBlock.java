package dialog;

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
		
		//Checking the list of substitutes for sane entries
		if (substitutes.size() == 0) {
			throw new IllegalArgumentException("List of Substitutes must not be empty!");
		}
		
		Set<Integer> posList = new LinkedHashSet<>();
		for (Substitute substitute : substitutes) {
			if (!posList.add(substitute.getSubstitutePosition())) {
				throw new IllegalArgumentException("List of Substitutes must not contain subtitutes with the same position!");
			}
		}
		
		StringBuilder sb = new StringBuilder(sentence);
		for (Substitute substitute : substitutes) {
			sb.insert(substitute.getSubstitutePosition(), substitute.getSubstitute());
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

package dialog;

import org.springframework.util.Assert;

public class Substitute implements Comparable<Substitute> {
	private int position;
	private String substitute;

	public Substitute(int position, String substitute) {
		Assert.hasLength(substitute);

		this.position = position;
		this.substitute = substitute;
	}

	public int getSubstituteLength() {
		return this.substitute.length();
	}

	public int getSubstitutePosition() {
		return this.position;
	}

	public String getSubstitute() {
		return this.substitute;
	}

	@Override
	public int compareTo(Substitute o) {
		if (this.position < o.getSubstitutePosition()) {
			return -1;
		}

		if (this.position > o.getSubstitutePosition()) {
			return 1;
		}

		return 0; // if this.position == o.getSubstitutedPosition();
	}
}

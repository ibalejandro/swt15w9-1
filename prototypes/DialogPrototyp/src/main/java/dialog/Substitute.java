package dialog;

import org.springframework.util.Assert;

public class Substitute {
	private int position;
	private String substitute;
	
	public Substitute(int position, String substitute) {
		Assert.hasLength(substitute);
		
		this.position = position;
		this.substitute = substitute;
	}
	
	public void setSubstitutePosition(int position) {
		this.position = position;
	}
	
	public void setSubstitute(String substitute) {
		this.substitute = substitute;
	}
	
	public int getSubstitutePosition() {
		return this.position;
	}
	
	public String getSubstitute() {
		return this.substitute;
	}
}

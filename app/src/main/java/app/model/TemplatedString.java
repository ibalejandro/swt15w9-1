package app.model;

import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.util.Assert;

public class TemplatedString extends MessageElement {
	private final String templateString;
	private Map<String, ? extends Object> valueMap;

	public TemplatedString(String templateString, Map<String, ? extends Object> valueMap) {
		Assert.hasText(templateString);
		Assert.notEmpty(valueMap);
		
		this.templateString = templateString;
		this.valueMap = valueMap;
	}

	/**
	 * Return the string, resulting from a substitution of the templateString
	 * with the valueMap.
	 * 
	 * @return the substituted string
	 */
	@Override
	public String getContent() {
		StrSubstitutor sub = new StrSubstitutor(this.valueMap);
		return sub.replace(this.templateString);
	}
	
	/**
	 * Changes the valueMap used for the substitution.
	 * 
	 * @param valueMap the new value map
	 */
	public void setValueMap(Map<String, ? extends Object> valueMap) {
		Assert.notEmpty(valueMap);
		
		this.valueMap = valueMap;
	}
}

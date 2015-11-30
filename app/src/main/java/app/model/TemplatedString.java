package app.model;

import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.springframework.util.Assert;

/**
 * <h1>Templated String</h1> The TemplatedString is used to represent an
 * coherent sentence with implicit meaning, which must be substituted by actual
 * statements. Therefore the template string contains certain expressions, which
 * get substituted by the {@link StrSubstitutor}. The substituter itself uses a
 * user supplied {@link Map}, which contains all substitutable expressions and
 * their values.
 * 
 * @author Mario Henze
 */
public class TemplatedString extends MessageElement {
	private final String templateString;
	private Map<String, ? extends Object> valueMap;

	/**
	 * Constructor.
	 * 
	 * @param templateString
	 *            The template String, which gets substituted
	 * @param valueMap
	 *            The map used to perform the substitution
	 */
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
	 * @param valueMap
	 *            the new value map
	 */
	public void setValueMap(Map<String, ? extends Object> valueMap) {
		Assert.notEmpty(valueMap);

		this.valueMap = valueMap;
	}
}

package app.model;

import java.util.Date;

public abstract class MessageElement {
	protected final Date date;

	public MessageElement() {
		this.date = new Date();
	}

	public Date getDate() {
		return this.date;
	}
	
	/**
	 * Retrieves the content of the element as String.
	 * 
	 * @return content-string
	 */
	public abstract String getContent();
}

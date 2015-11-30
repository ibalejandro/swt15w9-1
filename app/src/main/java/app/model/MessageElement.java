package app.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * <h1>MessageElement</h1> The MessageElement is the persistent object of an
 * entry in the {@link Dialog}. It serves as a foundation for the different
 * types of entries in a dialog.
 * 
 * @author Mario Henze
 */
@Entity
public abstract class MessageElement {
	protected @Id @GeneratedValue long id;
	protected final Date date;

	/**
	 * Constructor.
	 */
	public MessageElement() {
		this.date = new Date();
	}

	/**
	 * Getter mainly used by JPA.
	 * 
	 * @return the ID of the entry
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return The posting date of the entry
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * The method is used to retrieve the finished representation of an entry in
	 * a dialog.
	 * 
	 * @return The final string representation of the entry
	 */
	public abstract String getContent();
}

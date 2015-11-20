package app.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class MessageElement {
	protected @Id @GeneratedValue long id;
	protected final Date date;

	public MessageElement() {
		this.date = new Date();
	}

	public long getId() {
		return id;
	}

	public Date getDate() {
		return this.date;
	}
}

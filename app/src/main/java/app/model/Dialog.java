package app.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

//TODO: should it really be suppressed?
@SuppressWarnings("serial")
@Entity
public class Dialog implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	private long userId;
	
	private String title;
	
	@ElementCollection(fetch = FetchType.EAGER)
	protected List<MessageElement> messageHistory;
	
	@SuppressWarnings("unused")
	private Dialog() {
		
	}
	
	public Dialog(String title, long userId, List<MessageElement> messageHistory) {
		this.title = title;
		this.userId = userId;
		this.messageHistory = messageHistory;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<MessageElement> getMessageHistory() {
		return messageHistory;
	}

	public void setMessageHistory(List<MessageElement> messageHistory) {
		this.messageHistory = messageHistory;
	}
}

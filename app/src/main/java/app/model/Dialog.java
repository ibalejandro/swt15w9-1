package app.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.util.Assert;

//TODO: should it really be suppressed?
@SuppressWarnings("serial")
@Entity
public class Dialog implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User userA;
	
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User userB;
	
	private String title;
	
	//@ElementCollection(fetch = FetchType.EAGER)
	@OneToMany(cascade = CascadeType.ALL) 
	protected List<MessageElement> messageHistory = new LinkedList<>();
	
	@SuppressWarnings("unused")
	private Dialog() {
		
	}
	
	public Dialog(String title, User userA, User userB) {
		Assert.hasText(title, "Title of dialog must be not empty!");
		Assert.notNull(userA, "User A must not be empty!");
		Assert.notNull(userB, "User B must not be empty!");
		
		this.title = title;
		this.userA = userA;
		this.userB = userB;
	}
	
	public void addMessageElement(MessageElement msg) {
		this.messageHistory.add(msg);
	}

	public long getId() {
		return id;
	}

	public User getUserA() {
		return userA;
	}
	
	public User getUserB() {
		return userB;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Iterable<MessageElement> getMessageHistory() {
		return messageHistory;
	}
}

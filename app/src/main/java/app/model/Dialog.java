package app.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.util.Assert;

import app.textblocks.Chat;

/**
 * <h1>Dialog</h1> The Dialog "glues" all {@link Chat}s together with
 * both {@link User}s.
 * 
 * @author Mario Henze
 */
@Entity
public class Dialog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User userA;

	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private User userB;

	private String title;

	@ElementCollection(fetch = FetchType.EAGER)
	protected List<Chat> messageHistory = new LinkedList<>();

	/**
	 * Constructor.
	 * 
	 * @param title
	 *            The title of the Conversion
	 * @param userA
	 *            First participant
	 * @param userB
	 *            Second participant
	 */
	public Dialog(String title, User userA, User userB) {
		Assert.hasText(title, "Title of dialog must be not empty!");
		Assert.notNull(userA, "User A must not be empty!");
		Assert.notNull(userB, "User B must not be empty!");

		this.title = title;
		this.userA = userA;
		this.userB = userB;
	}

	/**
	 * Adds the given {@link Chat} to the messageHistory.
	 * 
	 * @param msg
	 *            The messageElement to be added
	 */
	public void addMessageElement(Chat msg) {
		this.messageHistory.add(msg);
	}

	/**
	 * Getter used mainly by JPA.
	 * 
	 * @return ID
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return First participant of dialog
	 */
	public User getUserA() {
		return userA;
	}

	/**
	 * @return Second participant of dialog
	 */
	public User getUserB() {
		return userB;
	}

	/**
	 * @return The title of the dialog
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            The new title of the dialog
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return A list of all entries in the dialog
	 */
	public Iterable<Chat> getMessageHistory() {
		return this.messageHistory;
	}
}

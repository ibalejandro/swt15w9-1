package app.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import app.textblocks.Chat;
import lombok.NonNull;
import lombok.ToString;

/**
 * <h1>Dialog</h1> The Dialog "glues" all {@link Chat}s together with both
 * {@link User}s.
 * 
 * @author Mario Henze
 */
@Entity
@ToString
public class Dialog implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne
	private User userA;

	@ManyToOne
	private User userB;

	private String title;

	@OneToMany(cascade = CascadeType.ALL)
	protected List<Chat> messageHistory = new LinkedList<>();

	public Dialog() {

	}

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
	public Dialog(@NonNull String title, @NonNull User userA, @NonNull User userB) {
		if (userA.getId() == userB.getId()) {
			throw new IllegalArgumentException("userA is the same as userB");
		}
		
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
	public void addMessageElement(@NonNull Chat msg) {
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
	public void setTitle(@NonNull String title) {
		this.title = title;
	}

	/**
	 * @return A list of all entries in the dialog
	 */
	public Iterable<Chat> getMessageHistory() {
		return this.messageHistory;
	}
}

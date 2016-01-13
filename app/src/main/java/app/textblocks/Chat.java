package app.textblocks;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import app.model.User;
import lombok.Getter;
import lombok.NonNull;

/**
 * This class represents a single conversation step that has happened. Multiple
 * text blocks, all with values in them.
 * <p>
 * Created by justusadam on 05/12/15.
 */
@Entity
public class Chat {
	@Id
	@GeneratedValue
	private Long id;

	private Date date;
	
	@ManyToOne
	private User author;

	@OneToMany(cascade = CascadeType.ALL)
	private List<TextBlockValue> blocks;

	public Chat() {

	}

	public Chat(@NonNull List<TextBlockValue> blocks, @NonNull User author) {
		this.blocks = blocks;
		this.date = new Date();
		this.author = author;
	}

	/**
	 * Renders the chat.
	 *
	 * @return rendered chat
	 */
	@Override
	public String toString() {
		return blocks.stream().map(TextBlockValue::toString).map((String s) ->
		// You may use this function to surround each text block value with a
		// html wrapper,
		// like some <div> elements
		"<blockquote class = \"entrytext\">" + s + "</blockquote>"
		).reduce(String::concat).get();
	}

	public List<TextBlockValue> getBlocks() {
		return blocks;
	}

	public void setBlocks(@NonNull List<TextBlockValue> blocks) {
		this.blocks = blocks;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}
	
	public User getAuthor() {
		return author;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
}

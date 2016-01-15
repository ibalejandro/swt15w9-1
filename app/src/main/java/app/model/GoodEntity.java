package app.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.servlet.http.Part;

// Imports für die Bildeinbindung.
import java.util.Collection;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * <h1>GoodEntity</h1> The GoodEntity is the persistent object of a good. It
 * contains the whole characteristics of a good and it is used to save goods in
 * the database.
 *
 * @author Alejandro Sánchez Aristizábal
 * @since 19.11.2015
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "GOODS")
public class GoodEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;

	/* Ferdinand's Code */

	private static final ImageObserver observer = (img, infoflags, x, y, width, height) -> true;

	private String name;
	private String description;

	/*
	 * The JPA created a technology named Lazy Loading to the classes
	 * attributes. We could define Lazy Loading by: “the desired information
	 * will be loaded (from database) only when it is needed”. The container
	 * will notice if the collection is a lazy attribute and it will “ask” the
	 * JPA to load this collection from the database. To avoid Lazy Loading you
	 * have to use Eager Loading.
	 */

	@OneToOne(targetEntity = TagEntity.class, fetch = FetchType.EAGER)
	private TagEntity tag;

	@Column(length = 90001)
	private byte[] picture;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User user;

	@SuppressWarnings("unused")
	private GoodEntity() {
	} // For the sake of JPA.

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            The good's name
	 * @param description
	 *            The good's description
	 * @param tag
	 *            The tag associated with the good
	 * @param picture
	 *            The link of the picture associated with the good
	 * @param user
	 *            The user who is offering the good
	 */

	public GoodEntity(String name, String description, TagEntity tag, Part pic, User user) {
		this.name = name;
		this.description = description;
		this.tag = tag;

		this.picture = createPicture(pic);

		this.user = user;
	}

	 /**
	  * Converts the given Part into a byte array, after resizing it if it's wider than 512px.
	  * If it's null, a placeholder image is returned instead.
	  * 
	  * @param picture Part from a multipart request representing a picture
	  * 
	  */
	
	public static byte[] createPicture(Part picture) {
		/* Ferdinand's code */
		try {
			InputStream is;
			if (picture == null || picture.getSize() == 0L) {
				System.out.println("Kein Bild");
				is = ClassLoader.getSystemResourceAsStream("static/resources/AltBild.png");
			} else {
				is = picture.getInputStream();
			}

			BufferedImage img = ImageIO.read(is);

			double scaling;
			if (img.getWidth() != 512) {
				scaling = 512.0 / img.getWidth();
			} else {
				scaling = 1.0;
			}

			BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
			img2.createGraphics().drawImage(img, 0, 0, Color.WHITE, observer);

			BufferedImage imgOut = new BufferedImage((int) (scaling * (img.getWidth())),
					(int) (scaling * (img.getHeight())), BufferedImage.TYPE_INT_RGB);
			Graphics2D g = imgOut.createGraphics();
			AffineTransform transform = AffineTransform.getScaleInstance(scaling, scaling);
			g.drawImage(img2, transform, observer);

			ByteArrayOutputStream imgoutput = new ByteArrayOutputStream();
			ImageIO.write(imgOut, "jpg", imgoutput);
			return imgoutput.toByteArray();

		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		/* */
	}

	/**
	 * This method builds an empty and invalid GoodEntity
	 * 
	 * @return GoodEntity An empty and invalid good
	 */
	public static GoodEntity createEmptyGood() {
		return new GoodEntity("", "", null, null, null);
	}

	/**
	 * This method returns the size of a given Iterable Object
	 * 
	 * @param Iterable<?>
	 *            An Iterable Object
	 * @return int The size of the given Iterable Object
	 */
	public static int getIterableSize(Iterable<?> it) {
		if (it instanceof Collection) {
			return ((Collection<?>) it).size();
		} else {
			int i = 0;
			for (@SuppressWarnings("unused")
			Object obj : it) {
				i++;
			}
			return i;
		}
	}

	/**
	 * This method returns the type 'good' and its respective id for a given
	 * item.
	 * 
	 * @return String The information of an item in the format good{id}
	 */
	public String getItemTypeAndId() {
		return "good" + id;
	}

	/**
	 * This method returns the id for a given construct good{id}.
	 * 
	 * @return String The good's id for the given construct good{id}
	 */
	public static String getIdFromConstruct(String construct) {
		return construct.substring(4);
	}

	public String getItemLink() {
		return "good/" + id;
	}

	public String getItemPictureLink() {
		return id + "/image";
	}

	/**
	 * This method builds a String in which the good's information is presented
	 * as a JSON object.
	 * 
	 * @return String The information of a good in JSON format
	 */
	@Override
	public String toString() {
		return String.format(
				"{\"id\": \"%d\", \"name\": \"%s\", " + "\"description\": \"%s\", \"tag\": \"%s\", "
						+ "\"picture\": \"%s\", \"user\": \"%s\"}",
				id, name, description, tag.toString(), picture, user.toString());
	}

	/**
	 * Getter.
	 * 
	 * @return long The good's id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter.
	 * 
	 * @param id
	 *            The good's id
	 * @return Nothing
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter.
	 * 
	 * @return String The good's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter.
	 * 
	 * @param String
	 *            The good's name
	 * @return Nothing
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter.
	 * 
	 * @return String The good's description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter.
	 * 
	 * @param String
	 *            The good's description
	 * @return Nothing
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter.
	 * 
	 * @return TagEntity The good's tag
	 */
	public TagEntity getTag() {
		return tag;
	}

	/**
	 * Setter.
	 * 
	 * @param TagEntity
	 *            The good's tag
	 * @return Nothing
	 */
	public void setTag(TagEntity tag) {
		this.tag = tag;
	}

	/**
	 * Getter.
	 * 
	 * @return byte[] The good's picture
	 */
	public byte[] getPicture() {
		return picture;
	}

	/**
	 * Setter.
	 * 
	 * @param byte[]
	 *            The good's picture
	 * @return Nothing
	 */
	public void setPicture(byte[] picture) {
		this.picture = picture;
	}

	/**
	 * Getter.
	 * 
	 * @return User The user who offered the good
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Setter.
	 * 
	 * @param User
	 *            The user who offered the good
	 * @return Nothing
	 */
	public void setUser(User user) {
		this.user = user;
	}

}

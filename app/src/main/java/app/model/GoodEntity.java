package app.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// Imports für die Bildeinbindung.
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;

/**
* <h1>GoodEntity</h1>
* The GoodEntity is the persistent object of a good. It contains the whole
* characteristics of a good and it is used to save goods in the database.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@SuppressWarnings("serial")
@Entity
@Table(name = "GOODS")
public class GoodEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private String description;
	
	/* Ferdinand's Code */
	
	private static final ImageObserver observer = (img, infoflags, x,y,width,height)->true;

	/*
   * The JPA created a technology named Lazy Loading to the classes 
   * attributes. We could define Lazy Loading by: “the desired information 
   * will be loaded (from database) only when it is needed”.
   * The container will notice if the collection is a lazy attribute and 
   * it will “ask” the JPA to load this collection from the database.
   * To avoid Lazy Loading you have to use Eager Loading.
   */
	@OneToOne(targetEntity = TagEntity.class, fetch = FetchType.EAGER) 
	private TagEntity tag;
	
	private String picture;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER) 
	private User user;
	
	@SuppressWarnings("unused")
	private GoodEntity() {} // For the sake of JPA.

	/**
	 * Constructor.
	 * @param name  The good's name
	 * @param description The good's description
	 * @param tag The tag associated with the good
	 * @param picture The link of the picture associated with the good
	 * @param user The user who is offering the good
	 */
	 
	 public GoodEntity(String name, String description, TagEntity tag, 
	                   String picture, User user) {
    this.name = name;
    this.description = description;
    this.tag = tag;
    
    /* Ferdinand's code */
		//Erschaffung eines temporären Files, um das Bild zu speichern.
		try{
			URL srcPic = new URL(picture);
			//Path hpath = Paths.get("src/main/resources/static/images");
			//System.out.println(hpath.toAbsolutePath().toString());
			Path tempPic = Files.createTempFile("picture", ".jpg").toAbsolutePath();
			BufferedImage img = ImageIO.read(srcPic);
			
			double scaling;
			if(img.getWidth()!=128){
				scaling = 128/img.getWidth();
			}else{
				scaling = 1;
			}
			
			BufferedImage imgOut = new BufferedImage((int)(scaling*img.getWidth()),(int)(scaling*img.getHeight()),img.getType());
			Graphics2D g = imgOut.createGraphics();
			AffineTransform transform = AffineTransform.getScaleInstance(scaling, scaling);
			g.drawImage(img, transform, observer);
			ImageIO.write(imgOut, "jpg", tempPic.toFile());
			String h = tempPic.toString();
			h = h.replace("\\", "/");
			if (!Paths.get(h).toFile().canRead()) {
			  System.out.println("Ich kanns nicht lesen!");
			}
			h = "file:///" + h;
			this.picture = h;
		} 
		catch(Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
		/* */
		
		this.user = user;
  }
	
	 /**
	   * This method builds an empty and invalid GoodEntity
	   * @return GoodEntity An empty and invalid good
	   */
	public static GoodEntity createEmptyGood() {
	  return new GoodEntity("", "", null, "", null);
	}
  
  /**
   * This method builds a String in which the good's information is presented
   * as a JSON object.
   * @return String The information of a good in JSON format
   */
  @Override
  public String toString() {
    return String.format("{\"id\": \"%d\", \"name\": \"%s\", "
                         + "\"description\": \"%s\", \"tag\": \"%s\", "
                         + "\"picture\": \"%s\", \"user\": \"%s\"}", id, name, 
                         description, tag.toString(), picture, user.toString());
  }

  /**
   * Getter.
   * @return long The good's id
   */
  public long getId() {
    return id;
  }
  
  /**
   * Setter.
   * @param id The good's id
   * @return Nothing
   */
  public void setId(long id) {
    this.id = id;
  }
  
  /**
   * Getter.
   * @return String The good's name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Setter.
   * @param String The good's name
   * @return Nothing
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Getter.
   * @return String The good's description
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * Setter.
   * @param String The good's description
   * @return Nothing
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * Getter.
   * @return TagEntity The good's tag
   */
  public TagEntity getTag() {
    return tag;
  }
  
  /**
   * Setter.
   * @param TagEntity The good's tag
   * @return Nothing
   */
  public void setTag(TagEntity tag) {
    this.tag = tag;
  }
  
  /**
   * Getter.
   * @return String The good's picture link
   */
  public String getPicture() {
    return picture;
  }
  
  /**
   * Setter.
   * @param String The good's picture link
   * @return Nothing
   */
  public void setPicture(String picture) {
    this.picture = picture;
  }

  /**
   * Getter.
   * @return User The user who offered the good
   */
  public User getUser() {
    return user;
  }
  
  /**
   * Setter.
   * @param User The user who offered the good
   * @return Nothing
   */
  public void setUser(User user) {
    this.user = user;
  }

}

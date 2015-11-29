package app.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

// Imports für die Bildeinbindung

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;

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
	public String pic;
	
	@ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL,
	 			  fetch = FetchType.EAGER) private User user;
	
	
	/*
	 * The JPA created a technology named Lazy Loading to the classes 
	 * attributes. We could define Lazy Loading by: “the desired information 
	 * will be loaded (from database) only when it is needed”.
	 * The container will notice if the collection is a lazy attribute and 
	 *  it will “ask” the JPA to load this collection from the database.
	 *  To avoid Lazy Loading you have to use Eager Loading
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	protected Set<String> tags = new HashSet<>();
	//private long userId;
	
	@SuppressWarnings("unused")
	private GoodEntity() {} // For the sake of JPA.

	/**
	 * Constructor.
	 * @param name  The good's name
	 * @param description The good's description
	 * @param tags The tags associated with the good
	 * @param userId The ID of the user who is offering the good
	 */
    this.name = name;
    this.description = description;
		//Erschaffung eines temporären Files, um das Bild zu speichern.
		try{
			URL srcPic = new URL(piclink);
			//Path hpath = Paths.get("src/main/resources/static/images");
			//System.out.println(hpath.toAbsolutePath().toString());
			Path tempPic = Files.createTempFile("picture", ".jpg").toAbsolutePath();
			BufferedImage img = ImageIO.read(srcPic);
			ImageIO.write(img, "jpg", tempPic.toFile());
			String h = tempPic.toString();
			h=h.replace("\\", "/");
			if(!Paths.get(h).toFile().canRead()){System.out.println("Ich kanns nicht lesen!");}
			h="file:///"+h;
			this.pic=h;
		} catch(Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	this.tags = tags;
		//this.userId = userId;
	this.user = user;
  }
    
  /**
   * This method builds a String in which all the tags are separated by commas.
   * @return String All the tags separated by commas
   */
  public String getTagsAsString() {
    String tagsAsString = "";
    if (!tags.isEmpty()) {
    	for (String tag : tags) tagsAsString += tag + ", ";
    	// This statement eliminates the last ", ".
    	tagsAsString = tagsAsString.substring(0, tagsAsString.length()-2);
  	}
      
    return tagsAsString;
  }
  
  /**
   * This method builds a String in which the good's information is presented
   * as a JSON object.
   * @return String The information of a good in JSON format
   */
  @Override
  public String toString() {
    return String.format("{\"id\": \"%d\", \"name\": \"%s\", "
                         + "\"description\": \"%s\", \"tags\": \"%s\", "
                         + "\"userId\": \"%d\"}", id, name, description, 
                         tags.toString(), user.getId());
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
   * @return Set<String> The good's tags
   */
  public Set<String> getTags() {
    return tags;
  }
  
  /**
   * Setter.
   * @param Set<String> The good's tags
   * @return Nothing
   */
  public void setTags(Set<String> tags) {
    this.tags = tags;
  }

  /**
   * Getter.
   * @return long The user-ID
   */
  public long getUserId() {
    return user.getId();
  }
  
  /**
   * Setter.
   * @param User The user
   * @return Nothing
   */
  public void setUser(User user) {
    this.user = user;
  }

}

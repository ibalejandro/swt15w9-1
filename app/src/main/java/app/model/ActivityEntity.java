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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

// Imports für die Bildeinbindung.
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;

/**
* <h1>ActivityEntity</h1>
* The ActivityEntity is the persistent object of an activity. It contains the 
* whole characteristics of an activity and it is used to save activities in the 
* database.
*
* @author Alejandro Sánchez Aristizábal
* @since  30.12.2015
*/
@SuppressWarnings("serial")
@Entity
@Table(name = "ACTIVITIES")
public class ActivityEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long id;
	
	/* Ferdinand's Code */
	
	private static final ImageObserver observer = (img, infoflags, x, y, width,
                                                 height)->true;
	
	private String name;
	private String description;

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
	
	@Column(length = 90001)
	private byte[] picture;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER) 
	private User user;
	
	@DateTimeFormat(iso = ISO.DATE)
	private Date startDate;
	
	@DateTimeFormat(iso = ISO.DATE)
  private Date endDate;
	
	@SuppressWarnings("unused")
	private ActivityEntity() {} // For the sake of JPA.

	/**
	 * Constructor.
	 * @param name  The activity's name
	 * @param description The activity's description
	 * @param tag The tag associated with the activity
	 * @param picture The link of the picture associated with the activity
	 * @param user The user who is offering the activity
	 * @param startDate The activity's start date
	 * @param endDate The activity's end date
	 */
	 
	 public ActivityEntity(String name, String description, TagEntity tag, 
	                       String picture, User user, Date startDate,
	                       Date endDate) {
    this.name = name;
    this.description = description;
    this.tag = tag;
    
    this.picture = createPicture(picture);
		
		this.user = user;
		
		this.startDate = startDate;
		this.endDate = endDate;
  }
	 
	public static byte[] createPicture(String pictureLink) {
	  /* Ferdinand's code */
    try{
      URL srcPic = new URL(pictureLink);
      BufferedImage img = ImageIO.read(srcPic);

			double scaling;
			if (img.getWidth() != 512) {
				scaling = 512.0 / img.getWidth();
			}
      else{
				scaling = 1.0;
			}
      
			BufferedImage img2 = new BufferedImage(img.getWidth(), img.getHeight(), 
                                             BufferedImage.TYPE_INT_RGB);
			img2.createGraphics().drawImage(img, 0, 0, Color.WHITE, observer);
			
			BufferedImage imgOut = new BufferedImage((int)(scaling*((double)img.getWidth())),(int)(scaling*((double)img.getHeight())),img.TYPE_INT_RGB);
			Graphics2D g = imgOut.createGraphics();
			AffineTransform transform = AffineTransform.getScaleInstance(scaling, 
                                                                   scaling);
			g.drawImage(img2, transform, observer);
      
      ByteArrayOutputStream imgoutput = new ByteArrayOutputStream();
      ImageIO.write(imgOut, "jpg", imgoutput);
      return imgoutput.toByteArray();
      
    } 
    catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
    /* */
	}
	
	/**
	 * This method builds an empty and invalid ActivityEntity
	 * @return ActivityEntity An empty and invalid activity
	 */
	public static ActivityEntity createEmptyActivity() {
	  return new ActivityEntity("", "", null, "", null, null, null);
	}
	
	/**
   * This method returns the size of a given Iterable Object
   * @param Iterable<?> An Iterable Object
   * @return int The size of the given Iterable Object
   */
	public static int getIterableSize(Iterable<?> it) {
    if (it instanceof Collection) return ((Collection<?>)it).size();
    else {
      int i = 0;
      for (@SuppressWarnings("unused") Object obj : it) i++;
      return i;
    }
  }
	
	/**
   * This method converts a date in String format into a java Date
   * @param String A date in String format
   * @return Date The corresponding java Date for the date in String format
   */
  public static Date convertStringIntoDate(String dateInString) {
    if (dateInString == null || dateInString.trim().isEmpty()) return null;
    
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    try {
      Date date = formatter.parse(dateInString);
      formatter.applyPattern("dd/MM/yyyy");
      String newFormatDateInString = formatter.format(date);
      date = formatter.parse(newFormatDateInString);
     
      return date;
    } 
    catch (ParseException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  /**
   * This method converts a date into a String
   * @param Date A java Date
   * @return String The corresponding String for the java Date
   */
  public static String getStringFromDate(Date date) {
    if (date == null) return null;
  
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
    
    return formatter.format(date);
  }
  
  /**
   * This method changes to 00:00:00 a date's time
   * @param Date A java Date
   * @return Date The given java Date but with its time changed to 00:00:00
   */
  public static Date getZeroTimeDate(Date date) {
    Date dateWithoutTime = date;
    Calendar calendar = Calendar.getInstance();

    calendar.setTime(date);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    calendar.set(Calendar.MILLISECOND, 0);

    dateWithoutTime = calendar.getTime();

    return dateWithoutTime;
  }
  
  /**
   * This method returns the type 'activity' and its respective id for a given 
   * item.
   * @return String The information of an item in the format activity{id}
   */
  public String getItemTypeAndId() {
    return "activity" + id;
  }
  
  public String getItemPictureLink(){
	  return id+"/activityimage";
  }
  
  /**
   * This method converts a date into a String for input fields
   * @param Date A java Date
   * @return String The corresponding String for the java Date
   */
  public static String getStringFromDateForInput(Date date) {
    if (date == null) return null;
  
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    
    return formatter.format(date);
  }
  
  /**
   * This method builds a String in which the activity's information is 
   * presented as a JSON object.
   * @return String The information of an activity in JSON format
   */
  @Override
  public String toString() {
    return String.format("{\"id\": \"%d\", \"name\": \"%s\", "
                         + "\"description\": \"%s\", \"tag\": \"%s\", "
                         + "\"picture\": \"%s\", \"user\": \"%s\""
                         + "\"startDate\": \"%s\", \"endDate\": \"%s\"}", id, 
                         name, description, tag.toString(), picture, 
                         user.toString(), getStringFromDate(startDate), 
                         getStringFromDate(endDate));
  }

  /**
   * Getter.
   * @return long The activity's id
   */
  public long getId() {
    return id;
  }
  
  /**
   * Setter.
   * @param id The activity's id
   * @return Nothing
   */
  public void setId(long id) {
    this.id = id;
  }
  
  /**
   * Getter.
   * @return String The activity's name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Setter.
   * @param String The activity's name
   * @return Nothing
   */
  public void setName(String name) {
    this.name = name;
  }
  
  /**
   * Getter.
   * @return String The activity's description
   */
  public String getDescription() {
    return description;
  }
  
  /**
   * Setter.
   * @param String The activity's description
   * @return Nothing
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * Getter.
   * @return TagEntity The activity's tag
   */
  public TagEntity getTag() {
    return tag;
  }
  
  /**
   * Setter.
   * @param TagEntity The activity's tag
   * @return Nothing
   */
  public void setTag(TagEntity tag) {
    this.tag = tag;
  }
  
  /**
   * Getter.
   * @return byte[] The activity's picture
   */
  public byte[] getPicture() {
    return picture;
  }
  
  /**
   * Setter.
   * @param byte[] The activity's picture
   * @return Nothing
   */
  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  /**
   * Getter.
   * @return User The user who offered the activity
   */
  public User getUser() {
    return user;
  }
  
  /**
   * Setter.
   * @param User The user who offered the activity
   * @return Nothing
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Getter.
   * @return Date The activity's start date
   */
  public Date getStartDate() {
    return startDate;
  }

  /**
   * Setter.
   * @param Date The activity's start date
   * @return Nothing
   */
  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  /**
   * Getter.
   * @return Date The activity's end date
   */
  public Date getEndDate() {
    return endDate;
  }

  /**
   * Setter.
   * @param Date The activity's end date
   * @return Nothing
   */
  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

}

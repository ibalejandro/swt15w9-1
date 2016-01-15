package app.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
* <h1>TagEntity</h1>
* The TagEntity is the persistent object of a tag. It contains an ID and the
* respective name and it is used to save tags in the database.
*
* @author Alejandro Sánchez Aristizábal
* @since  29.11.2015
*/
@Entity
public class TagEntity {
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  
  private String name;
  
  @OneToOne(targetEntity = GoodEntity.class, fetch = FetchType.EAGER) 
  private GoodEntity good;
  
  public static final String OTHERS = "Others";
  
  protected TagEntity() {} // For the sake of JPA.
  
  /**
   * Constructor.
   * @param name  The tag's name
   */
  public TagEntity(String name) {
    this.name = name;
  }
  
  /**
   * This method builds a String in which the tag's information is presented
   * as a JSON object.
   * @return String The information of a tag in JSON format
   */
  @Override
  public String toString() {
    return String.format("{\"id\": \"%d\", \"name\": \"%s\"}", id, name);
  }
  
  /**
   * Getter.
   * @return long The tag's id
   */
  public long getId() {
    return id;
  }
  
  /**
   * Setter.
   * @param id The tag's id
   * @return Nothing
   */
  public void setId(long id) {
    this.id = id;
  }
  
  /**
   * Getter.
   * @return String The tag's name
   */
  public String getName() {
    return name;
  }
  
  /**
   * Setter.
   * @param name The tags's name
   * @return Nothing
   */
  public void setName(String name) {
    this.name = name;
  }
   
}

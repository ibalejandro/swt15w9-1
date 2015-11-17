package prototyp.model;

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
import org.salespointframework.useraccount.UserAccount;

@SuppressWarnings("serial")
@Entity
@Table(name = "GOODS")
public class GoodEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private String description;
	/*
	 * @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL,
	 * 			  fetch = FetchType.EAGER) private User user;
	 */
	
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
	private long userId;
	
	@SuppressWarnings("unused")
	private GoodEntity() {} // For the sake of JPA.

    public GoodEntity(String name, String description, Set<String> tags,
    				  long userId /*User user*/) {
        this.name = name;
        this.description = description;
    	this.tags = tags;
    	this.userId = userId;
    	//this.user = user;
    }
    
    /*
     * This method returns all the tags in a String, in which they are
     * separated by commas.
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
    
    //This method shows a good in JSON format.
    @Override
    public String toString() {
        return String.format("{\"id\": \"%d\", \"name\": \"%s\", "
                             + "\"description\": \"%s\", \"tags\": \"%s\", "
                             + "\"userId\": \"%d\"}", id, name, description, 
                             tags.toString(), userId);
    }

    public long getId() {
      return id;
    }
    
    public void setId(long id) {
      this.id = id;
    }

    public String getName() {
      return name;
    }
    
    public void setName(String name) {
      this.name = name;
    }

    public String getDescription() {
      return description;
    }
    
    public void setDescription(String description) {
      this.description = description;
    }

    public Set<String> getTags() {
      return tags;
    }
    
    public void setTags(Set<String> tags) {
      this.tags = tags;
    }

    public long getUserId() {
      return userId;
    }
    
    public void setUserId(long userId) {
      this.userId = userId;
    }

}

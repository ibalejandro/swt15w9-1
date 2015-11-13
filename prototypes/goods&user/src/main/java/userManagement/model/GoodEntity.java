package userManagement.model;

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
	//@ManyToOne(targetEntity=User.class, cascade = CascadeType.ALL,fetch= FetchType.EAGER) private User user;
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

    public GoodEntity(String name, String description, Set<String> tags
    				  /*User user*/) {
        this.name = name;
        this.description = description;
    	this.tags = tags;
    	//this.user = user;
    }
    
    
    @Override
    public String toString() {
        return String.format("Good = [id = %d], [name = %s], [description = %s]"
                		     + ", [tags = %S], [user_id = %d]", id, name,
                		     description, tags.toString(), userId);
    }

}

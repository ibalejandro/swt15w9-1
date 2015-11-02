package userManagement.model;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;


	
@Entity
public class User {
	
	private @Id @GeneratedValue long id;
	
	private Location location;
	private String origin;
	private Set<String> languages;
	private boolean enabled;
	@OneToOne private UserAccount userAccount;
	  
	 
	
	public User(Location location, String origin, Set<String> languages, UserAccount userAccount) {
		super();
		this.location = location;
		this.origin = origin;
		this.languages = languages;
		this.userAccount = userAccount;
		this.enabled= userAccount.isEnabled();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Set<String> getLanguages() {
		return languages;
	}

	public void setLanguages(Set<String> languages) {
		this.languages = languages;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public long getId(){
		return id;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
}

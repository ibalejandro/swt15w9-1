package userm.models;

import org.springframework.security.core.userdetails.User;

import userManagement.model.Location;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
	
@Entity	
public class UserS {// Spring anstatt Salespoint: erweitert um zusätzliche Informationen, nicht weiter verwendet!
	

	private @Id @GeneratedValue long id;
	private String firstname;
	private String lastname;
	private String email;
	private Location location;
	private String origin;
	private  Set<String> languages;
	private boolean enabled;

	 @OneToOne private User userSpring;
		  
		 
		
	public UserS(String firstname, String lastname, String email, Location location, String origin, Set<String> languages, User userSpring) {
		super();
		this.firstname=firstname;
		this.lastname=lastname;
		this.email=email;
		this.location = location;
		this.origin = origin;
		this.languages = languages;
		this.userSpring = userSpring;
		this.enabled= userSpring.isEnabled();
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public User getUser() {
		return userSpring;
	}

	public long getId(){
		return id;
	}
	
	
	public boolean isEnabled(){
		return enabled;
	}
		
	

}

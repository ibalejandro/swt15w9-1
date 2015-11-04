package userManagement.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;


	
@SuppressWarnings("serial")
@Entity
public class User implements Serializable{
	
	private @Id @GeneratedValue long id;
	
	private Address location;
	private String origin;
	private String PrefferedLanguage;
	private boolean enabled;
	@OneToOne private UserAccount userAccount;
	  
	@SuppressWarnings("unused")
	private User() {} 
	
	public User(Address location, String origin, UserAccount userAccount) {
		super();
		this.location = location;
		this.origin = origin;
		this.userAccount = userAccount;
		this.enabled= userAccount.isEnabled();
	}
	
	

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	
	public String getLanguage() {
		return PrefferedLanguage;
	}

	public void setLanguage(String language) {
		this.PrefferedLanguage = language;
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

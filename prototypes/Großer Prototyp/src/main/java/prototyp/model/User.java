package prototyp.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

import prototyp.model.GoodEntity;

/**
* <h1>User</h1>
* The User is the persistent object of a User. It contains the whole
* information of a User and it is used to save User in the UserRepository.
*
* @author Friederike Kitzing
* 
*/
	
@SuppressWarnings("serial")
@Entity
public class User implements Serializable{
	
	private @Id @GeneratedValue long id;
	
	private Address location;
	private String origin;
	private String PrefferedLanguage;
	@OneToMany(cascade = CascadeType.ALL) private Set<Language> languages;
	private boolean enabled;
	@OneToOne private UserAccount userAccount;
	//Einbindung Güter:
	@OneToMany(targetEntity=GoodEntity.class, cascade = CascadeType.ALL,fetch= FetchType.EAGER) private Set<GoodEntity> goods;
	  
	@SuppressWarnings("unused")
	private User() {} 
	
	
	/**
	 * Constructor.
	 * @param userAccount  The Spring Object UserAccount which is connected to the User
	 * @param location The Address Object which is connected to the User
	 */
	public User( UserAccount userAccount, Address location) {
		super();
		this.location = location;
		this.userAccount = userAccount;
		this.enabled= userAccount.isEnabled();
		Set<Language>languages=new HashSet<>();
	}
	
	/**
	   * Adds a GoodEntity to the Set goods.
	   * @param GoodEntity The good to be added
	   * @return Nothing
	   */
	public void addGood(GoodEntity good){
		goods.add(good);
	}
	
	/**
	   * Getter.
	   * @return Iterable<GoodEntity> the Good
	   */
	public Iterable<GoodEntity> getGoods(){
		return goods;
	}

	/**
	   * Getter.	 
	   * @return Address The location of the User
	   */
	public Address getLocation() {
		return location;
	}

	/**
	   * Setter.
	   * @param Address the new location
	   * @return Nothing
	   */
	public void setLocation(Address location) {
		this.location = location;
	}

	/**
	   * Getter.
	   * @return String The Origin of the User
	   */
	public String getOrigin() {
		return origin;
	}

	/**
	   * Setter.
	   * @param String The User's Origin
	   * @return Nothing
	   */
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	/**
	   * Getter.
	   * @return Iterable<Language> All languages of this User
	   */
	public Iterable<Language> getLanguages() {
		return languages;
	}

	/**
	   * Setter.
	   * @param Language The new language 
	   * @return Nothing
	   */
	public void setLanguage(Language newLanguage) {
		languages.add(newLanguage);
	}
	
	//Hilfsfunktionen: umwandeln in Language-Objekt!!!
	public String getLanguage() {
		return PrefferedLanguage;
	}

	public void setLanguage(String language) {
		this.PrefferedLanguage = language;
	}
	//
	/**
	   * Getter.
	   * @return UserAccount The connected UserAccount of this User
	   */
	public UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	   * Getter.
	   * @return id The User's id
	   */
	public long getId(){
		return id;
	}
	
	/**
	   * Getter.
	   * @return Boolean
	   */
	public boolean isEnabled(){
		return enabled;
	}
	
}

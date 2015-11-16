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
	
	public User( UserAccount userAccount, Address location) {
		super();
		this.location = location;
		this.userAccount = userAccount;
		this.enabled= userAccount.isEnabled();
		Set<Language>languages=new HashSet<>();
	}
	
	public void addGood(GoodEntity good){
		goods.add(good);
	}
	
	public Iterable<GoodEntity> getGoods(){
		return goods;
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
	
	public Iterable<Language> getLanguages() {
		return languages;
	}

	public void setLanguage(Language newLanguage) {
		languages.add(newLanguage);
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

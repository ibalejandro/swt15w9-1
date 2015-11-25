package app.model;

import java.io.Serializable;
import java.util.Date;
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


	
@SuppressWarnings("serial")
@Entity
public class User implements Serializable{
	
	private @Id @GeneratedValue long id;
	
	private Address location;
	private String origin;
	private String PrefferedLanguage;
	@OneToMany(cascade = CascadeType.ALL) private Set<Language> languages;
	private boolean enabled;
	private boolean activated; // Aktivierungsstatus (d1456)
	private int registrationstate; // Registrierungsstatus
	private String activationkey; // String mit dem das Konto aktiviert wird (d1456)
	private Date registrationdate; //Registrierungsdatum (d1456)
	private String adresstyp; 
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
		this.activated = false;
		this.registrationstate = -1; 
		this.activationkey = "";
		this.adresstyp = "";
		//-1 ~ noch nichts eingegeben; 0-8 ~ für Registriegungsfortschritt; 9 ~ Konto von Admin o.ä. deaktiviert; 10 ~ Kontodaten vollständig und aktiviert; 
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
	
	//***************************** (d1456)
	
	public boolean isActivated(){
		return activated;
	}
	
	public void Activate(){
		this.activated = true;
	}
	
	public void DeActivate(){
		this.activated = false;
	}
	
	public int getRegistrationstate(){
		return registrationstate;
	}
	
	public void setRegistrationstate(int registrationstate){
		this.registrationstate=registrationstate;
	}
	
	public String getActivationkey(){
		return activationkey;
	}
	
	public void setActivationkey(String activationkey){
		this.activationkey=activationkey;
	}
	
	public Date getRegistrationdate(){
		return registrationdate;
	}
	
	public void setRegistrationdate(Date registrationdate){
		this.registrationdate=registrationdate;
	}
	
	public String getAdresstyp() {
		return adresstyp;
	}

	public void setAdresstyp(String adresstyp) {
		this.adresstyp = adresstyp;
	}
	
}

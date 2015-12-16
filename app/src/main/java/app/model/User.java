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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;
import org.springframework.util.Assert;

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
	
	private boolean enabled;
	private boolean activated; // Aktivierungsstatus (d1456)
	private int registrationstate; // Registrierungsstatus
	private String activationkey; // String mit dem das Konto aktiviert wird (d1456)
	private Date registrationdate; //Registrierungsdatum (d1456)
	private String adresstyp; 
	@OneToOne private UserAccount userAccount;
	
	//Einseitig:
	@ManyToOne (targetEntity=Language.class, cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH }, fetch=FetchType.EAGER) 
	@JoinColumn(name= "PREF_LANGUAGE")
		private Language PrefLanguage;
		
	@ManyToMany(targetEntity=Language.class,cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH }, fetch=FetchType.EAGER)
	private Set<Language> languages;
	
	//Bidirektional:
	@OneToMany(targetEntity=GoodEntity.class, cascade = CascadeType.ALL,fetch= FetchType.EAGER) private Set<GoodEntity> goods;
	@OneToMany(targetEntity=Dialog.class, cascade = CascadeType.ALL, fetch= FetchType.EAGER) private Set<Dialog> dialogs;
	  
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
		this.activated = false;
		this.registrationstate = -1; 
		this.activationkey = "";
		this.adresstyp = "";
		//-1 ~ noch nichts eingegeben; 0-8 ~ für Registriegungsfortschritt; 9 ~ Konto von Admin o.ä. deaktiviert; 10 ~ Kontodaten vollständig und aktiviert; 
		languages=new HashSet<>();
	}
	
	/**
	   * Adds a GoodEntity to the Set goods or updates a GoodEntity if it was 
	   * already saved.
	   * @param GoodEntity The good to be added/updated
	   * @return Nothing
	   */
	public void addGood(GoodEntity good){
	  // It is used to eliminate a good if it already exists.
	  removeGood(good);
	  
	  goods.add(good);
	}
	
	/**
   * Removes a GoodEntity from the Set goods.
   * @param GoodEntity The good to be removed
   * @return Nothing
   */
  public void removeGood(GoodEntity good){
    for (GoodEntity g : goods) {
      // It means that the user wants to update one of its offered goods.
      if (g.getId() == good.getId()) {
        goods.remove(g);
        break;
      }
    }
  }
	
	/**
	   * Getter.
	   * @return Iterable<GoodEntity> The goods offered by this User
	   */
	public Iterable<GoodEntity> getGoods(){
		return goods;
	}
	
	public void addDialog(Dialog dialog) {
		Assert.notNull(dialog);
		
		dialogs.add(dialog);
	}
	
	public Iterable<Dialog> getDialogs() {
		return dialogs;
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
	   * @param Language The new language that should be added.
	   * @return Nothing
	   */
	public void setLanguage(Language newLanguage) {
		languages.add(newLanguage);
	}
	
	
	/**
	   * Remove Language.
	   * @param Language The new language that should be added.
	   * @return Nothing
	   */
	public Language removeLanguage(Language language){
		if(language.equals(PrefLanguage))return null;
		if(languages.remove(language))return language;
		return null;
	}
	
	public void removeAllLanguages(){
		Set<Language> PL=new HashSet<>();
		PL.add(PrefLanguage);
		languages.retainAll(PL);
	}
	

	public Language getPrefLanguage() {
		return PrefLanguage;
	}
	
	
	public void setPrefLanguage(Language language) {
		//PrefLanguage in Set Languages enthalten
		if(!languages.contains(language)){
			languages.add(language);
			System.out.println("already here");
		}
		this.PrefLanguage=language;
	}
	
	public String showLanguages(){
		String languageNames="";
		for(Language l:languages){
			languageNames=languageNames+l.toString()+", ";
		}
		return languageNames.substring(0, languageNames.length()-2);
	}

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

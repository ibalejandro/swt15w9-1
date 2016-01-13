package app.model;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
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

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.GeocoderResult;

/**
 * <h1>User</h1> The User is the persistent object of a User. It contains the
 * whole information of a User and it is used to save User in the
 * UserRepository.
 *
 * @author Friederike Kitzing
 *
 */

@SuppressWarnings("serial")
@Entity
public class User implements Serializable {

	public enum AddresstypEnum {
		Wohnung, Refugees_home, empty
	}

	private @Id @GeneratedValue long id;

	private Address location;
	private Coordinates coordinates;
	private String origin;

	private boolean enabled;
	private boolean activated; // Aktivierungsstatus (d1456)
	private int registrationstate; // Registrierungsstatus
	private String activationkey; // String mit dem das Konto aktiviert wird
									// (d1456)
	private Date registrationdate; // Registrierungsdatum (d1456)
	private AddresstypEnum adresstyp;
	@OneToOne
	private UserAccount userAccount;

	// Einseitig:
	@ManyToOne(targetEntity = Language.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, fetch = FetchType.EAGER)
	@JoinColumn(name = "PREF_LANGUAGE")
	private Language PrefLanguage;

	@ManyToMany(targetEntity = Language.class, cascade = { CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH }, fetch = FetchType.EAGER)
	private Set<Language> languages;

	// Bidirektional:
	@OneToMany(targetEntity = GoodEntity.class, mappedBy="user", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
		CascadeType.REFRESH }, fetch = FetchType.EAGER)
	private Set<GoodEntity> goods;
	@OneToMany(targetEntity = ActivityEntity.class, mappedBy="user", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
		CascadeType.REFRESH }, fetch = FetchType.EAGER)
	private Set<ActivityEntity> activities;

	@SuppressWarnings("unused")
	private User() {
	}

	/**
	 * Constructor.
	 *
	 * @param userAccount
	 *            The Spring Object UserAccount which is connected to the User
	 * @param location
	 *            The Address Object which is connected to the User
	 */
	public User(UserAccount userAccount, Address location) {
		super();
		this.location = location;
		this.userAccount = userAccount;
		this.enabled = userAccount.isEnabled();
		this.activated = false;
		this.registrationstate = -1;
		// -1 ~ noch nichts eingegeben; 0-8 ~ für Registriegungsfortschritt; 9 ~
		// Konto von Admin o.ä. deaktiviert; 10 ~ Kontodaten vollständig und
		// aktiviert;
		this.activationkey = "";
		this.adresstyp = AddresstypEnum.empty;
		goods=new HashSet<>();
		languages = new HashSet<>();
	}

	@Override
	public boolean equals(Object other){
		if (other == null) return false;
		if (other == this) return true;
		if (!(other instanceof User))return false;
		User otherUser = (User)other;
		if(id==otherUser.getId()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Adds a GoodEntity to the Set goods or updates a GoodEntity if it was
	 * already saved.
	 *
	 * @param GoodEntity
	 *            The good to be added/updated
	 * @return Nothing
	 */
	public void addGood(GoodEntity good) {
		// It is used to eliminate a good if it already exists.
		removeGood(good);

		goods.add(good);
	}

	/**
	 * Removes a GoodEntity from the Set goods.
	 *
	 * @param GoodEntity
	 *            The good to be removed
	 * @return Nothing
	 */
	public void removeGood(GoodEntity good) {
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
	 *
	 * @return Iterable<GoodEntity> The goods offered by this User
	 */
	public Iterable<GoodEntity> getGoods() {
		return goods;
	}

	/**
	 * Adds an ActivityEntity to the Set activities or updates an ActivityEntity
	 * if it was already saved.
	 *
	 * @param ActivityEntity
	 *            The activity to be added/updated
	 * @return Nothing
	 */
	public void addActivity(ActivityEntity activity) {
		// It is used to eliminate an activity if it already exists.
		removeActivity(activity);

		activities.add(activity);
	}

	/**
	 * Removes an ActivityEntity from the Set activities.
	 *
	 * @param ActivityEntity
	 *            The activity to be removed
	 * @return Nothing
	 */
	public void removeActivity(ActivityEntity activity) {
		for (ActivityEntity a : activities) {
			// It means that the user wants to update one of its offered
			// activities.
			if (a.getId() == activity.getId()) {
				activities.remove(a);
				break;
			}
		}
	}

	/**
	 * Getter.
	 *
	 * @return Iterable<ActivityEntity> The activities offered by this User
	 */
	public Iterable<ActivityEntity> getActivities() {
		return activities;
	}

	/**
	 * Getter.
	 *
	 * @return Address The location of the User
	 */
	public Address getLocation() {
		return location;
	}
	
	public Boolean isOldLocation(Address location){
		System.out.println("isOldLocation check");
		System.out.println(location+"<>"+this.location);
		if(adresstyp.toString().equals("Wohnung")){
			if((!this.location.getStreet().equals(location.getStreet())) ||
					(!this.location.getHousenr().equals(location.getHousenr()))){
				return false;
			}
		}else{
			if((!this.location.getFlh_name().equals(location.getFlh_name())) ||
					(!this.location.getCityPart().equals(location.getCityPart()))){
				return false;
			}
		}
		if((!this.location.getCity().equals(location.getCity()))||
					(!this.location.getZipCode().equals(location.getZipCode()))){
				return false;
			}
		System.out.println("isOldLocation check: True");
		return true;
	}
	
	//Suchen Koordinaten
	public Coordinates createCoordinates(){
		System.out.println("create Coordinates!!");
		if((location.getCity().equals(""))){
			return new Coordinates(0.00,0.00);
		}
		
		//String url = "http://maps.googleapis.com/maps/api/geocode/json?address="+ URLEncoder.encode(location.toString(), "UTF-8")+"+" +"&sensor=false";
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(location.toString()).setLanguage("en").getGeocoderRequest();
		GeocodeResponse geocoderResponse;
		try {
			geocoderResponse = geocoder.geocode(geocoderRequest);
			if(!geocoderResponse.getStatus().toString().equals("OK")){
				System.out.println("No Results in Geocoder!");
				System.out.println(geocoderResponse.getStatus());
				 return new Coordinates(0.00,0.00);
			}
			GeocoderResult geoCode= geocoderResponse.getResults().get(0);
			System.out.println(geoCode);
		    float latitude = geoCode.getGeometry().getLocation().getLat().floatValue();
		    float longitude = geoCode.getGeometry().getLocation().getLng().floatValue();
		    System.out.println(latitude+", "+longitude);
	         
			Coordinates newCoordinates= new Coordinates(latitude,longitude);
			return newCoordinates;	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("No Response from Geocoder!");
		return new Coordinates(0.00,0.00);
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
	   * Setter.
	   * @param Coordinates The coordinates of the address
	   * @return Nothing
	   */
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	
	/**
	   * Getter.
	   * @return  Coordinates The coordinates of the address
	   */
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	/**
	   * Getter.
	   * @return Float The Longitude (Geographische LÃ¤nge)
	   */
	public double getLongitude() {		
		return this.coordinates.getLongitude();
	}

	/**
	   * Getter.
	   * @return Float The Latitude (Geographische Breite)
	   */
	public double getLatitude() {
		return this.coordinates.getLatitude();
	}
	


	/**
	 * Getter.
	 *
	 * @return String The Origin of the User
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Setter.
	 *
	 * @param String
	 *            The User's Origin
	 * @return Nothing
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * Getter.
	 *
	 * @return Iterable<Language> All languages of this User
	 */
	public Iterable<Language> getLanguages() {
		return languages;
	}

	/**
	 * Setter.
	 *
	 * @param Language
	 *            The new language that should be added.
	 * @return Nothing
	 */
	public void setLanguage(Language newLanguage) {
		languages.add(newLanguage);
	}

	/**
	 * Remove Language.
	 *
	 * @param Language
	 *            The language that should be removed.
	 * @return Nothing
	 */
	public Language removeLanguage(Language language) {
		if (languages.remove(language))
			return language;
		return null;
	}

	public void removeAllLanguages() {
		Set<Language> PL = new HashSet<>();
		PL.add(PrefLanguage);
		languages.retainAll(PL);
	}

	public Language getPrefLanguage() {
		return PrefLanguage;
	}

	public void setPrefLanguage(Language language) {
		// PrefLanguage in Set Languages enthalten
		if (!languages.contains(language)) {
			languages.add(language);
			System.out.println("already here");
		}
		this.PrefLanguage = language;
	}

	public String showLanguages() {
		String languageNames = "";
		for (Language l : languages) {
			languageNames = languageNames + l.toString() + ", ";
		}
		return languageNames.substring(0, languageNames.length() - 2);
	}

	/**
	 * Getter.
	 *
	 * @return UserAccount The connected UserAccount of this User
	 */
	public UserAccount getUserAccount() {
		return userAccount;
	}

	/**
	 * Getter.
	 *
	 * @return id The User's id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Getter.
	 *
	 * @return Boolean
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Getter.
	 *
	 * @return String The opposite of the activationstate
	 */
	public String getActivationStateOpposite() {
		if (userAccount.isEnabled())
			return "deactivate";
		else
			return "activate";
	}

	/**
	 * Getter.
	 *
	 * @return String The activationstate as a String
	 */
	public String getActivationState() {
		if (userAccount.isEnabled())
			return "activated";
		else
			return "deactivated";
	}

	// ***************************** (d1456)

	public boolean isActivated() {
		return activated;
	}

	public void Activate() {
		this.activated = true;
	}

	public void DeActivate() {
		this.activated = false;
	}

	public int getRegistrationstate() {
		return registrationstate;
	}

	public void setRegistrationstate(int registrationstate) {
		this.registrationstate = registrationstate;
	}

	public String getActivationkey() {
		return activationkey;
	}

	public void setActivationkey(String activationkey) {
		this.activationkey = activationkey;
	}

	public Date getRegistrationdate() {
		return registrationdate;
	}

	public void setRegistrationdate(Date registrationdate) {
		this.registrationdate = registrationdate;
	}

	public AddresstypEnum getAddresstyp() {
		return adresstyp;
	}

	public String getAddresstypString() {
		return adresstyp.toString();
	}

	public void setAddresstyp(AddresstypEnum adresstyp) {
		this.adresstyp = adresstyp;
	}
	public String toString(){
		return userAccount.getUsername();
	}

}
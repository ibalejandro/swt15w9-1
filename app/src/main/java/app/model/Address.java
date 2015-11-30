package app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
* <h1>Address</h1>
* The Address is the Entity that contains all information connected to the location of the User. 
* It is saved as an attribute of the respective User in the UserRepository.
* 
* @author Friederike Kitzing
*/
@SuppressWarnings("serial")
@Entity
public class Address implements  Serializable { //<> Flüchtlingsunterkunft
	
	private @Id @GeneratedValue long id;
	
	private String street;
	private String housenr;
	private String zipCode;
	private String city;
	
	@SuppressWarnings("unused")
	private Address() {}
	
	/**
	 * Constructor.
	 * @param String The street
	 * @param String The house number
	 * @param String The zip code
	 * @param String The city
	 * 	 */
	public Address(String street,String housenr, String zipCode, String city){
		this.street= street;
		this.housenr=housenr;
		this.zipCode=zipCode;
		this.city=city;
	}
 
	 /**
	   * Getter.
	   * @return String The street and the house number separated by a blank space
	   */
	public String getStreetAndNo() {
		return (street+" "+ housenr);
	}

	 /**
	   * Setter.
	   * @param String The street name
	   * @return Nothing
	   */
	public void setStreet(String street) {
		this.street = street;
	}
	
	 /**
	   * Setter.
	   * @param String The house number
	   * @return Nothing
	   */
	public void setHousenr(String housenr) {
		this.housenr = housenr;
	}

	 /**
	   * Getter.
	   * @return String The zip code
	   */
	public String getZipCode() {
		return zipCode;
	}

	 /**
	   * Setter.
	   * @param String The new zip code
	   * @return Nothing
	   */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	 /**
	   * Getter.
	   * @return String The name of the city
	   */
	public String getCity() {
		return city;
	}

	 /**
	   * Setter.
	   * @param String The new city name
	   * @return Nothing
	   */
	public void setCity(String city) {
		this.city = city;
	}

	 /**
	   * Getter, whole Address, can be used to find the location
	   * @return String Street name, house number and zip code
	   */
	//@Override
	public String getLocation() {
		return (street+" "+housenr +", "+zipCode);
	}
	
}

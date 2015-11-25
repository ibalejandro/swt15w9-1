package app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
	
	public Address(String street,String housenr, String zipCode, String city){
		this.street= street;
		this.housenr=housenr;
		this.zipCode=zipCode;
		this.city=city;
	}
 
	
	public String getStreetAndNo() {
		return (street+" "+ housenr);
	}


	public void setStreet(String street) {
		this.street = street;
	}
	
	public void setHousenr(String housenr) {
		this.housenr = housenr;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	//@Override
	public String getLocation() {
		return (street+" "+housenr +", "+zipCode);
	}
	
		

	

}

package userManagement.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Address implements Location { //<> Flüchtlingsunterkunft
	
	private @Id @GeneratedValue long id;
	
	private String streetAndNo;
	private String zipCode;
	private String city;
	
	public Address(String streetAndNo, String zipCode, String city){
		this.streetAndNo= streetAndNo;
		this.zipCode=zipCode;
		this.city=city;
	}
 
	
	public String getStreetAndNo() {
		return streetAndNo;
	}


	public void setStreetAndNo(String streetAndNo) {
		this.streetAndNo = streetAndNo;
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


	@Override
	public String getLocation() {
		return (streetAndNo+", "+zipCode);
	}
	
		

	

}

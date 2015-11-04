package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Good {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	
	private String name;
	private String description;
	private String tag;
	private String userFirstName;
	private String userLastName;
	private String userEmail;
	private String userOrigin;
	private String addressStreet;
	private int addressNumber;
	private int addressPostcode;
	private String addressCity;
	
	
	protected Good() {} //For the sake of JPA.

    public Good(String name, String description, String tag, User user) {
        this.name = name;
        this.description = description;
        this.tag = tag;
        this.userFirstName = user.getFirstName();
    	this.userLastName = user.getLastName();
    	this.userEmail = user.getEmail();
    	this.userOrigin = user.getOrigin();
    	this.addressStreet = user.getAdress().getStreet();
    	this.addressNumber = user.getAdress().getNumber();
    	this.addressPostcode = user.getAdress().getPostcode();
    	this.addressCity = user.getAdress().getCity();
    }

    @Override
    public String toString() {
        return String.format("Good = [id = %d], [name = %s], [description = %s]"
                		     + ", [tag = %s], [user_first_name = %s], "
                		     + "[user_last_name = %s], [user_email = %s], "
                		     + "[user_origin = %s], [address_street = %s], "
                		     + "[address_number = %d], [address_postcode = %d],"
                		     + " [address_city = %s]", id, name, description,
                		     tag, userFirstName, userLastName, userEmail,
                		     userOrigin, addressStreet, addressNumber,
                		     addressPostcode, addressCity);
    }
	
}

package com.example.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AddressEntity {

	@Id
	private long userId;
	
	private String street;
	private int number;
	private int postcode;
	private String city;
	
	protected AddressEntity() {} // For the sake of JPA.

    public AddressEntity(String street, int number, int postcode, String city) {
        this.street = street;
        this.number = number;
    	this.postcode = postcode;
    	this.city = city;
    }
    
}

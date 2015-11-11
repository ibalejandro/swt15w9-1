package com.example.model;

public class User {

	private String firstName;
	private String lastName;
	private String email;
	private String origin;
	private Address address;
	
	public User(String firstName, String lastName, String email, String origin,
				Address address) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.origin = origin;
		this.address = address;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getOrigin() {
		return origin;
	}
	
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	
	public Address getAdress() {
		return address;
	}
	
	public void setAdress(Address adress) {
		this.address = adress;
	}
	
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
}

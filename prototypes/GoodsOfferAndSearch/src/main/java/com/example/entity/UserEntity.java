package com.example.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String firstName;
	private String lastName;
	private String email;
	private String origin;
	@ElementCollection  
    protected Set<String> languages = new HashSet<String>();
	
	protected UserEntity() {} // For the sake of JPA.

    public UserEntity(String firstName, String lastName, String email,
    				  String origin, Set<String> languages) {
        this.firstName = firstName;
        this.lastName = lastName;
    	this.email = email;
    	this.origin = origin;
    	this.languages = languages;
    }
    
}

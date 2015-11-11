package com.example.entity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GoodEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	private String description;
	/*
	 * The JPA created a technology named Lazy Loading to the classes 
	 * attributes. We could define Lazy Loading by: “the desired information 
	 * will be loaded (from database) only when it is needed”.
	 * The container will notice if the collection is a lazy attribute and 
	 *  it will “ask” the JPA to load this collection from the database.
	 *  To avoid Lazy Loading you have to use Eager Loading
	 */
	@ElementCollection(fetch = FetchType.EAGER)
    protected Set<String> tags = new HashSet<>();
	private long userId;
	
	protected GoodEntity() {} // For the sake of JPA.

    public GoodEntity(String name, String description, Set<String> tags,
    				  long userId) {
        this.name = name;
        this.description = description;
    	this.tags = tags;
    	this.userId = userId;
    }
    
    
    @Override
    public String toString() {
        return String.format("{\"id\": \"%d\", \"name\": \"%s\", "
        					 + "\"description\": \"%s\", \"tags\": \"%s\", "
        					 + "\"userId\": \"%d\"}", id, name, description, 
        					 tags.toString(), userId);
    }

}

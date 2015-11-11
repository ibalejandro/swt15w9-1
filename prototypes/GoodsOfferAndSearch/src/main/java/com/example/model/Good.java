package com.example.model;

import java.util.HashSet;
import java.util.Set;
import com.example.entity.GoodEntity;

public class Good {

	private String name;
	private String description;
	protected Set<String> tags = new HashSet<>();
	
	public Good(String name, String description, Set<String> tags) {
		this.name = name;
        this.description = description;
    	this.tags = tags;
	}
	
	public GoodEntity createGoodEntity(long userId) {
		return new GoodEntity(name, description, tags, userId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<String> getTags() {
		return tags;
	}

	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	
}

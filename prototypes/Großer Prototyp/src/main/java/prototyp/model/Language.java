package prototyp.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;



@SuppressWarnings("serial")
@Entity
@Table(name = "LANGUAGES")
public class Language implements Serializable{
	
	private @Id @GeneratedValue long id;
	private String name;
	public Language(long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return name;
	}
	
	

}

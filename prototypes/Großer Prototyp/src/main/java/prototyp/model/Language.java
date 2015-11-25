package prototyp.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
* <h1>Language</h1>
* The Language is the Entity, which represents a certain Language.
* 
* @author Friederike Kitzing
*/
//Möglicherweise Repository notwendig?

@SuppressWarnings("serial")
@Entity
@Table(name = "LANGUAGES")
public class Language implements Serializable{
	
	private @Id @GeneratedValue long id;
	private String name;
	
	/**
	 * Constructor.
	 * @param String The name of the Language
	 */
	public Language(String name) {
		super();
		this.name = name;
	}
	
	/**
	   * Getter.
	   * @return The Language's id 
	   */
	public long getId() {
		return id;
	}
	
	/**
	   * Getter.
	   * @return String The Language's name
	   */
	public String getName() {
		return name;
	}
		
	/**
	   * toString 
	   * @return String The name of this Language
	   */
	@Override
	public String toString() {
		return name;
	}
	
	

}

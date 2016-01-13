package app.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Module {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String thymeLeafName;

	private String templateName;

	/**
	 * Private Constructor
	 */
	@SuppressWarnings("unused")
	private Module() {
	}

	/**
	 * Constructor of Module. Creates a new Module with a specific template- and
	 * thymeLeafName
	 * 
	 * @param templateName
	 * @param thymeLeafName
	 */
	public Module(String templateName, String thymeLeafName) {
		this.templateName = templateName;
		this.thymeLeafName = thymeLeafName;
	}

	/**
	 * getter for the id
	 * 
	 * @return
	 */
	public long getId() {
		return id;
	}

	/**
	 * getter for the ThymeLeafName
	 * 
	 * @return
	 */
	public String getThymeLeafName() {
		return thymeLeafName;
	}

	/**
	 * getter for the TemplateName
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return templateName;
	}
	
	
	public void setThymeLeafName(String thymeLeafName){
		this.thymeLeafName=thymeLeafName;
	}
	
	public void setTemplateName(String templateName){
		this.templateName=templateName;
	}
}

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

	private String tyhmeLeafName;

	private String templateName;

	/**
	 * Private Constructor
	 */
	@SuppressWarnings("unused")
	private Module() {
	}

	/**
	 * Constructor of Module. Creates a new Module with a specific template- and
	 * tyhmeLeafName
	 * 
	 * @param templateName
	 * @param tyhmeLeafName
	 */
	public Module(String templateName, String tyhmeLeafName) {
		this.templateName = templateName;
		this.tyhmeLeafName = tyhmeLeafName;
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
	 * getter for the TyhmeLeafName
	 * 
	 * @return
	 */
	public String getTyhmeLeafName() {
		return tyhmeLeafName;
	}

	/**
	 * getter for the TemplateName
	 * 
	 * @return
	 */
	public String getTemplateName() {
		return templateName;
	}
}

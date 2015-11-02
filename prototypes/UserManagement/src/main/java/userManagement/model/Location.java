package userManagement.model;

import org.springframework.data.repository.CrudRepository;


public interface Location //extends CrudRepository<Address, Long>  //address, (Flüchtlingsunterkunft)
{
	public String getLocation();// zur Abfrage für Addresssuche
	
}

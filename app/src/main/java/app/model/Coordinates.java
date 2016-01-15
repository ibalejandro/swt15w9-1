package app.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * <h1>Coordinates</h1>
 * 
 * 
 * @author Kilian Heret
 */

@Entity
@SuppressWarnings("serial")
public class Coordinates implements Serializable {

	private @Id @GeneratedValue long id;

	private double longitude; // Breite
	private double latitude;

	@SuppressWarnings("unused")
	private Coordinates() {
	}

	/**
	 * Constructor.
	 * 
	 * @param Float
	 *            The latitude; *Breite*
	 * @param Float
	 *            The longitude; *Länge*
	 * @return
	 */
	public Coordinates(double latitude /* Breite */, double longitude /* Länge */) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Getter.
	 * 
	 * @return Float The Longitude (Geographische Länge)
	 */
	public double getLongitude() {
		return (longitude);
	}

	/**
	 * Getter.
	 * 
	 * @return Float The Latitude (Geographische Breite)
	 */
	public double getLatitude() {
		return (latitude);
	}

	public Boolean isInvalid() {
		if (longitude == 0.00 && latitude == 0.00) {
			return true;
		}
		return false;
	}
}

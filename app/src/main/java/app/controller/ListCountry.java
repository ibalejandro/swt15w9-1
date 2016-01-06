package app.controller;

import java.util.LinkedList;
import java.util.Locale;

/**
 * <h1>ListCountry</h1> Gives a List of the Countrys.
 * 
 *
 * @author Kilian Heret
 * 
 */

public class ListCountry {
	public void printListOfCountries() {

		ListCountry obj = new ListCountry();

		// get a list of countries and display in ENGLISH
		obj.getListOfCountries(Locale.ENGLISH);

		// display in FRENCH
		// obj.getListOfCountries(Locale.FRENCH);

	}

	private void getListOfCountries(Locale locale) {

		String[] locales = Locale.getISOCountries();

		for (String countryCode : locales) {

			Locale obj = new Locale("", countryCode);

			// System.out.println("Country Code = " + obj.getCountry()
			// + ", Country Name = " + obj.getDisplayCountry(locale));

			System.out.println(obj.getDisplayCountry(locale));
		}

	}

	public LinkedList<String> getCountryList(Locale locale) {

		LinkedList<String> CountryList = new LinkedList<String>();

		String[] locales = Locale.getISOCountries();

		for (String countryCode : locales) {

			Locale obj = new Locale("", countryCode);

			// System.out.println("Country Code = " + obj.getCountry()
			// + ", Country Name = " + obj.getDisplayCountry(locale));

			String Countryname = obj.getDisplayCountry(locale) + ", " + obj.getDisplayCountry(Locale.GERMAN) + " ("
					+ obj.getCountry() + ")";

			CountryList.add(Countryname);
		}
		return CountryList;
	}

	public void printCountryList(LinkedList<String> list) {

		for (String S : list) {
			System.out.println(S);

		}

	}

}

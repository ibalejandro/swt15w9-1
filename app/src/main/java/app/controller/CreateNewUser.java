package app.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Address;
import app.model.Language;
import app.model.User;
import app.model.User.AddresstypEnum;
import app.model.UserRepository;
import app.repository.LanguageRepository;

/**
 * <h1>CreateNewUserController</h1> The CreateNewUserController is used for
 * registration and create UserAccounts.
 * 
 *
 * @author Kilian Heret
 * 
 */

@Controller
public class CreateNewUser {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
	private final LanguageRepository languageRepository;

	/**
	 * Autowire.
	 * 
	 * @param CreateNewUser
	 */
	@Autowired
	public CreateNewUser(UserAccountManager userAccountManager, UserRepository userRepository,
			LanguageRepository languageRepository) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		this.languageRepository = languageRepository;
	}

	int zufallzahl_1, zufallzahl_2; // Zufallszahlen
	boolean za;

	@RequestMapping(value = "/new_user")
	public String new_user0(Model modelMap) {
		modelMap.addAttribute("languages", languageRepository.findAll());

		ListCountry a = new ListCountry();
		LinkedList<String> L = a.getCountryList(Locale.ENGLISH);

		modelMap.addAttribute("countrys", L);

		// alte Daten einfügen:

		modelMap.addAttribute("firstname", "Lisa");
		// modelMap.add

		return "new_user";

	}

	@RequestMapping(value = "/new_user", method = RequestMethod.GET)
	public String new_user(@RequestParam("firstnameOld") final Optional<String> firstnameOld,
			@RequestParam("nameOld") final Optional<String> nameOld,
			@RequestParam("mailOld") final Optional<String> mailOld,
			@RequestParam("usernameOld") final Optional<String> usernameOld,

			@RequestParam("checked1Old") final Optional<String> checked1Old,
			@RequestParam("checked2Old") final Optional<String> checked2Old,

			@RequestParam("streetOld") final Optional<String> streetOld,
			@RequestParam("housenrOld") final Optional<String> housenrOld,
			@RequestParam("postcodeHOld") final Optional<String> postcodeHOld,
			@RequestParam("cityHOld") final Optional<String> cityHOld,

			@RequestParam("fhl_nameOld") final Optional<String> fhl_nameOld,
			@RequestParam("citypartOld") final Optional<String> citypartOld,
			@RequestParam("postcodeROld") final Optional<String> postcodeROld,
			@RequestParam("cityROld") final Optional<String> cityROld,

			@RequestParam("nativelanguageOld") final Optional<String> nativelanguageOld,
			@RequestParam("language2Old") final Optional<String> language2Old,
			@RequestParam("language3Old") final Optional<String> language3Old,
			@RequestParam("language4Old") final Optional<String> language4Old,
			@RequestParam("language5Old") final Optional<String> language5Old,
			@RequestParam("originOld") final Optional<String> originOld, Model modelMap) {
		modelMap.addAttribute("languages", languageRepository.findAll());

		ListCountry a = new ListCountry();
		LinkedList<String> L = a.getCountryList(Locale.ENGLISH);

		modelMap.addAttribute("countrys", L);

		// alte Daten einfügen:

		modelMap.addAttribute("firstnameOld", HelpFunctions.getOptionalString(firstnameOld));
		modelMap.addAttribute("nameOld", HelpFunctions.getOptionalString(nameOld));
		modelMap.addAttribute("mailOld", HelpFunctions.getOptionalString(mailOld));
		modelMap.addAttribute("usernameOld", HelpFunctions.getOptionalString(usernameOld));

		if ("checked".equals(HelpFunctions.getOptionalString(checked1Old))
				&& "checked".equals(HelpFunctions.getOptionalString(checked2Old))) {
			modelMap.addAttribute("checked1Old", true);
			// System.out.println("c1");
		} else {
			if ("checked".equals(HelpFunctions.getOptionalString(checked2Old))) {
				modelMap.addAttribute("checked2Old", true);
				System.out.println("c2");
			} else {
				modelMap.addAttribute("checked1Old", true);
				// System.out.println("c1");
			}
		}

		modelMap.addAttribute("streetOld", HelpFunctions.getOptionalString(streetOld));
		modelMap.addAttribute("housenrOld", HelpFunctions.getOptionalString(housenrOld));
		modelMap.addAttribute("postcodeHOld", HelpFunctions.getOptionalString(postcodeHOld));
		modelMap.addAttribute("cityHOld", HelpFunctions.getOptionalString(cityHOld));

		modelMap.addAttribute("fhl_nameOld", HelpFunctions.getOptionalString(fhl_nameOld));
		modelMap.addAttribute("citypartOld", HelpFunctions.getOptionalString(citypartOld));
		modelMap.addAttribute("postcodeROld", HelpFunctions.getOptionalString(postcodeROld));
		modelMap.addAttribute("cityROld", HelpFunctions.getOptionalString(cityROld));

		modelMap.addAttribute("nativelanguageOld", HelpFunctions.getOptionalString(nativelanguageOld));
		modelMap.addAttribute("language2Old", HelpFunctions.getOptionalString(language2Old));
		modelMap.addAttribute("language3Old", HelpFunctions.getOptionalString(language3Old));
		modelMap.addAttribute("language4Old", HelpFunctions.getOptionalString(language4Old));
		modelMap.addAttribute("language5Old", HelpFunctions.getOptionalString(language5Old));
		modelMap.addAttribute("originOld", HelpFunctions.getOptionalString(originOld));

		return "new_user";

	}

	@RequestMapping(value = "/submit_captcha")
	public String redirect_reCAPTCHA() {
		return "redirect:/reCAPTCHA-TEST";
	}

	@RequestMapping(value = "/reCAPTCHA-TEST")
	public String show_reCAPTCHA() {
		return "/reCAPTCHA";

	}

	@RequestMapping({ "/activationmail_gesendet" })
	public String activationmail_gesendet() {
		return "/activationmail_gesendet";
	}

	@RequestMapping({ "/activationmail_local" })
	public String activationmail_local() {
		return "/activationmail_local";
	}

	@RequestMapping(value = "/submit_captcha", method = RequestMethod.POST)
	public String recieve_reCAPTCHA(@RequestParam("g-recaptcha-response") String CaptchaResponse) {

		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);

		if (CaptchaResponse.isEmpty()) {
			return "redirect:/reCAPTCHA-TEST";
		} else {
			// http://localhost:8080/create_new_user_temp?mail=aa&username=a&password=a&repassword=a

			if (HelpFunctions.checkCaptcha(CaptchaResponse)) {
				return "/validation_success";
			} else {
				return "redirect:/reCAPTCHA-TEST";
			}
		}
	}

	@RequestMapping(value = "/activation/user/{user_temp}/{textactivationkey_temp}", method = RequestMethod.GET)
	public String recieve_activationkey(@PathVariable String user_temp, @PathVariable String textactivationkey_temp) {

		String user = user_temp.replace("{", "").replace("}", "");
		String textactivationkey = textactivationkey_temp.replace("{", "").replace("}", "");

		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}

		if (userAccountManager.findByUsername(user).isPresent()) {
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

			System.out.println("Get a activationlink for " + user_xyz.getUserAccount().getUsername() + "");

			if (textactivationkey.isEmpty()) {
				return "redirect:/";
			}

			if (user_xyz.isActivated()) {
				System.out.println("Nutzer bereits aktiviert.");
				return "redirect:/";
			}

			Date zeitstempel = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy
																					// HH:mm:ss"
			// simpleDateFormat.format(zeitstempel)

			if (HelpFunctions.sha256(user_xyz.getActivationkey() + simpleDateFormat.format(zeitstempel)
					+ user_xyz.getRegistrationstate()).equals(textactivationkey)) {
				user_xyz.Activate();
				System.out.println(user_xyz.getUserAccount().getUsername() + " wurde aktiviert.");

				user_xyz.setRegistrationstate(10);
				userRepository.save(user_xyz);

				System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			} else {
				System.out.println(user_xyz.getUserAccount().getUsername() + ": Aktivierung fehlgeschlagen");
			}

			return "redirect:/";
		}

		return "redirect:/";
	}

	@RequestMapping(value = "/not_activated/user/{user}", method = RequestMethod.GET)
	public String user_not_activated(@PathVariable final String user,
			@LoggedIn final Optional<UserAccount> userAccount) {

		if (userAccount.isPresent() == false) {
			return "redirect:/";
		}

		if (user.equals("")) {
			return "redirect:/";
		}

		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}

		if ((userAccount.get().getUsername().equals(user)) == false) {
			return "redirect:/";
		} else {
			System.out.println(userAccount.get().getUsername());

			if (userAccountManager.findByUsername(user).isPresent()) {
				User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

				if (user_xyz.isActivated()) {
					return "redirect:/"; // Alles OK.
				}

				switch (user_xyz.getRegistrationstate()) {
				case -1:
					return "redirect:/new_user_data";
				case 0: // Fall: Temporäres Registrierungsdatum fehlt.
					user_xyz.setRegistrationdate(new Date()); // Temporäres
																// Registrierungsdatum
																// um
																// unvollständige
																// Konten nach
																// einer
																// betimmten
																// Zeit zu
																// löschen.
					user_xyz.setRegistrationstate(1);

					userRepository.save(user_xyz);
					return "redirect:/new_user_aboutuser1/user/{user}";
				case 1:
					return "redirect:/new_user_aboutuser1/user/{user}";
				case 2:
					if (user_xyz.getAddresstypString().equals("Refugees_home")) {
						return "redirect:/new_user_aboutuser2a/user/{user}";
					}
					if (user_xyz.getAddresstypString().equals("Wohnung")) {
						return "redirect:/new_user_aboutuser2b/user/{user}";
					}
					return "redirect:/new_user_aboutuser1/user/{user}";
				case 3:
					return "redirect:/new_user_language_origin/user/{user}";
				case 4:
					return "redirect:/new_user_language_origin/user/{user}";
				case 5:
					return "redirect:/new_user_language_origin/user/{user}"; // Aktivierungskey
																				// noch
																				// nicht
																				// generiert
				case 6:
					return "redirect:/reCAPTCHA/user/{user}";
				case 7:
					return "redirect:/reCAPTCHA/user/{user}";
				case 8:
					return "redirect:/reCAPTCHA/user/{user}";
				case 9:
					return "redirect:/"; // Account deaktiviert.
				case 10:
					return "redirect:/"; // Alles OK.
				}
			}
			return "redirect:/";
		}
	}

	// ******************************************************************************************//*************************************************//
	// Registrierung:

	@RequestMapping(value = "/create_new_user", method = RequestMethod.POST)
	public String create_new_user(@RequestParam("mailIN") @Email @Valid final String Mail,
			@RequestParam("usernameIN") @Valid final String Username,
			@RequestParam("passwordIN") @Valid final String Password,
			@RequestParam("repasswordIN") @Valid final String RePassword, @RequestParam("nameIN") final String Name,
			@RequestParam("firstnameIN") final String Firstname, @RequestParam("wohnen") final String Adresstyp,
			@RequestParam("flh_name") final Optional<String> Flh_name_OPT,
			@RequestParam("citypart") final Optional<String> Citypart_OPT,
			@RequestParam("street") final Optional<String> Street_OPT,
			@RequestParam("housenr") final Optional<String> Housenr_OPT,
			@RequestParam("postcode_R") final Optional<String> Postcode_R,
			@RequestParam("city_R") final Optional<String> City_R,
			@RequestParam("postcode_H") final Optional<String> Postcode_H,
			@RequestParam("city_H") final Optional<String> City_H,
			@RequestParam("nativelanguage") final String Nativelanguage,
			@RequestParam("otherlanguages") final String OtherLanguages, @RequestParam("origin") final String Origin,
			@RequestParam("g-recaptcha-response") Optional<String> CaptchaResponse_OPT) {

		String CaptchaResponse = HelpFunctions.getOptionalString(CaptchaResponse_OPT);

		String filledFields = HelpFunctions.getOldData(Firstname, Name, Mail, Username, Adresstyp, Flh_name_OPT,
				Citypart_OPT, Street_OPT, Housenr_OPT, Postcode_R, City_R, Postcode_H, City_H, Nativelanguage,
				OtherLanguages, Origin);

		if (CaptchaResponse.isEmpty()) {
			return "redirect:/new_user?EmptyError_captcha" + filledFields;
		}

		System.out.println("/************ Create_New_User ************/");

		System.out.println(Mail);
		System.out.println(Username);
		//System.out.println(Password);
		//System.out.println(RePassword);
		System.out.println("---");

		if (Mail.isEmpty() || Username.isEmpty() || Password.isEmpty() || RePassword.isEmpty()) {
			String returnstr = "redirect:/new_user?";
			if (Mail.isEmpty()) {
				returnstr = returnstr + "&EmptyError_mail";
			}
			if (Username.isEmpty()) {
				returnstr = returnstr + "&EmptyError_username";
			}
			if (Password.isEmpty()) {
				returnstr = returnstr + "&EmptyError_password";
			}
			if (RePassword.isEmpty()) {
				returnstr = returnstr + "&EmptyError_repassword";
			}
			return returnstr.replace("?&", "?") + filledFields;
		}

		if (!Password.equals(RePassword)) {
			return "redirect:/new_user?NoeqlError_repassword" + filledFields;
		}

		if (Password.length() < 8) {
			System.out.println("E: Passwort zu kurz.");
			return "redirect:/new_user?ShortError_password" + filledFields;
		} else {
			int pwstrength = 0;
			pwstrength = HelpFunctions.checkPasswordStrength(Password);

			System.out.println("PasswordStrength: " + pwstrength);
			if (pwstrength == 0) {
				System.out.println("E: Passwort erfüllt nicht die Anforderungen.");
				return "redirect:/new_user?UnsecError_password" + filledFields;
			}
		}

		if (HelpFunctions.emailValidator(Mail) == false) {
			System.out.println("E: " + Mail + " ist eine ungültige Mailadresse.");
			return "redirect:/new_user?InvalError_mail" + filledFields;
		}

		boolean equalMail = false;
		for (UserAccount TempUA : userAccountManager.findAll()) {
			if ((!(TempUA == null)) && (equalMail == false)) {
				System.out.println(TempUA.getUsername());
				System.out.println(TempUA.getEmail());

				if (!(TempUA.getEmail() == null)) {
					if (TempUA.getEmail().equals(Mail)) {
						equalMail = true;
					}
				}
			}
		}

		if (equalMail) {
			System.out.println("E: " + Mail + " ist eine bereits verwendete Mailadresse.");
			return "redirect:/new_user?UsedError_mail" + filledFields;
		}

		// System.out.println("begin2");
		if (Nativelanguage.isEmpty() || Nativelanguage.equals("---- Select ----")) {
			return "redirect:/new_user?EmptyError_nativelanguage" + filledFields;
		} else {
			Language aLanguage = languageRepository.findByKennung(Nativelanguage);
			if (aLanguage == null) {
				return "redirect:/new_user?EmptyError_nativelanguage" + filledFields;
			}
		}
		// System.out.println("end2");

		if (Origin.isEmpty() || Origin.equals("---- Select ----")) {
			return "redirect:/new_user?EmptyError_origin" + filledFields;
		}

		if (Username.equals("new_user"))

		{
			String e_descrition = "E: Invalid Username.";
			System.out.println("ERROR: " + e_descrition);

			return "redirect:/new_user?InvalError_username" + filledFields;
		}

		if (userAccountManager.findByUsername(Username).isPresent())

		{
			String e_descrition = "E: Username already in use.";
			System.out.println("ERROR: " + e_descrition);

			return "redirect:/new_user?UsedError_username" + filledFields;
		}

		String Postcode_N;
		String City_N;
		Address address = new Address("", "", "", "", "", "");

		if (Adresstyp.equals("refugee")) // Refugees_home
		{
			// return "redirect:/new_user_aboutuser2a/user/{user}";

			System.out.println(Flh_name_OPT.get());
			System.out.println(Citypart_OPT.get());
			System.out.println(Postcode_R.get());
			System.out.println(City_R.get());

			String Flh_name;
			String Citypart;

			Flh_name = HelpFunctions.getOptionalString(Flh_name_OPT);

			Postcode_N = HelpFunctions.getOptionalString(Postcode_R);

			City_N = HelpFunctions.getOptionalString(City_R);

			Citypart = HelpFunctions.getOptionalString(Citypart_OPT);

			if ((Flh_name.isEmpty()) || (Citypart.isEmpty()) || Postcode_N.isEmpty() || City_N.isEmpty()) {
				String returnstr = "redirect:/new_user?";
				if (Flh_name.isEmpty()) {
					returnstr = returnstr + "&EmptyError_fhl_name";
				}
				if (Citypart.isEmpty()) {
					returnstr = returnstr + "&EmptyError_citypart";
				}
				if (Postcode_N.isEmpty()) {
					returnstr = returnstr + "&EmptyError_plzR";
				}
				if (City_N.isEmpty()) {
					returnstr = returnstr + "&EmptyError_cityR";
				}
				return returnstr.replace("?&", "?") + filledFields;
			}

			while (Postcode_N.length() < 5) {
				Postcode_N = "0" + Postcode_N;
			}

			if (Postcode_N.length() != 5) {
				System.out.println("E: Ungültige Postleitzahl");
				return "redirect:/new_user?InvalError_plzR" + filledFields;
			} else {
				String[] partialRegexChecks = { ".*[a-z]+.*", // lower
						".*[A-Z]+.*", // upper
						".*[\\d]+.*", // digits
						".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
				};
				int i = 0;
				while (i < 5) {
					if (!Postcode_N.substring(i, i + 1).matches(partialRegexChecks[2])) {
						System.out.println("E: Ungültige Postleitzahl");
						return "redirect:/new_user?InvalError_plzR" + filledFields;
					}

					i = i + 1;
				}
			}


			Address address = new Address("", "", Flh_name, Citypart, Postcode_N, City_N);
			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(3); // 3 ~ Flüchtlingsheim
			userRepository.save(user_xyz);
			user_xyz.setCoordinates(user_xyz.createCoordinates());
			userRepository.save(user_xyz);
			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			// return "redirect:/new_user_language_origin/user/{user}";

		}

		if (Adresstyp.equals("helper")) // Wohnung

		{
			// return "redirect:/new_user_aboutuser2b/user/{user}";

			System.out.println(Street_OPT.get());
			System.out.println(Housenr_OPT.get());
			System.out.println(Postcode_H.get());
			System.out.println(City_H.get());

			String Street;
			String Housenr;

			Street = HelpFunctions.getOptionalString(Street_OPT);

			Housenr = HelpFunctions.getOptionalString(Housenr_OPT);

			Postcode_N = HelpFunctions.getOptionalString(Postcode_H);

			City_N = HelpFunctions.getOptionalString(City_H);

			if (Street.isEmpty() || Housenr.isEmpty() || Postcode_N.isEmpty() || City_N.isEmpty()) {
				String returnstr = "redirect:/new_user?";
				if (Street.isEmpty()) {
					returnstr = returnstr + "&EmptyError_street";
				}
				if (Housenr.isEmpty()) {
					returnstr = returnstr + "&EmptyError_housenr";
				}
				if (Postcode_N.isEmpty()) {
					returnstr = returnstr + "&EmptyError_plzH";
				}
				if (City_N.isEmpty()) {
					returnstr = returnstr + "&EmptyError_cityH";
				}
				return returnstr.replace("?&", "?") + filledFields;
			}

			while (Postcode_N.length() < 5) {
				Postcode_N = "0" + Postcode_N;
			}

			if (Postcode_N.length() != 5) {
				System.out.println("E: Ungültige Postleitzahl");
				return "redirect:/new_user?InvalError_plzH" + filledFields;
			} else {
				String[] partialRegexChecks = { ".*[a-z]+.*", // lower
						".*[A-Z]+.*", // upper
						".*[\\d]+.*", // digits
						".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
				};
				int i = 0;
				while (i < 5) {
					if (!Postcode_N.substring(i, i + 1).matches(partialRegexChecks[2])) {
						System.out.println("E: Ungültige Postleitzahl");
						return "redirect:/new_user?InvalError_plzH" + filledFields;
					}
					i = i + 1;
				}
			}
			address = new Address(Street, Housenr, Postcode_N, City_N);
		}

		// Useraccount anlegen *********************************************

		UserAccount userAccount = userAccountManager.create(Username, Password, new Role("ROLE_NORMAL"));
		userAccountManager.save(userAccount);
		userAccount.setEmail(Mail);
		userAccountManager.save(userAccount);

		// userAccount.isEnabled = false;

		System.out.println("Account " + userAccount.getUsername() + " angelegt.");

		UserAccount LoggUser = userAccountManager.findByUsername(userAccount.getUsername()).get();
		Address emptyadress = new Address("", "", "", "");
		User user_xyz = new User(LoggUser, emptyadress);
		user_xyz.setRegistrationstate(0);
		userRepository.save(user_xyz);

		user_xyz.setRegistrationdate(new Date()); // Temporäres
													// Registrierungsdatum um
													// unvollständige Konten
													// nach einer betimmten Zeit
													// zu löschen.
		user_xyz.setRegistrationstate(1);
		userRepository.save(user_xyz);

		System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());

		System.out.println("/not_activated/user/" + user_xyz.getUserAccount().getUsername());

		// Step 2

		System.out.println(Name);
		System.out.println(Firstname);
		System.out.println(Adresstyp);

		if (Name.isEmpty() || Firstname.isEmpty() || Adresstyp.isEmpty()) {
			user_xyz.getUserAccount().setEmail("");
			userRepository.save(user_xyz);
			return "errorpage_empty";
		}

		user_xyz.getUserAccount().setLastname(Name);
		user_xyz.getUserAccount().setFirstname(Firstname);

		if (Adresstyp.equals("refugee")) {
			user_xyz.setAddresstyp(AddresstypEnum.Refugees_home);
		}
		if (Adresstyp.equals("helper")) {
			user_xyz.setAddresstyp(AddresstypEnum.Wohnung);
		}
		userAccountManager.save(user_xyz.getUserAccount());

		user_xyz.setRegistrationstate(2);
		userRepository.save(user_xyz);

		System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());

		if (Adresstyp.equals("refugee")) // Refugees_home
		{
			// return "redirect:/new_user_aboutuser2a/user/{user}";

			System.out.println(Flh_name_OPT.get());
			System.out.println(Citypart_OPT.get());
			System.out.println(Postcode_R.get());
			System.out.println(City_R.get());

			String Flh_name;
			String Citypart;

			Flh_name = HelpFunctions.getOptionalString(Flh_name_OPT);
			Postcode_N = HelpFunctions.getOptionalString(Postcode_R);
			City_N = HelpFunctions.getOptionalString(City_R);
			Citypart = HelpFunctions.getOptionalString(Citypart_OPT);

			// KLASSE FLÜCHTLINGSHEIM UND INTERFACE LOCATION!!!!!!!!!!!!!!
			if ((Flh_name.isEmpty()) || (Citypart.isEmpty()) || Postcode_N.isEmpty() || City_N.isEmpty()) {
				return "error";
			}

			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(3); // 3 ~ Flüchtlingsheim
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			// return "redirect:/new_user_language_origin/user/{user}";
		}

		if (Adresstyp.equals("helper")) // Wohnung

		{
			// return "redirect:/new_user_aboutuser2b/user/{user}";

			System.out.println(Street_OPT.get());
			System.out.println(Housenr_OPT.get());
			System.out.println(Postcode_H.get());
			System.out.println(City_H.get());

			String Street;
			String Housenr;

			Street = HelpFunctions.getOptionalString(Street_OPT);
			Housenr = HelpFunctions.getOptionalString(Housenr_OPT);
			Postcode_N = HelpFunctions.getOptionalString(Postcode_H);
			City_N = HelpFunctions.getOptionalString(City_H);

			if (Street.isEmpty() || Housenr.isEmpty() || Postcode_N.isEmpty() || City_N.isEmpty()) {
				return "error";
			}

			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(4); // 4 ~ Wohnung
			userRepository.save(user_xyz);
			user_xyz.setCoordinates(user_xyz.createCoordinates());
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			// return "redirect:/new_user_language_origin/user/{user}";
		}

		// Step 3;

		System.out.println(Nativelanguage);
		System.out.println(OtherLanguages);
		System.out.println(Origin);

		if (Nativelanguage.isEmpty() || Origin.isEmpty()) {
			user_xyz.getUserAccount().setEmail("");
			userRepository.save(user_xyz);
			return "errorpage_empty";
		}

		if (Origin.equals("---- Select ----")) {
			user_xyz.getUserAccount().setEmail("");
			userRepository.save(user_xyz);
			System.out.println("E: Kein Herkunftsland ausgewählt");
			return "errorpage_empty";
		}

		Language PreferredLanguage = languageRepository.findByKennung(Nativelanguage);
		if (PreferredLanguage == null)
			System.out.println("E: Prefl==null");
		else
			System.out.println(PreferredLanguage);

		user_xyz.setPrefLanguage(PreferredLanguage);
		System.out.println(user_xyz.getLanguages());
		userRepository.save(user_xyz);
		System.out.println("save");

		if (OtherLanguages != null && !OtherLanguages.isEmpty())

		{
			for (String languageName : OtherLanguages.split(",")) {
				System.out.println(languageName);
				if (languageRepository.findByKennung(languageName) != null) {
					// user_xyz.setLanguage(languageRepository.findByName(languageName));
					Language l1 = languageRepository.findByKennung(languageName);
					System.out.println(l1.toString());
					user_xyz.setLanguage(l1);
					userRepository.save(user_xyz);

				}
				System.out.println(user_xyz.getLanguages());
			}
		}
		/////////////////

		user_xyz.setOrigin(Origin);

		user_xyz.setRegistrationstate(5);
		userRepository.save(user_xyz);

		// Nutzeraktivierung vorbereiten:

		Integer z1, z2; // Zufallszahlen
		z1 = (int) (Math.random() * 1000000000) + 123456;
		z2 = (int) (Math.random() * 1000000000) + 117980;

		String activationkey = HelpFunctions.aktivationkeyCreation(user_xyz.getUserAccount().getUsername(),
				user_xyz.getUserAccount().getEmail(), z1, z2);
		user_xyz.setActivationkey(activationkey);

		user_xyz.setRegistrationdate(new Date());
		user_xyz.setRegistrationstate(6); // 6
											// ~
											// Bereit
											// zur
											// Aktivierung
		userRepository.save(user_xyz);

		System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
		// return "redirect:/reCAPTCHA/user/{user}";

		// Step 4;

		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);

		if (CaptchaResponse.isEmpty())

		{
			return "error";
		} else

		{
			// http://localhost:8080/create_new_user_temp?mail=aa&username=a&password=a&repassword=a

			if (HelpFunctions.checkCaptcha(CaptchaResponse)) {
				user_xyz.setRegistrationstate(7); // 7 ~ Captcha erfolgreich
													// geprüft
				userRepository.save(user_xyz);

				System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());

				Date zeitstempel = new Date();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy
																						// HH:mm:ss"
				// simpleDateFormat.format(zeitstempel)

				String domain = "http://refugee-app.tk/swt15w9"; // "http://localhost:8080";
				String link = domain + "/activation/user/{" + user_xyz.getUserAccount().getUsername() + "}/{"
						+ HelpFunctions.sha256(user_xyz.getActivationkey() + simpleDateFormat.format(zeitstempel)
								+ (user_xyz.getRegistrationstate() + 1))
						+ "}";
				String mailtext = "<html> <head> </head> <body> <h1>Activation of your RefugeesApp-Account ("
						+ user_xyz.getUserAccount().getUsername() + ")<h1> Hallo "
						+ user_xyz.getUserAccount().getUsername()
						+ " </h1><br/><br/> Please activate your RefugeesApp-Account with this link: <a href=\"" + link
						+ "\">Activationlink</a>  <br/><br/> Textlink: " + link + "  </body> </html>";
				String mailadresse = user_xyz.getUserAccount().getEmail();

				System.out.println(link);

				// Mail senden:
				if (!mailadresse.equals("test@test.test")) {
					// Mail senden:
					try {
						HelpFunctions.mailSenden(mailadresse, "Activation of your RefugeesApp-Account ("
								+ user_xyz.getUserAccount().getUsername() + ")", mailtext);
						System.out.println("Mail versandt");
					} catch (MessagingException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				user_xyz.setRegistrationstate(8); // 8 ~ Aktivierungsmail
													// versandt.
				userRepository.save(user_xyz);

				System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());

				if (!mailadresse.equals("test@test.test")) {
					return "redirect:/activationmail_gesendet";
				} else {
					return "redirect:/activationmail_local";
				}

			} else {
				return "redirect:/new_user?InvalError_captcha" + filledFields;
			}
		}

	}

}

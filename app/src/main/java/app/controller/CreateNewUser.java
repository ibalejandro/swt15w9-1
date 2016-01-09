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
import org.springframework.mail.MailSender;
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
	private final MailSender mailSender;

	/**
	 * Autowire.
	 * 
	 * @param CreateNewUser
	 */
	@Autowired
	public CreateNewUser(UserAccountManager userAccountManager, UserRepository userRepository,
			LanguageRepository languageRepository, MailSender mailSender) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		this.languageRepository = languageRepository;
		this.mailSender = mailSender;
	}

	int zufallzahl_1, zufallzahl_2; // Zufallszahlen
	boolean za;

	@RequestMapping(value = "/new_user")
	public String new_user0(Model modelMap) {
		ListCountry a = new ListCountry();
		LinkedList<String> L = a.getCountryList(Locale.ENGLISH);

		modelMap.addAttribute("countrys", L);

		return "new_user";

	}

	@RequestMapping(value = "/new_user_data")
	public String new_user() {

		Integer zufallzahl1 = (int) (Math.random() * 50) + 1;
		Integer zufallzahl2 = (int) (Math.random() * 48) + 3;

		zufallzahl_1 = zufallzahl1;
		zufallzahl_2 = zufallzahl2;
		za = true;

		String szufallzahl1 = zufallzahl1.toString();
		String szufallzahl2 = zufallzahl2.toString();

		// System.out.println("----1");

		return "redirect:/new_user_data_rd/" + szufallzahl1 + "/" + szufallzahl2;
	}

	@RequestMapping(value = "/new_user_data_rd/0/0", method = RequestMethod.GET)
	public String new_user_rd0() {
		return "redirect:/new_user_data";
	}

	@RequestMapping(value = "/new_user_data_rd/{zufallzahl1}/{zufallzahl2}", method = RequestMethod.GET)
	public String new_user_rd(@PathVariable("zufallzahl1") String szufallzahl1,
			@PathVariable("zufallzahl2") String szufallzahl2) {

		if (!za) {
			return "redirect:/new_user_data";
		}

		za = false;

		return "new_user_data";
	}

	@RequestMapping({ "/new_user_aboutuser1/user/{user}" })
	public String new_user1(@PathVariable String user) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}
		return "new_user_aboutuser1";

	}

	@RequestMapping({ "/new_user_aboutuser2" })
	public String new_user2() {
		return "redirect:new_user_aboutuser1";
	}

	@RequestMapping({ "/new_user_aboutuser2a/user/{user}" })
	public String new_user2a(@PathVariable String user) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}
		return "/new_user_aboutuser2a";
	}

	@RequestMapping({ "/new_user_aboutuser2b/user/{user}" })
	public String new_user2b(@PathVariable String user) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}
		return "/new_user_aboutuser2b";
	}

	@RequestMapping({ "/new_user_language_origin/user/{user}" })
	public String new_user_language_origin(@PathVariable String user, Model modelMap) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}
		ListCountry a = new ListCountry();
		LinkedList<String> L = a.getCountryList(Locale.ENGLISH);

		modelMap.addAttribute("countrys", L);

		return "/new_user_language_origin";
	}

	@RequestMapping(value = "/submit_captcha")
	public String redirect_reCAPTCHA() {
		return "redirect:/reCAPTCHA-TEST";
	}

	@RequestMapping(value = "/reCAPTCHA/user/{user}")
	public String show_reCAPTCHA_user(@PathVariable String user) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}
		return "/reCAPTCHA_User";
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

			String Secret = "6LcBYBATAAAAAPHUZfB4OFpbdwrVxp08YEaVX3Dr";
			String Returnstring = "";

			System.out.println("## Validate:");
			System.out.println("https://www.google.com/recaptcha/api/siteverify?response=" + CaptchaResponse
					+ "&secret=" + Secret);

			try {
				Returnstring = HelpFunctions.sendPost(CaptchaResponse, Secret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (Returnstring.equals("{  \"success\": true}")) {
				return "/validation_success";
			} else {
				return "redirect:/reCAPTCHA-TEST";
			}
		}
	}

	@RequestMapping(value = "/submit_captcha/user/{user}", method = RequestMethod.POST)
	public String recieve_reCAPTCHA_user(@PathVariable String user,
			@RequestParam("g-recaptcha-response") String CaptchaResponse) {

		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);

		if (CaptchaResponse.isEmpty() || (!userAccountManager.findByUsername(user).isPresent())) {
			if (!userAccountManager.findByUsername(user).isPresent()) {
				return "redirect:/";
			} else {
				return "redirect:/submit_captcha/user/" + user;
			}

		} else {
			// http://localhost:8080/create_new_user_temp?mail=aa&username=a&password=a&repassword=a

			String Secret = "6LcBYBATAAAAAPHUZfB4OFpbdwrVxp08YEaVX3Dr";
			String Returnstring = "";

			System.out.println("## Validate:");
			System.out.println("https://www.google.com/recaptcha/api/siteverify?response=" + CaptchaResponse
					+ "&secret=" + Secret);

			try {
				Returnstring = HelpFunctions.sendPost(CaptchaResponse, Secret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (Returnstring.equals("{  \"success\": true}")) {
				if (userAccountManager.findByUsername(user).isPresent()) {
					User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

					user_xyz.setRegistrationstate(7); // 7 ~ Captcha erfolgreich
														// geprüft
					userRepository.save(user_xyz);

					System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());

					Date zeitstempel = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy
																							// HH:mm:ss"
					// simpleDateFormat.format(zeitstempel)

					String domain = "http://refugee-app.tk/swt15w9"; // "http://localhost:8080";
					String link = domain + "/activation/user/{" + user_xyz.getUserAccount().getUsername()
							+ "}/{" + HelpFunctions.sha256(user_xyz.getActivationkey()
									+ simpleDateFormat.format(zeitstempel) + (user_xyz.getRegistrationstate() + 1))
							+ "}";
					String mailtext = "<html> <head> </head> <body> <h1>Activation of your RefugeesApp-Account ("
							+ user_xyz.getUserAccount().getUsername() + ")<h1> Hallo "
							+ user_xyz.getUserAccount().getUsername()
							+ " </h1><br/><br/> Please activate your RefugeesApp-Account with this link: <a href=\""
							+ link + "\">Activationlink</a>  <br/><br/> Textlink: " + link + "  </body> </html>";
					String mailadresse = user_xyz.getUserAccount().getEmail();

					System.out.println(link);

					// Mail senden:
					if (!mailadresse.equals("test@test.test")) {
						// Mail senden:
						try {
							HelpFunctions.Mailsenden(mailadresse, "Activation of your RefugeesApp-Account ("
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

				}
				return "redirect:/";

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
			@RequestParam("g-recaptcha-response") String CaptchaResponse) {
		if (CaptchaResponse.isEmpty()) {
			return "error_empty_captcha";
		}

		System.out.println("/************ Create_New_User ************/");

		System.out.println(Mail);
		System.out.println(Username);
		// System.out.println(Password);
		// System.out.println(RePassword);

		if (Mail.isEmpty() || Username.isEmpty() || Password.isEmpty()) {
			return "errorpage_empty";
		}

		if (!Password.equals(RePassword)) {
			return "errorpage_wrongpw";
		}

		if (Password.length() < 8) {
			System.out.println("E: Passwort zu kurz.");
			return "errorpage_wrongpw";
		} else {
			int pwstrength = 0;
			pwstrength = HelpFunctions.checkPasswordStrength(Password);

			System.out.println("PasswordStrength: " + pwstrength);
			if (pwstrength == 0) {
				System.out.println("E: Passwort erfüllt nicht die Anforderungen.");
				return "errorpage_wrongpw";
			}
		}

		if (HelpFunctions.emailValidator(Mail) == false) {
			System.out.println("E: " + Mail + " ist eine ungültige Mailadresse.");
			return "error";
		}

		boolean equalMail = false;
		for (UserAccount TempUA : userAccountManager.findAll()) {
			if ((!(TempUA == null)) && (equalMail == false)) {
				// System.out.println(TempUA.getUsername());
				// System.out.println(TempUA.getEmail());

				if (!(TempUA.getEmail() == null)) {
					if (TempUA.getEmail().equals(Mail)) {
						equalMail = true;
					}
				}
			}
		}

		if (equalMail) {
			System.out.println("E: " + Mail + " ist eine bereits verwendete Mailadresse.");
			return "error";
		}

		if (Username.equals("new_user")) {
			String e_descrition = "E: Invalid Username.";
			System.out.println("ERROR: " + e_descrition);

			return "error";
		}

		if (userAccountManager.findByUsername(Username).isPresent()) {
			String e_descrition = "E: Username already in use.";
			System.out.println("ERROR: " + e_descrition);

			return "error";
		}

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

		String Postcode_N;
		String City_N;

		if (Adresstyp.equals("refugee")) // Refugees_home
		{
			// return "redirect:/new_user_aboutuser2a/user/{user}";

			System.out.println(Flh_name_OPT.get());
			System.out.println(Citypart_OPT.get());
			System.out.println(Postcode_R.get());
			System.out.println(City_R.get());

			String Flh_name;
			String Citypart;

			if (Flh_name_OPT.isPresent()) {
				Flh_name = Flh_name_OPT.get();
			} else {
				Flh_name = "";
			}

			if (Postcode_R.isPresent()) {
				Postcode_N = Postcode_R.get();
			} else {
				Postcode_N = "";
			}

			if (City_R.isPresent()) {
				City_N = City_R.get();
			} else {
				City_N = "";
			}

			if (Citypart_OPT.isPresent()) {
				Citypart = Citypart_OPT.get();
			} else {
				Citypart = "";
			}

			// KLASSE FLÜCHTLINGSHEIM UND INTERFACE LOCATION!!!!!!!!!!!!!!
			if ((Flh_name.isEmpty()) || (Citypart.isEmpty()) || Postcode_N.isEmpty() || City_N.isEmpty()) {
				return "errorpage_empty";
			}

			if (Postcode_N.length() != 5) {
				System.out.println("E: Ungültige Postleitzahl");
				return "error";
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
						return "error";
					}

					i = i + 1;
				}
			}

			Address address = new Address("", "", Flh_name, Citypart, Postcode_N, City_N);
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

			if (Street_OPT.isPresent()) {
				Street = Street_OPT.get();
			} else {
				Street = "";
			}

			if (Housenr_OPT.isPresent()) {
				Housenr = Housenr_OPT.get();
			} else {
				Housenr = "";
			}

			if (Postcode_H.isPresent()) {
				Postcode_N = Postcode_H.get();
			} else {
				Postcode_N = "";
			}

			if (City_H.isPresent()) {
				City_N = City_H.get();
			} else {
				City_N = "";
			}

			if (Street.isEmpty() || Housenr.isEmpty() || Postcode_N.isEmpty() || City_N.isEmpty()) {
				return "errorpage_empty";
			}

			if (Postcode_N.length() != 5) {
				System.out.println("E: Ungültige Postleitzahl");
				return "error";
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
						return "error";
					}
					i = i + 1;
				}
			}

			Address address = new Address(Street, Housenr, Postcode_N, City_N);
			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(4); // 4 ~ Wohnung
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			// return "redirect:/new_user_language_origin/user/{user}";
		}

		// Step 3;

		System.out.println(Nativelanguage);
		System.out.println(OtherLanguages);
		System.out.println(Origin);

		if (Nativelanguage.isEmpty() || Origin.isEmpty()) {
			return "errorpage_empty";
		}
		if (Origin.equals("---- Select ----")) {
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

		if (OtherLanguages != null && !OtherLanguages.isEmpty()) {
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

		String activationkey = HelpFunctions.AktivierungskeyErzeugen(user_xyz.getUserAccount().getUsername(),
				user_xyz.getUserAccount().getEmail(), z1, z2);
		user_xyz.setActivationkey(activationkey);

		user_xyz.setRegistrationdate(new Date());
		user_xyz.setRegistrationstate(6); // 6 ~ Bereit zur Aktivierung
		userRepository.save(user_xyz);

		System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
		// return "redirect:/reCAPTCHA/user/{user}";

		// Step 4;

		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);

		if (CaptchaResponse.isEmpty()) {
			return "error";
		} else {
			// http://localhost:8080/create_new_user_temp?mail=aa&username=a&password=a&repassword=a

			String Secret = "6LcBYBATAAAAAPHUZfB4OFpbdwrVxp08YEaVX3Dr";
			String Returnstring = "";

			System.out.println("## Validate:");
			System.out.println("https://www.google.com/recaptcha/api/siteverify?response=" + CaptchaResponse
					+ "&secret=" + Secret);

			try {
				Returnstring = HelpFunctions.sendPost(CaptchaResponse, Secret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (Returnstring.equals("{  \"success\": true}")) {
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
						HelpFunctions.Mailsenden(mailadresse, "Activation of your RefugeesApp-Account ("
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
				return "redirect:/reCAPTCHA-TEST";
			}
		}

	}

	// ##################################################################################################################//
	// ##################################################################################################################//

	@RequestMapping(value = "/create_temp_new_user", method = RequestMethod.POST)
	public String create_new_user_t(@RequestParam("mail") @Email @Valid final String Mail,
			@RequestParam("username") @Valid final String Username,
			@RequestParam("password") @Valid final String Password,
			@RequestParam("repassword") @Valid final String RePassword,
			@RequestParam("summe") @Valid final String chsumme) {

		System.out.println(Mail);
		System.out.println(Username);
		System.out.println(Password);
		System.out.println(RePassword);

		if (Mail.isEmpty() || Username.isEmpty() || Password.isEmpty() || chsumme.isEmpty()) {
			return "errorpage0_empty";
		}

		if (!Password.equals(RePassword)) {
			return "errorpage0_wrongpw";
		}

		if (Password.length() < 8) {
			System.out.println("Passwort zu kurz.");
			return "errorpage0_wrongpw";
		} else {
			int pwstrength = 0;
			pwstrength = HelpFunctions.checkPasswordStrength(Password);

			System.out.println("PasswordStrength: " + pwstrength);
			if (pwstrength == 0) {
				System.out.println("Passwort erfüllt nicht die Anforderungen.");
				return "errorpage0_wrongpw";
			}
		}

		if (HelpFunctions.emailValidator(Mail) == false) {
			System.out.println(Mail + " ist eine ungültige Mailadresse.");
			return "error";
		}

		boolean equalMail = false;
		for (UserAccount TempUA : userAccountManager.findAll()) {
			if ((!(TempUA == null)) && (equalMail == false)) {
				// System.out.println(TempUA.getUsername());
				// System.out.println(TempUA.getEmail());

				if (!(TempUA.getEmail() == null)) {
					if (TempUA.getEmail().equals(Mail)) {
						equalMail = true;
					}
				}
			}
		}

		if (equalMail) {
			System.out.println(Mail + " ist eine bereits verwendete Mailadresse.");
			return "error";
		}

		Integer sum = zufallzahl_1 + zufallzahl_2;
		System.out.println(sum.toString() + " =? " + chsumme);

		if (!(sum.toString().equals(chsumme))) {
			return "redirect:/new_user_data";
		}

		if (Username.equals("new_user")) {
			String e_descrition = "Invalid Username.";
			System.out.println("ERROR: " + e_descrition);

			return "error";
		}

		if (userAccountManager.findByUsername(Username).isPresent()) {
			String e_descrition = "Username already in use.";
			System.out.println("ERROR: " + e_descrition);

			return "error";
		}

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

		return "redirect:/new_user_aboutuser1/user/" + userAccount.getUsername(); /* "index"; */
	}

	@RequestMapping(value = "/submit_userdata1/user/{user}", method = RequestMethod.POST)
	public String submit_userdata1(@PathVariable final String user, @RequestParam("name") final String Name,
			@RequestParam("firstname") final String Firstname, @RequestParam("Adresstyp") final String Adresstyp) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}

		System.out.println(Name);
		System.out.println(Firstname);
		System.out.println(Adresstyp);

		if (userAccountManager.findByUsername(user).isPresent()) {
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

			if (Name.isEmpty() || Firstname.isEmpty() || Adresstyp.isEmpty()) {
				return "errorpage1_empty";
			}

			System.out.println("user=" + user);

			user_xyz.getUserAccount().setLastname(Name);
			user_xyz.getUserAccount().setFirstname(Firstname);

			userAccountManager.save(user_xyz.getUserAccount());

			user_xyz.setRegistrationstate(2);
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());

			if (Adresstyp.equals("Refugees_home")) {
				return "redirect:/new_user_aboutuser2a/user/{user}";
			}

			if (Adresstyp.equals("Wohnung")) {
				return "redirect:/new_user_aboutuser2b/user/{user}";
			}

			return "redirect:/new_user_aboutuser1/user/{user}";
		}
		return "error";

	}

	@RequestMapping(value = "/submit_userdata2a/user/{user}", method = RequestMethod.POST)
	public String submit_userdata2a(@PathVariable final String user, @RequestParam("flh_name") final String Flh_name,
			@RequestParam("citypart") final String Citypart, @RequestParam("postcode") final String Postcode,
			@RequestParam("city") final String City) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}

		System.out.println(Flh_name);
		System.out.println(Citypart);
		System.out.println(Postcode);
		System.out.println(City);

		if (userAccountManager.findByUsername(user).isPresent()) {
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

			// KLASSE FLÜCHTLINGSHEIM UND INTERFACE LOCATION!!!!!!!!!!!!!!
			if (Flh_name.isEmpty() || Citypart.isEmpty() || Postcode.isEmpty() || City.isEmpty()) {
				return "errorpage2a_empty";
			}

			if (Postcode.length() != 5) {
				System.out.println("Ungültige Postleitzahl");
				return "error";
			} else {
				String[] partialRegexChecks = { ".*[a-z]+.*", // lower
						".*[A-Z]+.*", // upper
						".*[\\d]+.*", // digits
						".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
				};
				int i = 0;
				while (i < 5) {
					if (!Postcode.substring(i, i + 1).matches(partialRegexChecks[2])) {
						System.out.println("Ungültige Postleitzahl");
						return "error";
					}
					i = i + 1;
				}
			}

			Address address = new Address(Flh_name, Citypart, Postcode, City);
			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(3); // 3 ~ Flüchtlingsheim
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			return "redirect:/new_user_language_origin/user/{user}";
		}
		return "error";
	}

	@RequestMapping(value = "/submit_userdata2b/user/{user}", method = RequestMethod.POST)
	public String submit_userdata2b(@PathVariable final String user, @RequestParam("street") final String Street,
			@RequestParam("housenr") final String Housenr, @RequestParam("postcode") final String Postcode,
			@RequestParam("city") final String City) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}

		System.out.println(Street);
		System.out.println(Housenr);
		System.out.println(Postcode);
		System.out.println(City);

		if (userAccountManager.findByUsername(user).isPresent()) {
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

			if (Street.isEmpty() || Housenr.isEmpty() || Postcode.isEmpty() || City.isEmpty()) {
				return "errorpage2b_empty";
			}

			if (Postcode.length() != 5) {
				System.out.println("Ungültige Postleitzahl");
				return "error";
			} else {
				String[] partialRegexChecks = { ".*[a-z]+.*", // lower
						".*[A-Z]+.*", // upper
						".*[\\d]+.*", // digits
						".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
				};
				int i = 0;
				while (i < 5) {
					if (!Postcode.substring(i, i + 1).matches(partialRegexChecks[2])) {
						System.out.println("Ungültige Postleitzahl");
						return "error";
					}
					i = i + 1;
				}
			}

			Address address = new Address(Street, Housenr, Postcode, City);
			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(4); // 4 ~ Wohnung
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			return "redirect:/new_user_language_origin/user/{user}";
		}
		return "error";
	}

	@RequestMapping(value = "/submit_language_origin/user/{user}", method = RequestMethod.POST)
	public String submit_language_origin(@PathVariable final String user,
			@RequestParam("nativelanguage") final String Nativelanguage,
			@RequestParam("otherlanguages") final String OtherLanguages, @RequestParam("origin") final String Origin) {
		if (!userAccountManager.findByUsername(user).isPresent()) {
			return "redirect:/";
		}

		System.out.println(Nativelanguage);
		System.out.println(OtherLanguages);
		System.out.println(Origin);

		if (userAccountManager.findByUsername(user).isPresent()) {
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

			if (Nativelanguage.isEmpty() || Origin.isEmpty()) {
				return "errorpage2b_empty";
			}

			Language PreferredLanguage = languageRepository.findByName(Nativelanguage);
			if (PreferredLanguage == null)
				System.out.println("Prefl==null");
			else
				System.out.println(PreferredLanguage);

			user_xyz.setPrefLanguage(PreferredLanguage);
			System.out.println(user_xyz.getLanguages());
			userRepository.save(user_xyz);
			System.out.println("save");

			///////////////// Muss noch geÃ¤ndert werden, Spracheingabe
			if (OtherLanguages != null && !OtherLanguages.isEmpty()) {
				for (String languageName : OtherLanguages.split(",")) {
					System.out.println(languageName);
					if (languageRepository.findByName(languageName) != null) {
						// user_xyz.setLanguage(languageRepository.findByName(languageName));
						Language l1 = languageRepository.findByName(languageName);
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

			String activationkey = HelpFunctions.AktivierungskeyErzeugen(user_xyz.getUserAccount().getUsername(),
					user_xyz.getUserAccount().getEmail(), z1, z2);
			user_xyz.setActivationkey(activationkey);

			user_xyz.setRegistrationdate(new Date());
			user_xyz.setRegistrationstate(6); // 6 ~ Bereit zur Aktivierung
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: " + user_xyz.getRegistrationstate());
			return "redirect:/reCAPTCHA/user/{user}";
		}
		return "error";
	}
}

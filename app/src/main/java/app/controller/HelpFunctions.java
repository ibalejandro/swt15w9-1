package app.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
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

public class HelpFunctions {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
	private final LanguageRepository languageRepository;
	private final MailSender mailSender;

	/**
	 * Autowire.
	 * 
	 * @param Helpfunctions
	 */
	@Autowired
	public HelpFunctions(UserAccountManager userAccountManager, UserRepository userRepository,
			LanguageRepository languageRepository, MailSender mailSender) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		this.languageRepository = languageRepository;
		this.mailSender = mailSender;
	}

	public static String sha256(String base) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(base.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}

			return hexString.toString();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static boolean containsString(String s, String subString) {
		return s.indexOf(subString) > -1 ? true : false;
	}

	public static boolean emailValidator(String email) {
		boolean isValid = false;

		if (containsString(email, "@") == false) {
			return false;
		}

		if (containsString(email, ".") == false) {
			return false;
		}

		/*
		 * if (email.equals("test@test.test")) { return false; }
		 */

		try {
			//
			// Create InternetAddress object and validated the supplied
			// address which is this case is an email address.
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
		} catch (AddressException e) {
			System.out.println("You are in catch block -- Exception Occurred for: " + email);
		}
		return isValid;
	}

	public static int checkPasswordStrength(String password) {
		if (password.equals("")) {
			return 0;
		}

		boolean hasLower = false;
		boolean hasUpper = false;
		boolean hasDigits = false;
		boolean hasSymbols = false;

		float strengthPercentage = 0;
		String[] partialRegexChecks = { ".*[a-z]+.*", // lower
				".*[A-Z]+.*", // upper
				".*[\\d]+.*", // digits
				".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
		};

		int i = 0;
		while (i < (password.length())) {

			if (password.substring(i, i + 1).matches(partialRegexChecks[0])) {
				hasLower = true;
				strengthPercentage += 2;
			}
			if (password.substring(i, i + 1).matches(partialRegexChecks[1])) {
				hasUpper = true;
				strengthPercentage += 3;
			}
			if (password.substring(i, i + 1).matches(partialRegexChecks[2])) {
				hasDigits = true;
				strengthPercentage += 5;
			}
			if ((!password.substring(i, i + 1).matches(partialRegexChecks[0]))
					&& (!password.substring(i, i + 1).matches(partialRegexChecks[1]))
					&& (!password.substring(i, i + 1).matches(partialRegexChecks[2]))) {
				if (password.substring(i, i + 1).matches(partialRegexChecks[3])) {
					hasSymbols = true;
					strengthPercentage += 8;
				}
			}
			i = i + 1;
		}

		// 12 Zeichen empfohlen
		int pwlengthsec = (int) Math.floor(Math.floor(Math.log((password.length() - 6) * 0.00002)) + 11);
		strengthPercentage = strengthPercentage * (1 + (pwlengthsec / 10)) * 3.0f;

		if (!((hasLower && hasUpper) && (hasDigits && hasSymbols) && (password.length() >= 8))) {
			strengthPercentage = 0;
		}
		return Math.round(strengthPercentage);
	}

	public static void Mailsenden(String SendTo, String Subject, String Text) throws MessagingException, IOException {

		InputStream inputStream = null;

		String mailhost = "";
		String mailport = "";
		String mailusername = "";
		String mailpassword = "";

		try {
			Properties prop = new Properties();
			String propFileName = "application.properties";

			inputStream = HelpFunctions.class.getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			Date time = new Date(System.currentTimeMillis());

			// get the property value and print it out
			mailhost = prop.getProperty("spring.mail.host");
			mailport = prop.getProperty("spring.mail.port");
			mailusername = prop.getProperty("spring.mail.username");
			mailpassword = prop.getProperty("spring.mail.password");

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}

		// String mailhost = "***"; //Aus application.properties auslesen.
		// String mailusername = "***"; //Aus application.properties auslesen.
		// String mailpassword = "***"; //Aus application.properties auslesen.
		// String mailport= "***"; //Aus application.properties auslesen.
		String recipient = SendTo;

		Properties props = new Properties();

		props.put("mail.smtp.host", mailhost);
		props.put("mail.from", mailusername);
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.port", mailport);
		props.setProperty("mail.debug", "false");

		Session session = Session.getInstance(props, null);
		MimeMessage msg = new MimeMessage(session);

		msg.setRecipients(Message.RecipientType.TO, recipient);
		msg.setSubject(Subject);
		msg.setSentDate(new Date());
		msg.setContent(Text, "text/html; charset=utf-8");

		Transport transport = session.getTransport("smtp");

		transport.connect(mailusername, mailpassword);
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();

		return;

	}

	public static String AktivierungskeyErzeugen(String username, String mail, Integer Zufallszahl1,
			Integer Zufallszahl2) {
		float fl = Zufallszahl1 / Zufallszahl2;
		String starttext = HelpFunctions.sha256("s" + HelpFunctions.sha256(
				HelpFunctions.sha256("Aktivierungskey" + username + "123" + mail + "XYZ" + Float.toString(fl) + "fff")
						+ Zufallszahl1.toString())
				+ Zufallszahl2.toString());
		return HelpFunctions.sha256(
				HelpFunctions.sha256(HelpFunctions.sha256(HelpFunctions.sha256(HelpFunctions.sha256(starttext)))));
	}
}

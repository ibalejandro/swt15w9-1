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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Address;
import app.model.User;
import app.model.UserRepository;

/**
 * <h1>RestorePassword</h1> The RestorePasswordController is used for reset
 * Passwords from UserAccounts.
 * 
 *
 * @author Kilian Heret
 * 
 */

@Controller
public class RestorePassword {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;

	/**
	 * Autowire.
	 * 
	 * @param CreateNewUser
	 */
	@Autowired
	public RestorePassword(UserAccountManager userAccountManager, UserRepository userRepository) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
	}

	@RequestMapping({ "/restorePassword" })
	public String restorePassword() {
		return "/restorePassword";
	}

	@RequestMapping(value = "/submit_captcha_restorePW")
	public String recieve_reCAPTCHA() {

		return "redirect:/";

	}

	@RequestMapping(value = "/submit_captcha_restorePW", method = RequestMethod.POST)
	public String recieve_reCAPTCHA_user(@RequestParam("username") @Valid final String Username,
			@RequestParam("g-recaptcha-response") String CaptchaResponse) {

		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);

		if (CaptchaResponse.isEmpty() || (!userAccountManager.findByUsername(Username).isPresent())) {
			return "redirect:/";
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
				if (userAccountManager.findByUsername(Username).isPresent()) {
					User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(Username).get());

					if (user_xyz.getUserAccount().getEmail().isEmpty()) {
						return "redirect:/";
					}

					Date zeitstempel = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy
																							// HH:mm:ss"
					// simpleDateFormat.format(zeitstempel)

					String NewPassword = "PW:"
							+ HelpFunctions.sha256(user_xyz.getActivationkey() + zeitstempel).substring(4, 14);

					String domain = "http://localhost:8080";
					String mailtext = "<html> <head> </head> <body> <h1>Reset Password for your RefugeesApp-Account ("
							+ user_xyz.getUserAccount().getUsername() + ")<h1> Hallo "
							+ user_xyz.getUserAccount().getUsername()
							+ " </h1><br/><br/> This is your new temp Password: " + NewPassword + " </body> </html>";
					String mailadresse = user_xyz.getUserAccount().getEmail();

					// Mail senden:
					if (!mailadresse.equals("test@test.test")) {
						// Mail senden:
						try {
							HelpFunctions.Mailsenden(mailadresse, "Reset Password for your RefugeesApp-Account ("
									+ user_xyz.getUserAccount().getUsername() + ")", mailtext);
							System.out.println("Mail versandt");
						} catch (MessagingException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.out.println(NewPassword);
					}

					userAccountManager.changePassword(user_xyz.getUserAccount(), NewPassword);
					userRepository.save(user_xyz);

					System.out.println(NewPassword);

					if (!mailadresse.equals("test@test.test")) {
						return "redirect:/login";
					} else {
						return "redirect:/login";
					}

				}
				return "redirect:/";

			} else {
				return "redirect:/reCAPTCHA-TEST";
			}
		}
	}

}

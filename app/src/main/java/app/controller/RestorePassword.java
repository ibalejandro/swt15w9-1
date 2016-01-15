package app.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.mail.MessagingException;
import javax.validation.Valid;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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

			if (HelpFunctions.checkCaptcha(CaptchaResponse)) {
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
							HelpFunctions.mailSenden(mailadresse, "Reset Password for your RefugeesApp-Account ("
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

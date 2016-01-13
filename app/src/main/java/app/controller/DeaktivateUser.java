package app.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.mail.MessagingException;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.User;
import app.model.UserRepository;

/**
 * <h1>DeaktivateUser</h1> The DeaktivateUserController is used for deaktivate
 * UserAccounts.
 * 
 *
 * @author Kilian Heret
 * 
 */

@Controller
public class DeaktivateUser {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;

	/**
	 * Autowire.
	 * 
	 * @param DeaktivateUser
	 */
	@Autowired
	public DeaktivateUser(UserAccountManager userAccountManager, UserRepository userRepository) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
	}

	@RequestMapping({ "/deaktivateUser" })
	public String deaktivateUser(@LoggedIn Optional<UserAccount> userAccount, Model model) {

		System.out.println("dU");

		if (userAccount.isPresent()) {
			model.addAttribute("user", userRepository.findByUserAccount(userAccount.get()));
			return "deaktivateUser";
		} else {
			return "redirect:/";
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping({ "/modifyActivationStateByAdmin/{user}/{action}" })
	public String deaktivateUserByAdmin(@PathVariable final String user, @PathVariable final String action,
			@LoggedIn Optional<UserAccount> userAccount, Model model) {

		if (userAccount.isPresent()) {
			model.addAttribute("user", userRepository.findByUserAccount(userAccountManager.findByUsername(user).get()));
			if (action.equals("deactivate"))
				return "deaktivateUser";
			else
				return "aktivateUser";

		} else {
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/submit_deaktivateUser/{user}")
	public String submit_deaktivateUser0(@LoggedIn Optional<UserAccount> userAccount) {

		System.out.println("sdU");

		if (userAccount.isPresent()) {
			return "redirect:/deaktivateUser";
		} else {
			return "redirect:/";
		}

	}

	@RequestMapping(value = "/submit_deaktivateUser/{user}", method = RequestMethod.POST)
	public String submit_deaktivateUser(@PathVariable final String user,
			@RequestParam("deaktivate") String checkbox_deaktivate,
			@RequestParam("g-recaptcha-response") String CaptchaResponse, @LoggedIn Optional<UserAccount> userAccount,
			Model model) {

		System.out.println("## CaptchaResponse:");
		// System.out.println(CaptchaResponse);

		if (!userAccount.isPresent()) {
			return "redirect:/";
		}

		if (CaptchaResponse.isEmpty() || checkbox_deaktivate.isEmpty()) {
			return "redirect:/deaktivateUser";
		} else {
			if (!checkbox_deaktivate.equals("yes")) {
				return "redirect:/deaktivateUser";
			}

			// http://localhost:8080/create_new_user_temp?mail=aa&username=a&password=a&repassword=a

			if (HelpFunctions.checkCaptcha(CaptchaResponse)) {
				if (userAccount.isPresent()) {
					User user_xyz;
					if (userAccount.get().hasRole(new Role("ROLE_ADMIN"))) {
						user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
					} else {
						user_xyz = userRepository.findByUserAccount(userAccount.get());
					}
					if (user_xyz.getUserAccount().getEmail().isEmpty()) {
						return "redirect:/";
					}

					System.out.println("Nutzerdeaktivierung läuft ... 2");

					Date zeitstempel = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy
																							// HH:mm:ss"
					// simpleDateFormat.format(zeitstempel)

					String domain = "http://localhost:8080";
					String mailtext = "<html> <head> </head> <body> <h1>Deactivated RefugeesApp-Account ("
							+ user_xyz.getUserAccount().getUsername() + ")<h1> Hallo "
							+ user_xyz.getUserAccount().getUsername()
							+ " </h1><br/><br/> Dein Useraccount wurde deaktiviert. </body> </html>";
					String mailadresse = user_xyz.getUserAccount().getEmail();

					// Mail senden:
					if (!mailadresse.equals("test@test.test")) {
						// Mail senden:
						try {
							HelpFunctions.mailSenden(mailadresse,
									"Deactivated RefugeesApp-Account (" + user_xyz.getUserAccount().getUsername() + ")",
									mailtext);
							System.out.println("Mail versandt");
						} catch (MessagingException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					user_xyz.DeActivate();
					userAccountManager.disable(user_xyz.getUserAccount().getIdentifier());

					userRepository.save(user_xyz);

					System.out.println("Nutzer deaktiviert");
					if (userAccount.get().hasRole(new Role("ROLE_ADMIN"))) {
						model.addAttribute("user", user_xyz);
						if (user_xyz.getAddresstypString().equals("Wohnung")) {
							return "data";
						}
						return "data_refugee";
					}
					return "redirect:/login";

				}
				return "redirect:/";

			} else {
				return "redirect:/reCAPTCHA-TEST";
			}
		}
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping({ "/submit_aktivateUser/{user}" })
	public String submit_aktivateUser(@PathVariable final String user,
			@RequestParam("aktivate") String checkbox_deaktivate, @LoggedIn Optional<UserAccount> userAccount,
			Model model) {

		User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
		String domain = "http://localhost:8080";
		String mailtext = "<html> <head> </head> <body> <h1>Reactivated RefugeesApp-Account ("
				+ user_xyz.getUserAccount().getUsername() + ")<h1> Hallo " + user_xyz.getUserAccount().getUsername()
				+ " </h1><br/><br/> Dein Useraccount wurde wieder aktiviert. </body> </html>";
		String mailadresse = user_xyz.getUserAccount().getEmail();

		// Mail senden:
		if (!mailadresse.equals("test@test.test")) {
			// Mail senden:
			try {
				HelpFunctions.mailSenden(mailadresse,
						"Reactivated RefugeesApp-Account (" + user_xyz.getUserAccount().getUsername() + ")", mailtext);
				System.out.println("Mail versandt");
			} catch (MessagingException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		user_xyz.Activate();
		userAccountManager.enable(user_xyz.getUserAccount().getIdentifier());

		userRepository.save(user_xyz);

		System.out.println("Nutzer aktiviert");

		model.addAttribute("user", user_xyz);
		if (user_xyz.getAddresstypString().equals("Wohnung")) {
			return "data";
		}
		return "data_refugee";
	}

}

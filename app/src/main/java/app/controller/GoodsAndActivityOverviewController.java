package app.controller;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.UserRepository;
import app.repository.LanguageRepository;

/**
 * <h1>GoodsAndActivityOverviewConroller</h1> The
 * GoodsAndActivityOverviewConroller is used for viewing the goods and
 * activities.
 * 
 *
 * @author Kilian Heret
 * 
 */

@Controller
public class GoodsAndActivityOverviewController {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
	private final LanguageRepository languageRepository;

	/**
	 * Autowire.
	 * 
	 * @param GoodsAndActivityOverviewController
	 * @return
	 */
	@Autowired
	public GoodsAndActivityOverviewController(UserAccountManager userAccountManager, UserRepository userRepository,
			LanguageRepository languageRepository) {
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		this.languageRepository = languageRepository;
	}

	@RequestMapping(value = "/item/good", method = RequestMethod.POST)
	public String recieve_activationkey() {
		return "error";

	}

}

package app.controller;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
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

import app.model.Dialog;
import app.model.User;
import app.repository.DialogRepository;
import app.repository.UserRepository;

@Controller
@PreAuthorize("isAuthenticated()")
public class DialogController {
	private final UserRepository userRepository;
	private final DialogRepository dialogList;

	@Autowired
	public DialogController(DialogRepository dialogList, UserRepository userRepository) {
		Assert.notNull(dialogList);
		Assert.notNull(userRepository);

		this.dialogList = dialogList;
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/dialog/{id}", method = RequestMethod.GET)
	public String dialog(@PathVariable("id") Dialog dialog, Model model) {
		model.addAttribute("messages", dialogList.findAll());
		// model.addAttribute("form", form);
		return "dialog";
	}

	@RequestMapping(value = "/dialogList", method = RequestMethod.GET)
	public String dialogList(Model model,@LoggedIn Optional<UserAccount> loggedInUserAccount) {
		if (!loggedInUserAccount.isPresent()) {
			return "errorpage0_empty";
		}
		User loggedInUser = userRepository.findByUserAccount(loggedInUserAccount.get());
		
		model.addAttribute("dialogList", loggedInUser.getDialogs());
		return "dialogList";
	}

	@RequestMapping(value = "/dialogList", method = RequestMethod.POST)
	public String saveDialog(@RequestParam("otherUser") User user, @LoggedIn Optional<UserAccount> loggedInUserAccount, @RequestParam("title") String title) {
		if (!loggedInUserAccount.isPresent()) {
			return "errorpage0_empty";
		}
		User loggedInUser = userRepository.findByUserAccount(loggedInUserAccount.get());
		
		dialogList.save(new Dialog(title, loggedInUser, user));
		
		return "dialogList";
	}
}

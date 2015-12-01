package app.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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

import app.model.Dialog;
import app.model.User;
import app.model.UserRepository;
import app.repository.DialogRepository;

@Controller
@PreAuthorize("isAuthenticated()")
public class DialogController {
	private final UserRepository userRepository;
	private final UserAccountManager userAccountManager;
	private final DialogRepository dialogList;

	@Autowired
	public DialogController(DialogRepository dialogList, UserRepository userRepository,
			UserAccountManager userAccountManager) {
		Assert.notNull(dialogList);
		Assert.notNull(userRepository);
		Assert.notNull(userAccountManager);

		this.dialogList = dialogList;
		this.userRepository = userRepository;
		this.userAccountManager = userAccountManager;
	}

	@RequestMapping(value = "/dialog/{id}", method = RequestMethod.GET)
	public String dialog(@PathVariable("id") Dialog dialog, Model model) {
		model.addAttribute("messages", dialogList.findAll());
		// model.addAttribute("form", form);
		return "dialog";
	}

	@RequestMapping(value = "/dialogList", method = RequestMethod.GET)
	public String dialogList(Model model, @LoggedIn Optional<UserAccount> loggedInUserAccount) {
		if (!loggedInUserAccount.isPresent()) {
			return "errorpage0_empty";
		}
		User loggedInUser = userRepository.findByUserAccount(loggedInUserAccount.get());

		model.addAttribute("dialogList", loggedInUser.getDialogs());
		for (Dialog dialog : loggedInUser.getDialogs()) {
			System.out.println(dialog.getTitle());
		}
		return "dialogList";
	}

	@RequestMapping(value = "/startDialog", method = RequestMethod.GET)
	public String newDialog() {
		return "startDialog";
	}

	@RequestMapping(value = "/startDialog", method = RequestMethod.POST)
	public String postNewDialog(HttpServletRequest request, @LoggedIn Optional<UserAccount> loggedInUserAccount) {
		String title = request.getParameter("title");
		String participant = request.getParameter("participant");

		if (!loggedInUserAccount.isPresent()) {
			return "noUser";
		}

		User loggedInUser = userRepository.findByUserAccount(loggedInUserAccount.get());
		Optional<UserAccount> participantAccount = userAccountManager.findByUsername(participant);
		if (!participantAccount.isPresent()) {
			System.err.println("Couldn't find participant: " + participant);
			return "noUser";
		}
		User participantUser = userRepository.findByUserAccount(participantAccount.get());
		
		Dialog d = new Dialog(title, loggedInUser, participantUser);
		dialogList.save(d);
		loggedInUser.addDialog(d);
		participantUser.addDialog(d);

		return "redirect:/dialogList";
	}

	@RequestMapping(value = "/dialogList", method = RequestMethod.POST)
	public String saveDialog(@RequestParam("otherUser") User user, @LoggedIn Optional<UserAccount> loggedInUserAccount,
			@RequestParam("title") String title) {
		if (!loggedInUserAccount.isPresent()) {
			return "errorpage0_empty";
		}
		User loggedInUser = userRepository.findByUserAccount(loggedInUserAccount.get());

		dialogList.save(new Dialog(title, loggedInUser, user));

		return "dialogList";
	}
}

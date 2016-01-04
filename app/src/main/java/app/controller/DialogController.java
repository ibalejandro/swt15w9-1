package app.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Dialog;
import app.model.User;
import app.model.UserRepository;
import app.repository.DialogRepository;
import app.repository.TextBlockRepository;
import app.textblocks.TextBlock;

@Controller
@PreAuthorize("isAuthenticated()")
public class DialogController {
	private final UserRepository userRepository;
	private final UserAccountManager userAccountManager;
	private final DialogRepository dialogRepo;
	private final TextBlockRepository textBlockRepo;

	@Autowired
	public DialogController(DialogRepository dialogList, UserRepository userRepository,
			UserAccountManager userAccountManager, TextBlockRepository textBlockRepo) {
		this.dialogRepo = dialogList;
		this.userRepository = userRepository;
		this.userAccountManager = userAccountManager;
		this.textBlockRepo = textBlockRepo;
	}

	@RequestMapping(value = "/dialog", method = RequestMethod.GET)
	public String dialog(@RequestParam("id") Long id, Model model) {
		Dialog d = dialogRepo.findOne(id);

		model.addAttribute("dialog", d);
		model.addAttribute("title", d.getTitle());
		model.addAttribute("owner", d.getUserA());
		model.addAttribute("participant", d.getUserB());
		model.addAttribute("messages", d.getMessageHistory());
		
		List<String> textblockForms = new LinkedList<>();
		for (TextBlock textBlock: textBlockRepo.findAll()) {
			textblockForms.add(textBlock.asForm());
		}
		
		model.addAttribute("textblockForms", textblockForms);

		return "dialog";
	}

	@RequestMapping(value = "/dialogList", method = RequestMethod.GET)
	public String dialogList(Model model, @LoggedIn Optional<UserAccount> loggedInUserAccount) {
		if (!loggedInUserAccount.isPresent()) {
			return "errorpage0_empty";
		}
		User loggedInUser = userRepository.findByUserAccount(loggedInUserAccount.get());

		System.out.println(loggedInUser.getDialogs());
		model.addAttribute("dialogList", loggedInUser.getDialogs());
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
		Dialog savedDialog = dialogRepo.save(d);
		loggedInUser.addDialog(savedDialog);
		userRepository.save(loggedInUser);
		participantUser.addDialog(savedDialog);
		userRepository.save(participantUser);

		return "redirect:/dialogList";
	}
}

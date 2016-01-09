package app.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
		for (TextBlock textBlock : textBlockRepo.findAll()) {
			textblockForms.add(textBlock.asForm());
		}

		model.addAttribute("textblockForms", textblockForms);

		return "dialog";
	}

	@RequestMapping(value = "/dialog", method = RequestMethod.POST)
	public String dialog(@RequestParam("id") Long id, HttpServletRequest request) {
		Dialog d = dialogRepo.findOne(id);
		Enumeration<String> params = request.getParameterNames();
		List<Long> requestedBlocks = new LinkedList<>();
		Map<String, String> requestValues = new HashMap<>();

		while (params.hasMoreElements()) {
			String string = (String) params.nextElement();
			
			//TODO: remove debug print
			System.out.println(string);
			System.out.println(request.getParameter(string));
			
			if (string.endsWith("-selected")) {
				/*
				 * get all ids of the selected textblocks. These are implicitly
				 * given by the "xxx-selected" param name, where xxx represents
				 * the id. Only if the checkbox before the textblock is ticked
				 * in the form this param occurs in the params enum.
				 */
				requestedBlocks.add(Long.parseLong(string.substring(0, string.length() - 9)));
			}
		}

		Iterable<TextBlock> i = textBlockRepo.findAll(requestedBlocks);
		List<TextBlock> blocks = new LinkedList<>();
		for (TextBlock textBlock : i) {
			blocks.add(textBlock);
		}
		
		
		//ChatTemplate ct = new ChatTemplate(blocks);
		//d.addMessageElement(ct.fromForm(requestValues));

		return "redirect:/dialog?id=" + id;
	}

	@RequestMapping(value = "/dialogList", method = RequestMethod.GET)
	public String dialogList(Model model, @LoggedIn Optional<UserAccount> loggedInUserAccount) {
		if (!loggedInUserAccount.isPresent()) {
			return "errorpage0_empty";
		}
		User loggedInUser = userRepository.findByUserAccount(loggedInUserAccount.get());
		
		List<Dialog> userDialogs = new LinkedList<>();
		
		// Every Dialog in which the logged user is the owner of the Dialog
		for (Dialog dialog : dialogRepo.findByUserA(loggedInUser)) {
			userDialogs.add(dialog);
		}
		// Every Dialog in which the logged user is the participant of the Dialog
		for (Dialog dialog : dialogRepo.findByUserB(loggedInUser)) {
			userDialogs.add(dialog);
		}

		model.addAttribute("dialogList", userDialogs);
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
		dialogRepo.save(d);

		return "redirect:/dialogList";
	}
}

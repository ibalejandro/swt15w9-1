package app.controller;

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
import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.DialogRepository;
import app.repository.GoodsRepository;
import app.repository.TextBlockRepository;
import app.textblocks.ChatTemplate;
import app.textblocks.TextBlock;

@Controller
@PreAuthorize("isAuthenticated()")
public class DialogController {
	private final UserRepository userRepo;
	private final UserAccountManager userAccountManager;
	private final DialogRepository dialogRepo;
	private final TextBlockRepository textBlockRepo;
	private final GoodsRepository goodsRepo;

	@Autowired
	public DialogController(DialogRepository dialogList, UserRepository userRepository,
			UserAccountManager userAccountManager, TextBlockRepository textBlockRepo, GoodsRepository goodsRepo) {
		this.dialogRepo = dialogList;
		this.userRepo = userRepository;
		this.userAccountManager = userAccountManager;
		this.textBlockRepo = textBlockRepo;
		this.goodsRepo = goodsRepo;
	}

	@RequestMapping(value = "/dialog", method = RequestMethod.GET)
	public String dialog(@RequestParam("id") Long id, Model model) {
		Dialog d = dialogRepo.findOne(id);

		model.addAttribute("dialog", d);
		model.addAttribute("title", d.getTitle());
		model.addAttribute("owner", d.getUserA());
		model.addAttribute("participant", d.getUserB());
		model.addAttribute("messages", d.getMessageHistory());

		ChatTemplate ct = new ChatTemplate(getAllTextBlocks());

		model.addAttribute("textblockForm", ct.createForm());

		return "dialog";
	}

	@RequestMapping(value = "/dialog", method = RequestMethod.POST)
	public String dialog(@RequestParam("id") Long id, @RequestParam Map<String, String> allRequestParams) {
		ChatTemplate ct = new ChatTemplate(getAllTextBlocks());
		Dialog d = dialogRepo.findOne(id);
		d.addMessageElement(ct.fromForm(allRequestParams));
		dialogRepo.save(d);
		return "redirect:/dialog?id=" + id;
	}

	@RequestMapping(value = "/dialogList", method = RequestMethod.GET)
	public String dialogList(Model model, @LoggedIn Optional<UserAccount> loggedInUserAccount) {
		if (!loggedInUserAccount.isPresent()) {
			return "errorpage0_empty";
		}
		User loggedInUser = userRepo.findByUserAccount(loggedInUserAccount.get());

		List<Dialog> userDialogs = new LinkedList<>();

		// Every Dialog in which the logged user is the owner of the Dialog
		for (Dialog dialog : dialogRepo.findByUserA(loggedInUser)) {
			userDialogs.add(dialog);
		}
		// Every Dialog in which the logged user is the participant of the
		// Dialog
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

		User loggedInUser = userRepo.findByUserAccount(loggedInUserAccount.get());
		Optional<UserAccount> participantAccount = userAccountManager.findByUsername(participant);
		if (!participantAccount.isPresent()) {
			System.err.println("Couldn't find participant: " + participant);
			return "error";
		}
		User participantUser = userRepo.findByUserAccount(participantAccount.get());

		Dialog d = new Dialog(title, loggedInUser, participantUser);
		dialogRepo.save(d);

		return "redirect:/dialogList";
	}

	@RequestMapping(value = "/dialogByOffer", method = RequestMethod.POST)
	public String dialogByOffer(HttpServletRequest req, @LoggedIn Optional<UserAccount> loggedInUA) {
		GoodEntity g = goodsRepo.findOne(Long.parseLong(req.getParameter("goodId")));

		User owner;
		if (!loggedInUA.isPresent()) {
			return "noUser";
		} else {
			owner = userRepo.findByUserAccount(loggedInUA.get());
		}

		Dialog d = dialogRepo.save(new Dialog(g.getName(), owner, g.getUser()));

		return "redirect:/dialog?id=" + d.getId();
	}

	private List<TextBlock> getAllTextBlocks() {
		List<TextBlock> l = new LinkedList<>();
		textBlockRepo.findAll().forEach((TextBlock t) -> l.add(t));
		return l;
	}
}

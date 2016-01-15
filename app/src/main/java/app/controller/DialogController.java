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

import app.model.ActivityEntity;
import app.model.Dialog;
import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.DialogRepository;
import app.repository.GoodsRepository;
import app.repository.TextBlockRepository;
import app.textblocks.ChatTemplate;
import app.textblocks.TextBlock;
import lombok.NonNull;

@Controller
@PreAuthorize("isAuthenticated()")
public class DialogController {
	private final UserRepository userRepo;
	private final UserAccountManager userAccountManager;
	private final DialogRepository dialogRepo;
	private final TextBlockRepository textBlockRepo;
	private final GoodsRepository goodsRepo;
	private final ActivitiesRepository activitiesRepo;

	@Autowired
	public DialogController(DialogRepository dialogList, UserRepository userRepository,
			UserAccountManager userAccountManager, TextBlockRepository textBlockRepo, GoodsRepository goodsRepo,
			ActivitiesRepository activityRepo) {
		this.dialogRepo = dialogList;
		this.userRepo = userRepository;
		this.userAccountManager = userAccountManager;
		this.textBlockRepo = textBlockRepo;
		this.goodsRepo = goodsRepo;
		this.activitiesRepo = activityRepo;
	}

	@RequestMapping(value = "/dialog", method = RequestMethod.GET)
	public String dialog(@RequestParam("id") Long id, Model model) {
		Dialog d = dialogRepo.findOne(id);

		model.addAttribute("dialog", d);
		model.addAttribute("title", d.getTitle());
		model.addAttribute("owner", d.getUserA());
		model.addAttribute("participant", d.getUserB());
		model.addAttribute("chats", d.getMessageHistory());

		ChatTemplate ct = new ChatTemplate(getAllTextBlocks());

		model.addAttribute("textblockForm", ct.createForm());

		return "dialog";
	}

	@RequestMapping(value = "/dialog", method = RequestMethod.POST)
	public String dialog(@RequestParam("id") Long id, @RequestParam Map<String, String> allRequestParams,
			@LoggedIn Optional<UserAccount> loggedInUserAccount) {
		ChatTemplate ct = new ChatTemplate(getAllTextBlocks());
		Dialog d = dialogRepo.findOne(id);
		d.addMessageElement(ct.fromForm(allRequestParams, retrieveUser(loggedInUserAccount)));
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

		model.addAttribute("loggedInUser", loggedInUser);
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
		User participantUser = retrieveUser(userAccountManager.findByUsername(participant));

		if (loggedInUser.getId() == participantUser.getId()) {
			System.err.println("DialogPartner can not be DialogOwner");
			return "redirect:/dialogList";
		}

		dialogRepo.save(new Dialog(title, loggedInUser, participantUser));

		return "redirect:/dialogList";
	}

	@RequestMapping(value = "/dialogByOffer", method = RequestMethod.POST)
	public String dialogByOffer(@NonNull HttpServletRequest req, @LoggedIn Optional<UserAccount> loggedInUA) {
		String goodId = req.getParameter("id");
		String title;
		User participant;
		
		if (goodId.contains("good")) {
			GoodEntity g = goodsRepo.findOne(Long.parseLong(GoodEntity.getIdFromConstruct(goodId)));
			title = g.getName();
			participant = g.getUser();
		} else if (goodId.contains("activity")) {
			ActivityEntity a = activitiesRepo.findOne(Long.parseLong(ActivityEntity.getIdFromConstruct(goodId)));
			title = a.getName();
			participant = a.getUser();
		} else {
			throw new IllegalStateException("goodId unrecognizeable: " + goodId);
		}

		if (!loggedInUA.isPresent()) {
			return "noUser";
		}
		
		User owner = retrieveUser(loggedInUA);

		Dialog d = dialogRepo.save(new Dialog(title, owner, participant));

		return "redirect:/dialog?id=" + d.getId();
	}

	private List<TextBlock> getAllTextBlocks() {
		List<TextBlock> l = new LinkedList<>();
		textBlockRepo.findAll().forEach((TextBlock t) -> l.add(t));
		return l;
	}

	private User retrieveUser(Optional<UserAccount> ua) {
		if (!ua.isPresent()) {
			throw new IllegalArgumentException("UserAccount not present");
		}
		return userRepo.findByUserAccount(ua.get());
	}
}

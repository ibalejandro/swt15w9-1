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

/**
 * <h1>DialogController</h1> The DialogController handles all actions around
 * {@link Dialog} management.
 *
 * @author Mario Henze
 */
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

	/**
	 * Method for displaying a dialog.
	 * 
	 * @param id
	 *            of the dialog in the repository
	 * @param model
	 * @return template name
	 */
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

	/**
	 * Method for posting a new {@link Chat} element to the dialog.
	 * 
	 * @param id
	 *            of the dialog in the repository
	 * @param allRequestParams
	 *            as map
	 * @param loggedInUserAccount
	 * @return template name
	 */
	@RequestMapping(value = "/dialog", method = RequestMethod.POST)
	public String dialog(@RequestParam("id") Long id, @RequestParam Map<String, String> allRequestParams,
			@LoggedIn Optional<UserAccount> loggedInUserAccount) {
		ChatTemplate ct = new ChatTemplate(getAllTextBlocks());
		Dialog d = dialogRepo.findOne(id);
		d.addMessageElement(ct.fromForm(allRequestParams, retrieveUser(loggedInUserAccount)));
		dialogRepo.save(d);
		return "redirect:/dialog?id=" + id;
	}

	/**
	 * Method for displaying all dialogs in which the user is involved.
	 * 
	 * @param model
	 * @param loggedInUserAccount
	 * @return template name
	 */
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

	/**
	 * Method for displaying the template used to create a new {@link Dialog}.
	 * 
	 * @return template name
	 */
	@RequestMapping(value = "/startDialog", method = RequestMethod.GET)
	public String newDialog() {
		return "startDialog";
	}

	/**
	 * Method for creating a new Dialog
	 * 
	 * @param req
	 *            HTTP-Request
	 * @param loggedInUA
	 *            currently logged in {@link UserAccount}
	 * @return template name
	 */
	@RequestMapping(value = "/startDialog", method = RequestMethod.POST)
	public String postNewDialog(HttpServletRequest req, @LoggedIn Optional<UserAccount> loggedInUA) {
		String title = req.getParameter("title");
		String participant = req.getParameter("participant");

		if (!loggedInUA.isPresent()) {
			return "noUser";
		}

		User loggedInUser = userRepo.findByUserAccount(loggedInUA.get());
		User participantUser = retrieveUser(userAccountManager.findByUsername(participant));

		if (loggedInUser.getId() == participantUser.getId()) {
			System.err.println("DialogPartner can not be DialogOwner");
			return "redirect:/dialogList";
		}

		dialogRepo.save(new Dialog(title, loggedInUser, participantUser));

		return "redirect:/dialogList";
	}

	/**
	 * Method for creating a new dialog via offer.
	 * 
	 * @param req
	 *            HTTP-Request
	 * @param loggedInUA
	 * @return template name
	 */
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

	/**
	 * Helper method for retrieving all {@link TextBlock}s in the
	 * {@link TextBlockRepository}.
	 * 
	 * @return all textblocks
	 */
	private List<TextBlock> getAllTextBlocks() {
		List<TextBlock> l = new LinkedList<>();
		textBlockRepo.findAll().forEach((TextBlock t) -> l.add(t));
		return l;
	}

	/**
	 * Helper method for checking, if the {@link UserAccount} is present and
	 * retrieving it.
	 * 
	 * @param ua
	 *            UserAccount
	 * @return the User linked with the UserAccount
	 */
	private User retrieveUser(Optional<UserAccount> ua) {
		if (!ua.isPresent()) {
			throw new IllegalArgumentException("UserAccount not present");
		}
		return userRepo.findByUserAccount(ua.get());
	}
}

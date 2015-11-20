package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.UserRepository;
import app.repository.DialogRepository;

@Controller
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

	@RequestMapping(value = "/dialog", method = RequestMethod.GET)
	public String dialog(Model model) {
		model.addAttribute("messages", dialogList.findAll());
		//model.addAttribute("form", form);
		return "dialog";
	}
	
	@RequestMapping(value = "/dialogList", method = RequestMethod.GET)
	public String dialogList(Model model) {
		//FIXME: replace with actual userID!
		long userId = 1L;
		
		model.addAttribute("dialogs", dialogList.findByUserId(userId));
		return "dialogList";
	}
	
	/*
	@RequestMapping(value = "dialogList", method = RequestMethod.POST)
	public String saveDialog(HttpServletRequest request, Model model, @LoggedIn Optional<UserAccount> userAccount) {
		return "dialogList";
	}
	*/
}

package dialog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import dialog.Dialog;

@Controller
public class DialogController {
	private final Dialog messages;

	@Autowired
	public DialogController(Dialog messages) {
		Assert.notNull(messages);

		this.messages = messages;
	}

	@RequestMapping("/")
	String index() {
		return "redirect:/dialog";
	}

	@RequestMapping(value = "/dialog", method = RequestMethod.GET)
	String dialog(Model model) {
		model.addAttribute("messages", messages.findAll());
		return "dialog";
	}
}

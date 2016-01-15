package app.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.repository.TextBlockRepository;
import app.textblocks.FormatTag;
import app.textblocks.TextBlock;
import app.util.FormatStringTagFilter;

/**
 * <h1>TextblockController</h1> The TextblockController handles all actions
 * around {@link TextBlock} management.
 *
 * @author Mario Henze
 */
@Controller
@PreAuthorize("isAuthenticated()")
public class TextblockController {
	private final TextBlockRepository textBlockRepository;

	@Autowired
	public TextblockController(TextBlockRepository tbr) {
		this.textBlockRepository = tbr;
	}

	/**
	 * Method for showing a list of all {@link TextBlock}s in the
	 * {@link TextBlockRepository}.
	 * 
	 * @param model
	 * @return template name
	 */
	@RequestMapping(value = "/textBlockList", method = RequestMethod.GET)
	public String showTextBlocks(Model model) {
		Iterable<TextBlock> itb = textBlockRepository.findAll();
		List<String> tbFormatStrings = new LinkedList<>();
		List<String> tbHtmlInputStrings = new LinkedList<>();

		for (TextBlock textBlock : itb) {
			tbFormatStrings.add(textBlock.getFormatString());
			tbHtmlInputStrings.add(textBlock.asForm());
			System.out.println(textBlock.asForm());
		}

		model.addAttribute("textBlocks", tbFormatStrings);
		model.addAttribute("textBlocksHtmlInput", tbHtmlInputStrings);

		return "textBlockList";
	}

	/**
	 * Method for showing the template used to create a new {@link TextBlock}.
	 * 
	 * @return template name
	 */
	@RequestMapping(value = "/newTextBlock", method = RequestMethod.GET)
	public String showNewTextBlock() {
		return "newTextBlock";
	}

	/**
	 * Method for creating a new {@link TextBlock} with the supplied
	 * formatString.
	 * 
	 * @param request
	 *            HTTP-Request
	 * @return template name
	 */
	@RequestMapping(value = "/newTextBlock", method = RequestMethod.POST)
	public String newTextBlock(HttpServletRequest request) {
		String formatString = request.getParameter("formatString");
		List<FormatTag> tags = new FormatStringTagFilter(formatString).getTags();

		textBlockRepository.save(new TextBlock(formatString, tags));

		return "newTextBlock";
	}
}

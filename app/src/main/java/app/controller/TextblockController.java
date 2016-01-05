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

@Controller
@PreAuthorize("isAuthenticated()")
public class TextblockController {
	private final TextBlockRepository textBlockRepository;

	@Autowired
	public TextblockController(TextBlockRepository tbr) {
		this.textBlockRepository = tbr;
	}

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
	
	@RequestMapping(value = "/newTextBlock", method = RequestMethod.GET)
	public String showNewTextBlock() {
		return "newTextBlock";
	}
	
	@RequestMapping(value = "/newTextBlock", method = RequestMethod.POST)
	public String newTextBlock(HttpServletRequest request) {
		String formatString = request.getParameter("formatString");
		List<FormatTag> tags = new FormatStringTagFilter(formatString).getTags();
		
		//TODO: delete in final release!
		//System.out.println(formatString);
		
		textBlockRepository.save(new TextBlock(formatString, tags));
		
		return "newTextBlock";
	}
}

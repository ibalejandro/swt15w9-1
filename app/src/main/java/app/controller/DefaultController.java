package app.controller;

import java.util.LinkedList;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import app.model.UserRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;

@Controller
public class DefaultController {
	/*
	 * Idee für die Übersetzung: In Thymeleaf dokumenten Text eindeutige Namen
	 * geben. Durch Thymeleaf ersetzung den übersetzten text hohlen
	 * HttpServletRequest request und request.getLocale().getLanguage() die
	 * 2stellige ISO kennung holen und so die Sprache zuordnen.
	 */
	private final UserRepository userRepository;
	private final GoodsRepository goodsRepository;
	private final TagsRepository tagsRepository;
	private final LanguageRepository languageRepository;
	
	@Autowired
	public DefaultController(UserRepository userRepository, GoodsRepository goodsRepository, TagsRepository tagsRepository, LanguageRepository languageRepository){
		this.userRepository = userRepository;
		this.goodsRepository = goodsRepository;
		this.tagsRepository = tagsRepository;
		this.languageRepository = languageRepository;
	}

	/**
	 * This method is the answer for the request to '/index' or '/' and
	 * redirects to the main page (index template).
	 */
	@RequestMapping({ "/", "/index" })
	String index(HttpServletRequest request, Model modelMap) {
		// translation(modelMap);
		modelMap.addAttribute("result", goodsRepository.findAll());
		modelMap.addAttribute("languages", languageRepository.findAll());

		ListCountry a = new ListCountry();
		LinkedList<String> L = a.getCountryList(Locale.ENGLISH);
		
		modelMap.addAttribute("countrys", L);

		return "index";
	}

	// private void translation(ModelMap modelMap) {
	// modelMap.addAttribute(", attributeValue)
	//
	// }

}

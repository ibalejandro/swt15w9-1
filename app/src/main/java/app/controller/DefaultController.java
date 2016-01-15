package app.controller;


import java.util.List;
import java.util.Locale;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.InterfaceRepository;
import app.repository.LanguageRepository;
import app.repository.ModuleRepository;
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
	private final ModuleRepository moduleRepository;
	private final InterfaceRepository interfaceRepository;
	private final LanguageRepository languageRepository;
	private final ActivitiesRepository activitiesRepository;
	
	@Autowired
	public DefaultController(UserRepository userRepository,
			GoodsRepository goodsRepository,
			TagsRepository tagsRepository, 
			ModuleRepository moduleRepository, 
			InterfaceRepository interfaceRepository,
			LanguageRepository languageRepository,
	        ActivitiesRepository activitiesRepository) {
		this.userRepository = userRepository;
		this.goodsRepository = goodsRepository;
		this.tagsRepository = tagsRepository;
		this.moduleRepository = moduleRepository;
		this.interfaceRepository = interfaceRepository;
		this.languageRepository = languageRepository;
		this.activitiesRepository = activitiesRepository;
	}

	/**
	 * This method is the answer for the request to '/index' or '/' and
	 * redirects to the main page (index template).
	 */
	@RequestMapping({ "/", "/index" })
	String index(HttpServletRequest request, Model modelMap) {
		modelMap.addAttribute("resultGoods", goodsRepository.findAll());
		modelMap.addAttribute("resultActivities", activitiesRepository.findAll());
		modelMap.addAttribute("languages", languageRepository.findAll());
		
		ListCountry a = new ListCountry();
		List<String> L = a.getCountryList(Locale.ENGLISH);
		
		modelMap.addAttribute("countrys", L);
		
		return "index";
	}
}

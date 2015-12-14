package app.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;
import app.util.Tuple;

@Controller
public class DefaultController {
	/*
	 *	Idee für die Übersetzung:
	 *		In Thymeleaf dokumenten Text eindeutige Namen geben.
	 *		Durch Thymeleaf ersetzung den übersetzten text hohlen
	 *		HttpServletRequest request und request.getLocale().getLanguage() die 2stellige ISO kennung holen
	 *		und so die Sprache zuordnen.
	 */
	private final UserRepository userRepository;
	private final GoodsRepository goodsRepository;
	private final TagsRepository tagsRepository;
	
	@Autowired
	public DefaultController(UserRepository userRepository, GoodsRepository goodsRepository, TagsRepository tagsRepository){
		this.userRepository = userRepository;
		this.goodsRepository = goodsRepository;
		this.tagsRepository = tagsRepository;
	}
	
	/**
	   * This method is the answer for the request to '/index' or '/' and redirects to the main page (index template).
	   */
	@RequestMapping({"/","/index"})
	String index(HttpServletRequest request, ModelMap modelMap){
//		translation(modelMap);
		modelMap.addAttribute("result", goodsRepository.findAll());
		return "index";
	}

//	private void translation(ModelMap modelMap) {
//		modelMap.addAttribute(", attributeValue)
//		
//	}
	

}

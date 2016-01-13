package app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.InterfacePart;
import app.model.Language;
import app.model.Module;
import app.model.UserRepository;
import app.repository.GoodsRepository;
import app.repository.InterfaceRepository;
import app.repository.LanguageRepository;
import app.repository.ModuleRepository;
import app.repository.TagsRepository;
import app.util.Tuple;

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

	@Autowired
	public DefaultController(UserRepository userRepository,
			GoodsRepository goodsRepository,
			TagsRepository tagsRepository, 
			ModuleRepository moduleRepository, 
			InterfaceRepository interfaceRepository,
			LanguageRepository languageRepository) {
		this.userRepository = userRepository;
		this.goodsRepository = goodsRepository;
		this.tagsRepository = tagsRepository;
		this.moduleRepository = moduleRepository;
		this.interfaceRepository = interfaceRepository;
		this.languageRepository = languageRepository;
	}

	/**
	 * This method is the answer for the request to '/index' or '/' and
	 * redirects to the main page (index template).
	 */
	@RequestMapping({ "/", "/index" })
	String index(HttpServletRequest request, Model modelMap) {
		modelMap.addAttribute("result", goodsRepository.findAll());

//		Language lan = languageRepository.findByKennung(request.getLocale().getLanguage());
//		if (lan == null) {
//			lan = languageRepository.findByKennung("de");
//		}
//		
//		if (lan != null) {
//			List<InterfacePart> inPLan = interfaceRepository.findByLanguageId(lan.getId());
//
//			for (InterfacePart iP : inPLan) {
//				if (moduleRepository.findOne(iP.getModuleId()) != null) {
//					modelMap.addAttribute(moduleRepository.findOne(iP.getModuleId()).getThymeLeafName(), iP);
//					System.out.println(
//							moduleRepository.findOne(iP.getModuleId()).getThymeLeafName() + ", " + iP.getText());
//				}
//			}
//		}
		return "index";
	}
}

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

		Language lan = languageRepository.findByKennung(request.getLocale().getLanguage());
		if (lan == null) {
			lan = languageRepository.findByKennung("de");
		}
		
		if (lan != null) {
			List<InterfacePart> inPLan = interfaceRepository.findByLanguageId(lan.getId());

			for (InterfacePart iP : inPLan) {
				if (moduleRepository.findOne(iP.getModuleId()) != null) {
					modelMap.addAttribute(moduleRepository.findOne(iP.getModuleId()).getTyhmeLeafName(), iP);
					System.out.println(
							moduleRepository.findOne(iP.getModuleId()).getTyhmeLeafName() + ", " + iP.getText());
				}
			}
		}
		return "index";
	}

	@RequestMapping("/interface")
	String interfaceMaping(HttpServletRequest request, Model model) {

		List<String> sprachen = new ArrayList<String>();
		List<String> template = new ArrayList<String>();

		Iterable<Language> allLang = languageRepository.findAll();
		Iterable<Module> allModules = moduleRepository.findAll();

		for (Language lang : allLang) {
			if (!sprachen.contains(lang.getName())) {
				sprachen.add(lang.getName());
			}
		}

		for (Module mod : allModules) {
			if (!template.contains(mod.getTemplateName())) {
				template.add(mod.getTemplateName());
			}
		}
		model.addAttribute("template", template);
		model.addAttribute("language", sprachen);

		return "template_translation_manager";
	}

	@RequestMapping(value = "/changeTemplate/{template}") // , method =
															// RequestMethod.POST)
	public String changeTemplate(HttpServletRequest request, Model model,
			@PathVariable("template") String templatename) {
		String refSprache = "";// request.getParameter("refSprache");
		String changeSprache = "de";// request.getParameter("changeSprache");
		List<Module> mods = moduleRepository.findByTemplateName(templatename);
		List<Tuple<Module, Tuple<String, String>>> forMap = new ArrayList<Tuple<Module, Tuple<String, String>>>();

		long changeLangID = -1, refLangID = -1;
		boolean refSpracheBool = true;
		if (refSprache == null || languageRepository.findByKennung(refSprache) == null) {
			refSprache = "";
			refSpracheBool = false;
		} else {
			refLangID = languageRepository.findByKennung(refSprache).getId();
		}

		if (changeSprache == null || languageRepository.findByKennung(changeSprache) == null) {
			changeSprache = "de";
		}
		changeLangID = languageRepository.findByKennung(changeSprache).getId();

		for (Module mod : mods) {
			Tuple<Module, Tuple<String, String>> t;
			Tuple<String, String> sprache;

			sprache = new Tuple<String, String>(
					interfaceRepository.findByLanguageIdAndModuleId(changeLangID, mod.getId()).getText(), "");

			if (refSpracheBool) {
				sprache.set2(interfaceRepository.findByLanguageIdAndModuleId(refLangID, mod.getId()).getText());
			}

			t = new Tuple<Module, Tuple<String, String>>(mod, sprache);
			forMap.add(t);
		}

		model.addAttribute("refSprache", refSpracheBool);
		model.addAttribute("result", forMap);
		model.addAttribute("template", templatename);
		return "change_template";
	}

	/*
	 * TODO:	- änderrungen einlesen 
	 * 			- zuändernde Sprachen einlesen 
	 * 			- komplett sprachen ändern 
	 * 			- templates und alle verweise löschen 
	 * 				(Erst die InterfaceParts an der mittels ModuleID 
	 * 				finden und löschen Daraufhin das Module löschen) 
	 * 			- templates neu erstellen 
	 * 				(Module erstellen und dann auf change Template verweisen) 
	 * 			- sprachen neu erstellen 
	 * 				(neue Sprachen ins LanguageRepository eintragen und neue 
	 * 				InterfaceParts erstellen) 
	 * 			- Daten für Bisherige Templates erstellen und Templates anpassen
	 */
	/*
	 * Dieses Muss noch weiter angepasst werden, sowie das Template
	 */
	@RequestMapping(value = "/change_template_submit/{template}", method = RequestMethod.POST)
	public String changeTemplateSubmit(HttpServletRequest request, Model model,
			@PathVariable("template") String templatename) {
		String textValue = request.getParameter("Value");
		String thymeleafValue = request.getParameter("tymeleafValue");
		String[] texte = textValue.split(",");
		String[] thymeleaf = thymeleafValue.split(",");

		if (texte.length == thymeleaf.length) {

			model.addAttribute("refSprache", false);
			// model.addAttribute("result", forMap);
			model.addAttribute("template", templatename);
			return "change_template";
		}

		return "";
	}

}

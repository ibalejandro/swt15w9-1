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
					modelMap.addAttribute(moduleRepository.findOne(iP.getModuleId()).getThymeLeafName(), iP);
					System.out.println(
							moduleRepository.findOne(iP.getModuleId()).getThymeLeafName() + ", " + iP.getText());
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

	@RequestMapping(value = "/changeTemplate/", method = RequestMethod.POST)
	public String changeTemplate(HttpServletRequest request, Model model) {
		String templatename= request.getParameter("temp");
		String refSprache = request.getParameter("refSprache");
		String changeSprache = request.getParameter("changeSprache");
		List<Module> mods = moduleRepository.findByTemplateName(templatename);
		List<Tuple<Module, Tuple<String, String>>> forMap = new ArrayList<Tuple<Module, Tuple<String, String>>>();

		long changeLangID = -1, refLangID = -1;
		boolean refSpracheBool = true;
		if (refSprache == null || languageRepository.findByName(refSprache) == null) {
			refSprache = "";
			refSpracheBool = false;
		} else {
			refLangID = languageRepository.findByName(refSprache).getId();
		}

		if (changeSprache == null || languageRepository.findByName(changeSprache) == null) {
			changeSprache = "Deutsch";
		}
		changeLangID = languageRepository.findByName(changeSprache).getId();

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
		model.addAttribute("changeSprache", changeSprache);
		model.addAttribute("refSprache", refSpracheBool);
		model.addAttribute("result", forMap);
		model.addAttribute("template", templatename);
		return "change_template";
	}

	/*
	 * TODO:	- änderrungen einlesen <- fertig
	 * 			- zuändernde Sprachen einlesen <- fertig
	 * 			- komplett sprachen ändern 
	 * 			- templates und alle verweise löschen  <- fertig
	 * 				(Erst die InterfaceParts an der mittels ModuleID 
	 * 				finden und löschen Daraufhin das Module löschen) 
	 * 			- templates neu erstellen
	 * 				(Module erstellen und dann auf change Template verweisen) 
	 * 			- sprachen neu erstellen 
	 * 				(neue Sprachen ins LanguageRepository eintragen und neue 
	 * 				InterfaceParts erstellen) 
	 * 			- Daten für Bisherige Templates erstellen und Templates anpassen
	 */
	@RequestMapping(value = "/change_template_submit/{template}", method = RequestMethod.POST)
	public String changeTemplateSubmit(HttpServletRequest request, Model model,
			@PathVariable("template") String templatename) {
		//String temp = request.getParameter("temp");
		String changeSprache = request.getParameter("changeSprache");
		ArrayList<String> textValue = new ArrayList<String>();
		ArrayList<String> thymeLeafValue = new ArrayList<String>();
		ArrayList<String> thymeLeafId = new ArrayList<String>();
		for(int i=1; i<1000; i++){
			if(request.getParameter("Value"+i)==null){
				break;
			}
			textValue.add(request.getParameter("Value"+i));
		}
		for(int i=1; i<1000; i++){
			if(request.getParameter("thymeLeafValue"+i)==null){
				break;
			}
			thymeLeafValue.add(request.getParameter("thymeLeafValue"+i));
			thymeLeafId.add(request.getParameter("thymeLeafId"+i));
		}
		System.out.println(changeSprache);
		String[] texte = new String[textValue.size()];
		texte = textValue.toArray(texte);
		String[] thymeLeaf = new String[thymeLeafValue.size()];
		thymeLeaf = thymeLeafValue.toArray(thymeLeaf);
		String[] tLId = new String[thymeLeafId.size()];
		tLId = thymeLeafId.toArray(tLId);
		if(texte.length==thymeLeaf.length){
			for(int i=0;i<texte.length;i++){
				System.out.println(i+". textValue: "+texte[i]);
				System.out.println(i+". thymeLeafId: "+tLId[i]);
				System.out.println(i+". thymeLeafValue: "+thymeLeaf[i]);
				Module updatedMod = moduleRepository.findByTemplateNameAndThymeLeafName(moduleRepository.findById(Long.parseLong(tLId[i])).getTemplateName(), moduleRepository.findById(Long.parseLong(tLId[i])).getThymeLeafName());
				updatedMod.setThymeLeafName(thymeLeaf[i]);
				InterfacePart updatedInt = interfaceRepository.findByLanguageIdAndModuleId(languageRepository.findByName(changeSprache).getId(), updatedMod.getId());
				updatedInt.setText(texte[i]);
				interfaceRepository.save(updatedInt);
				moduleRepository.save(updatedMod);
			}
				
		}
		return interfaceMaping(request, model);
	}
	
	@RequestMapping(value = "/deleteTemplate/", method = RequestMethod.POST)
	public String deleteTemplate(HttpServletRequest request, Model model) {
		
		String temp = request.getParameter("temp");
		List<Module> modules = moduleRepository.findByTemplateName(temp);
		ArrayList<String> mods = new ArrayList<String>();
		for (Module mod : modules){
			mods.add(mod.getThymeLeafName());
		}
		model.addAttribute("temp", temp);
		model.addAttribute("mods", mods);
		return "delete_template";
	}
	
	@RequestMapping(value = "/delete_template_submit/", method = RequestMethod.POST)
	public String deleteTemplateSubmit(HttpServletRequest request, Model model) {
		
		String temp = request.getParameter("temp");
		String module = request.getParameter("module");
		
		Module deletedMod = moduleRepository.findByTemplateNameAndThymeLeafName(temp, module);
		for(InterfacePart deletedinterface : interfaceRepository.findByModuleId(deletedMod.getId())){
			interfaceRepository.delete(deletedinterface);
		}
		moduleRepository.delete(deletedMod);
		
		List<Module> modules = moduleRepository.findByTemplateName(temp);
		ArrayList<String> mods = new ArrayList<String>();
		for (Module mod : modules){
			mods.add(mod.getThymeLeafName());
		}
		
		model.addAttribute("temp", temp);
		model.addAttribute("mods", mods);
		
		return "delete_template";
	}

	@RequestMapping(value = "/newModule/", method = RequestMethod.POST)
	public String newModuleSubmit(HttpServletRequest request, Model model) {
		String name = request.getParameter("module");
		String templ = request.getParameter("templ");
		
		Module newmod = new Module(templ, name);
		moduleRepository.save(newmod);
		
		for(Language lang : languageRepository.findAll()){
			interfaceRepository.save(new InterfacePart("---", lang.getId(), newmod.getId()));
		}
		
		
		
		return interfaceMaping(request, model);
	}
	
	@RequestMapping(value = "/newTemplate/", method = RequestMethod.POST)
	public String newTemplateSubmit(HttpServletRequest request, Model model) {
		String templ = request.getParameter("templ");
		String modl = request.getParameter("modl");
		Module newmod = new Module(templ,modl);
		moduleRepository.save(newmod);
		for(Language lang : languageRepository.findAll()){
			interfaceRepository.save(new InterfacePart("---", lang.getId(), newmod.getId()));
		}
		
		return interfaceMaping(request, model);
	}
}

package app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.InterfacePart;
import app.model.Language;
import app.model.Module;
import app.model.User;
import app.model.UserRepository;
import app.repository.LanguageRepository;
import app.repository.InterfaceRepository;
import app.repository.ModuleRepository;
import app.util.Tuple;

/**
* <h1>AdminManagementController</h1>
* The AdminManagementController is used to modify UserAccounts or User with administrative rights.
* 
*
* @author Friederike Kitzing
* 
*/

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

private final UserRepository userRepository;
private final UserAccountManager userAccountManager;
private final LanguageRepository languageRepository;
private final InterfaceRepository interfaceRepository;
private final ModuleRepository moduleRepository;

	/**
	 * Autowire.
	 * @param userRepository The repository for the users
	 */
	@Autowired
	public AdminController(UserRepository userRepository, UserAccountManager userAccountManager, LanguageRepository languageRepository, InterfaceRepository interfaceRepository, ModuleRepository moduleRepository){
		this.userRepository=userRepository;
		this.userAccountManager=userAccountManager;
		this.languageRepository= languageRepository;
		this.interfaceRepository = interfaceRepository;
		this.moduleRepository = moduleRepository;
	}
	/**
	   * This method is the answer for the request to '/userDetails'. It finds
	   * and retrieves all the user in the UserRepository.
	   * @param ModelMap The modelmap to add the user
	   * @return String The name of the view to be shown after processing
	   */
	@RequestMapping("/userDetails")
	String userDetails(ModelMap map) {
		map.addAttribute("userDetails", userRepository.findAll());
		return "userDetails";
	}

	@RequestMapping("/modify/user/{user}")
	public String modify(@PathVariable final String user, Model model){
		
		model.addAttribute("user",userRepository.findByUserAccount(userAccountManager.findByUsername(user).get()));
		System.out.println(model.toString());
		return "modify";
	}
	
	@RequestMapping(value="/searchUser", method = RequestMethod.POST)
	public String searchUser(@RequestParam(value="userNameIN") final String UserName, Model model){
		User user_xyz=userRepository.findByUserAccount(userAccountManager.findByUsername(UserName).get());
		model.addAttribute("user", user_xyz);
		return "data";
		
	}
	@RequestMapping(value="/addLanguage")
	public String addLanguage_submit(){
		return "addLanguage";
	}
	
	
	@RequestMapping(value="/addLanguage_submit", method = RequestMethod.POST)
	public String addLanguage_submit(@RequestParam(value="languageName") final String LanguageName, @RequestParam(value="kennung") final String Kennung){
		if(languageRepository.findByName(LanguageName)==null){
			Language newLanguage= new Language(LanguageName, Kennung);
			languageRepository.save(newLanguage);
		}
		return "index";
		
	}
	
	@RequestMapping(value="/modify_submit/user/{user}", method = RequestMethod.POST)//user/{user}
	public String modify_submit(@PathVariable final String user, 
			@RequestParam(value="mailIN") final Optional<String>  Mail, 
			@RequestParam(value="nameIN") final Optional<String> Name, 
			@RequestParam(value="firstnameIN") final Optional<String> Firstname, 
			@RequestParam(value="wohnen") final String Adresstyp, 
			@RequestParam(value="flh_name") final Optional<String> Flh_name_OPT, 
			@RequestParam(value="citypart") final Optional<String> Citypart_OPT, 
			@RequestParam(value="street") final Optional<String> Street_OPT, 
			@RequestParam(value="housenr") final Optional<String> Housenr_OPT, 
			@RequestParam(value="postcode_R") final Optional<String> Postcode_R, 
			@RequestParam(value="city_R") final Optional<String> City_R, 
			@RequestParam(value="postcode_H") final Optional<String> Postcode_H, 
			@RequestParam(value="city_H") final Optional<String> City_H,
			@RequestParam(value="nativelanguage") final Optional<String> Nativelanguage, 
			@RequestParam(value="otherlanguages") final Optional<String> OtherLanguages, 
			@RequestParam(value="origin") final Optional<String> Origin)
	{
		User user_xyz=userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
		if(Mail.isPresent()&& !Mail.get().isEmpty()) user_xyz.getUserAccount().setEmail(Mail.get());
		System.out.println(user_xyz.getUserAccount().getEmail());
		if(Name.isPresent()&& !Name.get().isEmpty()) user_xyz.getUserAccount().setLastname(Name.get());
		System.out.println(user_xyz.getUserAccount().getLastname());
		if(Firstname.isPresent()&& !Firstname.get().isEmpty()) user_xyz.getUserAccount().setFirstname(Firstname.get());
		System.out.println(user_xyz.getUserAccount().getFirstname());
		
		if(!Adresstyp.equals(user_xyz.getAdresstyp()))user_xyz.setAdresstyp(Adresstyp);
		if(Adresstyp.equals("refugee")){
			System.out.println("refugee");
			if (Flh_name_OPT.isPresent()&& !Flh_name_OPT.get().isEmpty())user_xyz.getLocation().setStreet(Flh_name_OPT.get());			
			if ((Postcode_R.isPresent()&& !Postcode_R.get().isEmpty()) && (Postcode_R.get().matches("[0-9]{5}")))user_xyz.getLocation().setZipCode(Postcode_R.get());				
			if (City_R.isPresent()&& !City_R.get().isEmpty())user_xyz.getLocation().setCity(City_R.get());
			if (Citypart_OPT.isPresent()&& !Citypart_OPT.get().isEmpty())user_xyz.getLocation().setHousenr(Citypart_OPT.get());
		}else{
			System.out.println("helper");
			if (Street_OPT.isPresent()&& !Street_OPT.get().isEmpty())user_xyz.getLocation().setStreet(Street_OPT.get());	
			if (Housenr_OPT.isPresent()&& !Housenr_OPT.get().isEmpty())user_xyz.getLocation().setHousenr(Housenr_OPT.get());		
			if ((Postcode_H.isPresent()) && !Postcode_H.get().isEmpty() && (Postcode_H.get().matches("[0-9]{5}")))user_xyz.getLocation().setZipCode(Postcode_H.get());				
			if (City_H.isPresent()&& !City_H.get().isEmpty())user_xyz.getLocation().setCity(City_H.get());			
		}
		if(Nativelanguage.isPresent()&& !Nativelanguage.get().isEmpty()){
			System.out.println("modify language");
			Language PreferredLanguage= languageRepository.findByName(Nativelanguage.get());
			user_xyz.setPrefLanguage(PreferredLanguage);
		}
		if(OtherLanguages.isPresent() && !OtherLanguages.get().isEmpty()){
			System.out.println("modify languages");
			user_xyz.removeAllLanguages();
			if(OtherLanguages.get()!=null && !OtherLanguages.get().isEmpty()){
				for(String languageName:OtherLanguages.get().split(",")){
					System.out.println(languageName);
					if(languageRepository.findByName(languageName)!=null){
						//user_xyz.setLanguage(languageRepository.findByName(languageName));
						Language l1=languageRepository.findByName(languageName);
						System.out.println(l1.toString());
						user_xyz.setLanguage(l1);
						userRepository.save(user_xyz);					
					}
					System.out.println(user_xyz.getLanguages());
				}
			}
		}
		System.out.println("origin?");
		if(Origin.isPresent())user_xyz.setOrigin(Origin.get());
		userRepository.save(user_xyz);
		System.out.println("saved");
		return "redirect:/userDetails";
		
	}
	
	// Interface-Übersetzung
	
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
	 * 			- komplett sprachen ändern (wenn noch Zeit ist)
	 * 			- templates und alle verweise löschen  <- fertig
	 * 				(Erst die InterfaceParts an der mittels ModuleID 
	 * 				finden und löschen Daraufhin das Module löschen) 
	 * 			- templates neu erstellen <- fertig
	 * 				(Module erstellen und dann auf change Template verweisen) 
	 * 			- sprachen neu erstellen <- fertig
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
	
	@RequestMapping(value = "/newLanguage/", method = RequestMethod.POST)
	public String newLanguageSubmit(HttpServletRequest request, Model model) {
		
		String newLang = request.getParameter("newLang");
		String newLangShort = request.getParameter("newLangShort");
		
		Language nLang = new Language(newLang, newLangShort);
		languageRepository.save(nLang);
		
		
		for(Module module : moduleRepository.findAll()){
			interfaceRepository.save(new InterfacePart(interfaceRepository.findByLanguageIdAndModuleId(1L, module.getId()).getText()+" -- Auf "+nLang.getName(),languageRepository.findByName(newLang).getId(), module.getId())); 
		}
		
		return interfaceMaping(request, model);
	}
	
}

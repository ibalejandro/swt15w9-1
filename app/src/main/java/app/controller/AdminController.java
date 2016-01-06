package app.controller;

import java.util.Optional;

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

import app.model.Language;
import app.model.User;
import app.model.User.AddresstypEnum;
import app.model.UserRepository;
import app.repository.LanguageRepository;

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

	/**
	 * Autowire.
	 * @param userRepository The repository for the users
	 */
	@Autowired
	public AdminController(UserRepository userRepository, UserAccountManager userAccountManager, LanguageRepository languageRepository){
		this.userRepository=userRepository;
		this.userAccountManager=userAccountManager;
		this.languageRepository= languageRepository;
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
		model.addAttribute("languages", languageRepository.findAll());
		System.out.println(model.toString());
		return "modify";
	}
	
	@RequestMapping(value="/searchUser", method = RequestMethod.POST)
	public String searchUser(@RequestParam(value="userNameIN") final String UserName, Model model){
		User user_xyz=userRepository.findByUserAccount(userAccountManager.findByUsername(UserName).get());
		model.addAttribute("user", user_xyz);
		if(user_xyz.getAddresstypString().equals("Wohnung")){
			return "data";
		}
		return "data_refugee";
		
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
			@RequestParam(value="nativelanguage") final String Nativelanguage, 
			@RequestParam(value="otherlanguages") final String OtherLanguages, 
			@RequestParam(value="origin") final Optional<String> Origin)
	{
		User user_xyz=userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
		if(Mail.isPresent()&& !Mail.get().isEmpty()) user_xyz.getUserAccount().setEmail(Mail.get());
		System.out.println(user_xyz.getUserAccount().getEmail());
		if(Name.isPresent()&& !Name.get().isEmpty()) user_xyz.getUserAccount().setLastname(Name.get());
		System.out.println(user_xyz.getUserAccount().getLastname());
		if(Firstname.isPresent()&& !Firstname.get().isEmpty()) user_xyz.getUserAccount().setFirstname(Firstname.get());
		System.out.println(user_xyz.getUserAccount().getFirstname());
		
		if(Adresstyp.equals("refugee")){
			System.out.println("refugee");
			user_xyz.getLocation().setStreet("");
			user_xyz.getLocation().setHousenr("");
			user_xyz.setAddresstyp(AddresstypEnum.Refugees_home);
			if (Flh_name_OPT.isPresent()&& !Flh_name_OPT.get().isEmpty()){
				user_xyz.getLocation().setFlh_name(Flh_name_OPT.get());			
			}
			if ((Postcode_R.isPresent()&& !Postcode_R.get().isEmpty()) && (Postcode_R.get().matches("[0-9]{5}"))){
				user_xyz.getLocation().setZipCode(Postcode_R.get());				
			}
			if (City_R.isPresent()&& !City_R.get().isEmpty()){
				user_xyz.getLocation().setCity(City_R.get());
			}
			if (Citypart_OPT.isPresent()&& !Citypart_OPT.get().isEmpty()){
				user_xyz.getLocation().setCityPart(Citypart_OPT.get());
			}
		}else{
			System.out.println("helper");
			if (Street_OPT.isPresent()&& !Street_OPT.get().isEmpty()){
				user_xyz.getLocation().setStreet(Street_OPT.get());	
				user_xyz.setAddresstyp(AddresstypEnum.Wohnung);
				userRepository.save(user_xyz);
			}
			if (Housenr_OPT.isPresent()&& !Housenr_OPT.get().isEmpty()){
				user_xyz.getLocation().setHousenr(Housenr_OPT.get());
				user_xyz.setAddresstyp(AddresstypEnum.Wohnung);
				userRepository.save(user_xyz);
			}
			if ((Postcode_H.isPresent()) && !Postcode_H.get().isEmpty() && (Postcode_H.get().matches("[0-9]{5}"))){
				user_xyz.getLocation().setZipCode(Postcode_H.get());				
			}
			if (City_H.isPresent()&& !City_H.get().isEmpty()){
				user_xyz.getLocation().setCity(City_H.get());			
			}
			if(user_xyz.getAddresstypString().equals("Wohnung")){
				user_xyz.getLocation().setFlh_name("");
				user_xyz.getLocation().setCityPart("");
			}
		}
		if( !Nativelanguage.isEmpty()){
			System.out.println("modify language");
			user_xyz.removeLanguage(user_xyz.getPrefLanguage());
			Language PreferredLanguage=languageRepository.findByKennung(Nativelanguage);
			user_xyz.setPrefLanguage(PreferredLanguage);
		}
		if(!OtherLanguages.isEmpty()&& !OtherLanguages.toString().equals("-1")){
			System.out.println("modify languages");
			System.out.println(OtherLanguages);
			System.out.println(OtherLanguages.toString());
			Boolean nichtleer=false;
			for(String test:OtherLanguages.split(",")){
				if(!test.equals("-1")){
					nichtleer=true;
				}
			}
			if(nichtleer){
				user_xyz.removeAllLanguages();
			}
			if(OtherLanguages!=null && !OtherLanguages.isEmpty()){
				for(String languageName:OtherLanguages.split(",")){
					System.out.println(languageName);
					if(languageRepository.findByKennung(languageName)!=null){
						//user_xyz.setLanguage(languageRepository.findByName(languageName));
						Language l1=languageRepository.findByKennung(languageName);
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
}

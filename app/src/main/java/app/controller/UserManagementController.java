package app.controller;

import java.util.Optional;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Language;
import app.model.User;
import app.model.UserRepository;
import app.repository.LanguageRepository;

/**
* <h1>UserManagementController</h1>
* The UserManagementController is used to show all or a certain registered User.
* 
*
* @author Friederike Kitzing
* 
*/
@Controller
public class UserManagementController {
	
	private final UserRepository userRepository;
	private final UserAccountManager userAccountManager;
	private final LanguageRepository languageRepository;
	
	/**
   * Autowire.
   * @param userRepository The repository for the users
   */
	@Autowired
	public UserManagementController(UserRepository userRepository, UserAccountManager userAccountManager,LanguageRepository languageRepository) {
		this.userRepository = userRepository;
		this.userAccountManager=userAccountManager;
		this.languageRepository=languageRepository;
	}


	
	/*
	@RequestMapping("/admin")
	String userDetails(ModelMap map){
		map.addAttribute("users", userRepository.findAll());
		//map.addAttribute("offers",GoodsRepository.findAll());
		return "admin";
	}
	*/
	
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

	/**
	   * This method is the answer for the request to '/data'. It finds
	   * the user that is connected to the logged-in UserAccount in the 
	   * UserRepository. Is UserAccount found the method redirects to 'noUser'.
	   * @param Model The model to add the present User
	   * @return String The name of the view to be shown after processing
	   */
		@RequestMapping("/data")
		String data(Model model,  @LoggedIn Optional<UserAccount> userAccount) {
			if(userAccount.isPresent()){
				User LoggUser=userRepository.findByUserAccount(userAccount.get());
				model.addAttribute("user", LoggUser);
				return "data";
			}
			
			return "noUser";
		}
	
		@RequestMapping("/changePassword/{user}")
		public String changePassword(Model model,  @LoggedIn Optional<UserAccount> userAccount){
			if(userAccount.isPresent())model.addAttribute("userAccount",userAccount.get());
			return "changePassword";
		}
		
		@RequestMapping(value="/changePassword_submit/{user}", method = RequestMethod.POST)
		public String changePassword_submit(@LoggedIn Optional<UserAccount> userAccount,  @RequestParam("actualPassword") final String ActualPassword, @RequestParam("newPassword1") final String NewPassword1,@RequestParam("newPassword2") final String NewPassword2){
			if(!userAccount.isPresent())return "noUser";
			User user_xyz=userRepository.findByUserAccount(userAccount.get());
			
			if(ActualPassword!=null /*&& user_xyz.getUserAccount().getPassword().equals(new Password(oldPW))*/){
				if(NewPassword1.equals(NewPassword2) && validate(NewPassword1))userAccountManager.changePassword(user_xyz.getUserAccount(), NewPassword1);;
			}else{
				return "redirect:/";
			}
			userRepository.save(user_xyz);
			userAccountManager.save(user_xyz.getUserAccount());
			
			System.out.println("Passwort geändert");

			return "redirect:/data";
		}
		
		private boolean validate(String Password) {
			if (Password==null || Password.length()<8) 
			{
				System.out.println("Passwort zu kurz.");
				return false;
			}else{
				 String[] partialRegexChecks = 
			        	{
			        			".*[a-z]+.*", // lower
			        			".*[A-Z]+.*", // upper
			        			".*[0-9]+.*", // digits
			        			".*[@#Â§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
			        	};
				 if(Password.matches(partialRegexChecks[0]) 
						 && Password.matches(partialRegexChecks[1]) 
						 && Password.matches(partialRegexChecks[2]) 
						 && Password.matches(partialRegexChecks[3])) return true;
				 
				 System.out.println("Passwort erfÃ¼llt nicht die Anforderungen.");
				return false;
				
			}
		}

		@RequestMapping("/modifyUserAccount/{user}")
		public String modifyUserAccount(Model model,  @LoggedIn Optional<UserAccount> userAccount){
			if(userAccount.isPresent())model.addAttribute("userAccount",userAccount.get());
			return "modifyUserAccount";
		}
		
		private static boolean containsString( String s, String subString ) {
	        return s.indexOf( subString ) > -1 ? true : false;
	    }
		
		private boolean emailValidator(String email) {
			boolean isValid = false;
			
			if (containsString(email,"@") == false)
			{
				return false;	
			}
			
			if (containsString(email,".") == false)
			{
				return false;	
			}
			
		    /*    if (email.equals("test@test.test"))
	        {
	            return false;    
	        }    */

			
			try {
				//
				// Create InternetAddress object and validated the supplied
				// address which is this case is an email address.
				InternetAddress internetAddress = new InternetAddress(email);
				internetAddress.validate();
				isValid = true;
			} catch (AddressException e) {
				System.out.println("You are in catch block -- Exception Occurred for: " + email);
			}
			return isValid;
		}
		
		@RequestMapping(value="/modifyUserAccount_submit/{user}", method = RequestMethod.POST)
		public String modifyUserAccount(@LoggedIn Optional<UserAccount> userAccount,  @RequestParam(value = "firstname", required = false) final String Firstname, @RequestParam(value = "lastname", required = false) final String Lastname,@RequestParam(value = "email", required = false) final String Email){
			if(!userAccount.isPresent())return "noUser";
			User user_xyz=userRepository.findByUserAccount(userAccount.get());
			
			if ((Firstname!=null) && (!Firstname.equals("")))
				user_xyz.getUserAccount().setFirstname(Firstname);
			if ((Lastname!=null) && (!Lastname.equals("")))
				user_xyz.getUserAccount().setLastname(Lastname);
			if ((Email!=null) && (!Email.equals("")) && emailValidator(Email))
				user_xyz.getUserAccount().setEmail(Email);
			
			userAccountManager.save(user_xyz.getUserAccount());
			userRepository.save(user_xyz);
			return "redirect:/data";
		}
		
		@RequestMapping("/modifyAddress")
		public String modifyAddress(Model model,  @LoggedIn Optional<UserAccount> userAccount){
			if(userAccount.isPresent())model.addAttribute("userAccount",userAccount.get());
			return "modifyAddress";
		}
		
		@RequestMapping(value="/modifyAddress_submit", method = RequestMethod.POST)
		public String modifyAddress_submit( @LoggedIn Optional<UserAccount> userAccount, @RequestParam("street")final String Street,@RequestParam("houseNr")final String HouseNr,@RequestParam("zipCode")final String ZipCode,@RequestParam("city")final String City){
			if(!userAccount.isPresent())return "noUser";
			User user_xyz=userRepository.findByUserAccount(userAccount.get());
			
			user_xyz.getLocation().setStreet(Street);
			user_xyz.getLocation().setHousenr(HouseNr);
			if(ZipCode.matches("[0-9]{5}"))user_xyz.getLocation().setZipCode(ZipCode);
			user_xyz.getLocation().setCity(City);
			userRepository.save(user_xyz);
			
			return "redirect:/data";
		}
		
		@RequestMapping("/modifyLanguages")
		public String modifyLanguages(Model model,  @LoggedIn Optional<UserAccount> userAccount){
			if(userAccount.isPresent())model.addAttribute("user",userRepository.findByUserAccount(userAccount.get()));
			return "modifyLanguages";
		}
		
		@RequestMapping(value="/modifyLanguages_submit", method = RequestMethod.POST)
		public String modify( @LoggedIn Optional<UserAccount> userAccount, @RequestParam(value="secondLanguage", required=false)final String SecondLanguage,@RequestParam(value="thirdLanguage", required=false)final String ThirdLanguage){
			if(!userAccount.isPresent())return "noUser";
			User user_xyz=userRepository.findByUserAccount(userAccount.get());
			user_xyz.removeAllLanguages();
			
			if(SecondLanguage!=null && !(SecondLanguage.isEmpty())){
				Language l2;
				if(SecondLanguage.length()==2)l2=languageRepository.findByKennung(SecondLanguage);
				else  l2=languageRepository.findByName(SecondLanguage);
				System.out.println("1.1- "+l2.toString());
				if(l2!=null)user_xyz.setLanguage(l2);
				System.out.println("1.2- "+ user_xyz.getLanguages().toString());
			}
			if(ThirdLanguage!=null && !(ThirdLanguage.isEmpty())){
				Language l3;
				if(ThirdLanguage.length()==2)l3=languageRepository.findByKennung(ThirdLanguage);
				else  l3=languageRepository.findByName(ThirdLanguage);
				
				System.out.println("2.1- "+l3.toString());
				if(l3!=null)user_xyz.setLanguage(l3);
				System.out.println("2.2- "+ user_xyz.getLanguages().toString());
			}
			System.out.println("3.0");
			userRepository.save(user_xyz);
			
			return "redirect:/data";
		}
	}

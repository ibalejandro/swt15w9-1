package app.controller;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import app.model.UserRepository;

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
	
	/**
	   * Autowire.
	   * @param userRepository The repository for the users
	   */
	@Autowired
	public UserManagementController(UserRepository userRepository){
		this.userRepository=userRepository;
	}
	

	
	/*@RequestMapping("/admin")
	String userDetails(ModelMap map){
		map.addAttribute("users", userRepository.findAll());
		//map.addAttribute("offers",GoodsRepository.findAll());
		return "admin";
	}  */
	
	/**
	   * This method is the answer for the request to '/userDetails'. It finds
	   * and retrieves all the user in the UserRepository.
	   * @param ModelMap The modelmap to add the user
	   * @return String The name of the view to be shown after processing
	   */
	@RequestMapping("/userDetails")
	String userDetails(ModelMap map){
		map.addAttribute("userDetails", userRepository.findAll());
		return "userDetails";
	}

	/**
	   * This method is the answer for the request to '/data'. It finds
	   *  the user that is connected to the logged-in UserAccount in the UserRepository.
	   *  Is UserAccount found the method redirects to 'noUser'.
	   * @param Model The model to add the present User
	   * @return String The name of the view to be shown after processing
	   */
	@RequestMapping("/data")
	String data(Model model,  @LoggedIn Optional<UserAccount> userAccount){
		if(userAccount.isPresent()){
			UserAccount LoggUser=userAccount.get();
			model.addAttribute("userAccount", LoggUser);
			return "data";
		}
		
		return "noUser";
	}
}

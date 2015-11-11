package userManagement;

import java.util.Optional;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import userManagement.model.UserRepository;

@Controller
public class UserManagementController {
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserManagementController(UserRepository userRepository){
		this.userRepository=userRepository;
	}
	
	
	@RequestMapping({"/","/index"})
	String index(){
		return "index";
	}
	
	@RequestMapping("/admin")
	String userDetails(ModelMap map){
		map.addAttribute("users", userRepository.findAll());
		//map.addAttribute("offers",GoodRepository.findAll());
		return "admin";
	}
	
	@RequestMapping("/data")
	String data(Model model,  @LoggedIn Optional<UserAccount> userAccount){
		if(userAccount.isPresent()){
			UserAccount LoggUser=userAccount.get();
			model.addAttribute("userAccount", LoggUser);
			return "data";
		}
		
		return "error";
	}
}

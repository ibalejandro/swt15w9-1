package userManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import userManagement.model.UserRepository;

@RestController
public class UserManagementController {
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserManagementController(UserRepository userRepository){
		this.userRepository=userRepository;
	}
	
	
	@RequestMapping("/index")
	String index(){
		return "index";
	}
	
	@RequestMapping("/userDetails")
	String userDetails(ModelMap map){
		map.addAttribute("userDetails", userRepository.findAll());
		return "userDetails";
	}
}

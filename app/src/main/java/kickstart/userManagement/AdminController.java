package userManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import userManagement.model.UserRepository;



@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

private final UserRepository userRepository;
	
	@Autowired
	public AdminController(UserRepository userRepository){
		this.userRepository=userRepository;
	}
	
	@RequestMapping("/modify")
	public String modify(){
		
		return "modify";
	}
}

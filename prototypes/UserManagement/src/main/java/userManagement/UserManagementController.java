package userManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserManagementController {
	@RequestMapping("/login")
	String login(){
		
		
		return "login";
	}
}

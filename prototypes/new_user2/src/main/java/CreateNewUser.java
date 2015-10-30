import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CreateNewUser {	

	
	@RequestMapping({"/", "/index"})
	public String index() {
		return "index";
		
	}
	
	@RequestMapping({"/new_user_data"})
	public String new_user(){
      return "new_user_data";		
		
	}
	
	@RequestMapping({"/new_user_aboutuser1"})
	public String new_user1(){
      return "new_user_aboutuser1";		
		
	}
	
	@RequestMapping({"/new_user_aboutuser2a"})
	public String new_user2a(){
      return "new_user_aboutuser2a";		
		
	}
	
	@RequestMapping({"/reCAPTCHA-TEST"})
	public String show_reCAPTCHA(){
      return "reCAPTCHA-TEST";		
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/create_new_user_temp", method = RequestMethod.GET)
	public String create_new_user_t(@RequestParam("mail")String aMail, @RequestParam("username")String aUsername, @RequestParam("password")String aPassword, @RequestParam("repassword")String aRePassword) 
	{
		System.out.println(aMail);
		System.out.println(aUsername);
		System.out.println(aPassword);
		System.out.println(aRePassword);
		
		if (aMail.isEmpty() ||  aUsername.isEmpty() || aPassword.isEmpty())
		{
			return "All fields must not be empty! <br/> <a href=\"./new_user_data\">Back</a> ";
		}
		
		if (!aPassword.equals(aRePassword))
		{
			return "Passwords was not equal! <br/> <a href=\"./new_user_data\">Back</a> ";
		}
		
		return " <br/> <a href=\"./new_user_aboutuser1\">Next</a> ";
	}
}
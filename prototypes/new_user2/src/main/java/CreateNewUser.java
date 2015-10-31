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
      return "reCAPTCHA";		
		
	}
	
	//@ResponseBody
	@RequestMapping(value = "/create_new_user_temp", method = RequestMethod.GET)
	public String create_new_user_t(@RequestParam("mail")String aMail, @RequestParam("username")String aUsername, @RequestParam("password")String aPassword, @RequestParam("repassword")String aRePassword) 
	{
		System.out.println(aMail);
		System.out.println(aUsername);
		System.out.println(aPassword);
		System.out.println(aRePassword);
		
		if (aMail.isEmpty() ||  aUsername.isEmpty() || aPassword.isEmpty())
		{
			return "errorpage0_empty";
		}
		
		if (!aPassword.equals(aRePassword))
		{
			return "errorpage0_wrongpw";
		}
		
		return "new_user_aboutuser1"; //"index";
	}
	
	@RequestMapping(value = "/submit_userdata1", method = RequestMethod.GET)
	public String submit_userdata1(@RequestParam("name")String aName, @RequestParam("firstname")String aFirstname, @RequestParam("Adresstyp")String aAdresstyp) 
	{
		System.out.println(aName);
		System.out.println(aFirstname);
		System.out.println(aAdresstyp);
		
		if (aName.isEmpty() ||  aFirstname.isEmpty() || aAdresstyp.isEmpty())
		{
			return "errorpage1_empty";
		}
		
		if (!aAdresstyp.equals("Wohnung"))
		{return "new_user_aboutuser2a";}
		{return "new_user_aboutuser2b";}
		
	}
	
	@RequestMapping(value = "/submit_userdata2a", method = RequestMethod.GET)
	public String submit_userdata2a(@RequestParam("mail")String aMail, @RequestParam("username")String aUsername, @RequestParam("password")String aPassword, @RequestParam("repassword")String aRePassword) 
	{
		System.out.println(aMail);
		System.out.println(aUsername);
		System.out.println(aPassword);
		System.out.println(aRePassword);
		
		if (aMail.isEmpty() ||  aUsername.isEmpty() || aPassword.isEmpty())
		{
			return "errorpage2a_empty";
		}
		
		return "user";
	}
	
	@RequestMapping(value = "/submit_userdata2b", method = RequestMethod.GET)
	public String submit_userdata2b(@RequestParam("mail")String aMail, @RequestParam("username")String aUsername, @RequestParam("password")String aPassword, @RequestParam("repassword")String aRePassword) 
	{
		System.out.println(aMail);
		System.out.println(aUsername);
		System.out.println(aPassword);
		System.out.println(aRePassword);
		
		if (aMail.isEmpty() ||  aUsername.isEmpty() || aPassword.isEmpty())
		{
			return "errorpage2b_empty";
		}
		
		return "user";
	}
}
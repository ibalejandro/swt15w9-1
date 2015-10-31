import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
				
		// Mail senden:
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		JavaMailSender mailSender = context.getBean("mailSender", JavaMailSender.class);
		
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(aMail);
		msg.setSubject("Aktivation of your Refugees-App Account");
		msg.setText("Aktivierungslink");
		
		try{
            mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
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
		
		if (aAdresstyp.equals("Refugees_home"))
		{
			return "new_user_aboutuser2a";
		}

		if (aAdresstyp.equals("Wohnung"))
		{
			return "new_user_aboutuser2b";
		}
		
		return "dd";
		
	}
	
	@RequestMapping(value = "/submit_userdata2a", method = RequestMethod.GET)
	public String submit_userdata2a(@RequestParam("flh_name")String aFlh_name, @RequestParam("citypart")String aCitypart, @RequestParam("postcode")String aPostcode, @RequestParam("city")String aCity) 
	{
		System.out.println(aFlh_name);
		System.out.println(aCitypart);
		System.out.println(aPostcode);
		System.out.println(aCity);
		
		if (aFlh_name.isEmpty() ||  aCitypart.isEmpty() || aPostcode.isEmpty() || aCity.isEmpty())
		{
			return "errorpage2a_empty";
		}
		
		return "user";
	}
	
	@RequestMapping(value = "/submit_userdata2b", method = RequestMethod.GET)
	public String submit_userdata2b(@RequestParam("street")String aStreet, @RequestParam("housenr")String aHousenr, @RequestParam("postcode")String aPostcode, @RequestParam("city")String aCity) 
	{
		System.out.println(aStreet);
		System.out.println(aHousenr);
		System.out.println(aPostcode);
		System.out.println(aCity);
		
		if (aStreet.isEmpty() ||  aHousenr.isEmpty() || aPostcode.isEmpty() || aCity.isEmpty())
		{
			return "errorpage2b_empty";
		}
		
		return "user";
	}
}
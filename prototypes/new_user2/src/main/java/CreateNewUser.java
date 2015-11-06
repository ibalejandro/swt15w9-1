import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URI;

import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpHeaders;
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
	
	@RequestMapping(value = "/reCAPTCHA-TEST")
	public String show_reCAPTCHA(){
      return "reCAPTCHA";		
		
	}
	
/*	@RequestMapping(value = "/submit_captcha", method = RequestMethod.POST)
	public String validate_reCAPTCHA(@RequestParam("response")String CaptchaResponse, @RequestParam("secret")String Secret){
		
		Secret="6LcBYBATAAAAAPHUZfB4OFpbdwrVxp08YEaVX3Dr";
		return Secret;
		
		
	}*/
	
	@RequestMapping(value = "/submit_captcha", method = RequestMethod.POST)
	public String recieve_reCAPTCHA(@RequestParam("g-recaptcha-response")String CaptchaResponse){
        
		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);
		
		if (CaptchaResponse.isEmpty())
		{ 
		   return "/reCAPTCHA-TEST";	
		} 
		else
		{
			return "https://www.google.com/recaptcha/api/siteverify";
		}
	}
	
	//@ResponseBody
	@RequestMapping(value = "/create_new_user_temp", method = RequestMethod.POST)
	public String create_new_user_t(@RequestParam("mail")String Mail, @RequestParam("username")String Username, @RequestParam("password")String Password, @RequestParam("repassword")String RePassword) 
	{
		System.out.println(Mail);
		System.out.println(Username);
		System.out.println(Password);
		System.out.println(RePassword);
		
		if (Mail.isEmpty() ||  Username.isEmpty() || Password.isEmpty())
		{
			return "errorpage0_empty";
		}
		
		if (!Password.equals(RePassword))
		{
			return "errorpage0_wrongpw";
		}
				
	/*	// Mail senden:
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
		JavaMailSender mailSender = context.getBean("mailSender", JavaMailSender.class);
		
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(Mail);
		msg.setSubject("Aktivation of your Refugees-App Account");
		msg.setText("Aktivierungslink");
		
		try{
            mailSender.send(msg);
        }
        catch (MailException ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }   */
		
		return "new_user_aboutuser1"; //"index";
	}
	
	
	
	@RequestMapping(value = "/submit_userdata1", method = RequestMethod.POST)
	public String submit_userdata1(@RequestParam("name")String Name, @RequestParam("firstname")String Firstname, @RequestParam("Adresstyp")String Adresstyp) 
	{
		System.out.println(Name);
		System.out.println(Firstname);
		System.out.println(Adresstyp);
		
		if (Name.isEmpty() ||  Firstname.isEmpty() || Adresstyp.isEmpty())
		{
			return "errorpage1_empty";
		}
		
		if (Adresstyp.equals("Refugees_home"))
		{
			return "new_user_aboutuser2a";
		}

		if (Adresstyp.equals("Wohnung"))
		{
			return "new_user_aboutuser2b";
		}
		
		return "dd";
		
	}
	
	@RequestMapping(value = "/submit_userdata2a", method = RequestMethod.POST)
	public String submit_userdata2a(@RequestParam("flh_name")String Flh_name, @RequestParam("citypart")String Citypart, @RequestParam("postcode")String Postcode, @RequestParam("city")String City) 
	{
		System.out.println(Flh_name);
		System.out.println(Citypart);
		System.out.println(Postcode);
		System.out.println(City);
		
		if (Flh_name.isEmpty() ||  Citypart.isEmpty() || Postcode.isEmpty() || City.isEmpty())
		{
			return "errorpage2a_empty";
		}
		
		return "user";
	}
	
	@RequestMapping(value = "/submit_userdata2b", method = RequestMethod.POST)
	public String submit_userdata2b(@RequestParam("street")String Street, @RequestParam("housenr")String Housenr, @RequestParam("postcode")String Postcode, @RequestParam("city")String City) 
	{
		System.out.println(Street);
		System.out.println(Housenr);
		System.out.println(Postcode);
		System.out.println(City);
		
		if (Street.isEmpty() ||  Housenr.isEmpty() || Postcode.isEmpty() || City.isEmpty())
		{
			return "errorpage2b_empty";
		}
		
		return "user";
	}
}
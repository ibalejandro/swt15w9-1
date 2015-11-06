import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

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
	
	@RequestMapping({"/new_user_aboutuser2"})
	public String new_user2(){
      return "redirect:new_user_aboutuser1";				
	}
	
	@RequestMapping({"/new_user_aboutuser2a"})
	public String new_user2a(){
      return "/new_user_aboutuser2a";				
	}
	
	@RequestMapping({"/new_user_aboutuser2b"})
	public String new_user2b(){
      return "/new_user_aboutuser2b";				
	}
	
	@RequestMapping(value = "/submit_captcha")
	public String redirect_reCAPTCHA(){
	      return "redirect:/reCAPTCHA-TEST";		
			
		}
	
	@RequestMapping(value = "/reCAPTCHA-TEST")
	public String show_reCAPTCHA(){
      return "/reCAPTCHA";		
		
	}
	
	private String sendPost(String CaptchaResponse, String Secret) throws Exception {

		String url = "https://www.google.com/recaptcha/api/siteverify"+"?response="+CaptchaResponse+"&secret="+Secret;
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");

		String urlParameters = "";
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		//System.out.println("\nSending 'POST' request to URL : " + url);
		//System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		//print result
		System.out.println(response.toString());
		
		return response.toString();

	}
	
	@RequestMapping(value = "/submit_captcha", method = RequestMethod.POST)
	public String recieve_reCAPTCHA(@RequestParam("g-recaptcha-response")String CaptchaResponse){
        
		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);
		
		if (CaptchaResponse.isEmpty())
		{ 
		   return "redirect:/reCAPTCHA-TEST";	
		} 
		else
		{   
			//http://localhost:8080/create_new_user_temp?mail=aa&username=a&password=a&repassword=a
			
			String Secret="6LcBYBATAAAAAPHUZfB4OFpbdwrVxp08YEaVX3Dr";
			String Returnstring="";
			
			System.out.println("## Validate:");
			System.out.println("https://www.google.com/recaptcha/api/siteverify?response="+CaptchaResponse+"&secret="+Secret);
			
			try {
				Returnstring=sendPost(CaptchaResponse,Secret);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (Returnstring.equals("{  \"success\": true}"))
			{
				return "/validation_success";
			}
			else
			{
				return "redirect:/reCAPTCHA-TEST";	
			}
		}
	}
	
	//@ResponseBody
	@RequestMapping(value = "/create_temp_new_user", method = RequestMethod.POST)
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
		
		return "redirect:/new_user_aboutuser1"; //"index";
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
			return "redirect:/new_user_aboutuser2a";
		}

		if (Adresstyp.equals("Wohnung"))
		{
			return "redirect:/new_user_aboutuser2b";
		}
		
		return "redirect:/new_user_aboutuser1";
		
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
		
		return "redirect:/user";
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
		
		return "redirect:/user";
	}
}
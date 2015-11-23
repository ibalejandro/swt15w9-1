package app.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Address;
import app.model.User;
import app.model.UserRepository;



@Controller
public class CreateNewUser {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;

	@Autowired
	public CreateNewUser (UserAccountManager userAccountManager, UserRepository userRepository) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
	}

	@RequestMapping({"/new_user_data"})
	public String new_user(){
      return "new_user_data";

	}

	@RequestMapping({"/new_user_aboutuser1/user/{user}"})
	public String new_user1(@PathVariable String user){
      return "new_user_aboutuser1";

	}

	@RequestMapping({"/new_user_aboutuser2"})
	public String new_user2(){
      return "redirect:new_user_aboutuser1";
	}

	@RequestMapping({"/new_user_aboutuser2a/user/{user}"})
	public String new_user2a(@PathVariable String user){
      return "/new_user_aboutuser2a";
	}

	@RequestMapping({"/new_user_aboutuser2b/user/{user}"})
	public String new_user2b(@PathVariable String user){
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

		if (Username.equals("new_user"))
		{
			String e_descrition = "Invalid Username.";
			System.out.println("ERROR: "+e_descrition);


			return "error";
		}

		if (userAccountManager.findByUsername(Username).isPresent())
		{
			String e_descrition = "Username already in use.";
			System.out.println("ERROR: "+e_descrition);

			return "error";
		}


		UserAccount userAccount = userAccountManager.create(Username, Password,
				new Role("ROLE_NORMALE"));
		userAccountManager.save(userAccount);
		userAccount.setEmail(Mail);
		userAccountManager.save(userAccount);

		System.out.println("Account "+userAccount.getUsername()+" angelegt.");

		return "redirect:/new_user_aboutuser1/user/"+userAccount.getUsername(); /*"index"; */
	}



	@RequestMapping(value = "/submit_userdata1/user/{user}", method = RequestMethod.POST)
	public String submit_userdata1(@PathVariable String user, @RequestParam("name")String Name, @RequestParam("firstname")String Firstname, @RequestParam("Adresstyp")String Adresstyp)
	{
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}

		System.out.println(Name);
		System.out.println(Firstname);
		System.out.println(Adresstyp);

	    if(userAccountManager.findByUsername(user).isPresent()){
			UserAccount LoggUser=userAccountManager.findByUsername(user).get();

			if (Name.isEmpty() ||  Firstname.isEmpty() || Adresstyp.isEmpty())
			{
				return "errorpage1_empty";
			}

			System.out.println("user="+user);

			LoggUser.setLastname(Name);
        	LoggUser.setFirstname(Firstname);
			userAccountManager.save(LoggUser);

			if (Adresstyp.equals("Refugees_home"))
			{
				return "redirect:/new_user_aboutuser2a/user/{user}";
			}

			if (Adresstyp.equals("Wohnung"))
			{
				return "redirect:/new_user_aboutuser2b/user/{user}";
			}

			return "redirect:/new_user_aboutuser1/user/{user}";
		}
		return "error";

	}

	@RequestMapping(value = "/submit_userdata2a/user/{user}", method = RequestMethod.POST)
	public String submit_userdata2a(@PathVariable String user, @RequestParam("flh_name")String Flh_name, @RequestParam("citypart")String Citypart, @RequestParam("postcode")String Postcode, @RequestParam("city")String City)
	{
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}

		System.out.println(Flh_name);
		System.out.println(Citypart);
		System.out.println(Postcode);
		System.out.println(City);

		if(userAccountManager.findByUsername(user).isPresent()){
			UserAccount LoggUser=userAccountManager.findByUsername(user).get();

		//KLASSE FLÜCHTLINGSHEIM UND INTERFACE LOCATION!!!!!!!!!!!!!!
			if (Flh_name.isEmpty() ||  Citypart.isEmpty() || Postcode.isEmpty() || City.isEmpty())
			{
				return "errorpage2a_empty";
			}
			Address address= new Address(Flh_name, Citypart, Postcode, City);
			User user_1=new User(LoggUser, address);
			userRepository.save(user_1);

			return "redirect:/";
		}
		return "error";
	}

	@RequestMapping(value = "/submit_userdata2b/user/{user}", method = RequestMethod.POST)
	public String submit_userdata2b(@PathVariable String user, @RequestParam("street")String Street, @RequestParam("housenr")String Housenr, @RequestParam("postcode")String Postcode, @RequestParam("city")String City)
	{
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}

		System.out.println(Street);
		System.out.println(Housenr);
		System.out.println(Postcode);
		System.out.println(City);

		if(userAccountManager.findByUsername(user).isPresent()){
			UserAccount LoggUser=userAccountManager.findByUsername(user).get();

			if (Street.isEmpty() ||  Housenr.isEmpty() || Postcode.isEmpty() || City.isEmpty())
			{
				return "errorpage2b_empty";
			}
			Address address= new Address(Street, Housenr, Postcode, City);
			User user_1=new User(LoggUser, address);
			userRepository.save(user_1);

			return "redirect:/";
		}
		return "error";
	}
}

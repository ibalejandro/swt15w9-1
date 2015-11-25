package app.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.MailSender;
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
   // private final MailSender mailSender;

	@Autowired
	public CreateNewUser (UserAccountManager userAccountManager, UserRepository userRepository /*, MailSender mailSender*/) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		
		//this.mailSender=mailSender;
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
	
	@RequestMapping({"/new_user_language_origin/user/{user}"})
	public String new_user_language_origin(@PathVariable String user){
      return "/new_user_language_origin";
	}

	@RequestMapping(value = "/submit_captcha")
	public String redirect_reCAPTCHA(){
	      return "redirect:/reCAPTCHA-TEST";
		}
	
	@RequestMapping(value = "/reCAPTCHA/user/{user}")
	public String show_reCAPTCHA_user(@PathVariable String user){
	      return "/reCAPTCHA_User";
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


	
	
	
	/*@Autowired
	private void Mailsenden(String SendTo, String Subject, String Text) throws MessagingException
	{
		
		String mailhost = "smtp.gmail.com"; 			//Aus application.properties auslesen.
		String mailusername = "***"; 	//Aus application.properties auslesen.
	    String mailpassword = "***"; 			//Aus application.properties auslesen.
	    String mailport= "587"; 						//Aus application.properties auslesen.
	    String recipient = SendTo;

	    Properties props = new Properties();

	    props.put("mail.smtp.host", mailhost);
	    props.put("mail.from", mailusername);
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.port", mailport);
	    props.setProperty("mail.debug", "false");

	    Session session = Session.getInstance(props, null);
	    MimeMessage msg = new MimeMessage(session);

	    msg.setRecipients(Message.RecipientType.TO, recipient);
	    msg.setSubject(Subject);
	    msg.setSentDate(new Date());
	    msg.setText(Text);

	    Transport transport = session.getTransport("smtp");

	    transport.connect(mailusername, mailpassword);
	    transport.sendMessage(msg, msg.getAllRecipients());
	    transport.close();
		
		return;
		
	} */
	
	public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
          throw new RuntimeException(ex);
        }
    }
	
	private String AktivierungskeyErzeugen(String username, String mail, Integer Zufallszahl1, Integer Zufallszahl2){
		float fl = Zufallszahl1/Zufallszahl2;
		String starttext = sha256("s"+sha256(sha256("Aktivierungskey"+username+"123"+mail+"XYZ"+Float.toString(fl)+"fff")+Zufallszahl1.toString())+Zufallszahl2.toString());
		return sha256(sha256(sha256(sha256(sha256(starttext)))));
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
	
	@RequestMapping(value = "/submit_captcha/user/{user}", method = RequestMethod.POST)
	public String recieve_reCAPTCHA_user(@PathVariable String user, @RequestParam("g-recaptcha-response")String CaptchaResponse){

		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);

		if (CaptchaResponse.isEmpty() || (!userAccountManager.findByUsername(user).isPresent()))
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
				if(userAccountManager.findByUsername(user).isPresent()){
		            User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
		            
		            
		            user_xyz.setRegistrationstate(7); //7 ~ Captcha erfolgreich geprüft
					userRepository.save(user_xyz);
					
					
					System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());	
					
					String link="/activation/user/{"+user_xyz.getUserAccount().getUsername()+"}/{"+user_xyz.getActivationkey()+"}";
					String mailtext = "<h1>Activation of your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")<h1> Hallo "+user_xyz.getUserAccount().getUsername()+" <br/><br/> Please activate your RefugeesApp-Account with this link: <a href=\""+link+"\">Activationlink</a>  <br/><br/> Textlink: "+link+" ";
					
					System.out.println(link);
					
					//Mail senden: 
					/*	try {
					Mailsenden(Mail,"Activation of your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")",mailtext);
					System.out.println("Mail versandt");
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}     */
					
					user_xyz.setRegistrationstate(8); //8 ~ Aktivierungsmail versandt.
					userRepository.save(user_xyz);
					
					return "/validation_success";
		            
				}
				return "redirect:/";
				
			}
			else
			{
				return "redirect:/reCAPTCHA-TEST";
			}
		}
	}
	
	@RequestMapping(value = "/activation/user/{user}/{textactivationkey}", method = RequestMethod.POST)
	public String recieve_activationkey(@PathVariable String user, @PathVariable String textactivationkey){
		
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}

	    if(userAccountManager.findByUsername(user).isPresent()){
            User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
	    	
			if (textactivationkey.isEmpty())
			{
				return "redirect:/";
			}

			if (user_xyz.getActivationkey().equals(textactivationkey))
			{
			    user_xyz.Activate();
			    System.out.println(user_xyz.getUserAccount().getUsername()+" wurde aktiviert.");
			}
			else
			{	
				System.out.println(user_xyz.getUserAccount().getUsername()+": Aktivierung fehlgeschlagen");
			}	

			return "redirect:/";
		}
	
		
		
       return "redirect:/";
	}		

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

		//userAccount.isEnabled = false;
		
		System.out.println("Account "+userAccount.getUsername()+" angelegt.");
		
		UserAccount LoggUser=userAccountManager.findByUsername(userAccount.getUsername()).get();
		Address emptyadress= new Address("", "", "", "");
		User user_xyz=new User(LoggUser, emptyadress);
		user_xyz.setRegistrationstate(0);
		userRepository.save(user_xyz);
		
		user_xyz.setRegistrationdate(new Date()); //Temporäres Registrierungsdatum um unvollständige Konten nach einer betimmten Zeit zu löschen.
		user_xyz.setRegistrationstate(1);
		userRepository.save(user_xyz);

		System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
		
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
            User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
	    	
			if (Name.isEmpty() ||  Firstname.isEmpty() || Adresstyp.isEmpty())
			{
				return "errorpage1_empty";
			}

			System.out.println("user="+user);

			user_xyz.getUserAccount().setLastname(Name);
			user_xyz.getUserAccount().setFirstname(Firstname);
			userAccountManager.save(user_xyz.getUserAccount());
			
			user_xyz.setRegistrationstate(2);
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
			
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
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

		//KLASSE FLÜCHTLINGSHEIM UND INTERFACE LOCATION!!!!!!!!!!!!!!
			if (Flh_name.isEmpty() ||  Citypart.isEmpty() || Postcode.isEmpty() || City.isEmpty())
			{
				return "errorpage2a_empty";
			}
			Address address= new Address(Flh_name, Citypart, Postcode, City);			
			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(3); //3 ~ Flüchtlingsheim
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
			return "redirect:/new_user_language_origin/user/{user}";
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
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

			if (Street.isEmpty() ||  Housenr.isEmpty() || Postcode.isEmpty() || City.isEmpty())
			{
				return "errorpage2b_empty";
			}
			
			Address address= new Address(Street, Housenr, Postcode, City);
			user_xyz.setLocation(address);
			user_xyz.setRegistrationstate(4); //4 ~ Wohnung
			userRepository.save(user_xyz);
			
			System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());			
			return "redirect:/new_user_language_origin/user/{user}";
		}
		return "error";
	}
	
	@RequestMapping(value = "/submit_language_origin/user/{user}", method = RequestMethod.POST)
	public String submit_language_origin(@PathVariable String user, @RequestParam("nativelanguage")String Nativelanguage, @RequestParam("otherlanguages")String OtherLanguages, @RequestParam("origin")String Origin)
	{
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}

		System.out.println(Nativelanguage);
		System.out.println(OtherLanguages);
		System.out.println(Origin);

		if(userAccountManager.findByUsername(user).isPresent()){
			User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());

			if (Nativelanguage.isEmpty() ||  Origin.isEmpty() )
			{
				return "errorpage2b_empty";
			}
            
			user_xyz.setLanguage(Nativelanguage);
			
			user_xyz.setOrigin(Origin);
			
			user_xyz.setRegistrationstate(5);   
			userRepository.save(user_xyz);   
			
		//	Nutzeraktivierung vorbereiten:
			
			Integer z1, z2; //Zufallszahlen
			z1 = (int)(Math.random() * 1000000000)+123456;
			z2 = (int)(Math.random() * 1000000000)+117980;
			
			String activationkey = AktivierungskeyErzeugen(user_xyz.getUserAccount().getUsername(), user_xyz.getUserAccount().getEmail(), z1, z2);
			user_xyz.setActivationkey(activationkey);
			
			user_xyz.setRegistrationdate(new Date());
			user_xyz.setRegistrationstate(6); // 6 ~ Bereit zur Aktivierung
			userRepository.save(user_xyz);
	
			System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
			return "redirect:/reCAPTCHA/user/{user}";
		}
		return "error";
	}
}

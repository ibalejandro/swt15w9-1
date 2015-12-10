package app.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Address;
import app.model.User;
import app.model.UserRepository;

/**
* <h1>CreateNewUserController</h1>
* The CreateNewUserController is used for registration and create UserAccounts.
* 
*
* @author Kilian Heret
* 
*/

@Controller
public class CreateNewUser {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
    private final MailSender mailSender;

	/**
	   * Autowire.
	   * @param CreateNewUser
	   */
	@Autowired
	public CreateNewUser (UserAccountManager userAccountManager, UserRepository userRepository , MailSender mailSender){
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		
		this.mailSender = mailSender;
	}
	
	int zufallzahl_1, zufallzahl_2; //Zufallszahlen
	boolean za;

	@RequestMapping( value="/new_user")
    public String new_user0(){
		return "redirect:/";
		
	}
	
	@RequestMapping( value="/new_user_data")
    public String new_user(){
		
		Integer zufallzahl1 = (int)(Math.random() * 50)+1;
		Integer zufallzahl2 = (int)(Math.random() * 48)+3;
		
		zufallzahl_1 = zufallzahl1;
		zufallzahl_2 = zufallzahl2;
		za = true;
		
		String szufallzahl1 = zufallzahl1.toString();
		String szufallzahl2 = zufallzahl2.toString();
		
		//System.out.println("----1");
		
		return "redirect:/new_user_data_rd/"+szufallzahl1+"/"+szufallzahl2;
	}  
	
	@RequestMapping(value ="/new_user_data_rd/0/0", method = RequestMethod.GET)
	public String new_user_rd0(){
		return "redirect:/new_user_data";
	}
	
	@RequestMapping(value ="/new_user_data_rd/{zufallzahl1}/{zufallzahl2}", method = RequestMethod.GET)
	public String new_user_rd(@PathVariable("zufallzahl1")String szufallzahl1 , @PathVariable("zufallzahl2")String szufallzahl2){
		
		if (!za) {
			return "redirect:/new_user_data";			
		}
		
		za = false;
		
		return "new_user_data";
	}

	@RequestMapping({"/new_user_aboutuser1/user/{user}"})
	public String new_user1(@PathVariable String user){
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}
		return "new_user_aboutuser1";

	}

	@RequestMapping({"/new_user_aboutuser2"})
	public String new_user2(){
		return "redirect:new_user_aboutuser1";
	}

	@RequestMapping({"/new_user_aboutuser2a/user/{user}"})
	public String new_user2a(@PathVariable String user){
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}
		return "/new_user_aboutuser2a";
	}

	@RequestMapping({"/new_user_aboutuser2b/user/{user}"})
	public String new_user2b(@PathVariable String user){
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}
		return "/new_user_aboutuser2b";
	}
	
	@RequestMapping({"/new_user_language_origin/user/{user}"})
	public String new_user_language_origin(@PathVariable String user){
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}
		return "/new_user_language_origin";
	}

	@RequestMapping(value = "/submit_captcha")
	public String redirect_reCAPTCHA(){
	      return "redirect:/reCAPTCHA-TEST";
		}
	
	@RequestMapping(value = "/reCAPTCHA/user/{user}")
	public String show_reCAPTCHA_user(@PathVariable String user)
	{
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}
		return "/reCAPTCHA_User";
	}

	@RequestMapping(value = "/reCAPTCHA-TEST")
	public String show_reCAPTCHA(){
      return "/reCAPTCHA";

	}
	
	@RequestMapping({"/activationmail_gesendet"})
    public String activationmail_gesendet(){
        return "/activationmail_gesendet";
    }
    
    @RequestMapping({"/activationmail_local"})
    public String activationmail_local(){
        return "/activationmail_local";
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


	private void Mailsenden(String SendTo, String Subject, String Text) throws MessagingException, IOException
	{
		
		String result = "";
		InputStream inputStream = null;
		
		String mailhost = "";
		String mailport = "";
		String mailusername = "";
		String mailpassword = "";
	 
			try {
				Properties prop = new Properties();
				String propFileName = "application.properties";
	 
				inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
	 
				if (inputStream != null) {
					prop.load(inputStream);
				} else {
					throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
				}
	 
				Date time = new Date(System.currentTimeMillis());
	 
				// get the property value and print it out
				mailhost = prop.getProperty("spring.mail.host");
				mailport = prop.getProperty("spring.mail.port");
				mailusername = prop.getProperty("spring.mail.username");
				mailpassword = prop.getProperty("spring.mail.password");
	
			} catch (Exception e) {
				System.out.println("Exception: " + e);
			} finally {
				inputStream.close();
			}


		
		//String mailhost = "***"; 			//Aus application.properties auslesen.
		//String mailusername = "***"; 	//Aus application.properties auslesen.
	    //String mailpassword = "***"; 			//Aus application.properties auslesen.
	    //String mailport= "***"; 						//Aus application.properties auslesen.
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
	    msg.setContent(Text, "text/html; charset=utf-8");


	    Transport transport = session.getTransport("smtp");

	    transport.connect(mailusername, mailpassword);
	    transport.sendMessage(msg, msg.getAllRecipients());
	    transport.close();
		
		return;
		 
	}   
	
	
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
			if (!userAccountManager.findByUsername(user).isPresent())
			{
				return "redirect:/";
			}
			else
			{
				return "redirect:/submit_captcha/user/"+user;
			}
		   
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
					
					Date zeitstempel = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy HH:mm:ss"
                    // simpleDateFormat.format(zeitstempel)
                    
                    String domain     = "http://localhost:8080";
                    String link       = domain + "/activation/user/{"+user_xyz.getUserAccount().getUsername()+"}/{"+sha256(user_xyz.getActivationkey()+simpleDateFormat.format(zeitstempel)+(user_xyz.getRegistrationstate()+1))+"}";
                    String mailtext = "<html> <head> </head> <body> <h1>Activation of your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")<h1> Hallo "+user_xyz.getUserAccount().getUsername()+" </h1><br/><br/> Please activate your RefugeesApp-Account with this link: <a href=\""+link+"\">Activationlink</a>  <br/><br/> Textlink: "+link+"  </body> </html>";
                    String mailadresse = user_xyz.getUserAccount().getEmail();
					
					System.out.println(link);
					
					//Mail senden: 
                    if (!mailadresse.equals("test@test.test"))                    
                    {
                        //Mail senden: 
                        try {
                            Mailsenden(mailadresse,"Activation of your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")",mailtext);
                            System.out.println("Mail versandt");
                        } catch (MessagingException | IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }   
                    }

					
					user_xyz.setRegistrationstate(8); //8 ~ Aktivierungsmail versandt.
					userRepository.save(user_xyz);
				
					System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
                    
                    if (!mailadresse.equals("test@test.test"))
                    {
                        return "redirect:/activationmail_gesendet";
                    }    
                    else
                    {
                        return "redirect:/activationmail_local";
                    }
		            
				}
				return "redirect:/";
				
			}
			else
			{
				return "redirect:/reCAPTCHA-TEST";
			}
		}
	}
	                         
	@RequestMapping(value = "/activation/user/{user_temp}/{textactivationkey_temp}", method = RequestMethod.GET)
	public String recieve_activationkey(@PathVariable String user_temp, @PathVariable String textactivationkey_temp){
		
		String user = user_temp.replace("{", "").replace("}", "");
		String textactivationkey = textactivationkey_temp.replace("{", "").replace("}", "");
		
		if (!userAccountManager.findByUsername(user).isPresent())
		{
			return "redirect:/";
		}

	    if(userAccountManager.findByUsername(user).isPresent()){
            User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
	    	
            System.out.println("Get a activationlink for "+user_xyz.getUserAccount().getUsername()+"");
            
			if (textactivationkey.isEmpty())
			{
				return "redirect:/";
			}

            if (user_xyz.isActivated())
            {
                System.out.println("Nutzer bereits aktiviert.");
                return "redirect:/";
            }    
            
            Date zeitstempel = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy HH:mm:ss"
            // simpleDateFormat.format(zeitstempel)
            
            if (sha256(user_xyz.getActivationkey()+simpleDateFormat.format(zeitstempel)+user_xyz.getRegistrationstate()).equals(textactivationkey))
			{
			    user_xyz.Activate();
			    System.out.println(user_xyz.getUserAccount().getUsername()+" wurde aktiviert.");
			    
			    user_xyz.setRegistrationstate(10);
			    userRepository.save(user_xyz);
			    
			    System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
			}
			else
			{	
				System.out.println(user_xyz.getUserAccount().getUsername()+": Aktivierung fehlgeschlagen");
			}	

			return "redirect:/";
		}
	
		
		
       return "redirect:/";
	}		
	
	
	@RequestMapping(value = "/not_activated/user/{user}", method = RequestMethod.GET)
	public String user_not_activated(@PathVariable final String user, @LoggedIn final Optional<UserAccount> userAccount){

		if (userAccount.isPresent() == false) 
		{
			return "redirect:/";
		}
		
		if (user.equals("")) 
		{
			return "redirect:/";
		}
		
		if (!userAccountManager.findByUsername(user).isPresent()) 
		{
		   return "redirect:/";
		}
		
		if ((userAccount.get().getUsername().equals(user)) == false)
		{
		   return "redirect:/";
		}
		else
		{
			System.out.println(userAccount.get().getUsername());
			
			if(userAccountManager.findByUsername(user).isPresent()){
	            User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
		    	
	            if (user_xyz.isActivated())
	            {
	            	return "redirect:/";   //Alles OK.
	            }
	            
	            switch (user_xyz.getRegistrationstate()) {
	            case -1: return "redirect:/new_user_data";
				case 0:  // Fall: Temporäres Registrierungsdatum fehlt.
					user_xyz.setRegistrationdate(new Date()); //Temporäres Registrierungsdatum um unvollständige Konten nach einer betimmten Zeit zu löschen.
					user_xyz.setRegistrationstate(1);
					userRepository.save(user_xyz); 
					     return "redirect:/new_user_aboutuser1/user/{user}";
				case 1:  return "redirect:/new_user_aboutuser1/user/{user}";
				case 2:  
						if (user_xyz.getAdresstyp().equals("Refugees_home"))
						{
							return "redirect:/new_user_aboutuser2a/user/{user}";
						}
						if (user_xyz.getAdresstyp().equals("Wohnung"))
						{
							return "redirect:/new_user_aboutuser2b/user/{user}";
						}
					    return "redirect:/new_user_aboutuser1/user/{user}";
				case 3:  return "redirect:/new_user_language_origin/user/{user}";
				case 4:  return "redirect:/new_user_language_origin/user/{user}";
				case 5:  return "redirect:/new_user_language_origin/user/{user}";  // Aktivierungskey noch nicht generiert
				case 6:  return "redirect:/reCAPTCHA/user/{user}";
				case 7:  return "redirect:/reCAPTCHA/user/{user}";
				case 8:  return "redirect:/reCAPTCHA/user/{user}";
				case 9:  return "redirect:/";    //Account deaktiviert.
				case 10:  return "redirect:/";   //Alles OK.
	            }
			}	
			return "redirect:/"; 
		} 
	}
	
	private static boolean containsString( String s, String subString ) {
        return s.indexOf( subString ) > -1 ? true : false;
    }
	
	private boolean emailValidator(String email) {
		boolean isValid = false;
		
		if (containsString(email,"@") == false)
		{
			return false;	
		}
		
		if (containsString(email,".") == false)
		{
			return false;	
		}
		
	    /*    if (email.equals("test@test.test"))
        {
            return false;    
        }    */

		
		try {
			//
			// Create InternetAddress object and validated the supplied
			// address which is this case is an email address.
			InternetAddress internetAddress = new InternetAddress(email);
			internetAddress.validate();
			isValid = true;
		} catch (AddressException e) {
			System.out.println("You are in catch block -- Exception Occurred for: " + email);
		}
		return isValid;
	}
	
	
	
	private int checkPasswordStrength(String password) 
	{
		if (password.equals("")) 
		{
			return 0;
		}
		
		boolean hasLower =false;
		boolean hasUpper =false;
		boolean hasDigits =false;
		boolean hasSymbols =false;
		
		
        float strengthPercentage=0;
        String[] partialRegexChecks = 
        	{
        			".*[a-z]+.*", // lower
        			".*[A-Z]+.*", // upper
        			".*[\\d]+.*", // digits
        			".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
        	};
        
            int i=0;    
            while (i< (password.length())){
            	
            	if (password.substring(i, i+1).matches(partialRegexChecks[0])) 
                {
                	hasLower = true;
                	strengthPercentage+=2;
                }
                if (password.substring(i, i+1).matches(partialRegexChecks[1])) 
                {
                	hasUpper = true;
                	strengthPercentage+=3;
                }
                if (password.substring(i, i+1).matches(partialRegexChecks[2])) 
                {
                	hasDigits = true;
                	strengthPercentage+=5;
                }
                if ((!password.substring(i, i+1).matches(partialRegexChecks[0])) && (!password.substring(i, i+1).matches(partialRegexChecks[1])) && (!password.substring(i, i+1).matches(partialRegexChecks[2])) )
                {
                	if (password.substring(i, i+1).matches(partialRegexChecks[3])) {
                		hasSymbols = true;
                		strengthPercentage+=8;  
                	}	
                }
                i = i+1;
            }
            
            // 12 Zeichen empfohlen
            int pwlengthsec = (int) Math.floor(Math.floor( Math.log((password.length()-6)*0.00002))+11);
            strengthPercentage = strengthPercentage * (1+(pwlengthsec/10)) *3.0f;

            if (! ((hasLower && hasUpper) && (hasDigits && hasSymbols) && (password.length()>=8) ))
            {
            	strengthPercentage = 0;
            }
            return Math.round(strengthPercentage);
        }


	
	//******************************************************************************************//*************************************************//
	// Registrierung:

	@RequestMapping(value = "/create_new_user", method = RequestMethod.POST)
	public String create_new_user(@RequestParam("mailIN") @Email @Valid final String  Mail, @RequestParam("usernameIN") @Valid final String Username, @RequestParam("passwordIN") @Valid final String  Password, @RequestParam("repasswordIN") @Valid final String RePassword, @RequestParam("nameIN") final String Name,  @RequestParam("firstnameIN") final String Firstname, @RequestParam("wohnen") final String Adresstyp, @RequestParam("flh_name") final Optional<String> Flh_name_OPT, @RequestParam("citypart") final Optional<String> Citypart_OPT, @RequestParam("street") final Optional<String> Street_OPT, @RequestParam("housenr") final Optional<String> Housenr_OPT, @RequestParam("postcode") final String Postcode, @RequestParam("city") final String City, @RequestParam("nativelanguage") final String Nativelanguage, @RequestParam("otherlanguages") final String OtherLanguages, @RequestParam("origin") final String Origin, @RequestParam("g-recaptcha-response")String CaptchaResponse)
	{
		if (CaptchaResponse.isEmpty() )
		{
		  return "error_empty_captcha"; 
		}
		
		
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
		
		if (Password.length()<8) 
		{
			System.out.println("Passwort zu kurz.");
			return "errorpage0_wrongpw";
		}else
		{
			int pwstrength=0; 
			pwstrength = checkPasswordStrength(Password);
			
			System.out.println("PasswordStrength: "+pwstrength);
			if (pwstrength==0)
			{
				System.out.println("Passwort erfüllt nicht die Anforderungen.");
				return "errorpage0_wrongpw";
			}
		}
		
		if (emailValidator(Mail) == false)
		{
			System.out.println(Mail+" ist eine ungültige Mailadresse.");
			return "error";
		}
		
		boolean equalMail = false;
		for (UserAccount TempUA: userAccountManager.findAll())
		{
			if ((!(TempUA == null)) && (equalMail == false))
			{
				//System.out.println(TempUA.getUsername());
				//System.out.println(TempUA.getEmail());
				
				if (!(TempUA.getEmail() == null))
				{
					if (TempUA.getEmail().equals(Mail))
					{
						equalMail = true;
					} 
				}	
			}
		}
		
		if (equalMail)
		{
			System.out.println(Mail+" ist eine bereits verwendete Mailadresse.");
			return "error";
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
				new Role("ROLE_NORMAL"));
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
		
		//Step 2
		
		System.out.println(Name);
		System.out.println(Firstname);
		System.out.println(Adresstyp);

	    	
			if (Name.isEmpty() ||  Firstname.isEmpty() || Adresstyp.isEmpty())
			{
				return "errorpage1_empty";
			}

			user_xyz.getUserAccount().setLastname(Name);
			user_xyz.getUserAccount().setFirstname(Firstname);
			user_xyz.setAdresstyp(Adresstyp);
			userAccountManager.save(user_xyz.getUserAccount());
			
			user_xyz.setRegistrationstate(2);
			userRepository.save(user_xyz);

			System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
			System.out.println("/not_activated/user/"+user_xyz.getUserAccount().getUsername() );
			
			if (Adresstyp.equals("refugee"))  //Refugees_home
			{
				//return "redirect:/new_user_aboutuser2a/user/{user}";
				
				String Postcode_N;
				String City_N;
				
				if ((Postcode.isEmpty()) || (Postcode.length()<=3))
				{
					Postcode_N="";
				}
				else
				{
					if (Postcode.substring(Postcode.length()-1).equals(","))
					{
						Postcode_N = Postcode.substring(0, Postcode.length()-1);	
					}
					else
					{
						Postcode_N= Postcode;
					}
					
					if (Postcode_N.substring(0,1).equals(","))
					{
						Postcode_N = Postcode_N.substring(1, Postcode_N.length());	
					}
				}
				
				if ((City.isEmpty()) || (City.length()<=2))
				{
					City_N="";
				}
				else
				{
					if (City.substring(City.length()-1).equals(","))
					{
						City_N = City.substring(0, City.length()-1);	
					}
					else 
					{
						City_N= City;
					}
					
					if (City_N.substring(0,1).equals(","))
					{
						City_N = City_N.substring(1, City_N.length());	
					}

				}
				
				System.out.println(Flh_name_OPT.get());
				System.out.println(Citypart_OPT.get());
				System.out.println(Postcode_N);
				System.out.println(City_N);
				
				String Flh_name;
				String Citypart;
				
				if (Flh_name_OPT.isPresent())
				{
					Flh_name = Flh_name_OPT.get();
				}
				else
				{
					Flh_name = "";
				}
				
				if (Citypart_OPT.isPresent())
				{
					Citypart = Citypart_OPT.get();
				}
				else
				{
					Citypart = "";
				}

				//KLASSE FLÜCHTLINGSHEIM UND INTERFACE LOCATION!!!!!!!!!!!!!!
					if ((Flh_name.isEmpty()) ||  (Citypart.isEmpty()) || Postcode_N.isEmpty() || City_N.isEmpty())
					{
						return "errorpage2a_empty";
					}
					
					if (Postcode_N.length()!=5) 
					{
						System.out.println("Ungültige Postleitzahl");
						return "error";
					}
					else
					{ 
						String[] partialRegexChecks = 
				        	{
				        			".*[a-z]+.*", // lower
				        			".*[A-Z]+.*", // upper
				        			".*[\\d]+.*", // digits
				        			".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
				        	};
						int i=0; 
			            while (i< 5)
			            {       	
			            	if (! Postcode_N.substring(i, i+1).matches(partialRegexChecks[2]))
			                {
			            		System.out.println("Ungültige Postleitzahl");
			    				return "error";
			                }
			            	i = i+1;
			            }	
					}
					
					
					Address address= new Address(Flh_name, Citypart, Postcode_N, City_N);			
					user_xyz.setLocation(address);
					user_xyz.setRegistrationstate(3); //3 ~ Flüchtlingsheim
					userRepository.save(user_xyz);

					System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
					//return "redirect:/new_user_language_origin/user/{user}";
			}

			if (Adresstyp.equals("helper"))  //Wohnung
			{
				//return "redirect:/new_user_aboutuser2b/user/{user}";
				
				String Postcode_N;
				String City_N;
				
				if ((Postcode.isEmpty()) || (Postcode.length()<=3))
				{
					Postcode_N="";
				}
				else
				{
					if (Postcode.substring(Postcode.length()-1).equals(","))
					{
						Postcode_N = Postcode.substring(0, Postcode.length()-1);	
					}
					else
					{
						Postcode_N= Postcode;
					}
					
					if (Postcode_N.substring(0,1).equals(","))
					{
						Postcode_N = Postcode_N.substring(1, Postcode_N.length());	
					}
				}
				
				if ((City.isEmpty()) || (City.length()<=2))
				{
					City_N="";
				}
				else
				{
					if (City.substring(City.length()-1).equals(","))
					{
						City_N = City.substring(0, City.length()-1);	
					}
					else 
					{
						City_N= City;
					}
					
					if (City_N.substring(0,1).equals(","))
					{
						City_N = City_N.substring(1, City_N.length());	
					}
				}
				
				System.out.println(Street_OPT.get());
				System.out.println(Housenr_OPT.get());
				System.out.println(Postcode_N);
				System.out.println(City_N);

				String Street;
				String Housenr;
				
				if (Street_OPT.isPresent())
				{
					Street = Street_OPT.get();
				}
				else
				{
					Street = "";
				}
				
				if (Housenr_OPT.isPresent())
				{
					Housenr = Housenr_OPT.get();
				}
				else
				{
					Housenr = "";
				}
				
					if (Street.isEmpty() ||  Housenr.isEmpty() || Postcode_N.isEmpty() || City_N.isEmpty())
					{
						return "errorpage2b_empty";
					}
					
					if (Postcode_N.length()!=5) 
					{
						System.out.println("Ungültige Postleitzahl");
						return "error";
					}
					else
					{ 
						String[] partialRegexChecks = 
				        	{
				        			".*[a-z]+.*", // lower
				        			".*[A-Z]+.*", // upper
				        			".*[\\d]+.*", // digits
				        			".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
				        	};
						int i=0; 
			            while (i< 5)
			            {       	
			            	if (! Postcode_N.substring(i, i+1).matches(partialRegexChecks[2]))
			                {
			            		System.out.println("Ungültige Postleitzahl");
			    				return "error";
			                }
			            	i = i+1;
			            }	
					}
					
					
					Address address= new Address(Street, Housenr, Postcode_N, City_N);
					user_xyz.setLocation(address);
					user_xyz.setRegistrationstate(4); //4 ~ Wohnung
					userRepository.save(user_xyz);
					
					System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());			
					//return "redirect:/new_user_language_origin/user/{user}";
			}
			
			// Step 3;
			
			System.out.println(Nativelanguage);
			System.out.println(OtherLanguages);
			System.out.println(Origin);
			
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
			//return "redirect:/reCAPTCHA/user/{user}";

			//Step 4;
			
			System.out.println("## CaptchaResponse:");
			System.out.println(CaptchaResponse);

			if (CaptchaResponse.isEmpty() )
			{
			  return "error"; 
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
			            user_xyz.setRegistrationstate(7); //7 ~ Captcha erfolgreich geprüft
						userRepository.save(user_xyz);
						
						
						System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());	
						
						Date zeitstempel = new Date();
	                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy HH:mm:ss"
	                    // simpleDateFormat.format(zeitstempel)
	                    
	                    String domain     = "http://localhost:8080";
	                    String link       = domain + "/activation/user/{"+user_xyz.getUserAccount().getUsername()+"}/{"+sha256(user_xyz.getActivationkey()+simpleDateFormat.format(zeitstempel)+(user_xyz.getRegistrationstate()+1))+"}";
	                    String mailtext = "<html> <head> </head> <body> <h1>Activation of your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")<h1> Hallo "+user_xyz.getUserAccount().getUsername()+" </h1><br/><br/> Please activate your RefugeesApp-Account with this link: <a href=\""+link+"\">Activationlink</a>  <br/><br/> Textlink: "+link+"  </body> </html>";
	                    String mailadresse = user_xyz.getUserAccount().getEmail();
						
						System.out.println(link);
						
						//Mail senden: 
	                    if (!mailadresse.equals("test@test.test"))                    
	                    {
	                        //Mail senden: 
	                        try {
	                            Mailsenden(mailadresse,"Activation of your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")",mailtext);
	                            System.out.println("Mail versandt");
	                        } catch (MessagingException | IOException e) {
	                            // TODO Auto-generated catch block
	                            e.printStackTrace();
	                        }   
	                    }

						
						user_xyz.setRegistrationstate(8); //8 ~ Aktivierungsmail versandt.
						userRepository.save(user_xyz);
					
						System.out.println("Registrationstate: "+user_xyz.getRegistrationstate());
	                    
	                    if (!mailadresse.equals("test@test.test"))
	                    {
	                        return "redirect:/activationmail_gesendet";
	                    }    
	                    else
	                    {
	                        return "redirect:/activationmail_local";
	                    }
			            
					
				}
				else
				{
					return "redirect:/reCAPTCHA-TEST";
				}
			}

	}
	
	//##################################################################################################################//
	//##################################################################################################################//
	
	@RequestMapping(value = "/create_temp_new_user", method = RequestMethod.POST)
	public String create_new_user_t(@RequestParam("mail") @Email @Valid final String  Mail, @RequestParam("username") @Valid final String Username, @RequestParam("password") @Valid final String  Password, @RequestParam("repassword") @Valid final String RePassword, @RequestParam("summe") @Valid final String chsumme)
	{

		System.out.println(Mail);
		System.out.println(Username);
		System.out.println(Password);
		System.out.println(RePassword);

		if (Mail.isEmpty() ||  Username.isEmpty() || Password.isEmpty() || chsumme.isEmpty())
		{
			return "errorpage0_empty";
		}

		if (!Password.equals(RePassword))
		{
			return "errorpage0_wrongpw";
		}
		
		if (Password.length()<8) 
		{
			System.out.println("Passwort zu kurz.");
			return "errorpage0_wrongpw";
		}else
		{
			int pwstrength=0; 
			pwstrength = checkPasswordStrength(Password);
			
			System.out.println("PasswordStrength: "+pwstrength);
			if (pwstrength==0)
			{
				System.out.println("Passwort erfüllt nicht die Anforderungen.");
				return "errorpage0_wrongpw";
			}
		}
		
		if (emailValidator(Mail) == false)
		{
			System.out.println(Mail+" ist eine ungültige Mailadresse.");
			return "error";
		}
		
		boolean equalMail = false;
		for (UserAccount TempUA: userAccountManager.findAll())
		{
			if ((!(TempUA == null)) && (equalMail == false))
			{
				//System.out.println(TempUA.getUsername());
				//System.out.println(TempUA.getEmail());
				
				if (!(TempUA.getEmail() == null))
				{
					if (TempUA.getEmail().equals(Mail))
					{
						equalMail = true;
					} 
				}	
			}
		}
		
		if (equalMail)
		{
			System.out.println(Mail+" ist eine bereits verwendete Mailadresse.");
			return "error";
		}

		
		Integer sum = zufallzahl_1 + zufallzahl_2;
		System.out.println(sum.toString()+ " =? "+ chsumme);
		
		if ( !(sum.toString().equals(chsumme)) )
		{
			return "redirect:/new_user_data";
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
				new Role("ROLE_NORMAL"));
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
	public String submit_userdata1(@PathVariable final String user, @RequestParam("name") final String Name, @RequestParam("firstname") final String Firstname, @RequestParam("Adresstyp") final String Adresstyp)
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
			user_xyz.setAdresstyp(Adresstyp);
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
	public String submit_userdata2a(@PathVariable final String user, @RequestParam("flh_name") final String Flh_name, @RequestParam("citypart") final String Citypart, @RequestParam("postcode") final String Postcode, @RequestParam("city") final String City)
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
			
			if (Postcode.length()!=5) 
			{
				System.out.println("Ungültige Postleitzahl");
				return "error";
			}
			else
			{ 
				String[] partialRegexChecks = 
		        	{
		        			".*[a-z]+.*", // lower
		        			".*[A-Z]+.*", // upper
		        			".*[\\d]+.*", // digits
		        			".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
		        	};
				int i=0; 
	            while (i< 5)
	            {       	
	            	if (! Postcode.substring(i, i+1).matches(partialRegexChecks[2]))
	                {
	            		System.out.println("Ungültige Postleitzahl");
	    				return "error";
	                }
	            	i = i+1;
	            }	
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
	public String submit_userdata2b(@PathVariable final String user, @RequestParam("street") final String Street, @RequestParam("housenr") final String Housenr, @RequestParam("postcode") final String Postcode, @RequestParam("city") final String City)
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
			
			if (Postcode.length()!=5) 
			{
				System.out.println("Ungültige Postleitzahl");
				return "error";
			}
			else
			{ 
				String[] partialRegexChecks = 
		        	{
		        			".*[a-z]+.*", // lower
		        			".*[A-Z]+.*", // upper
		        			".*[\\d]+.*", // digits
		        			".*[@#§$%&/()=?{}#+-~.,;:<>|\\!]+.*" // symbols
		        	};
				int i=0; 
	            while (i< 5)
	            {       	
	            	if (! Postcode.substring(i, i+1).matches(partialRegexChecks[2]))
	                {
	            		System.out.println("Ungültige Postleitzahl");
	    				return "error";
	                }
	            	i = i+1;
	            }	
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
	public String submit_language_origin(@PathVariable final String user, @RequestParam("nativelanguage") final String Nativelanguage, @RequestParam("otherlanguages") final String OtherLanguages, @RequestParam("origin") final String Origin)
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

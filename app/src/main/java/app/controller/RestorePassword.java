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
public class RestorePassword {
	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
    private final MailSender mailSender;

	/**
	   * Autowire.
	   * @param CreateNewUser
	   */
	@Autowired
	public RestorePassword (UserAccountManager userAccountManager, UserRepository userRepository , MailSender mailSender){
		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");

		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		
		this.mailSender = mailSender;
	}
	
	
	@RequestMapping({"/restorePassword"})
	public String restorePassword(){
		return "/restorePassword";
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
	
	@RequestMapping(value = "/submit_captcha_restorePW")
	public String recieve_reCAPTCHA(){

		
				return "redirect:/";
		
	}
	
	@RequestMapping(value = "/submit_captcha_restorePW", method = RequestMethod.POST)
	public String recieve_reCAPTCHA_user(@RequestParam("username") @Valid final String Username, @RequestParam("g-recaptcha-response")String CaptchaResponse){

		System.out.println("## CaptchaResponse:");
		System.out.println(CaptchaResponse);

		if (CaptchaResponse.isEmpty() || (!userAccountManager.findByUsername(Username).isPresent()))
		{
			return "redirect:/";		   
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
				if(userAccountManager.findByUsername(Username).isPresent()){
		            User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(Username).get());					
					
		            if (user_xyz.getUserAccount().getEmail().isEmpty()) 
		            {
		            	return "redirect:/";	
		            }
		            
		            
					Date zeitstempel = new Date();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy"); // "dd.MM.yyyy HH:mm:ss"
                    // simpleDateFormat.format(zeitstempel)
                    
                    String NewPassword = sha256(user_xyz.getActivationkey() + zeitstempel).substring(4, 14); 
                    
                    String domain     = "http://localhost:8080";
                    String mailtext = "<html> <head> </head> <body> <h1>New Password for your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")<h1> Hallo "+user_xyz.getUserAccount().getUsername()+" </h1><br/><br/> This is your new temp Password: "+NewPassword+" </body> </html>";
                    String mailadresse = user_xyz.getUserAccount().getEmail();
					
					
					//Mail senden: 
                    if (!mailadresse.equals("test@test.test"))                    
                    {
                        //Mail senden: 
                        try {
                            Mailsenden(mailadresse,"New Password for your RefugeesApp-Account ("+user_xyz.getUserAccount().getUsername()+")",mailtext);
                            System.out.println("Mail versandt");
                        } catch (MessagingException | IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }   
                    }
                    else
                    {
                    	System.out.println(NewPassword);
                    }

					
                    userAccountManager.changePassword(user_xyz.getUserAccount(), NewPassword);
					userRepository.save(user_xyz);
					
					System.out.println(NewPassword);
                    
                    if (!mailadresse.equals("test@test.test"))
                    {
                        return "redirect:/login";
                    }    
                    else
                    {
                        return "redirect:/login";
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
	                         
}
	
	

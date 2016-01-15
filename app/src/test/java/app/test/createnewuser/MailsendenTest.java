package app.test.createnewuser;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import javax.mail.MessagingException;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import app.controller.HelpFunctions;
import app.model.User;
import app.model.UserRepository;

public class MailsendenTest {

	@Autowired
	UserRepository userRepository;
	@Autowired
	UserAccountManager userAccountManager;

	@Test
	public void createMailSendenTest() {
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		String mailadresse = user1.getUserAccount().getEmail();

		boolean erfolgreich = false;

		try {
			HelpFunctions.mailSenden(mailadresse,
					"Activation of your RefugeesApp-Account (" + user1.getUserAccount().getUsername() + ")", "Test");

			erfolgreich = true;
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, erfolgreich);
	}
}

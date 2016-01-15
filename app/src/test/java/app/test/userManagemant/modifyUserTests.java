package app.test.userManagemant;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import app.model.Language;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;
import app.test.coordinates.AbstractWebIntegrationTests;

public class modifyUserTests extends AbstractWebIntegrationTests{
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserAccountManager userAccountManager;
	@Autowired
	LanguageRepository languageRepository;
	@Autowired
	TagsRepository tagsRepository;
	@Autowired
	GoodsRepository goodsRepository;
	@Autowired
	ActivitiesRepository activitiesRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Test
	public void modifyLanguagesTest() throws Exception{
		
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(
				authenticationManager.authenticate(authentication));

		mvc.perform(
				get("/modifyLanguages").with(user("testUser1").roles("NORMAL"))).//
				andExpect(status().isOk()).//
				andExpect(view().name("modifyLanguages")).//
				andExpect(model().attributeExists("languages")).
				andExpect(model().attributeExists("user"));

		//1.Sprache
		mvc.perform(
				post("/modifyLanguages_submit")
						.param("nativelanguage", "de")
						.param("otherlanguages", "")
						.with(user("testUser1").roles("NORMAL"))).
				andExpect(view().name("redirect:/data"));
		
		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		
		assertEquals("de",user1.getPrefLanguage().getkennung());
		//1.&2.Sprache
		mvc.perform(
				post("/modifyLanguages_submit")
						.param("nativelanguage", "en")
						.param("otherlanguages", "ar")
						.with(user("testUser1").roles("NORMAL"))).
				andExpect(view().name("redirect:/data"));
		
		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		
		assertEquals("en",user1.getPrefLanguage().getkennung());
		for(Language language:user1.getLanguages())	{
			assertTrue((language.getkennung().equals("en"))
					||(language.getkennung().equals("ar")));
		}
		
		//1.&2.&3.Sprache
		mvc.perform(
				post("/modifyLanguages_submit")
						.param("nativelanguage", "en")
						.param("otherlanguages", "ar,de")
						.with(user("testUser1").roles("NORMAL"))).
				andExpect(view().name("redirect:/data"));
		
		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		
		assertEquals("en",user1.getPrefLanguage().getkennung());
		for(Language language:user1.getLanguages())	{
			assertTrue((language.getkennung().equals("en"))
					||(language.getkennung().equals("ar"))
					||(language.getkennung().equals("de")));
		}
	}
	
	@Test
	public void changePwTest() throws Exception{
		
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser7").get());
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"testUser7", "pw");
		SecurityContextHolder.getContext().setAuthentication(
				authenticationManager.authenticate(authentication));
		
		mvc.perform(
				get("/changePassword/{user}",user1.getUserAccount().getUsername())
				.with(user("testUser7").roles("NORMAL"))).
				andExpect(status().isOk()).
				andExpect(view().name("changePassword")).
				andExpect(model().attributeExists("userAccount"));

		
		mvc.perform(
				post("/changePassword_submit/{user}", user1.getUserAccount().getUsername())
						.param("actualPassword", "pw")
						.param("newPassword1", "Test08.12")
						.param("newPassword2", "Test08.12")
						.with(user("testUser7").roles("NORMAL"))).
				andExpect(view().name("redirect:/data"));
		

		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser7").get());
		/*
		mvc.perform(
				post("/changePassword_submit/{user}", user1.getUserAccount().getUsername())
						.param("actualPassword", "pw")
						.param("newPassword1", "Test08.12")
						.param("newPassword2", "Test08.12")
						.with(user("testUser7").roles("NORMAL"))).
				andExpect(view().name("redirect:/"));
		*/
		mvc.perform(
				post("/changePassword_submit/{user}", user1.getUserAccount().getUsername())
						.param("actualPassword", "Test08.12")
						.param("newPassword1", "Test0812")
						.param("newPassword2", "Test0812")
						.with(user("testUser7").roles("NORMAL"))).
				andExpect(view().name( "redirect:/changePassword/{user}"));
		
		mvc.perform(
				post("/changePassword_submit/{user}", user1.getUserAccount().getUsername())
						.param("actualPassword", "Test08.12")
						.param("newPassword1", "est08.12")
						.param("newPassword2", "est08.12")
						.with(user("testUser7").roles("NORMAL"))).
				andExpect(view().name("redirect:/changePassword/{user}"));
		
		mvc.perform(
				post("/changePassword_submit/{user}", user1.getUserAccount().getUsername())
						.param("actualPassword", "Test08.12")
						.param("newPassword1", "Test.Test")
						.param("newPassword2", "Test.Test")
						.with(user("testUser7").roles("NORMAL"))).
				andExpect(view().name( "redirect:/changePassword/{user}"));
		
		mvc.perform(
				post("/changePassword_submit/{user}", user1.getUserAccount().getUsername())
						.param("actualPassword", "Test08.12")
						.param("newPassword1", "TEST08.12")
						.param("newPassword2", "TEST08.12")
						.with(user("testUser7").roles("NORMAL"))).
				andExpect(view().name( "redirect:/changePassword/{user}"));
	}
	
	@Test
	public void modifyUserAccount() throws Exception{
		
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser2").get());
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"testUser2", "pw");
		SecurityContextHolder.getContext().setAuthentication(
				authenticationManager.authenticate(authentication));
		
		mvc.perform(
				get("/modifyUserAccount/{user}",user1.getUserAccount().getUsername())
				.with(user("testUser2").roles("NORMAL"))).
				andExpect(status().isOk()).
				andExpect(view().name("modifyUserAccount")).
				andExpect(model().attributeExists("userAccount"));

		
		mvc.perform(
				post("/modifyUserAccount_submit/{user}", user1.getUserAccount().getUsername())
						.param("firstname", "Hans")
						.param("lastname", "Hahn")
						.param("email", "test@test.test")
						.with(user("testUser2").roles("NORMAL"))).
				andExpect(view().name("redirect:/data"));
		
		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser2").get());
		
		assertEquals("Hans",user1.getUserAccount().getFirstname());
		assertEquals("Hahn",user1.getUserAccount().getLastname());
		assertEquals("test@test.test",user1.getUserAccount().getEmail());
		
	}
	


}

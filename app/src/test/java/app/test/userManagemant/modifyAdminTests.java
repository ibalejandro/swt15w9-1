package app.test.userManagemant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Optional;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.Language;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;
import app.test.coordinates.AbstractWebIntegrationTests;

public class modifyAdminTests extends AbstractWebIntegrationTests {

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
	public void modifyAllTest() throws Exception {
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"admin", "123");
		SecurityContextHolder.getContext().setAuthentication(
				authenticationManager.authenticate(authentication));

		mvc.perform(
				get("/modify/user/{user}", user1.getUserAccount().getUsername())
						.with(user("admin").roles("ADMIN")))
				.andExpect(status().isOk()).andExpect(view().name("modify"))
				.andExpect(model().attributeExists("languages"))
				.andExpect(model().attributeExists("user"));

		mvc.perform(
				post("/modify_submit/user/{user}",
						user1.getUserAccount().getUsername())
						.param("mailIN", "test@test.test")
						.param("nameIN", "Cam")
						.param("firstnameIN", "Bea")
						.param("wohnen", "refugee")
						.param("flh_name", "NUK Friedrichshagen")
						.param("citypart", "Treptow-Köpenick")
						.param("street", user1.getLocation().getStreet())
						.param("housenr", user1.getLocation().getHousenr())
						.param("postcode_R", "12587")
						.param("city_R", "Berlin")
						.param("postcode_H", user1.getLocation().getZipCode())
						.param("city_H", user1.getLocation().getCity())
						.param("nativelanguage", "ar")
						.param("otherlanguages", "de,es")
						.param("origin",
								"United Arab Emirates, Vereinigte Arabische Emirate (AE)")
						.with(user("admin").roles("ADMIN"))).andExpect(
				view().name("redirect:/userDetails"));

		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());

		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());

		assertEquals("test@test.test", user1.getUserAccount().getEmail());
		assertEquals("Cam", user1.getUserAccount().getLastname());
		assertEquals("Bea", user1.getUserAccount().getFirstname());
		assertEquals("Refugees_home", user1.getAddresstypString());
		assertEquals("NUK Friedrichshagen", user1.getLocation().getFlh_name());
		assertEquals("Treptow-Köpenick", user1.getLocation().getCityPart());
		assertEquals("12587", user1.getLocation().getZipCode());
		assertEquals("Berlin", user1.getLocation().getCity());
		assertEquals("ar", user1.getPrefLanguage().getkennung());
		assertEquals("United Arab Emirates, Vereinigte Arabische Emirate (AE)",
				user1.getOrigin());
	}

	@Test
	public void modifyJustLanguages() throws Exception {
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		Authentication authentication = new UsernamePasswordAuthenticationToken(
				"admin", "123");
		SecurityContextHolder.getContext().setAuthentication(
				authenticationManager.authenticate(authentication));

		mvc.perform(
				post("/modify_submit/user/{user}", user1)
						.param("mailIN", user1.getUserAccount().getEmail())
						.param("nameIN", user1.getUserAccount().getLastname())
						.param("firstnameIN",
								user1.getUserAccount().getFirstname())
						.param("wohnen", "helper")
						.param("flh_name", user1.getLocation().getFlh_name())
						.param("citypart", user1.getLocation().getStreet())
						.param("street", user1.getLocation().getStreet())
						.param("housenr", user1.getLocation().getHousenr())
						.param("postcode_R", user1.getLocation().getZipCode())
						.param("city_R", user1.getLocation().getCity())
						.param("postcode_H", user1.getLocation().getZipCode())
						.param("city_H", user1.getLocation().getCity())
						.param("nativelanguage", "en")
						.param("otherlanguages", "ar,es")
						.param("origin", user1.getOrigin())
						.with(user("admin").roles("ADMIN"))).andExpect(
				view().name("redirect:/userDetails"));

		user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());

		assertEquals("en", user1.getPrefLanguage().getkennung());
		for (Language language : user1.getLanguages()) {
			assertTrue((language.getkennung().equals("en"))
					|| (language.getkennung().equals("es"))
					|| (language.getkennung().equals("ar")));
		}
	}
}

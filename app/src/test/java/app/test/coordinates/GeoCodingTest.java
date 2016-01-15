package app.test.coordinates;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import app.model.Address;
import app.model.Coordinates;
import app.model.User;
import app.model.UserRepository;
import app.model.User.AddresstypEnum;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;

@Ignore("Sparen von immer gleichen API-Anfragen, verhindern von !OVER QUERY LIMIT!")
public class GeoCodingTest extends AbstractWebIntegrationTests {

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

	public void prepaire() {

		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		User user2 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser2").get());
		User user3 = userRepository.findByUserAccount(userAccountManager.findByUsername("Lisa").get());
		User user4 = userRepository.findByUserAccount(userAccountManager.findByUsername("Peter").get());
		User user5 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser5").get());
		User user6 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser6").get());
		User user7 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser7").get());

		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		// 2.44km
		Address testAddressWohnung2 = new Address("Prager Str.", "10", "01069", "Dresden");
		// 158.49km
		Address testAddressWohnung3 = new Address("Scharnweberstr.", "22 A", "12587", "Berlin");
		// 45km
		Address testAddressWohnung4 = new Address("Merzdorfer Str.", "21 - 25", "01591", "Riesa");
		// 100.27
		Address testAddressWohnung5 = new Address("Martin-Luther-Ring", "4-6", "04109", "Leipzig");
		// 10.83km
		Address testAddressWohnung6 = new Address("Wittgensdorfer Str.", "20", "01731", "Kreischa");
		// 60km
		Address testAddressWohnung7 = new Address("Reichenhainer Straße", "41", "09126", "Chemnitz");

		if (!user1.isOldLocation(testAddressWohnung1)) {
			user1.setLocation(testAddressWohnung1);
			user1.setAddresstyp(AddresstypEnum.Wohnung);
			userRepository.save(user1);
			user1.setCoordinates(user1.createCoordinates());
			userRepository.save(user1);
		}

		if (!user2.isOldLocation(testAddressWohnung2)) {
			user2.setLocation(testAddressWohnung2);
			user2.setAddresstyp(AddresstypEnum.Wohnung);
			userRepository.save(user2);
			user2.setCoordinates(user2.createCoordinates());
			userRepository.save(user2);
		}

		if (!user3.isOldLocation(testAddressWohnung3)) {
			user3.setLocation(testAddressWohnung3);
			user3.setAddresstyp(AddresstypEnum.Wohnung);
			userRepository.save(user3);
			user3.setCoordinates(user3.createCoordinates());
			userRepository.save(user3);
		}

		if (!user4.isOldLocation(testAddressWohnung4)) {
			user4.setLocation(testAddressWohnung4);
			user4.setAddresstyp(AddresstypEnum.Wohnung);
			userRepository.save(user4);
			user4.setCoordinates(user4.createCoordinates());
			userRepository.save(user4);
		}

		if (!user5.isOldLocation(testAddressWohnung5)) {
			user5.setLocation(testAddressWohnung5);
			user5.setAddresstyp(AddresstypEnum.Wohnung);
			userRepository.save(user5);
			user5.setCoordinates(user5.createCoordinates());
			userRepository.save(user5);
		}

		if (!user6.isOldLocation(testAddressWohnung6)) {
			user6.setLocation(testAddressWohnung6);
			user6.setAddresstyp(AddresstypEnum.Wohnung);
			userRepository.save(user6);
			user6.setCoordinates(user6.createCoordinates());
			userRepository.save(user6);
		}

		if (!user7.isOldLocation(testAddressWohnung7)) {
			user7.setLocation(testAddressWohnung7);
			user7.setAddresstyp(AddresstypEnum.Wohnung);
			userRepository.save(user7);
			user7.setCoordinates(user7.createCoordinates());
			userRepository.save(user7);
		}
	}

	@Test
	public void createCoordinatesTest() {
		prepaire();
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		User user2 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser2").get());
		Address testAddressWohnung3 = new Address("Scharnweberstr.", "22 A", "12587", "Berlin");
		Address testAddressRefugee1 = new Address("", "", "Dresden 6", "Südvorstadt", "01187", "Dresden");
		Address testAddressRefugee2 = new Address("", "", "NUK Friedrichshagen", "Friedrichshagen", "12587", "Berlin");
		double delta = 0.00001;

		// mittels 2x createCoordinate, Coordinates.getLatitude()
		assertEquals(51.045846, user2.createCoordinates().getLatitude(), delta);
		assertEquals(13.736928, user2.createCoordinates().getLongitude(), delta);

		// createCoordinates(), setCoordinates(), User.getLatitude()
		Coordinates cu1 = user1.createCoordinates();
		user1.setCoordinates(cu1);
		userRepository.save(user1);
		assertEquals(51.025451, user1.getLatitude(), delta);
		assertEquals(13.722943, user1.getLongitude(), delta);

		// mit setLocation
		user1.setLocation(testAddressWohnung3);
		userRepository.save(user1);
		Coordinates cu2 = user1.createCoordinates();
		user1.setCoordinates(cu2);
		userRepository.save(user1);
		assertEquals(52.45002, user1.getLatitude(), delta);
		assertEquals(13.62774, user1.getLongitude(), delta);

		user1.setLocation(testAddressRefugee1);
		userRepository.save(user1);
		user1.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user1);
		Coordinates cu3 = user1.createCoordinates();
		user1.setCoordinates(cu3);
		userRepository.save(user1);
		assertEquals(51.034958, user1.getLatitude(), delta);
		assertEquals(13.728136, user1.getLongitude(), delta);

		user2.setLocation(testAddressRefugee2);
		user2.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user2);
		assertEquals(52.461374, user2.createCoordinates().getLatitude(), delta);
		assertEquals(13.640606, user2.createCoordinates().getLongitude(), delta);
	}

	@Test
	public void modifyAddressHelper() throws Exception {
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		double delta = 0.00001;

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(get("/modifyAddress").with(user("testUser1").roles("NORMAL"))).//
				andExpect(status().isOk()).//
				andExpect(view().name("modifyAddress")).//
				andExpect(model().attributeExists("userAccount"));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "Prager Str.")
				.param("housenr", "10").param("postcode_H", "01069").param("city_H", "Dresden")
				.with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		assertEquals(51.045846, user1.getLatitude(), delta);
		assertEquals(13.736928, user1.getLongitude(), delta);

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "Nöthnitzer Str.")
				.param("housenr", "46").param("postcode_H", "01187").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		assertEquals(51.025451, user1.getLatitude(), delta);
		assertEquals(13.722943, user1.getLongitude(), delta);

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "Altplauen")
				.param("housenr", "19").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		assertEquals(51.02992, user1.getLatitude(), delta);
		assertEquals(13.70262, user1.getLongitude(), delta);

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("housenr", "2")
				.with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		assertEquals(51.029727, user1.getLatitude(), delta);
		assertEquals(13.70582, user1.getLongitude(), delta);
	}

	@Test
	public void modifyAddressRefugee() throws Exception {
		User user3 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		double delta = 0.00001;

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "refugee").param("flh_name", "Dresden 6")
				.param("citypart", "Südvorstadt").param("postcode_R", "01187").param("city_R", "Dresden")
				.with(user("testUser2").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user3 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		assertEquals(51.034958, user3.getLatitude(), delta);
		assertEquals(13.728136, user3.getLongitude(), delta);
	}

	@Test
	public void modifyAddressAdmin() throws Exception {
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		String user = "testUser1";
		double delta = 0.00001;

		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "123");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modify_submit/user/{user}", user).param("wohnen", "helper").param("street", "Prager Str.")
				.param("housenr", "10").param("postcode_H", "01069").param("city_H", "Dresden")
				.param("nativelanguage", "de").param("otherlanguages", "en").with(user("admin").roles("ADMIN"))).//
				andExpect(view().name("redirect:/userDetails"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		assertEquals(51.045846, user1.getLatitude(), delta);
		assertEquals(13.736928, user1.getLongitude(), delta);
	}
}

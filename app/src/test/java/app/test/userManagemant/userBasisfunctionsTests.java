package app.test.userManagemant;

import static org.junit.Assert.*;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import app.model.Address;
import app.model.Language;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;
import app.test.coordinates.AbstractWebIntegrationTests;

public class userBasisfunctionsTests extends AbstractWebIntegrationTests {

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
	public void addressTest() {
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");

		user1.setLocation(testAddressWohnung1);
		assertTrue(user1.isOldLocation(testAddressWohnung1));
	}

	@Test
	public void prefLanguageTest() {
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		Language l1 = languageRepository.findOne(1L);
		user1.setPrefLanguage(l1);

		assertEquals(l1.getkennung(), user1.getPrefLanguage().getkennung());
		assertEquals(l1.getName(), user1.getPrefLanguage().getName());

	}

	@Test
	public void otherLanguageTest() {
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		Language l1 = languageRepository.findOne(1L);
		Language l2 = languageRepository.findOne(2L);
		Language l3 = languageRepository.findOne(3L);

		user1.setPrefLanguage(l1);
		user1.removeAllLanguages();
		for (Language language : user1.getLanguages()) {
			assertTrue(language.getkennung().equals(l1.getkennung()));
			assertTrue(language.getName().equals(l1.getName()));
		}

		user1.setLanguage(l2);
		user1.setLanguage(l3);
		userRepository.save(user1);
		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		for (Language language : user1.getLanguages()) {
			assertTrue((language.getkennung().equals(l1.getkennung()) || (language.getkennung().equals(l2.getkennung()))
					|| (language.getkennung().equals(l3.getkennung()))));
			assertTrue((language.getName().equals(l1.getName()) || (language.getName().equals(l2.getName()))
					|| (language.getName().equals(l3.getName()))));
		}
		user1.removeLanguage(l3);
		userRepository.save(user1);
		languageRepository.save(l3);
		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		for (Language language : user1.getLanguages()) {
			assertFalse(language.getkennung().equals(l3.getkennung()));
			assertFalse(language.getName().equals(l3.getName()));
		}
	}

}

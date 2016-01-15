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

import app.model.Address;
import app.model.User;
import app.model.User.AddresstypEnum;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;
import app.test.coordinates.AbstractWebIntegrationTests;

public class modifyAdressTests extends AbstractWebIntegrationTests {

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
	public void modifyAddressWohnung() throws Exception {
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(get("/modifyAddress").with(user("testUser1").roles("NORMAL"))).andExpect(status().isOk())
				.andExpect(view().name("modifyAddress")).andExpect(model().attributeExists("user"));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "Prager Str.")
				.param("housenr", "10").param("postcode_H", "00000").param("city_H", "Dresden")
				.with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Wohnung", user1.getAddresstypString());
		assertEquals("Prager Str.", user1.getLocation().getStreet());
		assertEquals("10", user1.getLocation().getHousenr());
		assertEquals("00000", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
	}

	@Test
	public void modifyAddressRefugee() throws Exception {
		Address testAddressRefugee1 = new Address("", "", "Dresden 6", "Südvorstadt", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressRefugee1);
		user1.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(get("/modifyAddress").with(user("testUser1").roles("NORMAL"))).andExpect(status().isOk())
				.andExpect(view().name("modifyAddress")).andExpect(model().attributeExists("isRefugee"))
				.andExpect(model().attributeExists("user"));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "refugee").param("flh_name", "Dresden 6")
				.param("citypart", "Südvorstadt").param("postcode_R", "99999").param("city_R", "Dresden")
				.with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Refugees_home", user1.getAddresstypString());
		assertEquals("", user1.getLocation().getStreet());
		assertEquals("", user1.getLocation().getHousenr());
		assertEquals("99999", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
		assertEquals("Dresden 6", user1.getLocation().getFlh_name());
		assertEquals("Südvorstadt", user1.getLocation().getCityPart());
	}

	@Test
	public void modifyAddressToRefugee() throws Exception {
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "refugee").param("flh_name", "Dresden 6")
				.param("citypart", "Südvorstadt").param("postcode_R", "99999").param("city_R", "Dresden")
				.param("street", "Nöthnitzer Str.").param("housenr", "46").param("postcode_H", "01187")
				.param("city_H", "DD").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Refugees_home", user1.getAddresstypString());
		assertEquals("", user1.getLocation().getStreet());
		assertEquals("", user1.getLocation().getHousenr());
		assertEquals("99999", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
		assertEquals("Dresden 6", user1.getLocation().getFlh_name());
		assertEquals("Südvorstadt", user1.getLocation().getCityPart());
	}

	@Test
	public void modifyAddressToWohnung() throws Exception {
		Address testAddressRefugee1 = new Address("", "", "Dresden 6", "Südvorstadt", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressRefugee1);
		user1.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("flh_name", "Dresden 6")
				.param("citypart", "Südvorstadt").param("postcode_R", "01187").param("city_R", "DD")
				.param("street", "Nöthnitzer Str.").param("housenr", "46").param("postcode_H", "00000")
				.param("city_H", "Dresden").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Wohnung", user1.getAddresstypString());
		assertEquals("Nöthnitzer Str.", user1.getLocation().getStreet());
		assertEquals("46", user1.getLocation().getHousenr());
		assertEquals("00000", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
		assertEquals("", user1.getLocation().getFlh_name());
		assertEquals("", user1.getLocation().getCityPart());
	}

	@Test
	public void modifyAddressToWohnungFail() throws Exception {
		Address testAddressRefugee1 = new Address("", "", "Dresden 6", "Südvorstadt", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressRefugee1);
		user1.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("flh_name", "Dresden 6")
				.param("citypart", "Südvorstadt").param("postcode_R", "01187").param("city_R", "DD").param("street", "")
				.param("housenr", "").param("postcode_H", "00000").param("city_H", "Dresden")
				.with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Refugees_home", user1.getAddresstypString());
		assertEquals("", user1.getLocation().getStreet());
		assertEquals("", user1.getLocation().getHousenr());
		assertEquals("01187", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
		assertEquals("Dresden 6", user1.getLocation().getFlh_name());
		assertEquals("Südvorstadt", user1.getLocation().getCityPart());
	}

	@Test
	public void modifyAddressRefugeeFailZipCode() throws Exception {
		Address testAddressRefugee1 = new Address("", "", "Dresden 6", "Südvorstadt", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressRefugee1);
		user1.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "refugee").param("flh_name", "Dresden 6")
				.param("citypart", "Südvorstadt").param("postcode_R", "1111").param("city_R", "Dresden")
				.with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Refugees_home", user1.getAddresstypString());
		assertEquals("", user1.getLocation().getStreet());
		assertEquals("", user1.getLocation().getHousenr());
		assertEquals("01187", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
		assertEquals("Dresden 6", user1.getLocation().getFlh_name());
		assertEquals("Südvorstadt", user1.getLocation().getCityPart());
	}

	@Test
	public void modifyAddressWohnungFailZipCode() throws Exception {
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "Prager Str.")
				.param("housenr", "10").param("postcode_H", "0000").param("city_H", "Dresden")
				.with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Wohnung", user1.getAddresstypString());
		assertEquals("Prager Str.", user1.getLocation().getStreet());
		assertEquals("10", user1.getLocation().getHousenr());
		assertEquals("01187", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
	}

	@Test
	public void modifyAddressWohnungPart1() throws Exception {
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "").param("housenr", "10")
				.param("postcode_H", "00000").param("city_H", "Dresden").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Wohnung", user1.getAddresstypString());
		assertEquals("Nöthnitzer Str.", user1.getLocation().getStreet());
		assertEquals("10", user1.getLocation().getHousenr());
		assertEquals("00000", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
	}

	@Test
	public void modifyAddressWohnungPart2() throws Exception {
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "").param("housenr", "")
				.param("postcode_H", "00000").param("city_H", "Dresden").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Wohnung", user1.getAddresstypString());
		assertEquals("Nöthnitzer Str.", user1.getLocation().getStreet());
		assertEquals("46", user1.getLocation().getHousenr());
		assertEquals("00000", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
	}

	@Test
	public void modifyAddressWohnungPart3() throws Exception {
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "").param("housenr", "28")
				.param("postcode_H", "").param("city_H", "Dresden").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Wohnung", user1.getAddresstypString());
		assertEquals("Nöthnitzer Str.", user1.getLocation().getStreet());
		assertEquals("28", user1.getLocation().getHousenr());
		assertEquals("01187", user1.getLocation().getZipCode());
		assertEquals("Dresden", user1.getLocation().getCity());
	}

	@Test
	public void modifyAddressWohnungPart4() throws Exception {
		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46", "01187", "Dresden");
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);

		Authentication authentication = new UsernamePasswordAuthenticationToken("testUser1", "pw");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(post("/modifyAddress_submit").param("wohnen", "helper").param("street", "").param("housenr", "5")
				.param("postcode_H", "").param("city_H", "Hamburg").with(user("testUser1").roles("NORMAL"))).//
				andExpect(view().name("redirect:/data"));

		user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());

		assertEquals("Wohnung", user1.getAddresstypString());
		assertEquals("Nöthnitzer Str.", user1.getLocation().getStreet());
		assertEquals("5", user1.getLocation().getHousenr());
		assertEquals("01187", user1.getLocation().getZipCode());
		assertEquals("Hamburg", user1.getLocation().getCity());
	}

}

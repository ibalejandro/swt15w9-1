package app.test.view;

import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import app.model.User;
import app.model.UserRepository;
import app.model.User.AddresstypEnum;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;
import app.test.coordinates.AbstractWebIntegrationTests;

public class viewTests extends AbstractWebIntegrationTests {

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
	public void viewTestAdmin() throws Exception {
		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("testUser1").get());
		Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "123");
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));

		mvc.perform(get("/userDetails").with(user("admin").roles("ADMIN"))).//
				andExpect(status().isOk()).//
				andExpect(view().name("userDetails")).//
				andExpect(model().attributeExists("userDetails"));

		mvc.perform(post("/searchUser").param("userNameIN", "tester").with(user("admin").roles("ADMIN")))
				.andExpect(view().name("redirect:/userDetails"));

		User user4 = userRepository.findByUserAccount(userAccountManager.findByUsername("Peter").get());
		user4.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user4);
		mvc.perform(post("/searchUser").param("userNameIN", "Peter").with(user("admin").roles("ADMIN")))
				.andExpect(view().name("data_refugee"));

		user4 = userRepository.findByUserAccount(userAccountManager.findByUsername("Peter").get());
		user4.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user4);
		mvc.perform(post("/searchUser").param("userNameIN", "Peter").with(user("admin").roles("ADMIN")))
				.andExpect(view().name("data"));
	}
}

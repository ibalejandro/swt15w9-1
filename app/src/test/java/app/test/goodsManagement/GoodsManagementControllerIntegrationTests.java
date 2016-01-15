package app.test.goodsManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import app.model.GoodEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;

@Ignore
public class GoodsManagementControllerIntegrationTests extends AbstractWebIntegrationTests {

	private GoodEntity good1, good2, good3, good4, good5;
	private static int iterableSizeWithOnlyOneSave = 1;
	private static int iterableSize = 4;
	private static int user1Goods = 1;

	@Autowired
	GoodsRepository goodsRepository;
	@Autowired
	TagsRepository tagsRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserAccountManager userAccountManager;

	@Autowired
	AuthenticationManager authenticationManager;

	protected void login(String userName, String password) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userName, password);
		SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
	}

	@Before
	public void createGoodEntities() {
		String name1 = "Bicycle";
		String description1 = "This bicycle is for girls under 12 years old. It's " + "pink and purple";
		String name2 = "Jacket";
		String description2 = "The jacket is for men. It's black with a gray hood";
		String name3 = "Soccer ball";
		String description3 = "This is an almost new soccer ball for teenagers";
		String name4 = "Winter gloves";
		String description4 = "The winter gloves are for women's size S";
		String name5 = "Beautiful bear";
		String description5 = "A beautiful little bear for beautiful little girls";

		TagEntity tag1 = tagsRepository.findByName("Sporting Goods");
		TagEntity tag2 = tagsRepository.findByName("Clothes, Shoes and Accesories");
		TagEntity tag3 = tagsRepository.findByName("Dolls & Bears");

		User user1 = userRepository.findByUserAccount(userAccountManager.findByUsername("Lisa").get());
		User user2 = userRepository.findByUserAccount(userAccountManager.findByUsername("Peter").get());

		login(user1.getUserAccount().getUsername(), "pw");

		good1 = new GoodEntity(name1, description1, tag1, null, user1);
		good2 = new GoodEntity(name2, description2, tag2, null, user2);
		good3 = new GoodEntity(name3, description3, tag1, null, user1);
		good4 = new GoodEntity(name4, description4, tag2, null, user1);
		good5 = new GoodEntity(name5, description5, tag3, null, user1);
	}

	@Ignore
	@Test
	public void testListUserOfferedGoods() throws Exception {
		goodsRepository.save(good1);
		goodsRepository.save(good2);
		mvc.perform(get("/myOfferedGoods")).andExpect(status().isOk())
				.andExpect(model().attribute("resultGoods", is(not(emptyIterable())))).andExpect(model()
						.attribute("resultGoods", is(iterableWithSize(iterableSizeWithOnlyOneSave + user1Goods))));
	}

	@Test
	public void testShowGoodToUpdate() throws Exception {
		GoodEntity savedGood = goodsRepository.save(good3);
		mvc.perform(post("/update").param("id", String.valueOf(savedGood.getId()))).andExpect(status().isOk())
				.andExpect(model().attribute("good", is(not(emptyIterable()))))
				.andExpect(model().attribute("good", Matchers.hasProperty("name", Matchers.equalTo(good3.getName()))))
				.andExpect(model().attribute("good",
						Matchers.hasProperty("description", Matchers.equalTo(good3.getDescription()))))
				/*
				 * .andExpect (model().attribute("good",
				 * Matchers.hasProperty("tag", Matchers.equalTo
				 * (good3.getTag()))))
				 */
				.andExpect(model().attribute("good", Matchers.hasProperty("user", Matchers.equalTo(good3.getUser()))));
	}

	@Test
	public void testUpdateGood() throws Exception {
		GoodEntity savedGood = goodsRepository.save(good4);
		mvc.perform(post("/updatedGood").param("id", String.valueOf(savedGood.getId())).param("name", "Updated name")
				.param("description", "Updated description").param("tagId", String.valueOf(good1.getTag().getId())))
				.andExpect(status().isOk()).andExpect(model().attribute("result", is(not(emptyIterable()))))
				.andExpect(model().attribute("result", Matchers.hasProperty("name", Matchers.equalTo("Updated name"))))
				.andExpect(model().attribute("result",
						Matchers.hasProperty("description", Matchers.equalTo("Updated description"))))
				/*
				 * .andExpect (model().attribute("result",
				 * Matchers.hasProperty("tag", Matchers.equalTo
				 * (good1.getTag()))))
				 */
				.andExpect(
						model().attribute("result", Matchers.hasProperty("user", Matchers.equalTo(good4.getUser()))));
	}

	@Ignore
	@Test
	public void testDeleteGood() throws Exception {
		goodsRepository.save(good5);
		mvc.perform(post("/deletedGood").param("id", String.valueOf(good5.getId()))).andExpect(status().isOk())
				.andExpect(model().attribute("result", is(not(emptyIterable()))))
				.andExpect(model().attribute("result", Matchers.hasProperty("name", Matchers.equalTo(good5.getName()))))
				.andExpect(model().attribute("result",
						Matchers.hasProperty("description", Matchers.equalTo(good5.getDescription()))))
				/*
				 * .andExpect (model().attribute("result",
				 * Matchers.hasProperty("tag", Matchers.equalTo
				 * (good5.getTag()))))
				 */
				.andExpect(
						model().attribute("result", Matchers.hasProperty("user", Matchers.equalTo(good5.getUser()))));

		mvc.perform(get("/myOfferedGoods")).andExpect(status().isOk())
				.andExpect(model().attribute("resultGoods", is(not(emptyIterable()))))
				.andExpect(model().attribute("resultGoods", is(iterableWithSize(iterableSize - 1 + user1Goods))));
	}

}

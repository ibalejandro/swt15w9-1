package app.test.activitiesManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import app.controller.GoodsOfferController;
import app.model.ActivityEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.TagsRepository;

public class ActivitiesOfferControllerIntegrationTests extends AbstractWebIntegrationTests {

	private ActivityEntity activity1, activity2;
	private static int iterableSize = 2;
	private static int activitiesInTestDaten = 0;
	private static int userOfferedActivitiesInTestDaten = 0;

	@Autowired
	GoodsOfferController controller;
	@Autowired
	ActivitiesRepository activitiesRepository;
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
		String name1 = "Trip to Leipzig";
		String description1 = "This is a wonderful opportunity to visit Leipzig" + " for free!.";
		TagEntity tag1 = tagsRepository.findByName("Tickets & Experiences");
		String name2 = "Soccer game";
		String description2 = "Come and join us for the funniest soccer game";
		TagEntity tag2 = tagsRepository.findByName("Sporting Goods");

		Date date = new Date();

		User user = userRepository.findByUserAccount(userAccountManager.findByUsername("Lisa").get());

		login(user.getUserAccount().getUsername(), "pw");

		activity1 = new ActivityEntity(name1, description1, tag1, null, user, date, date);
		activity2 = new ActivityEntity(name2, description2, tag2, null, user, date, date);
	}

	@Ignore
	@Test
	public void testSaveActivity() throws Exception {
		mvc.perform(post("/offeredActivity").param("name", activity1.getName())
				.param("description", activity1.getDescription())
				.param("tagId", String.valueOf(activity1.getTag().getId()))
				.param("startDate", getRequiredStringFromDate(activity1.getStartDate()))
				.param("endDate", getRequiredStringFromDate(activity1.getEndDate()))).andExpect(status().isOk())
				.andExpect(model().attribute("result", is(not(emptyIterable()))))
				.andExpect(model().attribute("result",
						Matchers.hasProperty("name", Matchers.equalTo(activity1.getName()))))
				.andExpect(model().attribute("result",
						Matchers.hasProperty("description", Matchers.equalTo(activity1.getDescription()))))
				/*
				 * .andExpect (model().attribute("result",
				 * Matchers.hasProperty("tag", Matchers.equalTo
				 * (activity1.getTag()))))
				 */

				.andExpect(model().attribute("result", Matchers.hasProperty("startDate",
						Matchers.equalTo(ActivityEntity.getZeroTimeDate(activity1.getStartDate())))))

				.andExpect(model().attribute("result", Matchers.hasProperty("startDate",
						Matchers.equalTo(ActivityEntity.getZeroTimeDate(activity1.getStartDate())))))

				.andExpect(model().attribute("result",
						Matchers.hasProperty("user", Matchers.equalTo(activity1.getUser()))));

		mvc.perform(post("/offeredActivity").param("name", activity2.getName())
				.param("description", activity2.getDescription())
				.param("tagId", String.valueOf(activity2.getTag().getId()))
				.param("startDate", getRequiredStringFromDate(activity2.getStartDate()))
				.param("endDate", getRequiredStringFromDate(activity2.getEndDate()))).andExpect(status().isOk())
				.andExpect(model().attribute("result", is(not(emptyIterable()))))
				.andExpect(model().attribute("result",
						Matchers.hasProperty("name", Matchers.equalTo(activity2.getName()))))
				.andExpect(model().attribute("result",
						Matchers.hasProperty("description", Matchers.equalTo(activity2.getDescription()))))
				/*
				 * .andExpect (model().attribute("result",
				 * Matchers.hasProperty("tag", Matchers.equalTo
				 * (activity2.getTag()))))
				 */
				.andExpect(model().attribute("result", Matchers.hasProperty("startDate",
						Matchers.equalTo(ActivityEntity.getZeroTimeDate(activity2.getStartDate())))))

				.andExpect(model().attribute("result", Matchers.hasProperty("endDate",
						Matchers.equalTo(ActivityEntity.getZeroTimeDate(activity2.getEndDate())))))

				.andExpect(model().attribute("result",
						Matchers.hasProperty("user", Matchers.equalTo(activity2.getUser()))));

		mvc.perform(get("/myOfferedActivities")).andExpect(status().isOk())
				.andExpect(model().attribute("resultActivities", is(not(emptyIterable())))).andExpect(model().attribute(
						"resultActivities", is(iterableWithSize(iterableSize - userOfferedActivitiesInTestDaten))));
	}

	@Ignore
	@Test
	@SuppressWarnings("unchecked")
	public void testListAllActivities() {
		Model model = new ExtendedModelMap();

		String returnedView = controller.listAllGoodsAndActivities(model);

		assertThat(returnedView, is("home"));

		Iterable<Object> object = (Iterable<Object>) model.asMap().get("resultActivities");
		assertThat(object, is(iterableWithSize(iterableSize + activitiesInTestDaten)));
	}

	/*
	 * @Test
	 * 
	 * @SuppressWarnings("unchecked") public void testListAllActivities() {
	 * Model model = new ExtendedModelMap();
	 * 
	 * String returnedView = controller.listAllGoodsAndActivities(model);
	 * 
	 * assertThat(returnedView, is("home"));
	 * 
	 * Iterable<Object> object = (Iterable<Object>)
	 * model.asMap().get("resultActivities"); assertThat(object,
	 * is(iterableWithSize(iterableSize + activitiesInTestDaten))); }
	 */

	public String getRequiredStringFromDate(Date date) {
		return ActivityEntity.getStringFromDateForInput(ActivityEntity.getZeroTimeDate(date));
	}

}

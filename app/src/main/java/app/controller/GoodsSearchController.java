package app.controller;

import java.util.Optional;
import java.util.Set;

import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import app.model.ActivityEntity;
import app.model.GoodEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;

/**
 * <h1>GoodsSearchController</h1> The GoodsSearchController is used to search a
 * good/activity by a given tag.
 *
 * @author Alejandro Sánchez Aristizábal
 * @since 19.11.2015
 */
@Controller
public class GoodsSearchController {

	private final GoodsRepository goodsRepository;
	private final ActivitiesRepository activitiesRepository;
	private final TagsRepository tagsRepository;
	private final UserRepository userRepository;
	@Autowired
	DistanceFunctions distanceFunctions;

	/**
	 * Autowire.
	 * 
	 * @param GoodsRepository
	 *            The repository for the goods
	 * @param ActivitiesRepository
	 *            The repository for the activities
	 * @param TagsRepository
	 *            The repository for the tags
	 */
	@Autowired
	public GoodsSearchController(GoodsRepository goodsRepository, ActivitiesRepository activitiesRepository,
			TagsRepository tagsRepository, UserRepository userRepository) {
		this.goodsRepository = goodsRepository;
		this.activitiesRepository = activitiesRepository;
		this.tagsRepository = tagsRepository;
		this.userRepository = userRepository;
	}
	///////////////////////////////////////////////////////// end

	/**
	 * This method is the answer for the request to '/search'. It retrieves and
	 * populates the tags dropdown with the whole available tags.
	 * 
	 * @param Model
	 *            The model to add response's attributes
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String populateTagsDropdown(Model model) {
		model.addAttribute("navTags", tagsRepository.findAllByOrderByNameAsc());
		System.out.println(model);
		return "search";
	}

	/**
	 * This method is the answer for the request to '/searchResultsByTag'. It
	 * finds and retrieves a list of goods/activities that matches with the
	 * given tag.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/searchResultsByTag", method = RequestMethod.POST)
	public String searchGoodOrActivityByTag(@RequestParam("tagId") final String TagId,
			@RequestParam("distance") final String Distance, @LoggedIn Optional<UserAccount> userAccount, Model model) {
		if (!userAccount.isPresent())
			return "noUser";
		
		User searchingUser = userRepository.findByUserAccount(userAccount.get());
		String parameterType = "tag";

		long tagId = Long.parseLong(TagId);
		int distance = Integer.parseInt(Distance);

		/*
		 * The type of parameter and the parameter itself for the search are
		 * sent to the view, so that the user can see his search criteria.
		 */
		Iterable<GoodEntity> goodsFound;
		Iterable<ActivityEntity> activitiesFound;
		if (tagId != -1L) {
			TagEntity tag = tagsRepository.findOne(tagId);
			model.addAttribute("resultParameter", tag.getName());
			if (distance == -1 || userAccount.get().hasRole(new Role ("ROLE_ADMIN"))) {
				goodsFound = goodsRepository.findByTag(tag);
				activitiesFound = activitiesRepository.findByTag(tag);
			} else {
				Set<User> userByDistance = distanceFunctions.getUserByDistance(distance, searchingUser);
				goodsFound = distanceFunctions.collectGoodsByDistance(tag, userByDistance);
				activitiesFound = distanceFunctions.collectActivitiesByDistance(tag, userByDistance);
			}
		}
		/*
		 * If the tagId is -1L that means that the user doesn't want to filter
		 * by any search criteria.
		 */
		else {
		  model.addAttribute("resultParameter", "All");
			if (distance == -1 || userAccount.get().hasRole(new Role ("ROLE_ADMIN"))) {
				goodsFound = goodsRepository.findAll();
				activitiesFound = activitiesRepository.findAll();
			} else {
				Set<User> userByDistance = distanceFunctions.getUserByDistance(distance, searchingUser);
				goodsFound = distanceFunctions.collectGoodsByDistance(userByDistance);
				activitiesFound = distanceFunctions.collectActivitiesByDistance(userByDistance);
			}
		}

		model.addAttribute("resultGoods", goodsFound);
		model.addAttribute("resultActivities", activitiesFound);
		model.addAttribute("resultParameterType", parameterType);
		model.addAttribute("numberOfResults", GoodEntity.getIterableSize(goodsFound));
		model.addAttribute("numberOfResultsActivities", ActivityEntity.getIterableSize(activitiesFound));
		return "searchResults";
	}

}

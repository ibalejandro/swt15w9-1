package app.controller;

import java.util.HashSet;
import java.util.Set;

import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import app.model.ActivityEntity;
import app.model.GoodEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;
import app.test.coordinates.AbstractWebIntegrationTests;

/**
 * @author Friederike Kitzing
 */

@Controller
public class DistanceFunctions {

	private final UserRepository userRepository;
	private final UserAccountManager userAccountManager;
	private final LanguageRepository languageRepository;
	private final TagsRepository tagsRepository;
	private final GoodsRepository goodsRepository;
	private final ActivitiesRepository activitiesRepository;

	/**
	 * Autowire.
	 * 
	 * @param userRepository
	 *            The repository for the users
	 */
	@Autowired
	public DistanceFunctions(UserRepository userRepository,
			UserAccountManager userAccountManager,
			LanguageRepository languageRepository,
			TagsRepository tagsRepository, GoodsRepository goodsRepository,
			ActivitiesRepository activitiesRepository) {
		this.userRepository = userRepository;
		this.userAccountManager = userAccountManager;
		this.languageRepository = languageRepository;
		this.tagsRepository = tagsRepository;
		this.goodsRepository = goodsRepository;
		this.activitiesRepository = activitiesRepository;
	}

	public static double distanceInKm(User user1, User user2) {
		int radius = 6370;

		double lat = Math.toRadians(user2.getLatitude() - user1.getLatitude());
		double lon = Math
				.toRadians(user2.getLongitude() - user1.getLongitude());

		double a = Math.sin(lat / 2) * Math.sin(lat / 2)
				+ Math.cos(Math.toRadians(user1.getLatitude()))
				* Math.cos(Math.toRadians(user2.getLatitude()))
				* Math.sin(lon / 2) * Math.sin(lon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = radius * c;

		return Math.abs(d);
	}

	public Set<User> getUserByDistance(int distance, User searchingUser) {
		Set<User> userByDistance = new HashSet<>();
		for (User user : userRepository.findAll()) {
			if ((distanceInKm(searchingUser, user) <= distance) && !(user.getId()==searchingUser.getId())) {
				userByDistance.add(user);
			}
		}
		if(userByDistance.remove(searchingUser)){
			System.out.println("searchingUser entfernt!");
		}
		return userByDistance;
	}

	public Iterable<GoodEntity> collectGoodsByDistance(
			Set<User> userByDistance) {
		Set<GoodEntity> goodsByDistance = new HashSet<>();
		for (User user : userByDistance) {
			for(GoodEntity good:user.getGoods()){
				goodsByDistance.add(good);
			}
		}
		return goodsByDistance;
	}
	
	public Iterable<GoodEntity> collectGoodsByDistance(TagEntity tag,
			Set<User> userByDistance) {
		Set<GoodEntity> goodsByDistance = new HashSet<>();
		for (User user : userByDistance) {
			for (GoodEntity good : goodsRepository.findByTagAndUser(tag, user)) {
				goodsByDistance.add(good);
			}
		}
		return goodsByDistance;
	}

	public Iterable<ActivityEntity> collectActivitiesByDistance(
			Set<User> userByDistance) {
		Set<ActivityEntity> activitiesByDistance = new HashSet<>();
		for (User user : userByDistance) {
			for (ActivityEntity activity : user.getActivities()) {
				activitiesByDistance.add(activity);
			}
		}
		return activitiesByDistance;
	}

	
	public Iterable<ActivityEntity> collectActivitiesByDistance(TagEntity tag,
			Set<User> userByDistance) {
		Set<ActivityEntity> activitiesByDistance = new HashSet<>();
		for (User user : userByDistance) {
			for (ActivityEntity activity : activitiesRepository
					.findByTagAndUser(tag, user)) {
				activitiesByDistance.add(activity);
			}
		}
		return activitiesByDistance;
	}

}

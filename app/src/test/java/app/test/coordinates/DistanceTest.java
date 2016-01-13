package app.test.coordinates;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;

import app.controller.DistanceFunctions;
import app.model.Address;
import app.model.Coordinates;
import app.model.GoodEntity;
import app.model.User;
import app.model.User.AddresstypEnum;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;

@Ignore("Sparen von immer gleichen API-Anfragen, verhindern von !OVER QUERY LIMIT!")
public class DistanceTest extends AbstractWebIntegrationTests {

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
	@Autowired 
	DistanceFunctions distanceFunctions;

	@Test
	public void distanceCalculation() {
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		User user2 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser2").get());

		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46",
				"01187", "Dresden");
		Address testAddressWohnung2 = new Address("Prager Str.", "10", "01069",
				"Dresden");
		Address testAddressWohnung3 = new Address("Scharnweberstr.", "22 A",
				"12587", "Berlin");
		//Kartäusergasse 12, 90402 Nürnberg
		Address testAddressWohnung4 = new Address("Kartäusergasse", "12",
				"90402", "Nürnberg");
		//Martin-Luther-Ring 4-6, 04109 Leipzig 
		Address testAddressWohnung5 = new Address("Martin-Luther-Ring", "4-6",
				"04109", "Leipzig");
		//Säbener Straße 51-57, 81547 München
		Address testAddressWohnung6 = new Address("Säbener Straße", "51-57",
				"81547", "München");
		Address testAddressRefugee1 = new Address("", "", "Dresden 6",
				"Südvorstadt", "01187", "Dresden");
		Address testAddressRefugee2 = new Address("", "",
				"NUK Friedrichshagen", "Friedrichshagen", "12587", "Berlin");
		double delta = 0.1;

		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);
		Coordinates cu1 = user1.createCoordinates();
		user1.setCoordinates(cu1);
		userRepository.save(user1);

		user2.setLocation(testAddressWohnung2);
		user2.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user2);
		Coordinates cu2 = user2.createCoordinates();
		user2.setCoordinates(cu2);
		userRepository.save(user2);

		assertEquals(2.44, DistanceFunctions.distanceInKm(user1, user2), delta);

		user2.setLocation(testAddressWohnung3);
		user2.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user2);
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		assertEquals("delta="+(DistanceFunctions.distanceInKm(user1, user2)-158.49),158.49 , DistanceFunctions.distanceInKm(user1, user2), delta);
		
		user2.setLocation(testAddressRefugee1);
		user2.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user2);
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		assertEquals("delta="+(DistanceFunctions.distanceInKm(user1, user2)-1.09), 1.09 , DistanceFunctions.distanceInKm(user1, user2), delta);

		user2.setLocation(testAddressRefugee2);
		user2.setAddresstyp(AddresstypEnum.Refugees_home);
		userRepository.save(user2);
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		assertEquals("delta="+(DistanceFunctions.distanceInKm(user1, user2)-159.74), 159.74 , DistanceFunctions.distanceInKm(user1, user2), delta);

		user2.setLocation(testAddressWohnung4);
		user2.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user2);
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		assertEquals("delta="+(DistanceFunctions.distanceInKm(user1, user2)-257.38), 257.38 , DistanceFunctions.distanceInKm(user1, user2), delta);

		user2.setLocation(testAddressWohnung5);
		user2.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user2);
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		assertEquals("delta="+(DistanceFunctions.distanceInKm(user1, user2)-100.27), 100.27 , DistanceFunctions.distanceInKm(user1, user2), delta);

		user2.setLocation(testAddressWohnung6);
		user2.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user2);
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		assertEquals("delta="+(DistanceFunctions.distanceInKm(user1, user2)- 360.19),  360.19 , DistanceFunctions.distanceInKm(user1, user2), delta);

	}
	
	public void prepaire(){
		Role normalUserRole = new Role("ROLE_NORMAL");
		
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		User user2 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser2").get());
		User user3 =userRepository.findByUserAccount(userAccountManager
				.findByUsername("Lisa").get());
		User user4 =userRepository.findByUserAccount(userAccountManager
				.findByUsername("Peter").get());
		
		UserAccount testUser5 = userAccountManager.create("testUser5", "pw", normalUserRole);
		userAccountManager.save(testUser5);
		
		UserAccount testUser6 = userAccountManager.create("testUser6", "pw", normalUserRole);
		userAccountManager.save(testUser6);
		
		UserAccount testUser7 = userAccountManager.create("testUser7", "pw", normalUserRole);
		userAccountManager.save(testUser7);

		Address testAddressWohnung1 = new Address("Nöthnitzer Str.", "46",
				"01187", "Dresden");
		//2.44km
		Address testAddressWohnung2 = new Address("Prager Str.", "10", "01069",
				"Dresden");
		//158.49km
		Address testAddressWohnung3 = new Address("Scharnweberstr.", "22 A",
				"12587", "Berlin");
		//45km
		Address testAddressWohnung4 = new Address("Merzdorfer Str.", "21 - 25",
				"01591", "Riesa");
		//100.27
		Address testAddressWohnung5 = new Address("Martin-Luther-Ring", "4-6",
				"04109", "Leipzig");
		//10.83km
		Address testAddressWohnung6 = new Address("Wittgensdorfer Str.", "20",
				"01731", "Kreischa");
		//60km 
		Address testAddressWohnung7 = new Address("Reichenhainer Straße", "41",
				"09126", "Chemnitz");
		
		user1.setLocation(testAddressWohnung1);
		user1.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user1);
		user1.setCoordinates(user1.createCoordinates());
		userRepository.save(user1);
		
		user2.setLocation(testAddressWohnung2);
		user2.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user2);
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		user3.setLocation(testAddressWohnung3);
		user3.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user3);
		user3.setCoordinates(user3.createCoordinates());
		userRepository.save(user3);
		
		user4.setLocation(testAddressWohnung4);
		user4.setAddresstyp(AddresstypEnum.Wohnung);
		userRepository.save(user4);
		user4.setCoordinates(user4.createCoordinates());
		userRepository.save(user4);
		
		User user5 = new User(testUser5, testAddressWohnung5);
	    user5.setAddresstyp(AddresstypEnum.Wohnung);
	    userRepository.save(user5);
		user5.setCoordinates(user5.createCoordinates());
		userRepository.save(user5);
		
		User user6 = new User(testUser6, testAddressWohnung6);
	    user6.setAddresstyp(AddresstypEnum.Wohnung);
	    userRepository.save(user6);
		user6.setCoordinates(user6.createCoordinates());
		userRepository.save(user6);
		
		User user7 = new User(testUser7, testAddressWohnung7);
	    user7.setAddresstyp(AddresstypEnum.Wohnung);
	    userRepository.save(user7);
		user7.setCoordinates(user7.createCoordinates());
		userRepository.save(user7);
		
	}
	
	public void prepaireTestGoods(){
		User user2 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser2").get());
		User user3 =userRepository.findByUserAccount(userAccountManager
				.findByUsername("Lisa").get());
		User user6 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser6").get());
		
		GoodEntity testGood1= new GoodEntity("Test1", "First TestGood", tagsRepository.findByName("Books"), null, 
                  user2);
		GoodEntity savedGood1 = goodsRepository.save(testGood1);
		user2.addGood(savedGood1);
		
		GoodEntity testGood2= new GoodEntity("Test2", "Second TestGood", tagsRepository.findByName("Books"), null, 
                user3);
		GoodEntity savedGood2 = goodsRepository.save(testGood2);
		user3.addGood(savedGood2);
		
		GoodEntity testGood3= new GoodEntity("Test3", "Third TestGood", tagsRepository.findByName("Books"), null, 
                user6);
		GoodEntity savedGood3 = goodsRepository.save(testGood3);
		user6.addGood(savedGood3);
		GoodEntity testGood4= new GoodEntity("Test4", "Forth TestGood", tagsRepository.findByName("Books"), null, 
                user6);
		GoodEntity savedGood4 = goodsRepository.save(testGood4);
		user6.addGood(savedGood4);
		System.out.println(user6.getGoods());
		
		GoodEntity testGood5= new GoodEntity("Test5", "5th TestGood", tagsRepository.findByName("Jewelry & Watches"), null, 
                user6);
		GoodEntity savedGood5 = goodsRepository.save(testGood5);
		user6.addGood(savedGood5);
		System.out.println(user6.getGoods());
		
	}
	
	@Test

	public void userByDistanceTest() {
		
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		User user2 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser2").get());
		User user3 =userRepository.findByUserAccount(userAccountManager
				.findByUsername("Lisa").get());
		User user4 =userRepository.findByUserAccount(userAccountManager
				.findByUsername("Peter").get());
		User user5 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser5").get());
		User user6 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser6").get());
		User user7 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser7").get());
				
		Set<User> userByDistance5= distanceFunctions.getUserByDistance(5,user1);
		Set<Long> userIds= new HashSet<>();
		for(User user: userByDistance5){
			userIds.add(user.getId());
		}
		System.out.println(userByDistance5.size());
		System.out.println(userByDistance5);
		assertTrue(user2.equals(userRepository.findByUserAccount(userAccountManager.findByUsername("testUser2").get())));
		/*
		assertTrue(userByDistance5.contains(user2));
		assertFalse(userByDistance5.contains(user3));
		assertFalse(userByDistance5.contains(user4));
		assertFalse(userByDistance5.contains(user5));
		assertFalse(userByDistance5.contains(user6));
		assertFalse(userByDistance5.contains(user7));
		*/
		
		Set<User> userByDistance10= distanceFunctions.getUserByDistance(10,user1);
		System.out.println(userByDistance10.size());
		System.out.println(userByDistance10);
		/*
		assertTrue(userByDistance10.contains(user2));
		assertFalse(userByDistance10.contains(user3));
		assertFalse(userByDistance10.contains(user4));
		assertFalse(userByDistance10.contains(user5));
		assertFalse(userByDistance10.contains(user6));
		assertFalse(userByDistance10.contains(user7));
		*/
		
		Set<User> userByDistance20= distanceFunctions.getUserByDistance(20,user1);
		System.out.println(userByDistance20.size());
		System.out.println(userByDistance20);
		/*
		assertTrue(userByDistance20.contains(user2));
		assertFalse(userByDistance20.contains(user3));
		assertFalse(userByDistance20.contains(user4));
		assertFalse(userByDistance20.contains(user5));
		assertTrue(userByDistance20.contains(user6));
		assertFalse(userByDistance20.contains(user7));
		*/
		Set<User> userByDistance30= distanceFunctions.getUserByDistance(30,user1);
		System.out.println(userByDistance30.size());
		System.out.println(userByDistance30);
		/*
		assertTrue(userByDistance30.contains(user2));
		assertFalse(userByDistance30.contains(user3));
		assertFalse(userByDistance30.contains(user4));
		assertFalse(userByDistance30.contains(user5));
		assertTrue(userByDistance30.contains(user6));
		assertFalse(userByDistance30.contains(user7));
		*/
		Set<User> userByDistance50= distanceFunctions.getUserByDistance(50,user1);
		System.out.println(userByDistance50.size());
		System.out.println(userByDistance50);
		/*
		assertTrue(userByDistance50.contains(user2));
		assertFalse(userByDistance50.contains(user3));
		assertTrue(userByDistance50.contains(user4));
		assertFalse(userByDistance50.contains(user5));
		assertTrue(userByDistance50.contains(user6));
		assertFalse(userByDistance50.contains(user7));
		*/
		Set<User> userByDistance100= distanceFunctions.getUserByDistance(100,user1);
		System.out.println(userByDistance100.size());
		System.out.println(userByDistance100);
		/*
		assertTrue(userByDistance100.contains(user2));
		assertFalse(userByDistance100.contains(user3));
		assertTrue(userByDistance100.contains(user4));
		assertFalse(userByDistance100.contains(user5));
		assertFalse(userByDistance100.contains(user6));
		assertTrue(userByDistance100.contains(user7));
		/*
		 * Set<User> userByDistance150= distanceFunctions.getUserByDistance(150,user1);
		 * Set<User> userByDistance200= distanceFunctions.getUserByDistance(200,user1);
		 */
		
	}
	
	@Test
	public void goodsByDistance(){
		
		prepaire();
		prepaireTestGoods();
	
		User user1 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser1").get());
		User user2 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser2").get());
		User user3 =userRepository.findByUserAccount(userAccountManager
				.findByUsername("Lisa").get());
		User user4 =userRepository.findByUserAccount(userAccountManager
				.findByUsername("Peter").get());
		User user5 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser5").get());
		User user6 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser6").get());
		User user7 = userRepository.findByUserAccount(userAccountManager
				.findByUsername("testUser7").get());
	
		Set<User> userByDistance5= distanceFunctions.getUserByDistance(5,user1);
		Set<Long> goods= new HashSet<>();
		for(GoodEntity good: distanceFunctions.collectGoodsByDistance(userByDistance5)){
			goods.add(good.getId());
		}
		for(GoodEntity goodUser:user2.getGoods()){
			assertTrue(goods.contains(goodUser.getId()));
		}
		
		Set<User> userByDistance100= distanceFunctions.getUserByDistance(100,user1);
		goods.clear();
		for(GoodEntity good: distanceFunctions.collectGoodsByDistance(userByDistance100)){
			goods.add(good.getId());
		}
		for(GoodEntity goodUser:user2.getGoods()){
			assertTrue(goods.contains(goodUser.getId()));
		}
		for(GoodEntity goodUser:user3.getGoods()){
			assertFalse(goods.contains(goodUser.getId()));
		}
		for(GoodEntity goodUser:user4.getGoods()){
			assertTrue(goods.contains(goodUser.getId()));
		}
		for(GoodEntity goodUser:user5.getGoods()){
			assertFalse(goods.contains(goodUser.getId()));
		}
		for(GoodEntity goodUser:user6.getGoods()){
			assertTrue(goods.contains(goodUser.getId()));
		}
		for(GoodEntity goodUser:user7.getGoods()){
			assertTrue(goods.contains(goodUser.getId()));
		}
		
		
		goods.clear();
		for(GoodEntity good: distanceFunctions.collectGoodsByDistance(tagsRepository.findByName("Books"),userByDistance5)){
			goods.add(good.getId());
		}
		for(GoodEntity goodUser:user2.getGoods()){
			if(goodUser.getTag().getName().equals("Books")){
				assertTrue(goods.contains(goodUser.getId()));
			}else{
				assertFalse(goods.contains(goodUser.getId()));
			}
		}
		
		goods.clear();
		for(GoodEntity good: distanceFunctions.collectGoodsByDistance(tagsRepository.findByName("Books"),userByDistance100)){
			goods.add(good.getId());
		}
		for(GoodEntity goodUser:user2.getGoods()){
			if(goodUser.getTag().getName().equals("Books")){
				assertTrue(goods.contains(goodUser.getId()));
			}else{
				assertFalse(goods.contains(goodUser.getId()));
			}
		}
		for(GoodEntity goodUser:user3.getGoods()){
			assertFalse(goods.contains(goodUser.getId()));
		}
		for(GoodEntity goodUser:user4.getGoods()){
			if(goodUser.getTag().getName().equals("Books")){
				assertTrue(goods.contains(goodUser.getId()));
			}else{
				assertFalse(goods.contains(goodUser.getId()));
			}
		}
		for(GoodEntity goodUser:user5.getGoods()){
			assertFalse(goods.contains(goodUser.getId()));
		}
		for(GoodEntity goodUser:user6.getGoods()){
			if(goodUser.getTag().getName().equals("Books")){
				assertTrue(goods.contains(goodUser.getId()));
			}else{
				assertFalse(goods.contains(goodUser.getId()));
			}
		}
		for(GoodEntity goodUser:user7.getGoods()){
			if(goodUser.getTag().getName().equals("Books")){
				assertTrue(goods.contains(goodUser.getId()));
			}else{
				assertFalse(goods.contains(goodUser.getId()));
			}
		}
	}
}

package app.controller;

import java.util.ArrayList;
import java.util.Date;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import app.model.Address;
import app.model.Language;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.DialogRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;

@Component
public class TestDaten implements DataInitializer {

	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
	private final DialogRepository dialogRepository;
	private final LanguageRepository languageRepository;
	private final TagsRepository tagsRepository;

	@Autowired
	public TestDaten(UserAccountManager userAccountManager, 
	                 UserRepository userRepository, 
	                 DialogRepository dialogRepository,
	                 LanguageRepository languageRepository,
	                 TagsRepository tagsRepository) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");
		Assert.notNull(dialogRepository, "DialogRepository must not be null!");
		Assert.notNull(tagsRepository, "TagsRepository must not be null!");
		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		this.dialogRepository = dialogRepository;
		this.languageRepository=languageRepository;
		this.tagsRepository = tagsRepository;
	}

	@Override
	public void initialize() {

		initializeUsers(userAccountManager, userRepository, dialogRepository, languageRepository,
		                tagsRepository);

	}

	private void initializeUsers(UserAccountManager userAccountManager, 
	                             UserRepository userRepository, 
	                             DialogRepository dialogRepository, 
	                             LanguageRepository languageRepository,
	                             TagsRepository tagsRepository) {

		if (userAccountManager.findByUsername("boss").isPresent()) {
			return;
		}

		UserAccount bossAccount = userAccountManager
		                          .create("admin", "123", new Role("ROLE_ADMIN"));
		userAccountManager.save(bossAccount);

		Role normalUserRole = new Role("ROLE_NORMAL");
		UserAccount u1 = userAccountManager.create("Lisa", "pw", normalUserRole);
		u1.setFirstname("Lisa-Marie");
		u1.setLastname("Maier");
		u1.setEmail("Maier@gmail.com");
		userAccountManager.save(u1);

		UserAccount u2=userAccountManager.create("Peter", "pw", normalUserRole);
		u2.setFirstname("Peter");
		u2.setLastname("U.");
		u2.setEmail("test@test.test");
		userAccountManager.save(u2);

		Address address1 = new Address("Mittelstraße", " 1", "11587", "Dresden");

		User user1 = new User(u1, address1);
		user1.setOrigin("Germany, Deutschland (DE)");
		user1.setRegistrationdate(new Date());
		user1.Activate();

		User user2 = new User(u2, address1);
		user2.setOrigin("United Arab Emirates, Vereinigte Arabische Emirate (AE)");
		user2.setRegistrationdate(new Date());
		userRepository.save(user1);
		userRepository.save(user2);
		
		//Sprachen:
		Language language1= new Language("Deutsch", "de");
		Language language2= new Language("Englisch", "en");
		Language language3= new Language("Arabisch", "ar");
		Language language4= new Language("Spanisch", "es");
		languageRepository.save(language1);
		languageRepository.save(language2);
		languageRepository.save(language3);
		languageRepository.save(language4);
		
		user1.setPrefLanguage(language1);
		user1.setLanguage(language2);
		
		user2.setPrefLanguage(language3);
		
		userRepository.save(user1);
		userRepository.save(user2);

		/*
		Dialog d = new Dialog("My Dialog", user1, user2);
		Dialog savedDialog = dialogRepository.save(d);
		user1.addDialog(savedDialog);
		user2.addDialog(savedDialog);
		*/
		
		ArrayList<TagEntity> tags = createTags();
		for (TagEntity tag : tags) tagsRepository.save(tag);
	}
	
	/**
   * This method creates the whole tags to save them in the database, so that 
   * they can be accessible and the user can select one of them for his offers.
   * @return ArrayList<TagEntity> A list with all available tags
   */
	public ArrayList<TagEntity> createTags() {
	  ArrayList<TagEntity> tags = new ArrayList<>();
	  
	  TagEntity tag1 = new TagEntity("Baby");
    TagEntity tag2 = new TagEntity("Books");
    TagEntity tag3 = new TagEntity("Cameras & Photo");
    TagEntity tag4 = new TagEntity("Cell Phones & Accesories");
    TagEntity tag5 = new TagEntity("Clothes, Shoes and Accesories");
    TagEntity tag6 = new TagEntity("Computers/Tablets & Networking");
    TagEntity tag7 = new TagEntity("Consumer Electronics");
    TagEntity tag8 = new TagEntity("Dolls & Bears");
    TagEntity tag9 = new TagEntity("DVDs & Movies");
    TagEntity tag10 = new TagEntity("Gift Cards & Coupons");
    TagEntity tag11 = new TagEntity("Health & Beauty");
    TagEntity tag12 = new TagEntity("Jewelry & Watches");
    TagEntity tag13 = new TagEntity("Music");
    TagEntity tag14 = new TagEntity("Musical Instruments and Gear");
    TagEntity tag15 = new TagEntity("Pottery & Glass");
    TagEntity tag16 = new TagEntity("Sporting Goods");
    TagEntity tag17 = new TagEntity("Sports Mem, Cards & Fan Shop");
    TagEntity tag18 = new TagEntity("Tickets & Experiences");
    TagEntity tag19 = new TagEntity("Toys & Hobbies");
    TagEntity tag20 = new TagEntity("Video Games & Consoles");
    TagEntity tag21 = new TagEntity("Others");
    
    tags.add(tag1);
    tags.add(tag2);
    tags.add(tag3);
    tags.add(tag4);
    tags.add(tag5);
    tags.add(tag6);
    tags.add(tag7);
    tags.add(tag8);
    tags.add(tag9);
    tags.add(tag10);
    tags.add(tag11);
    tags.add(tag12);
    tags.add(tag13);
    tags.add(tag14);
    tags.add(tag15);
    tags.add(tag16);
    tags.add(tag17);
    tags.add(tag18);
    tags.add(tag19);
    tags.add(tag20);
    tags.add(tag21);
    
    return tags;
	}

}

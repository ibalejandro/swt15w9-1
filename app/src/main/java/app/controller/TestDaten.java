package app.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import app.model.Address;
import app.model.Coordinates;
import app.model.GoodEntity;
import app.model.InterfacePart;
import app.model.Language;
import app.model.Module;
import app.model.TagEntity;
import app.model.User;
import app.model.User.AddresstypEnum;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.DialogRepository;
import app.repository.GoodsRepository;
import app.repository.InterfaceRepository;
import app.repository.LanguageRepository;
import app.repository.ModuleRepository;
import app.repository.TagsRepository;
import app.repository.TextBlockRepository;
import app.textblocks.TextBlock;
import app.util.FormatStringTagFilter;

@Component
public class TestDaten implements DataInitializer {

	private final UserAccountManager userAccountManager;
	private final UserRepository userRepository;
	private final DialogRepository dialogRepository;
	private final LanguageRepository languageRepository;
	private final TagsRepository tagsRepository;
	private final ModuleRepository moduleRepository;
	private final InterfaceRepository interfaceRepository;
	private final TextBlockRepository textBlockRepository;
	private final GoodsRepository goodsRepository;
	private final ActivitiesRepository activitiesRepository;

	@Autowired
	public TestDaten(UserAccountManager userAccountManager, 
	                 UserRepository userRepository, 
	                 DialogRepository dialogRepository,
	                 LanguageRepository languageRepository,
	                 TagsRepository tagsRepository,
	                 ModuleRepository moduleRepository,
	                 InterfaceRepository interfaceRepository,
	                 TextBlockRepository textBlockRepository,
	                 GoodsRepository goodsRepository,
	                 ActivitiesRepository activitiesRepository) {

		Assert.notNull(userAccountManager, "UserAccountManager must not be null!");
		Assert.notNull(userRepository, "UserRepository must not be null!");
		Assert.notNull(dialogRepository, "DialogRepository must not be null!");
		Assert.notNull(tagsRepository, "TagsRepository must not be null!");
		this.userAccountManager = userAccountManager;
		this.userRepository = userRepository;
		this.dialogRepository = dialogRepository;
		this.languageRepository=languageRepository;
		this.tagsRepository = tagsRepository;
		this.moduleRepository = moduleRepository;
		this.interfaceRepository = interfaceRepository;
		this.textBlockRepository = textBlockRepository;
		this.goodsRepository=goodsRepository;
		this.activitiesRepository=activitiesRepository;
	}

	@Override
	public void initialize() {

		initializeUsers(userAccountManager, userRepository, dialogRepository, 
		                languageRepository, moduleRepository, interfaceRepository);
		initializeTags(tagsRepository);
		initializeGoodsAndActivities(userAccountManager, userRepository, dialogRepository, languageRepository,
                tagsRepository, moduleRepository, interfaceRepository);
		initializeTextBlocks();
	}

	private void initializeUsers(UserAccountManager userAccountManager, 
	                             UserRepository userRepository, 
	                             DialogRepository dialogRepository, 
	                             LanguageRepository languageRepository,
	                             ModuleRepository moduleRepository,
	                             InterfaceRepository interfaceRepository) {

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
		u1.setEmail("test@test.test");
		userAccountManager.save(u1);

		UserAccount u2=userAccountManager.create("Peter", "pw", normalUserRole);
		u2.setFirstname("Peter");
		u2.setLastname("U.");
		u2.setEmail("test@test.test");
		userAccountManager.save(u2);
		
		UserAccount testUser1 = userAccountManager.create("testUser1", "pw", normalUserRole);
		testUser1.setFirstname("Susi");
		testUser1.setLastname("Müller");
		testUser1.setEmail("test@test.test");
		userAccountManager.save(testUser1);

		UserAccount ut2=userAccountManager.create("testUser2", "pw", normalUserRole);
		ut2.setFirstname("Paul");
		ut2.setLastname("Mustermann");
		ut2.setEmail("test@test.test");
		userAccountManager.save(ut2);
		
		UserAccount testUser5 = userAccountManager.create("testUser5", "pw", normalUserRole);
		testUser5.setFirstname("Abc");
		testUser5.setLastname("Def");
		testUser5.setEmail("test@test.test");
		userAccountManager.save(testUser5);
		
		UserAccount testUser6 = userAccountManager.create("testUser6", "pw", normalUserRole);
		testUser6.setFirstname("Ghi");
		testUser6.setLastname("Jkl");
		testUser6.setEmail("test@test.test");
		userAccountManager.save(testUser6);
		
		UserAccount testUser7 = userAccountManager.create("testUser7", "pw", normalUserRole);
		testUser7.setFirstname("Abc");
		testUser7.setLastname("Def");
		testUser7.setEmail("test@test.test");
		userAccountManager.save(testUser7);
		
		Address address1 = new Address("Nöthnitzer Str.", "46",
				"01187", "Dresden");
		Address address2 = new Address("Prager Str.", "10", "01069",
				"Dresden");
		Address testAddressWohnung3 = new Address("Scharnweberstr.", "22 A",
						"12587", "Berlin");
		Address testAddressWohnung4 = new Address("Merzdorfer Str.", "21 - 25",
						"01591", "Riesa");
		Address testAddressWohnung5 = new Address("Martin-Luther-Ring", "4-6",
						"04109", "Leipzig");
		Address testAddressRefugee1 = new Address("", "", "Dresden 6",
				"Südvorstadt", "01187", "Dresden");
		Address testAddressRefugee2 = new Address("", "",
				"NUK Friedrichshagen", "Friedrichshagen", "12587", "Berlin");

		User user1 = new User(u1, address1);
	    user1.setAddresstyp(AddresstypEnum.Wohnung);
		user1.setOrigin("Germany, Deutschland (DE)");
		user1.setRegistrationdate(new Date());
		user1.Activate();
		user1.setCoordinates(user1.createCoordinates());
		userRepository.save(user1);
		
		User user2 = new User(u2, address2);
		user2.setAddresstyp(AddresstypEnum.Wohnung);
		user2.setOrigin("United Arab Emirates, Vereinigte Arabische Emirate (AE)");
		user2.setRegistrationdate(new Date());
		user2.Activate();
		user2.setCoordinates(user2.createCoordinates());
		userRepository.save(user2);
		
		User user3 = new User(testUser1, testAddressWohnung3);
	    user3.setAddresstyp(AddresstypEnum.Wohnung);
		user3.setOrigin("Germany, Deutschland (DE)");
		user3.setRegistrationdate(new Date());
		user3.Activate();
		user3.setCoordinates(user3.createCoordinates());
		userRepository.save(user3);
		
		User user4 = new User(ut2, testAddressWohnung4);
		user4.setAddresstyp(AddresstypEnum.Wohnung);
		user4.setOrigin("United Arab Emirates, Vereinigte Arabische Emirate (AE)");
		user4.setRegistrationdate(new Date());
		user4.Activate();
		user4.setCoordinates(user4.createCoordinates());
		userRepository.save(user4);
		
		User user5 = new User(testUser5, testAddressWohnung5);
	    user5.setAddresstyp(AddresstypEnum.Wohnung);
		user5.setOrigin("Germany, Deutschland (DE)");
		user5.setRegistrationdate(new Date());
		user5.Activate();
		user5.setCoordinates(user5.createCoordinates());
		userRepository.save(user5);
		
		User user6 = new User(testUser6, testAddressRefugee1);
		user6.setAddresstyp(AddresstypEnum.Refugees_home);
		user6.setOrigin("United Arab Emirates, Vereinigte Arabische Emirate (AE)");
		user6.setRegistrationdate(new Date());
		user6.Activate();
		user6.setCoordinates(user6.createCoordinates());
		userRepository.save(user6);
		
		User user7 = new User(testUser7, testAddressRefugee2);
		user7.setAddresstyp(AddresstypEnum.Refugees_home);
		user7.setOrigin("United Arab Emirates, Vereinigte Arabische Emirate (AE)");
		user7.setRegistrationdate(new Date());
		user7.Activate();
		user7.setCoordinates(user7.createCoordinates());
		userRepository.save(user7);
		
		
		//Sprachen:
		Language language1= new Language("Deutsch", "de");
		Language language2= new Language("English", "en");
		Language language3= new Language("العربية", "ar");
		Language language4= new Language("español", "es");
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
		
		List<Module> mods = createModule();
		for(Module mod : mods){
			moduleRepository.save(mod);
		}
		
		List<InterfacePart> inPs = createInterfacePart(moduleRepository, languageRepository);
		for(InterfacePart inP : inPs){
			interfaceRepository.save(inP);
		}
	}
	
	private final void initializeTags(TagsRepository tagsRepository) {
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
	
	private final void initializeTextBlocks() {
		List<String> tbformatStrings = new LinkedList<>();
		
		tbformatStrings.add("Hallo ${name}");
		tbformatStrings.add("Das ist neuwertig ${offer}");
		
		List<TextBlock> tbl = new LinkedList<>();
		tbformatStrings.forEach((String s) -> tbl.add(new TextBlock(s, new FormatStringTagFilter(s).getTags())));
		textBlockRepository.save(tbl);
	}
	
	private void initializeGoodsAndActivities(UserAccountManager userAccountManager, 
            UserRepository userRepository, 
            DialogRepository dialogRepository, 
            LanguageRepository languageRepository,
            TagsRepository tagsRepository,
            ModuleRepository moduleRepository,
            InterfaceRepository interfaceRepository) {
		
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
	
	/**
	 * Creates a List with all Test Modules
	 * @return
	 */
	public List<Module> createModule(){
		List<Module> res = new ArrayList<Module>();
		
		Module mod1 = new Module("login", "login_link_text");
		Module mod2 = new Module("login", "login_error_text");
		Module mod3 = new Module("login", "login_nutzername_text");
		Module mod4 = new Module("login", "login_passwort_text");
		Module mod5 = new Module("login", "login_button_text");
		
		res.add(mod1);
		res.add(mod2);
		res.add(mod3);
		res.add(mod4);
		res.add(mod5);
		
		return res;
	}

	/**
	 * Creates a List with all Test InterfaceParts for the following
	 * Languages:
	 * 		- English
	 * 		- German
	 * 		- Arabic
	 * @param modRes ModuleRepository
	 * @param lanRes LanguageRepository
	 * @return
	 */
	public List<InterfacePart> createInterfacePart(ModuleRepository modRes, LanguageRepository lanRes){
		List<InterfacePart> res = new ArrayList<InterfacePart>();
		
		Language lanEN = lanRes.findByKennung("en");
		Language lanDE = lanRes.findByKennung("de");
		Language lanAR = lanRes.findByKennung("ar");
				
		Module mod1 = modRes.findByTemplateNameAndThymeLeafName("login", "login_link_text");
		Module mod2 = modRes.findByTemplateNameAndThymeLeafName("login", "login_error_text");
		Module mod3 = modRes.findByTemplateNameAndThymeLeafName("login", "login_nutzername_text");
		Module mod4 = modRes.findByTemplateNameAndThymeLeafName("login", "login_passwort_text");
		Module mod5 = modRes.findByTemplateNameAndThymeLeafName("login", "login_button_text");

		InterfacePart in0 = new InterfacePart("Noch nicht Registriert?", lanDE.getId(), mod1.getId());
		InterfacePart in1 = new InterfacePart("Der Nutzername oder das Passwort sind falsch!", lanDE.getId(), mod2.getId());
		InterfacePart in2 = new InterfacePart("Nutzername:", lanDE.getId(), mod3.getId());
		InterfacePart in3 = new InterfacePart("Passwort:", lanDE.getId(), mod4.getId());
		InterfacePart in4 = new InterfacePart("Login", lanDE.getId(), mod5.getId());
		
		InterfacePart in5 = new InterfacePart("Not registered yet?", lanEN.getId(), mod1.getId());
		InterfacePart in6 = new InterfacePart("The username or password is incorrect!", lanEN.getId(), mod2.getId());
		InterfacePart in7 = new InterfacePart("Username:", lanEN.getId(), mod3.getId());
		InterfacePart in8 = new InterfacePart("Password:", lanEN.getId(), mod4.getId());
		InterfacePart in9 = new InterfacePart("Login", lanEN.getId(), mod5.getId());
		
		InterfacePart in10 = new InterfacePart("Noch nicht Registriert? -- Auf Arabisch", lanAR.getId(), mod1.getId());
		InterfacePart in11 = new InterfacePart("Der Nutzername oder das Passwort sind falsch! -- Auf Arabisch", lanAR.getId(), mod2.getId());
		InterfacePart in12 = new InterfacePart("Nutzername -- Auf Arabisch:", lanAR.getId(), mod3.getId());
		InterfacePart in13 = new InterfacePart("Passwort -- Auf Arabisch:", lanAR.getId(), mod4.getId());
		InterfacePart in14 = new InterfacePart("Login -- Auf Arabisch", lanAR.getId(), mod5.getId());
		
		res.add(in0);
		res.add(in1);
		res.add(in2);
		res.add(in3);
		res.add(in4);
		res.add(in5);
		res.add(in6);
		res.add(in7);
		res.add(in8);
		res.add(in9);
		res.add(in10);
		res.add(in11);
		res.add(in12);
		res.add(in13);
		res.add(in14);
		
		return res;
	}

}

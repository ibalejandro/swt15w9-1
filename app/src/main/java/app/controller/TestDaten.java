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
		Module mod6 = new Module("login","login_forgot_password_text");
		Module mod7 = new Module("login","login_text");
		Module mod8 = new Module("activation","activation_mailsent_text");
		Module mod9 = new Module("activation","activation_link_text");
		Module mod10 = new Module("activation","activation_eclipse_text");
		Module mod11 = new Module("item_general","item_general_name_text");
		Module mod12 = new Module("item_general","item_general_picture_text");
		Module mod13 = new Module("item_general","item_general_description_text");
		Module mod14 = new Module("item_general","item_general_tag_text");
		Module mod15 = new Module("item_general","item_general_startdate_text");
		Module mod16 = new Module("item_general","item_general_enddate_text");
		Module mod17 = new Module("item_general","item_general_userid_text");
		Module mod18 = new Module("item_general","item_general_goodbutton_text");
		Module mod19 = new Module("item_general","item_general_activitybutton_text");
		Module mod20 = new Module("tag","tag_addednewtag_text");
		Module mod21 = new Module("language","language_language_text");
		Module mod22 = new Module("language","language_iso_text");
		Module mod23 = new Module("tag","tag_newtag_text");
		Module mod24 = new Module("user","user_firstname_text");
		Module mod25 = new Module("user","user_lastname_text");
		Module mod26 = new Module("user","user_email_text");
		Module mod27 = new Module("user","user_origin_text");
		Module mod28 = new Module("user","user_username_text");
		Module mod29 = new Module("user","user_registrationdate_text");
		Module mod30 = new Module("user","user_activationstatus_text");
		Module mod31 = new Module("item_general","item_general_offeredby_text");
		Module mod32 = new Module("admin","admin_activateuser_text");
		Module mod33 = new Module("admin","admin_toconfirm_text");
		Module mod34 = new Module("admin","admin_submit_text");
		Module mod35 = new Module("tag","tag_delete_text");
		Module mod36 = new Module("tag","tag_update_text");
		Module mod37 = new Module("tag","tag_tags_text");
		Module mod38 = new Module("template","template_edithead_text");
		Module mod39 = new Module("template","template_thymeleafname_text");
		Module mod40 = new Module("template","template_reflang_text");
		Module mod41 = new Module("template","template_changelang_text");
		Module mod42 = new Module("template","template_editsave_text");
		Module mod43 = new Module("user","user_changepwhead_text");
		Module mod44 = new Module("user","user_oldpw_text");
		Module mod45 = new Module("user","user_newpw_text");
		Module mod46 = new Module("user","user_next_text");
		Module mod47 = new Module("user","user_userdata_text");
		Module mod48 = new Module("user","user_changepw_text");
		Module mod49 = new Module("user","user_deactivateuser_text");
		Module mod50 = new Module("user","user_changedata_text");
		Module mod51 = new Module("user","user_addtype_text");
		Module mod52 = new Module("user","user_refugeehome_text");
		Module mod53 = new Module("user","user_citypart_text");
		Module mod54 = new Module("user","user_cityzip_text");
		Module mod55 = new Module("user","user_changeadd_text");
		Module mod56 = new Module("user","user_preflang_text");
		Module mod57 = new Module("user","user_otherlangs_text");
		Module mod58 = new Module("user","user_changelang_text");
		Module mod59 = new Module("admin","admin_changeuser_text");
		Module mod60 = new Module("user","user_streethnr_text");
		Module mod61 = new Module("user","user_deactivateconfirm_text");
		Module mod62 = new Module("user","user_jscaptcha_text");
		Module mod63 = new Module("template","template_deletetemphead_text");
		Module mod64 = new Module("user","user_delete_text");
		Module mod65 = new Module("item_general","item_general_deletedact_text");
		Module mod66 = new Module("item_general","item_general_deletedgood_text");
		Module mod67 = new Module("tag","tag_deletedtag_text");
		Module mod68 = new Module("dialog","dialog_mydialog_text");
		Module mod69 = new Module("dialog","dialog_title_text");
		Module mod70 = new Module("dialog","dialog_partner_text");
		Module mod71 = new Module("dialog","dialog_link_text");
		Module mod72 = new Module("dialog","dialog_goto_text");
		Module mod73 = new Module("error","error_error_text");
		Module mod74 = new Module("error","error_unusedcaptcha_text");
		Module mod75 = new Module("error","error_back_text");
		Module mod76 = new Module("error","error_unknownerror_text");
		Module mod77 = new Module("error","error_emptyfields_text");
		Module mod78 = new Module("error","error_notactivated_text");
		Module mod79 = new Module("item_general","item_general_allgoods_text");
		Module mod80 = new Module("item_general","item_general_allacts_text");
		Module mod81 = new Module("item_general","item_general_update_text");
		Module mod82 = new Module("user","user_otherlang_text");
		Module mod83 = new Module("user","user_hometype_text");
		Module mod84 = new Module("user","user_flat_text");
		Module mod85 = new Module("user","user_street_text");
		Module mod86 = new Module("user","user_housenr_text");
		Module mod87 = new Module("user","user_zip_text");
		Module mod88 = new Module("user","user_city_text");
		Module mod89 = new Module("user","user_rhomename_text");
		Module mod90 = new Module("user","user_yourhome_text");
		Module mod91 = new Module("user","user_yourlangs_text");
		Module mod92 = new Module("error","error_invalid_text");
		Module mod93 = new Module("user","user_aboutyou_text");
		Module mod94 = new Module("item_general","item_general_acts_text");
		Module mod95 = new Module("item_general","item_general_goods_text");
		Module mod96 = new Module("nav","nav_refugeeapp_text");
		Module mod97 = new Module("nav","nav_menu_text");
		Module mod98 = new Module("nav","nav_register_text");
		Module mod99 = new Module("nav","nav_login_text");
		Module mod100 = new Module("nav","nav_offer_text");
		Module mod101 = new Module("nav","nav_good_text");
		Module mod102 = new Module("nav","nav_activity_text");
		Module mod103 = new Module("nav","nav_alltags_text");
		Module mod104 = new Module("nav","nav_newtblock_text");
		Module mod105 = new Module("nav","nav_tblocklist_text");
		Module mod106 = new Module("nav","nav_userdetails_text");
		Module mod107 = new Module("nav","nav_logout_text");
		Module mod108 = new Module("nav","nav_data_text");
		Module mod109 = new Module("nav","nav_myofferedg_text");
		Module mod110 = new Module("nav","nav_myoffereda_text");
		Module mod111 = new Module("nav","nav_dialoglist_text");
		Module mod112 = new Module("nav","nav_startdialog_text");
		Module mod113 = new Module("user","user_createacc_text");
		Module mod114 = new Module("error","error_empty_text");
		Module mod115 = new Module("error","error_inuse_text");
		Module mod116 = new Module("error","error_short_text");
		Module mod117 = new Module("error","error_insecure_text");
		Module mod118 = new Module("user","user_reppassword_text");
		Module mod119 = new Module("error","error_unequal_text");
		Module mod120 = new Module("error","error_select_text");
		Module mod121 = new Module("error","error_nonexist_text");
		Module mod122 = new Module("error","error_nonexistact_text");
		Module mod123 = new Module("error","error_nonexistgood_text");
		Module mod124 = new Module("error","error_nonexisttag_text");
		Module mod125 = new Module("error","error_notyours_text");
		Module mod126 = new Module("error","error_loggedout_text");
		Module mod127 = new Module("item_general","item_general_offeract_text");
		Module mod128 = new Module("item_general","item_general_offergood_text");
		Module mod129 = new Module("item_general","item_general_offeredact_text");
		Module mod130 = new Module("item_general","item_general_offeredgood_text");
		Module mod131 = new Module("user","user_restpass_text");
		Module mod132 = new Module("nav","nav_search_text");
		Module mod133 = new Module("user","user_searchby_text");
		Module mod134 = new Module("dialog","dialog_new_text");
		Module mod135 = new Module("template","template_interface_text");
		Module mod136 = new Module("template","template_newlang_text");
		Module mod137 = new Module("template","template_langshort_text");
		Module mod138 = new Module("template","template_newtemp_text");
		Module mod139 = new Module("template","template_modname_text");
		Module mod140 = new Module("template","template_tempname_text");
		Module mod141 = new Module("template","template_newmod_text");
		Module mod142 = new Module("template","template_delmod_text");
		Module mod143 = new Module("nav","nav_rfs_text");
		Module mod144 = new Module("nav","nav_tbaf_text");
		Module mod145 = new Module("item_general","item_general_updatedact_text");
		Module mod146 = new Module("item_general","item_general_updatedgood_text");
		Module mod147 = new Module("item_general","item_general_updatedtag_text");
		Module mod148 = new Module("error","error_norights_text");
		Module mod149 = new Module("user","user_address_text");
		Module mod150 = new Module("user","user_valisucc_text");
		Module mod151 = new Module("user","user_valisend_text");
		Module mod152 = new Module("user","user_passwordreqs_text");
		
		res.add(mod1);
		res.add(mod2);
		res.add(mod3);
		res.add(mod4);
		res.add(mod5);
		res.add(mod6);
		res.add(mod7);
		res.add(mod8);
		res.add(mod9);
		res.add(mod10);
		res.add(mod11);
		res.add(mod12);
		res.add(mod13);
		res.add(mod14);
		res.add(mod15);
		res.add(mod16);
		res.add(mod17);
		res.add(mod18);
		res.add(mod19);
		res.add(mod20);
		res.add(mod21);
		res.add(mod22);
		res.add(mod23);
		res.add(mod24);
		res.add(mod25);
		res.add(mod26);
		res.add(mod27);
		res.add(mod28);
		res.add(mod29);
		res.add(mod30);
		res.add(mod31);
		res.add(mod32);
		res.add(mod33);
		res.add(mod34);
		res.add(mod35);
		res.add(mod36);
		res.add(mod37);
		res.add(mod38);
		res.add(mod39);
		res.add(mod40);
		res.add(mod41);
		res.add(mod42);
		res.add(mod43);
		res.add(mod44);
		res.add(mod45);
		res.add(mod46);
		res.add(mod47);
		res.add(mod48);
		res.add(mod49);
		res.add(mod50);
		res.add(mod51);
		res.add(mod52);
		res.add(mod53);
		res.add(mod54);
		res.add(mod55);
		res.add(mod56);
		res.add(mod57);
		res.add(mod58);
		res.add(mod59);
		res.add(mod60);
		res.add(mod61);
		res.add(mod62);
		res.add(mod63);
		res.add(mod64);
		res.add(mod65);
		res.add(mod66);
		res.add(mod67);
		res.add(mod68);
		res.add(mod69);
		res.add(mod70);
		res.add(mod71);
		res.add(mod72);
		res.add(mod73);
		res.add(mod74);
		res.add(mod75);
		res.add(mod76);
		res.add(mod77);
		res.add(mod78);
		res.add(mod79);
		res.add(mod80);
		res.add(mod81);
		res.add(mod82);
		res.add(mod83);
		res.add(mod84);
		res.add(mod85);
		res.add(mod86);
		res.add(mod87);
		res.add(mod88);
		res.add(mod89);
		res.add(mod90);
		res.add(mod91);
		res.add(mod92);
		res.add(mod93);
		res.add(mod94);
		res.add(mod95);
		res.add(mod96);
		res.add(mod97);
		res.add(mod98);
		res.add(mod99);
		res.add(mod100);
		res.add(mod101);
		res.add(mod102);
		res.add(mod103);
		res.add(mod104);
		res.add(mod105);
		res.add(mod106);
		res.add(mod107);
		res.add(mod108);
		res.add(mod109);
		res.add(mod110);
		res.add(mod111);
		res.add(mod112);
		res.add(mod113);
		res.add(mod114);
		res.add(mod115);
		res.add(mod116);
		res.add(mod117);
		res.add(mod118);
		res.add(mod119);
		res.add(mod120);
		res.add(mod121);
		res.add(mod122);
		res.add(mod123);
		res.add(mod124);
		res.add(mod125);
		res.add(mod126);
		res.add(mod127);
		res.add(mod128);
		res.add(mod129);
		res.add(mod130);
		res.add(mod131);
		res.add(mod132);
		res.add(mod133);
		res.add(mod134);
		res.add(mod135);
		res.add(mod136);
		res.add(mod137);
		res.add(mod138);
		res.add(mod139);
		res.add(mod140);
		res.add(mod141);
		res.add(mod142);
		res.add(mod143);
		res.add(mod144);
		res.add(mod145);
		res.add(mod146);
		res.add(mod147);
		res.add(mod148);
		res.add(mod149);
		res.add(mod150);
		res.add(mod151);
		res.add(mod152);
		
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
		Module mod6 = modRes.findByTemplateNameAndThymeLeafName("login", "login_forgot_password_text");
		Module mod7 = modRes.findByTemplateNameAndThymeLeafName("login", "login_text");
		Module mod8 = modRes.findByTemplateNameAndThymeLeafName("activation","activation_mailsent_text");
		Module mod9 = modRes.findByTemplateNameAndThymeLeafName("activation","activation_link_text");
		Module mod10 = modRes.findByTemplateNameAndThymeLeafName("activation","activation_eclipse_text");
		Module mod11 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_name_text");
		Module mod12 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_picture_text");
		Module mod13 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_description_text");
		Module mod14 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_tag_text");
		Module mod15 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_startdate_text");
		Module mod16 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_enddate_text");
		Module mod17 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_userid_text");
		Module mod18 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_goodbutton_text");
		Module mod19 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_activitybutton_text");
		Module mod20 = modRes.findByTemplateNameAndThymeLeafName("tag","tag_addednewtag_text");
		Module mod21 = modRes.findByTemplateNameAndThymeLeafName("language","language_language_text");
		Module mod22 = modRes.findByTemplateNameAndThymeLeafName("language","language_iso_text");
		Module mod23 = modRes.findByTemplateNameAndThymeLeafName("tag","tag_newtag_text");
		Module mod24 = modRes.findByTemplateNameAndThymeLeafName("user","user_firstname_text");
		Module mod25 = modRes.findByTemplateNameAndThymeLeafName("user","user_lastname_text");
		Module mod26 = modRes.findByTemplateNameAndThymeLeafName("user","user_email_text");
		Module mod27 = modRes.findByTemplateNameAndThymeLeafName("user","user_origin_text");
		Module mod28 = modRes.findByTemplateNameAndThymeLeafName("user","user_username_text");
		Module mod29 = modRes.findByTemplateNameAndThymeLeafName("user","user_registrationdate_text");
		Module mod30 = modRes.findByTemplateNameAndThymeLeafName("user","user_activationstatus_text");
		Module mod31 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_offeredby_text");
		Module mod32 = modRes.findByTemplateNameAndThymeLeafName("admin","admin_activateuser_text");
		Module mod33 = modRes.findByTemplateNameAndThymeLeafName("admin","admin_toconfirm_text");
		Module mod34 = modRes.findByTemplateNameAndThymeLeafName("admin","admin_submit_text");
		Module mod35 = modRes.findByTemplateNameAndThymeLeafName("tag","tag_delete_text");
		Module mod36 = modRes.findByTemplateNameAndThymeLeafName("tag","tag_update_text");
		Module mod37 = modRes.findByTemplateNameAndThymeLeafName("tag","tag_tags_text");
		Module mod38 = modRes.findByTemplateNameAndThymeLeafName("template","template_edithead_text");
		Module mod39 = modRes.findByTemplateNameAndThymeLeafName("template","template_thymeleafname_text");
		Module mod40 = modRes.findByTemplateNameAndThymeLeafName("template","template_reflang_text");
		Module mod41 = modRes.findByTemplateNameAndThymeLeafName("template","template_changelang_text");
		Module mod42 = modRes.findByTemplateNameAndThymeLeafName("template","template_editsave_text");
		Module mod43 = modRes.findByTemplateNameAndThymeLeafName("user","user_changepwhead_text");
		Module mod44 = modRes.findByTemplateNameAndThymeLeafName("user","user_oldpw_text");
		Module mod45 = modRes.findByTemplateNameAndThymeLeafName("user","user_newpw_text");
		Module mod46 = modRes.findByTemplateNameAndThymeLeafName("user","user_next_text");
		Module mod47 = modRes.findByTemplateNameAndThymeLeafName("user","user_userdata_text");
		Module mod48 = modRes.findByTemplateNameAndThymeLeafName("user","user_changepw_text");
		Module mod49 = modRes.findByTemplateNameAndThymeLeafName("user","user_deactivateuser_text");
		Module mod50 = modRes.findByTemplateNameAndThymeLeafName("user","user_changedata_text");
		Module mod51 = modRes.findByTemplateNameAndThymeLeafName("user","user_addtype_text");
		Module mod52 = modRes.findByTemplateNameAndThymeLeafName("user","user_refugeehome_text");
		Module mod53 = modRes.findByTemplateNameAndThymeLeafName("user","user_citypart_text");
		Module mod54 = modRes.findByTemplateNameAndThymeLeafName("user","user_cityzip_text");
		Module mod55 = modRes.findByTemplateNameAndThymeLeafName("user","user_changeadd_text");
		Module mod56 = modRes.findByTemplateNameAndThymeLeafName("user","user_preflang_text");
		Module mod57 = modRes.findByTemplateNameAndThymeLeafName("user","user_otherlangs_text");
		Module mod58 = modRes.findByTemplateNameAndThymeLeafName("user","user_changelang_text");
		Module mod59 = modRes.findByTemplateNameAndThymeLeafName("admin","admin_changeuser_text");
		Module mod60 = modRes.findByTemplateNameAndThymeLeafName("user","user_streethnr_text");
		Module mod61 = modRes.findByTemplateNameAndThymeLeafName("user","user_deactivateconfirm_text");
		Module mod62 = modRes.findByTemplateNameAndThymeLeafName("user","user_jscaptcha_text");
		Module mod63 = modRes.findByTemplateNameAndThymeLeafName("template","template_deletetemphead_text");
		Module mod64 = modRes.findByTemplateNameAndThymeLeafName("user","user_delete_text");
		Module mod65 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_deletedact_text");
		Module mod66 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_deletedgood_text");
		Module mod67 = modRes.findByTemplateNameAndThymeLeafName("tag","tag_deletedtag_text");
		Module mod68 = modRes.findByTemplateNameAndThymeLeafName("dialog","dialog_mydialog_text");
		Module mod69 = modRes.findByTemplateNameAndThymeLeafName("dialog","dialog_title_text");
		Module mod70 = modRes.findByTemplateNameAndThymeLeafName("dialog","dialog_partner_text");
		Module mod71 = modRes.findByTemplateNameAndThymeLeafName("dialog","dialog_link_text");
		Module mod72 = modRes.findByTemplateNameAndThymeLeafName("dialog","dialog_goto_text");
		Module mod73 = modRes.findByTemplateNameAndThymeLeafName("error","error_error_text");
		Module mod74 = modRes.findByTemplateNameAndThymeLeafName("error","error_unusedcaptcha_text");
		Module mod75 = modRes.findByTemplateNameAndThymeLeafName("error","error_back_text");
		Module mod76 = modRes.findByTemplateNameAndThymeLeafName("error","error_unknownerror_text");
		Module mod77 = modRes.findByTemplateNameAndThymeLeafName("error","error_emptyfields_text");
		Module mod78 = modRes.findByTemplateNameAndThymeLeafName("error","error_notactivated_text");
		Module mod79 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_allgoods_text");
		Module mod80 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_allacts_text");
		Module mod81 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_update_text");
		Module mod82 = modRes.findByTemplateNameAndThymeLeafName("user","user_otherlang_text");
		Module mod83 = modRes.findByTemplateNameAndThymeLeafName("user","user_hometype_text");
		Module mod84 = modRes.findByTemplateNameAndThymeLeafName("user","user_flat_text");
		Module mod85 = modRes.findByTemplateNameAndThymeLeafName("user","user_street_text");
		Module mod86 = modRes.findByTemplateNameAndThymeLeafName("user","user_housenr_text");
		Module mod87 = modRes.findByTemplateNameAndThymeLeafName("user","user_zip_text");
		Module mod88 = modRes.findByTemplateNameAndThymeLeafName("user","user_city_text");
		Module mod89 = modRes.findByTemplateNameAndThymeLeafName("user","user_rhomename_text");
		Module mod90 = modRes.findByTemplateNameAndThymeLeafName("user","user_yourhome_text");
		Module mod91 = modRes.findByTemplateNameAndThymeLeafName("user","user_yourlangs_text");
		Module mod92 = modRes.findByTemplateNameAndThymeLeafName("error","error_invalid_text");
		Module mod93 = modRes.findByTemplateNameAndThymeLeafName("user","user_aboutyou_text");
		Module mod94 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_acts_text");
		Module mod95 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_goods_text");
		Module mod96 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_refugeeapp_text");
		Module mod97 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_menu_text");
		Module mod98 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_register_text");
		Module mod99 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_login_text");
		Module mod100 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_offer_text");
		Module mod101 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_good_text");
		Module mod102 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_activity_text");
		Module mod103 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_alltags_text");
		Module mod104 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_newtblock_text");
		Module mod105 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_tblocklist_text");
		Module mod106 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_userdetails_text");
		Module mod107 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_logout_text");
		Module mod108 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_data_text");
		Module mod109 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_myofferedg_text");
		Module mod110 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_myoffereda_text");
		Module mod111 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_dialoglist_text");
		Module mod112 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_startdialog_text");
		Module mod113 = modRes.findByTemplateNameAndThymeLeafName("user","user_createacc_text");
		Module mod114 = modRes.findByTemplateNameAndThymeLeafName("error","error_empty_text");
		Module mod115 = modRes.findByTemplateNameAndThymeLeafName("error","error_inuse_text");
		Module mod116 = modRes.findByTemplateNameAndThymeLeafName("error","error_short_text");
		Module mod117 = modRes.findByTemplateNameAndThymeLeafName("error","error_insecure_text");
		Module mod118 = modRes.findByTemplateNameAndThymeLeafName("user","user_reppassword_text");
		Module mod119 = modRes.findByTemplateNameAndThymeLeafName("error","error_unequal_text");
		Module mod120 = modRes.findByTemplateNameAndThymeLeafName("error","error_select_text");
		Module mod121 = modRes.findByTemplateNameAndThymeLeafName("error","error_nonexist_text");
		Module mod122 = modRes.findByTemplateNameAndThymeLeafName("error","error_nonexistact_text");
		Module mod123 = modRes.findByTemplateNameAndThymeLeafName("error","error_nonexistgood_text");
		Module mod124 = modRes.findByTemplateNameAndThymeLeafName("error","error_nonexisttag_text");
		Module mod125 = modRes.findByTemplateNameAndThymeLeafName("error","error_notyours_text");
		Module mod126 = modRes.findByTemplateNameAndThymeLeafName("error","error_loggedout_text");
		Module mod127 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_offeract_text");
		Module mod128 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_offergood_text");
		Module mod129 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_offeredact_text");
		Module mod130 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_offeredgood_text");
		Module mod131 = modRes.findByTemplateNameAndThymeLeafName("user","user_restpass_text");
		Module mod132 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_search_text");
		Module mod133 = modRes.findByTemplateNameAndThymeLeafName("user","user_searchby_text");
		Module mod134 = modRes.findByTemplateNameAndThymeLeafName("dialog","dialog_new_text");
		Module mod135 = modRes.findByTemplateNameAndThymeLeafName("template","template_interface_text");
		Module mod136 = modRes.findByTemplateNameAndThymeLeafName("template","template_newlang_text");
		Module mod137 = modRes.findByTemplateNameAndThymeLeafName("template","template_langshort_text");
		Module mod138 = modRes.findByTemplateNameAndThymeLeafName("template","template_newtemp_text");
		Module mod139 = modRes.findByTemplateNameAndThymeLeafName("template","template_modname_text");
		Module mod140 = modRes.findByTemplateNameAndThymeLeafName("template","template_tempname_text");
		Module mod141 = modRes.findByTemplateNameAndThymeLeafName("template","template_newmod_text");
		Module mod142 = modRes.findByTemplateNameAndThymeLeafName("template","template_delmod_text");
		Module mod143 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_rfs_text");
		Module mod144 = modRes.findByTemplateNameAndThymeLeafName("nav","nav_tbaf_text");
		Module mod145 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_updatedact_text");
		Module mod146 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_updatedgood_text");
		Module mod147 = modRes.findByTemplateNameAndThymeLeafName("item_general","item_general_updatedtag_text");
		Module mod148 = modRes.findByTemplateNameAndThymeLeafName("error","error_norights_text");
		Module mod149 = modRes.findByTemplateNameAndThymeLeafName("user","user_address_text");
		Module mod150 = modRes.findByTemplateNameAndThymeLeafName("user","user_valisucc_text");
		Module mod151 = modRes.findByTemplateNameAndThymeLeafName("user","user_valisend_text");
		Module mod152 = modRes.findByTemplateNameAndThymeLeafName("user","user_passwordreqs_text");
		
		InterfacePart in0 = new InterfacePart ("Noch nicht Registriert?", lanDE.getId(), mod1.getId());
		InterfacePart in1 = new InterfacePart ("Der Nutzername oder das Passwort sind falsch!", lanDE.getId(), mod2.getId());
		InterfacePart in2 = new InterfacePart ("Nutzername", lanDE.getId(), mod3.getId());
		InterfacePart in3 = new InterfacePart ("Passwort", lanDE.getId(), mod4.getId());
		InterfacePart in4 = new InterfacePart ("Einloggen", lanDE.getId(), mod5.getId());
		InterfacePart in5 = new InterfacePart ("Passwort vergessen?", lanDE.getId(), mod6.getId());
		InterfacePart in6 = new InterfacePart ("Login", lanDE.getId(), mod7.getId());
		
		InterfacePart in7 = new InterfacePart ("Not registered yet?", lanEN.getId(), mod1.getId());
		InterfacePart in8 = new InterfacePart ("The username or password is incorrect!", lanEN.getId(), mod2.getId());
		InterfacePart in9 = new InterfacePart ("Username", lanEN.getId(), mod3.getId());
		InterfacePart in10 = new InterfacePart ("Password", lanEN.getId(), mod4.getId());
		InterfacePart in11 = new InterfacePart ("Log in", lanEN.getId(), mod5.getId());
		InterfacePart in12 = new InterfacePart ("Forgot Password?", lanEN.getId(), mod6.getId());
		InterfacePart in13 = new InterfacePart ("Login", lanEN.getId(), mod7.getId());
		
		InterfacePart in14 = new InterfacePart ("Noch nicht Registriert? -- Auf Arabisch", lanAR.getId(), mod1.getId());
		InterfacePart in15 = new InterfacePart ("Der Nutzername oder das Passwort sind falsch! -- Auf Arabisch", lanAR.getId(), mod2.getId());
		InterfacePart in16 = new InterfacePart ("Nutzername -- Auf Arabisch", lanAR.getId(), mod3.getId());
		InterfacePart in17 = new InterfacePart ("Passwort -- Auf Arabisch", lanAR.getId(), mod4.getId());
		InterfacePart in18 = new InterfacePart ("Login -- Auf Arabisch", lanAR.getId(), mod5.getId());
		InterfacePart in19 = new InterfacePart ("Passwort vergessen? -- Auf Arabisch", lanAR.getId(), mod6.getId());
		InterfacePart in20 = new InterfacePart ("Login -- Auf Arabisch", lanAR.getId(), mod7.getId());
		
		InterfacePart in21 = new InterfacePart ("Die Aktivierungsmail wurde an ihre Mailadresse gesendet. Bitte klicken sie den Link darin, um ihren Account zu aktivieren.",lanDE.getId(),mod8.getId());
		InterfacePart in22 = new InterfacePart ("The activation mail has been sent to your mail address. Please click on the link in it to activate your account.",lanEN.getId(),mod8.getId());
		
		InterfacePart in23 = new InterfacePart ("Lokaler Aktivierungslink ",lanDE.getId(),mod9.getId());
		InterfacePart in24 = new InterfacePart ("Link for local activation ",lanEN.getId(),mod9.getId());
		InterfacePart in25 = new InterfacePart (" Wird im Eclise ausgegeben ",lanDE.getId(),mod10.getId());
		InterfacePart in26 = new InterfacePart (" Will be shown in eclipse ",lanEN.getId(),mod10.getId());
		
		InterfacePart in27 = new InterfacePart ("Name",lanDE.getId(),mod11.getId());
		InterfacePart in28 = new InterfacePart ("Name",lanEN.getId(),mod11.getId());
		InterfacePart in29 = new InterfacePart ("Bild",lanDE.getId(),mod12.getId());
		InterfacePart in30 = new InterfacePart ("Picture",lanEN.getId(),mod12.getId());
		InterfacePart in31 = new InterfacePart ("Beschreibung",lanDE.getId(),mod13.getId());
		InterfacePart in32 = new InterfacePart ("Description",lanEN.getId(),mod13.getId());
		InterfacePart in33 = new InterfacePart ("Kategorie",lanDE.getId(),mod14.getId());
		InterfacePart in34 = new InterfacePart ("Tag",lanEN.getId(),mod14.getId());
		InterfacePart in35 = new InterfacePart ("Beginn",lanDE.getId(),mod15.getId());
		InterfacePart in36 = new InterfacePart ("Start date",lanEN.getId(),mod15.getId());
		InterfacePart in37 = new InterfacePart ("Ende",lanDE.getId(),mod16.getId());
		InterfacePart in38 = new InterfacePart ("End date",lanEN.getId(),mod16.getId());
		InterfacePart in39 = new InterfacePart ("Benutzer ID",lanDE.getId(),mod17.getId());
		InterfacePart in40 = new InterfacePart ("User ID",lanEN.getId(),mod17.getId());
		InterfacePart in41 = new InterfacePart ("Gut anfragen",lanDE.getId(),mod18.getId());
		InterfacePart in42 = new InterfacePart ("Ask for this good",lanEN.getId(),mod18.getId());
		InterfacePart in43 = new InterfacePart ("Teilnahme anfragen",lanDE.getId(),mod19.getId());
		InterfacePart in44 = new InterfacePart ("Ask for this activity",lanEN.getId(),mod19.getId());
		
		InterfacePart in45 = new InterfacePart (" Sie haben gerade diese Kategorie hinzugefügt ",lanDE.getId(),mod20.getId());
		InterfacePart in46 = new InterfacePart (" You just added this tag ",lanEN.getId(),mod20.getId());
		
		InterfacePart in47 = new InterfacePart ("Sprache",lanDE.getId(),mod21.getId());
		InterfacePart in48 = new InterfacePart ("Language",lanEN.getId(),mod21.getId());
		InterfacePart in49 = new InterfacePart ("ISO Code",lanDE.getId(),mod22.getId());
		InterfacePart in50= new InterfacePart ("ISO Code",lanEN.getId(),mod22.getId());
		
		InterfacePart in51 = new InterfacePart ("Neue Kategorie hinzufügen",lanDE.getId(),mod23.getId());
		InterfacePart in52 = new InterfacePart ("Add new tag",lanEN.getId(),mod23.getId());
		
		InterfacePart in53 = new InterfacePart ("Vorname",lanDE.getId(),mod24.getId());
		InterfacePart in54 = new InterfacePart ("First name",lanEN.getId(),mod24.getId());
		InterfacePart in55 = new InterfacePart ("Nachname",lanDE.getId(),mod25.getId());
		InterfacePart in56 = new InterfacePart ("Last name",lanEN.getId(),mod25.getId());
		InterfacePart in57 = new InterfacePart ("E-Mail",lanDE.getId(),mod26.getId());
		InterfacePart in58 = new InterfacePart ("E-Mail",lanEN.getId(),mod26.getId());
		InterfacePart in59 = new InterfacePart ("Herkunft",lanDE.getId(),mod27.getId());
		InterfacePart in60 = new InterfacePart ("Origin",lanEN.getId(),mod27.getId());
		InterfacePart in61 = new InterfacePart ("Benutzername",lanDE.getId(),mod28.getId());
		InterfacePart in62 = new InterfacePart ("Username",lanEN.getId(),mod28.getId());
		InterfacePart in63 = new InterfacePart ("Registrierungsdatum",lanDE.getId(),mod29.getId());
		InterfacePart in64 = new InterfacePart ("Date of registration",lanEN.getId(),mod29.getId());
		InterfacePart in65 = new InterfacePart ("Aktivierungsstatus",lanDE.getId(),mod30.getId());
		InterfacePart in66 = new InterfacePart ("State of activation",lanEN.getId(),mod30.getId());
		
		InterfacePart in67 = new InterfacePart ("Bereitgestellt bei",lanDE.getId(),mod31.getId());
		InterfacePart in68 = new InterfacePart ("Offered by",lanEN.getId(),mod31.getId());
		
		InterfacePart in69 = new InterfacePart ("Nutzerkonto aktivieren",lanDE.getId(),mod32.getId());
		InterfacePart in70 = new InterfacePart ("Activate user account",lanEN.getId(),mod32.getId());
		InterfacePart in71 = new InterfacePart ("Wollen Sie wirklich ",lanDE.getId(),mod33.getId());
		InterfacePart in72 = new InterfacePart ("Do you really want to ",lanEN.getId(),mod33.getId());
		InterfacePart in73 = new InterfacePart ("Abschicken",lanDE.getId(),mod34.getId());
		InterfacePart in74 = new InterfacePart ("Submit",lanEN.getId(),mod34.getId());
		
		InterfacePart in75 = new InterfacePart ("Kategorie löschen",lanDE.getId(),mod35.getId());
		InterfacePart in76 = new InterfacePart ("Delete tag",lanEN.getId(),mod35.getId());
		InterfacePart in77 = new InterfacePart ("Kategorie ändern",lanDE.getId(),mod36.getId());
		InterfacePart in78 = new InterfacePart ("Update tag",lanEN.getId(),mod36.getId());
		InterfacePart in79 = new InterfacePart ("Kategorien",lanDE.getId(),mod37.getId());
		InterfacePart in80 = new InterfacePart ("Tags",lanEN.getId(),mod37.getId());
		
		InterfacePart in81 = new InterfacePart ("Übersetzung bearbeiten ",lanDE.getId(),mod38.getId());
		InterfacePart in82 = new InterfacePart ("Edit translation ",lanEN.getId(),mod38.getId());
		InterfacePart in83 = new InterfacePart ("Thymeleaf",lanDE.getId(),mod39.getId());
		InterfacePart in84 = new InterfacePart ("Thymeleaf",lanEN.getId(),mod39.getId());
		InterfacePart in85 = new InterfacePart ("Referenzsprache",lanDE.getId(),mod40.getId());
		InterfacePart in86 = new InterfacePart ("Reference language",lanEN.getId(),mod40.getId());
		InterfacePart in87 = new InterfacePart ("Zu übersetzende Sprache",lanDE.getId(),mod41.getId());
		InterfacePart in88 = new InterfacePart ("Language to be translated",lanEN.getId(),mod41.getId());
		InterfacePart in89 = new InterfacePart ("Speichern",lanDE.getId(),mod42.getId());
		InterfacePart in90 = new InterfacePart ("Save",lanEN.getId(),mod42.getId());

		InterfacePart in91 = new InterfacePart ("Passwort ändern ",lanDE.getId(),mod43.getId());
		InterfacePart in92 = new InterfacePart ("Change password ",lanEN.getId(),mod43.getId());
		InterfacePart in93 = new InterfacePart ("Altes Passwort",lanDE.getId(),mod44.getId());
		InterfacePart in94 = new InterfacePart ("Old password",lanEN.getId(),mod44.getId());
		InterfacePart in95 = new InterfacePart ("Neues Passwort",lanDE.getId(),mod45.getId());
		InterfacePart in96 = new InterfacePart ("New password",lanEN.getId(),mod45.getId());
		InterfacePart in97 = new InterfacePart ("Weiter",lanDE.getId(),mod46.getId());
		InterfacePart in98 = new InterfacePart ("Next",lanEN.getId(),mod46.getId());
		
		InterfacePart in99 = new InterfacePart ("Kundendaten ",lanDE.getId(),mod47.getId());
		InterfacePart in108 = new InterfacePart ("User data ",lanEN.getId(),mod47.getId());
		InterfacePart in109 = new InterfacePart ("Passwort ändern",lanDE.getId(),mod48.getId());
		InterfacePart in110 = new InterfacePart ("Change Password",lanEN.getId(),mod48.getId());
		InterfacePart in111 = new InterfacePart ("Nutzerkonto deaktivieren",lanDE.getId(),mod49.getId());
		InterfacePart in112 = new InterfacePart ("Deactivate user account",lanEN.getId(),mod49.getId());
		
		InterfacePart in113 = new InterfacePart ("Nutzerdaten bearbeiten",lanDE.getId(),mod50.getId());
		InterfacePart in114 = new InterfacePart ("Edit user data",lanEN.getId(),mod50.getId());
		
		InterfacePart in115 = new InterfacePart ("Adresstyp",lanDE.getId(),mod51.getId());
		InterfacePart in116 = new InterfacePart ("Address type",lanEN.getId(),mod51.getId());
		InterfacePart in117 = new InterfacePart ("Flüchtlingsheim",lanDE.getId(),mod52.getId());
		InterfacePart in118 = new InterfacePart ("Refugee home",lanEN.getId(),mod52.getId());
		InterfacePart in119 = new InterfacePart ("Stadtteil",lanDE.getId(),mod53.getId());
		InterfacePart in120 = new InterfacePart ("City part",lanEN.getId(),mod53.getId());
		InterfacePart in121 = new InterfacePart ("Stadt, Postleitzahl",lanDE.getId(),mod54.getId());
		InterfacePart in122 = new InterfacePart ("City, zip code",lanEN.getId(),mod54.getId());
		
		InterfacePart in123 = new InterfacePart ("Adresse bearbeiten",lanDE.getId(),mod55.getId());
		InterfacePart in124 = new InterfacePart ("Edit address",lanEN.getId(),mod55.getId());
		
		InterfacePart in125 = new InterfacePart ("Bevorzugte Sprache",lanDE.getId(),mod56.getId());
		InterfacePart in126 = new InterfacePart ("Preferred language",lanEN.getId(),mod56.getId());
		InterfacePart in127 = new InterfacePart ("Andere Sprachen",lanDE.getId(),mod57.getId());
		InterfacePart in128 = new InterfacePart ("Other languages",lanEN.getId(),mod57.getId());
		
		InterfacePart in129 = new InterfacePart ("Sprachen bearbeiten",lanDE.getId(),mod58.getId());
		InterfacePart in130 = new InterfacePart ("Change languages",lanEN.getId(),mod58.getId());
		InterfacePart in131 = new InterfacePart ("Benutzer bearbeiten",lanDE.getId(),mod59.getId());
		InterfacePart in132 = new InterfacePart ("Edit user",lanEN.getId(),mod59.getId());
		InterfacePart in133 = new InterfacePart ("Straße, Hausnummer",lanDE.getId(),mod60.getId());
		InterfacePart in134 = new InterfacePart ("Street, house number",lanEN.getId(),mod60.getId());
		
		InterfacePart in135 = new InterfacePart ("Wollen Sie ihr Nutzerkonto wirklich deaktivieren? Diese Aktion kann nur von einem Administrator r&uuml;ckg&auml;ngig gemacht werden. Deaktivierte Nutzerkonten werden nach 3 Monaten gel&ouml;scht.",lanDE.getId(),mod61.getId());
		InterfacePart in136 = new InterfacePart ("Do you really want to deactivate your user account? This action can only be undone by an admin. Deactivated accounts will be deleted after three months.",lanEN.getId(),mod61.getId());
		InterfacePart in137 = new InterfacePart ("In Ihrem Browser ist JavaScript deaktiviert. Für die Anzeige des Captcha wird Javascript ben&ouml;tigt.",lanDE.getId(),mod62.getId());
		InterfacePart in138 = new InterfacePart ("JavaScript is deactivated in your browser. It's required for displaying the Captcha.",lanEN.getId(),mod62.getId());

		InterfacePart in139 = new InterfacePart ("Übersetzung löschen ",lanDE.getId(),mod63.getId());
		InterfacePart in140 = new InterfacePart ("Delete translation ",lanEN.getId(),mod63.getId());
		InterfacePart in141 = new InterfacePart ("Löschen",lanDE.getId(),mod64.getId());
		InterfacePart in142 = new InterfacePart ("Delete",lanEN.getId(),mod64.getId());
		
		InterfacePart in143 = new InterfacePart ("Sie haben soeben die folgende Aktivität gelöscht ",lanDE.getId(),mod65.getId());
		InterfacePart in144 = new InterfacePart ("You just deleted this activity ",lanEN.getId(),mod65.getId());
		
		InterfacePart in145 = new InterfacePart ("Sie haben soeben das folgende Gut gelöscht ",lanDE.getId(),mod66.getId());
		InterfacePart in146 = new InterfacePart ("You just deleted this good ",lanEN.getId(),mod66.getId());
		
		InterfacePart in147 = new InterfacePart ("Sie haben soeben die folgende Kategorie gelöscht ",lanDE.getId(),mod67.getId());
		InterfacePart in148 = new InterfacePart ("You just deleted this tag ",lanEN.getId(),mod67.getId());
		
		InterfacePart in149 = new InterfacePart ("Meine Dialoge",lanDE.getId(),mod68.getId());
		InterfacePart in150 = new InterfacePart ("My dialogues",lanEN.getId(),mod68.getId());
		InterfacePart in151 = new InterfacePart ("Titel",lanDE.getId(),mod69.getId());
		InterfacePart in152 = new InterfacePart ("Title",lanEN.getId(),mod69.getId());
		InterfacePart in153 = new InterfacePart ("Gesprächspartner",lanDE.getId(),mod70.getId());
		InterfacePart in154 = new InterfacePart ("Dialog partner",lanEN.getId(),mod70.getId());
		InterfacePart in155 = new InterfacePart ("Link",lanDE.getId(),mod71.getId());
		InterfacePart in156 = new InterfacePart ("Link",lanEN.getId(),mod71.getId());
		InterfacePart in157 = new InterfacePart ("Ansehen",lanDE.getId(),mod72.getId());
		InterfacePart in158 = new InterfacePart ("Goto",lanEN.getId(),mod72.getId());
		
		InterfacePart in159 = new InterfacePart ("FEHLER",lanDE.getId(),mod73.getId());
		InterfacePart in160 = new InterfacePart ("ERROR",lanEN.getId(),mod73.getId());
		InterfacePart in161 = new InterfacePart ("Das Captcha muss ausgefüllt werden!",lanDE.getId(),mod74.getId());
		InterfacePart in162 = new InterfacePart ("Captcha must be used!",lanEN.getId(),mod74.getId());
		InterfacePart in163 = new InterfacePart ("Zurück zur Startseite",lanDE.getId(),mod75.getId());
		InterfacePart in164 = new InterfacePart ("Back to index",lanEN.getId(),mod75.getId());
		InterfacePart in165 = new InterfacePart ("Es ist leider ein unbekannter Fehler aufgetreten.",lanDE.getId(),mod76.getId());
		InterfacePart in166 = new InterfacePart ("An unknown error has occurred.",lanEN.getId(),mod76.getId());
		InterfacePart in167 = new InterfacePart ("Alle Felder müssen ausgefüllt werden!",lanDE.getId(),mod77.getId());
		InterfacePart in168 = new InterfacePart ("All fields must not be empty!",lanEN.getId(),mod77.getId());
		InterfacePart in169 = new InterfacePart ("Ihr Konto ist noch nicht aktiviert. Bitten klicken Sie auf den Aktivierungslink in der an Sie gesendeten E-Mail.",lanDE.getId(),mod78.getId());
		InterfacePart in170 = new InterfacePart ("Your account is not yet activated. Please click on the activation link in the mail we sent to you.",lanEN.getId(),mod78.getId());
		
		InterfacePart in171 = new InterfacePart ("Alle Güter",lanDE.getId(),mod79.getId());
		InterfacePart in172 = new InterfacePart ("All goods",lanEN.getId(),mod79.getId());
		InterfacePart in173 = new InterfacePart ("Alle Aktivitäten",lanDE.getId(),mod80.getId());
		InterfacePart in174 = new InterfacePart ("All activities",lanEN.getId(),mod80.getId());
		InterfacePart in175 = new InterfacePart ("Bearbeiten",lanDE.getId(),mod81.getId());
		InterfacePart in176 = new InterfacePart ("Update",lanEN.getId(),mod81.getId());
		
		InterfacePart in177 = new InterfacePart ("Weitere Sprache",lanDE.getId(),mod82.getId());
		InterfacePart in178 = new InterfacePart ("Other language",lanEN.getId(),mod82.getId());
		InterfacePart in179 = new InterfacePart ("Wohnungstyp",lanDE.getId(),mod83.getId());
		InterfacePart in180 = new InterfacePart ("Type of housing",lanEN.getId(),mod83.getId());
		InterfacePart in181 = new InterfacePart ("Wohnung/Haus",lanDE.getId(),mod84.getId());
		InterfacePart in182 = new InterfacePart ("Flat/House",lanEN.getId(),mod84.getId());
		InterfacePart in183 = new InterfacePart ("Straße",lanDE.getId(),mod85.getId());
		InterfacePart in184 = new InterfacePart ("Street",lanEN.getId(),mod85.getId());
		InterfacePart in185 = new InterfacePart ("Hausnummer",lanDE.getId(),mod86.getId());
		InterfacePart in186 = new InterfacePart ("House number",lanEN.getId(),mod86.getId());
		InterfacePart in187 = new InterfacePart ("Postleitzahl",lanDE.getId(),mod87.getId());
		InterfacePart in188 = new InterfacePart ("Zip code",lanEN.getId(),mod87.getId());
		InterfacePart in189 = new InterfacePart ("Stadt",lanDE.getId(),mod88.getId());
		InterfacePart in190 = new InterfacePart ("City",lanEN.getId(),mod88.getId());
		InterfacePart in191 = new InterfacePart ("Name des Flüchtlingsheims",lanDE.getId(),mod89.getId());
		InterfacePart in192 = new InterfacePart ("Name of the refugee home",lanEN.getId(),mod89.getId());
		InterfacePart in193 = new InterfacePart ("Ihre Behausung: ",lanDE.getId(),mod90.getId());
		InterfacePart in194 = new InterfacePart ("Your home: ",lanEN.getId(),mod90.getId());
		InterfacePart in195 = new InterfacePart ("Ihre Sprachen: ",lanDE.getId(),mod91.getId());
		InterfacePart in196 = new InterfacePart ("Your languages: ",lanEN.getId(),mod91.getId());
		
		InterfacePart in197 = new InterfacePart ("Ungültig!",lanDE.getId(),mod92.getId());
		InterfacePart in198 = new InterfacePart ("Invalid!",lanEN.getId(),mod92.getId());
		
		InterfacePart in199 = new InterfacePart ("Über Sie: ",lanDE.getId(),mod93.getId());
		InterfacePart in200 = new InterfacePart ("About You: ",lanEN.getId(),mod93.getId());
		
		InterfacePart in201 = new InterfacePart (" Aktivitäten ",lanDE.getId(),mod94.getId());
		InterfacePart in202 = new InterfacePart (" Activities ",lanEN.getId(),mod94.getId());
		InterfacePart in203 = new InterfacePart (" Güter ",lanDE.getId(),mod95.getId());
		InterfacePart in204 = new InterfacePart (" Goods ",lanEN.getId(),mod95.getId());
		
		InterfacePart in205 = new InterfacePart ("Flüchtlings-App",lanDE.getId(),mod96.getId());
		InterfacePart in206 = new InterfacePart ("Refugee App",lanEN.getId(),mod96.getId());
		InterfacePart in207 = new InterfacePart ("Menü",lanDE.getId(),mod97.getId());
		InterfacePart in208 = new InterfacePart ("Menu",lanEN.getId(),mod97.getId());
		InterfacePart in209 = new InterfacePart ("Registrieren",lanDE.getId(),mod98.getId());
		InterfacePart in210 = new InterfacePart ("Register",lanEN.getId(),mod98.getId());
		InterfacePart in211 = new InterfacePart ("Einloggen",lanDE.getId(),mod99.getId());
		InterfacePart in212 = new InterfacePart ("Login",lanEN.getId(),mod99.getId());
		InterfacePart in213 = new InterfacePart ("Angebot",lanDE.getId(),mod100.getId());
		InterfacePart in214 = new InterfacePart ("Offer",lanEN.getId(),mod100.getId());
		InterfacePart in215 = new InterfacePart ("Gut",lanDE.getId(),mod101.getId());
		InterfacePart in216 = new InterfacePart ("Good",lanEN.getId(),mod101.getId());
		InterfacePart in217 = new InterfacePart ("Aktivität",lanDE.getId(),mod102.getId());
		InterfacePart in218 = new InterfacePart ("Activity",lanEN.getId(),mod102.getId());
		InterfacePart in219 = new InterfacePart ("Alle Kategorien",lanDE.getId(),mod103.getId());
		InterfacePart in220 = new InterfacePart ("All tags",lanEN.getId(),mod103.getId());
		InterfacePart in221 = new InterfacePart ("Neuer Textblock",lanDE.getId(),mod104.getId());
		InterfacePart in222 = new InterfacePart ("New text block",lanEN.getId(),mod104.getId());
		InterfacePart in223 = new InterfacePart ("Textblockliste",lanDE.getId(),mod105.getId());
		InterfacePart in224 = new InterfacePart ("Text block list",lanEN.getId(),mod105.getId());
		InterfacePart in225 = new InterfacePart ("Kundenliste",lanDE.getId(),mod106.getId());
		InterfacePart in226 = new InterfacePart ("User list",lanEN.getId(),mod106.getId());
		InterfacePart in227 = new InterfacePart ("Ausloggen",lanDE.getId(),mod107.getId());
		InterfacePart in228 = new InterfacePart ("Logout",lanEN.getId(),mod107.getId());
		InterfacePart in229 = new InterfacePart ("Eigene Daten",lanDE.getId(),mod108.getId());
		InterfacePart in230 = new InterfacePart ("My data",lanEN.getId(),mod108.getId());
		InterfacePart in231 = new InterfacePart ("Meine angebotenen Güter",lanDE.getId(),mod109.getId());
		InterfacePart in232 = new InterfacePart ("My offered goods",lanEN.getId(),mod109.getId());
		InterfacePart in233 = new InterfacePart ("Meine angebotenen Aktivitäten",lanDE.getId(),mod110.getId());
		InterfacePart in234 = new InterfacePart ("My offered activities",lanEN.getId(),mod110.getId());
		InterfacePart in235 = new InterfacePart ("Eigene Dialoge",lanDE.getId(),mod111.getId());
		InterfacePart in236 = new InterfacePart ("My dialogues",lanEN.getId(),mod111.getId());
		InterfacePart in237 = new InterfacePart ("Neuer dialog",lanDE.getId(),mod112.getId());
		InterfacePart in238 = new InterfacePart ("New dialogue",lanEN.getId(),mod112.getId());
		InterfacePart in239 = new InterfacePart ("Benutzerkonto erstellen",lanDE.getId(),mod113.getId());
		InterfacePart in240 = new InterfacePart ("Create user account",lanEN.getId(),mod113.getId());
		InterfacePart in241 = new InterfacePart ("Dieses Feld muss ausgefüllt werden!",lanDE.getId(),mod114.getId());
		InterfacePart in242 = new InterfacePart ("This field must not be empty!",lanEN.getId(),mod114.getId());
		InterfacePart in243 = new InterfacePart ("Dies ist schon in Benutzung!",lanDE.getId(),mod115.getId());
		InterfacePart in244 = new InterfacePart ("This is already in use!",lanEN.getId(),mod115.getId());
		InterfacePart in245 = new InterfacePart ("Das Passwort ist zu kurz! Mindestens 8 Zeichen.",lanDE.getId(),mod116.getId());
		InterfacePart in246 = new InterfacePart ("Your password is too short! It needs at least 8 characters.",lanEN.getId(),mod116.getId());
		InterfacePart in247 = new InterfacePart ("Das Passwort ist zu unsicher!",lanDE.getId(),mod117.getId());
		InterfacePart in248 = new InterfacePart ("Your password is too insecure!",lanEN.getId(),mod117.getId());
		InterfacePart in249 = new InterfacePart ("Passwort wiederholen",lanDE.getId(),mod118.getId());
		InterfacePart in250 = new InterfacePart ("Repeat password",lanEN.getId(),mod118.getId());
		InterfacePart in251 = new InterfacePart ("Die Passwörter stimmen nicht überein!",lanDE.getId(),mod119.getId());
		InterfacePart in252 = new InterfacePart ("The passwords are unequal!",lanEN.getId(),mod119.getId());
		InterfacePart in253 = new InterfacePart ("Bitte wählen Sie etwas aus!",lanDE.getId(),mod120.getId());
		InterfacePart in254 = new InterfacePart ("Please choose an option!",lanEN.getId(),mod120.getId());
		InterfacePart in255 = new InterfacePart (" existiert nicht.",lanDE.getId(),mod121.getId());
		InterfacePart in256 = new InterfacePart (" doesn't exist.",lanEN.getId(),mod121.getId());
		InterfacePart in257 = new InterfacePart ("Die Aktivität ",lanDE.getId(),mod122.getId());
		InterfacePart in258 = new InterfacePart ("The activity ",lanEN.getId(),mod122.getId());
		InterfacePart in259 = new InterfacePart ("Das Gut ",lanDE.getId(),mod123.getId());
		InterfacePart in260 = new InterfacePart ("The good ",lanEN.getId(),mod123.getId());
		InterfacePart in261 = new InterfacePart ("Die Kategorie ",lanDE.getId(),mod124.getId());
		InterfacePart in262 = new InterfacePart ("The tag ",lanEN.getId(),mod124.getId());
		InterfacePart in263 = new InterfacePart (" gehört Ihnen nicht.",lanDE.getId(),mod125.getId());
		InterfacePart in264 = new InterfacePart (" doesn't belong to you",lanEN.getId(),mod125.getId());
		InterfacePart in265 = new InterfacePart ("Bitte loggen Sie sich zuerst ein!",lanDE.getId(),mod126.getId());
		InterfacePart in266 = new InterfacePart ("Please log in first!",lanEN.getId(),mod126.getId());
		InterfacePart in267 = new InterfacePart ("Aktivität anbieten",lanDE.getId(),mod127.getId());
		InterfacePart in268 = new InterfacePart ("Offer activity",lanEN.getId(),mod127.getId());
		InterfacePart in269 = new InterfacePart ("Gut anbieten",lanDE.getId(),mod128.getId());
		InterfacePart in270 = new InterfacePart ("Offer good",lanEN.getId(),mod128.getId());
		InterfacePart in271 = new InterfacePart ("Sie haben soeben die folgende Aktivität angeboten",lanDE.getId(),mod129.getId());
		InterfacePart in272 = new InterfacePart ("You just offered this good",lanEN.getId(),mod129.getId());
		InterfacePart in273 = new InterfacePart ("Sie haben soeben das folgende Gut angeboten",lanDE.getId(),mod130.getId());
		InterfacePart in274 = new InterfacePart ("You just offered this activity",lanEN.getId(),mod130.getId());
		InterfacePart in275 = new InterfacePart ("Passwort zurücksetzen",lanDE.getId(),mod131.getId());
		InterfacePart in276 = new InterfacePart ("Reset password",lanEN.getId(),mod131.getId());
		InterfacePart in277 = new InterfacePart ("Suchen",lanDE.getId(),mod132.getId());
		InterfacePart in278 = new InterfacePart ("Search",lanEN.getId(),mod132.getId());
		InterfacePart in279 = new InterfacePart ("Suche mit ",lanDE.getId(),mod133.getId());
		InterfacePart in280 = new InterfacePart ("Search with ",lanEN.getId(),mod133.getId());
		InterfacePart in281 = new InterfacePart ("Neuer Dialog",lanDE.getId(),mod134.getId());
		InterfacePart in282 = new InterfacePart ("New Dialogue",lanEN.getId(),mod134.getId());
		
		InterfacePart in283 = new InterfacePart ("Interface-Übersetzung",lanDE.getId(),mod135.getId());
		InterfacePart in284 = new InterfacePart ("Interface translation",lanEN.getId(),mod135.getId());
		InterfacePart in285 = new InterfacePart ("Neue Sprache",lanDE.getId(),mod136.getId());
		InterfacePart in286 = new InterfacePart ("New Language",lanEN.getId(),mod136.getId());
		InterfacePart in287 = new InterfacePart ("Sprachkürzel",lanDE.getId(),mod137.getId());
		InterfacePart in288 = new InterfacePart ("Language abbreviation",lanEN.getId(),mod137.getId());
		InterfacePart in289 = new InterfacePart ("Neue Übersetzungskategorie",lanDE.getId(),mod138.getId());
		InterfacePart in290 = new InterfacePart ("New translation category",lanEN.getId(),mod138.getId());
		InterfacePart in291 = new InterfacePart ("Modulname",lanDE.getId(),mod139.getId());
		InterfacePart in292 = new InterfacePart ("Module name",lanEN.getId(),mod139.getId());
		InterfacePart in293 = new InterfacePart ("Übersetzungskategoriename",lanDE.getId(),mod140.getId());
		InterfacePart in294 = new InterfacePart ("Translation category name",lanEN.getId(),mod140.getId());
		InterfacePart in295 = new InterfacePart ("Neues Modul",lanDE.getId(),mod141.getId());
		InterfacePart in296 = new InterfacePart ("New module",lanEN.getId(),mod141.getId());
		InterfacePart in297 = new InterfacePart ("Modul löschen",lanDE.getId(),mod142.getId());
		InterfacePart in298 = new InterfacePart ("Delete module",lanEN.getId(),mod142.getId());
		InterfacePart in299 = new InterfacePart ("Raw FormatStrings",lanDE.getId(),mod143.getId());
		InterfacePart in300 = new InterfacePart ("Raw FormatStrings",lanEN.getId(),mod143.getId());
		InterfacePart in301 = new InterfacePart ("Textblöcke als Formular",lanDE.getId(),mod144.getId());
		InterfacePart in302 = new InterfacePart ("Textblocks as Form",lanEN.getId(),mod144.getId());
		InterfacePart in303 = new InterfacePart ("Sie haben soeben diese Aktivität geändert.",lanDE.getId(),mod145.getId());
		InterfacePart in304 = new InterfacePart ("You just updated this activity.",lanEN.getId(),mod145.getId());
		InterfacePart in305 = new InterfacePart ("Sie haben soeben dieses Gut geändert.",lanDE.getId(),mod146.getId());
		InterfacePart in306 = new InterfacePart ("You just updated this good.",lanEN.getId(),mod146.getId());
		InterfacePart in307 = new InterfacePart ("Sie haben soeben diese Kategorie geändert.",lanDE.getId(),mod147.getId());
		InterfacePart in308 = new InterfacePart ("You just updated this tag.",lanEN.getId(),mod147.getId());
		InterfacePart in309 = new InterfacePart ("Keine Berechtigung!",lanDE.getId(),mod148.getId());
		InterfacePart in310 = new InterfacePart ("You don't have the rights to do this.",lanEN.getId(),mod148.getId());
		InterfacePart in311 = new InterfacePart ("Adresse",lanDE.getId(),mod149.getId());
		InterfacePart in312 = new InterfacePart ("Address",lanEN.getId(),mod149.getId());
		InterfacePart in313 = new InterfacePart ("Validierung erfolgreich!",lanDE.getId(),mod150.getId());
		InterfacePart in314 = new InterfacePart ("Validation successful!",lanEN.getId(),mod150.getId());
		InterfacePart in315 = new InterfacePart ("Mail senden",lanDE.getId(),mod151.getId());
		InterfacePart in316 = new InterfacePart ("Send Mail",lanEN.getId(),mod151.getId());
		InterfacePart in317 = new InterfacePart ("Mindestens 8 Zeichen, min ein Kleinbuchstaben, min ein Gro&szlig;buchstaben, min eine Zahl und min ein Sonderzeichen.",lanDE.getId(),mod152.getId());
		InterfacePart in318 = new InterfacePart ("At least 8 characters, at least one lower case letter, at least one upper case letter, at least one number and at least one special character.",lanEN.getId(),mod152.getId());
		
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
		res.add(in15);
		res.add(in16);
		res.add(in17);
		res.add(in18);
		res.add(in19);
		res.add(in20);
		res.add(in21);
		res.add(in22);
		res.add(in23);
		res.add(in24);
		res.add(in25);
		res.add(in26);
		res.add(in27);
		res.add(in28);
		res.add(in29);
		res.add(in30);
		res.add(in31);
		res.add(in32);
		res.add(in33);
		res.add(in34);
		res.add(in35);
		res.add(in36);
		res.add(in37);
		res.add(in38);
		res.add(in39);
		res.add(in40);
		res.add(in41);
		res.add(in42);
		res.add(in43);
		res.add(in44);
		res.add(in45);
		res.add(in46);
		res.add(in47);
		res.add(in48);
		res.add(in49);
		res.add(in50);
		res.add(in51);
		res.add(in52);
		res.add(in53);
		res.add(in54);
		res.add(in55);
		res.add(in56);
		res.add(in57);
		res.add(in58);
		res.add(in59);
		res.add(in60);
		res.add(in61);
		res.add(in62);
		res.add(in63);
		res.add(in64);
		res.add(in65);
		res.add(in66);
		res.add(in67);
		res.add(in68);
		res.add(in69);
		res.add(in70);
		res.add(in71);
		res.add(in72);
		res.add(in73);
		res.add(in74);
		res.add(in75);
		res.add(in76);
		res.add(in77);
		res.add(in78);
		res.add(in79);
		res.add(in80);
		res.add(in81);
		res.add(in82);
		res.add(in83);
		res.add(in84);
		res.add(in85);
		res.add(in86);
		res.add(in87);
		res.add(in88);
		res.add(in89);
		res.add(in90);
		res.add(in91);
		res.add(in92);
		res.add(in93);
		res.add(in94);
		res.add(in95);
		res.add(in96);
		res.add(in97);
		res.add(in98);
		res.add(in99);
		res.add(in108);
		res.add(in109);
		res.add(in110);
		res.add(in111);
		res.add(in112);
		res.add(in113);
		res.add(in114);
		res.add(in115);
		res.add(in116);
		res.add(in117);
		res.add(in118);
		res.add(in119);
		res.add(in120);
		res.add(in121);
		res.add(in122);
		res.add(in123);
		res.add(in124);
		res.add(in125);
		res.add(in126);
		res.add(in127);
		res.add(in128);
		res.add(in129);
		res.add(in130);
		res.add(in131);
		res.add(in132);
		res.add(in133);
		res.add(in134);
		res.add(in135);
		res.add(in136);
		res.add(in137);
		res.add(in138);
		res.add(in139);
		res.add(in140);
		res.add(in141);
		res.add(in142);
		res.add(in143);
		res.add(in144);
		res.add(in145);
		res.add(in146);
		res.add(in147);
		res.add(in148);
		res.add(in149);
		res.add(in150);
		res.add(in151);
		res.add(in152);
		res.add(in153);
		res.add(in154);
		res.add(in155);
		res.add(in156);
		res.add(in157);
		res.add(in158);
		res.add(in159);
		res.add(in160);
		res.add(in161);
		res.add(in162);
		res.add(in163);
		res.add(in164);
		res.add(in165);
		res.add(in166);
		res.add(in167);
		res.add(in168);
		res.add(in169);
		res.add(in170);
		res.add(in171);
		res.add(in172);
		res.add(in173);
		res.add(in174);
		res.add(in175);
		res.add(in176);
		res.add(in177);
		res.add(in178);
		res.add(in179);
		res.add(in180);
		res.add(in181);
		res.add(in182);
		res.add(in183);
		res.add(in184);
		res.add(in185);
		res.add(in186);
		res.add(in187);
		res.add(in188);
		res.add(in189);
		res.add(in190);
		res.add(in191);
		res.add(in192);
		res.add(in193);
		res.add(in194);
		res.add(in195);
		res.add(in196);
		res.add(in197);
		res.add(in198);
		res.add(in199);
		res.add(in200);
		res.add(in201);
		res.add(in202);
		res.add(in203);
		res.add(in204);
		res.add(in205);
		res.add(in206);
		res.add(in207);
		res.add(in208);
		res.add(in209);
		res.add(in210);
		res.add(in211);
		res.add(in212);
		res.add(in213);
		res.add(in214);
		res.add(in215);
		res.add(in216);
		res.add(in217);
		res.add(in218);
		res.add(in219);
		res.add(in220);
		res.add(in221);
		res.add(in222);
		res.add(in223);
		res.add(in224);
		res.add(in225);
		res.add(in226);
		res.add(in227);
		res.add(in228);
		res.add(in229);
		res.add(in230);
		res.add(in231);
		res.add(in232);
		res.add(in233);
		res.add(in234);
		res.add(in235);
		res.add(in236);
		res.add(in237);
		res.add(in238);
		res.add(in239);
		res.add(in240);
		res.add(in241);
		res.add(in242);
		res.add(in243);
		res.add(in244);
		res.add(in245);
		res.add(in246);
		res.add(in247);
		res.add(in248);
		res.add(in249);
		res.add(in250);
		res.add(in251);
		res.add(in252);
		res.add(in253);
		res.add(in254);
		res.add(in255);
		res.add(in256);
		res.add(in257);
		res.add(in258);
		res.add(in259);
		res.add(in260);
		res.add(in261);
		res.add(in262);
		res.add(in263);
		res.add(in264);
		res.add(in265);
		res.add(in266);
		res.add(in267);
		res.add(in268);
		res.add(in269);
		res.add(in270);
		res.add(in271);
		res.add(in272);
		res.add(in273);
		res.add(in274);
		res.add(in275);
		res.add(in276);
		res.add(in277);
		res.add(in278);
		res.add(in279);
		res.add(in280);
		res.add(in281);
		res.add(in282);
		res.add(in283);
		res.add(in284);
		res.add(in285);
		res.add(in286);
		res.add(in287);
		res.add(in288);
		res.add(in289);
		res.add(in290);
		res.add(in291);
		res.add(in292);
		res.add(in293);
		res.add(in294);
		res.add(in295);
		res.add(in296);
		res.add(in297);
		res.add(in298);
		res.add(in299);
		res.add(in300);
		res.add(in301);
		res.add(in302);
		res.add(in303);
		res.add(in304);
		res.add(in305);
		res.add(in306);
		res.add(in307);
		res.add(in308);
		res.add(in309);
		res.add(in310);
		res.add(in311);
		res.add(in312);
		res.add(in313);
		res.add(in314);
		res.add(in315);
		res.add(in316);
		res.add(in317);
		res.add(in318);

		
		return res;
	}

}

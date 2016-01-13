package app.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import app.model.ActivityEntity;
import app.model.GoodEntity;
import app.model.Language;
import app.model.TagEntity;
import app.model.User;
import app.model.User.AddresstypEnum;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.LanguageRepository;
import app.repository.TagsRepository;
import app.validator.ActivityValidator;
import app.validator.GoodValidator;
import app.validator.TagValidator;

/**
 * <h1>AdminManagementController</h1> The AdminManagementController is used to
 * modify UserAccounts or User with administrative rights. It is also used to
 * manage the available tags and the offered goods and activities.
 * 
 *
 * @author Friederike Kitzing
 * 
 */

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

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
	public AdminController(UserRepository userRepository, UserAccountManager userAccountManager,
			LanguageRepository languageRepository, TagsRepository tagsRepository, GoodsRepository goodsRepository,
			ActivitiesRepository activitiesRepository) {
		this.userRepository = userRepository;
		this.userAccountManager = userAccountManager;
		this.languageRepository = languageRepository;
		this.tagsRepository = tagsRepository;
		this.goodsRepository = goodsRepository;
		this.activitiesRepository = activitiesRepository;
	}

	/**
	 * This method is the answer for the request to '/userDetails'. It finds and
	 * retrieves all the user in the UserRepository.
	 * 
	 * @param ModelMap
	 *            The modelmap to add the user
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping("/userDetails")
	String userDetails(ModelMap map) {
		map.addAttribute("userDetails", userRepository.findAll());
		return "userDetails";
	}

	@RequestMapping("/modify/user/{user}")
	public String modify(@PathVariable final String user, Model model) {

		model.addAttribute("user", userRepository.findByUserAccount(userAccountManager.findByUsername(user).get()));
		model.addAttribute("languages", languageRepository.findAll());
		System.out.println(model.toString());
		return "modify";
	}

	@RequestMapping(value = "/searchUser", method = RequestMethod.POST)
	public String searchUser(@RequestParam(value = "userNameIN") final Optional<String> userNameOPT, Model model) {
		String userName = HelpFunctions.getOptionalString(userNameOPT);
		if (userName.isEmpty() || (!userAccountManager.findByUsername(userName).isPresent())) {
			return "redirect:/userDetails";
		}

		User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(userName).get());
		model.addAttribute("user", user_xyz);
		if (user_xyz.getAddresstypString().equals("Wohnung")) {
			return "data";
		}
		return "data_refugee";
	}

	@RequestMapping(value = "/viewUser", method = RequestMethod.GET)
	public String viewUser(@RequestParam(value = "userNameIN") final Optional<String> userNameOPT, Model model) {
		String userName = HelpFunctions.getOptionalString(userNameOPT);
		if (userName.isEmpty() || (!userAccountManager.findByUsername(userName).isPresent())) {
			return "redirect:/userDetails";
		}

		User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(userName).get());
		model.addAttribute("user", user_xyz);
		if (user_xyz.getAddresstypString().equals("Wohnung")) {
			return "data";
		}
		return "data_refugee";

	}

	@RequestMapping(value = "/addLanguage")
	public String addLanguage_submit() {
		return "addLanguage";
	}

	@RequestMapping(value = "/addLanguage_submit", method = RequestMethod.POST)
	public String addLanguage_submit(@RequestParam(value = "languageName") final String LanguageName,
			@RequestParam(value = "kennung") final String Kennung) {
		if (languageRepository.findByName(LanguageName) == null) {
			Language newLanguage = new Language(LanguageName, Kennung);
			languageRepository.save(newLanguage);
		}
		return "index";

	}

	@RequestMapping(value = "/modify_submit/user/{user}", method = RequestMethod.POST) // user/{user}
	public String modify_submit(@PathVariable final String user,
			@RequestParam(value = "mailIN") final Optional<String> Mail,
			@RequestParam(value = "nameIN") final Optional<String> Name,
			@RequestParam(value = "firstnameIN") final Optional<String> Firstname,
			@RequestParam(value = "wohnen") final String Adresstyp,
			@RequestParam(value = "flh_name") final Optional<String> Flh_name_OPT,
			@RequestParam(value = "citypart") final Optional<String> Citypart_OPT,
			@RequestParam(value = "street") final Optional<String> Street_OPT,
			@RequestParam(value = "housenr") final Optional<String> Housenr_OPT,
			@RequestParam(value = "postcode_R") final Optional<String> Postcode_R,
			@RequestParam(value = "city_R") final Optional<String> City_R,
			@RequestParam(value = "postcode_H") final Optional<String> Postcode_H,
			@RequestParam(value = "city_H") final Optional<String> City_H,
			@RequestParam(value = "nativelanguage") final String Nativelanguage,
			@RequestParam(value = "otherlanguages") final String OtherLanguages,
			@RequestParam(value = "origin") final Optional<String> Origin) {
		User user_xyz = userRepository.findByUserAccount(userAccountManager.findByUsername(user).get());
		if (Mail.isPresent() && !Mail.get().isEmpty())
			user_xyz.getUserAccount().setEmail(Mail.get());
		System.out.println(user_xyz.getUserAccount().getEmail());
		if (Name.isPresent() && !Name.get().isEmpty())
			user_xyz.getUserAccount().setLastname(Name.get());
		System.out.println(user_xyz.getUserAccount().getLastname());
		if (Firstname.isPresent() && !Firstname.get().isEmpty())
			user_xyz.getUserAccount().setFirstname(Firstname.get());
		System.out.println(user_xyz.getUserAccount().getFirstname());

		if (Adresstyp.equals("refugee")) {
			System.out.println("refugee");
			user_xyz.getLocation().setStreet("");
			user_xyz.getLocation().setHousenr("");
			user_xyz.setAddresstyp(AddresstypEnum.Refugees_home);
			if (Flh_name_OPT.isPresent() && !Flh_name_OPT.get().isEmpty()) {
				user_xyz.getLocation().setFlh_name(Flh_name_OPT.get());
			}
			if ((Postcode_R.isPresent() && !Postcode_R.get().isEmpty()) && (Postcode_R.get().matches("[0-9]{5}"))) {
				user_xyz.getLocation().setZipCode(Postcode_R.get());
			}
			if (City_R.isPresent() && !City_R.get().isEmpty()) {
				user_xyz.getLocation().setCity(City_R.get());
			}
			if (Citypart_OPT.isPresent() && !Citypart_OPT.get().isEmpty()) {
				user_xyz.getLocation().setCityPart(Citypart_OPT.get());
			}
		} else {
			System.out.println("helper");
			if (Street_OPT.isPresent() && !Street_OPT.get().isEmpty()) {
				user_xyz.getLocation().setStreet(Street_OPT.get());
				user_xyz.setAddresstyp(AddresstypEnum.Wohnung);
				userRepository.save(user_xyz);
			}
			if (Housenr_OPT.isPresent() && !Housenr_OPT.get().isEmpty()) {
				user_xyz.getLocation().setHousenr(Housenr_OPT.get());
				user_xyz.setAddresstyp(AddresstypEnum.Wohnung);
				userRepository.save(user_xyz);
			}
			if ((Postcode_H.isPresent()) && !Postcode_H.get().isEmpty() && (Postcode_H.get().matches("[0-9]{5}"))) {
				user_xyz.getLocation().setZipCode(Postcode_H.get());
			}
			if (City_H.isPresent() && !City_H.get().isEmpty()) {
				user_xyz.getLocation().setCity(City_H.get());
			}
			if (user_xyz.getAddresstypString().equals("Wohnung")) {
				user_xyz.getLocation().setFlh_name("");
				user_xyz.getLocation().setCityPart("");
			}
		}
		if (!Nativelanguage.isEmpty()) {
			System.out.println("modify language");
			user_xyz.removeLanguage(user_xyz.getPrefLanguage());
			Language PreferredLanguage = languageRepository.findByKennung(Nativelanguage);
			user_xyz.setPrefLanguage(PreferredLanguage);
		}
		if (!OtherLanguages.isEmpty() && !OtherLanguages.toString().equals("-1")) {
			System.out.println("modify languages");
			System.out.println(OtherLanguages);
			System.out.println(OtherLanguages.toString());
			Boolean nichtleer = false;
			for (String test : OtherLanguages.split(",")) {
				if (!test.equals("-1")) {
					nichtleer = true;
				}
			}
			if (nichtleer) {
				user_xyz.removeAllLanguages();
			}
			if (OtherLanguages != null && !OtherLanguages.isEmpty()) {
				for (String languageName : OtherLanguages.split(",")) {
					System.out.println(languageName);
					if (languageRepository.findByKennung(languageName) != null) {
						// user_xyz.setLanguage(languageRepository.findByName(languageName));
						Language l1 = languageRepository.findByKennung(languageName);
						System.out.println(l1.toString());
						user_xyz.setLanguage(l1);
						userRepository.save(user_xyz);
					}
					System.out.println(user_xyz.getLanguages());
				}
			}
		}
		System.out.println("origin?");
		if (Origin.isPresent())
			user_xyz.setOrigin(Origin.get());
		userRepository.save(user_xyz);
		System.out.println("saved");
		return "redirect:/userDetails";

	}

	/**
	 * This method is the answer for the request to '/availableTags'. It finds
	 * and retrieves all the available tags except for the default tag "Others".
	 * 
	 * @param Model
	 *            The model to add response's attributes
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to see all the available tags
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/availableTags", method = RequestMethod.GET)
	public String listAllAvailableTags(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		/*
		 * If there wasn't a log in instance for the admin, then he is
		 * redirected to an error page.
		 */
		if (!userAccount.isPresent())
			return "noUser";

		TagEntity defaultTag = tagsRepository.findByName(TagEntity.OTHERS);
		long defTagId = defaultTag.getId();

		model.addAttribute("result", tagsRepository.findByIdNotOrderByNameAsc(defTagId));
		return "availableTags";
	}

	/**
	 * This method is the answer for the request to '/updateTag'. It finds and
	 * retrieves the particular tag that the admin wants to update.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param TagEntity
	 *            The tag to be refilled and updated
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to update one of the available
	 *            tags
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/updateTag", method = { RequestMethod.GET, RequestMethod.POST })
	public String showTagToUpdate(HttpServletRequest request, Model model, @ModelAttribute("tag") TagEntity tag,
			@LoggedIn Optional<UserAccount> userAccount) {
		long id;
		String error = null;
		/*
		 * This condition is made because the way parameters are read is
		 * different for get and post requests.
		 */
		if (request.getMethod().equals("POST")) {
			id = Long.parseLong(request.getParameter("id"));
		} 
		else {
			id = (Long) model.asMap().get("id");
			error = (String) model.asMap().get("error");
		}

		if (!userAccount.isPresent())
			return "noUser";

		TagEntity tagToUpdate = tagsRepository.findOne(id);

		// If the tag is null, the admin is redirected to the available tags
		// again.
		if (tagToUpdate == null) {
			model.addAttribute("wantedAction", "update");
			return "noSuchTag";
		}

		model.addAttribute("tag", tagToUpdate);
		/*
     * If the tag that the admin wanted to create already exists, an error
     * message is returned to inform the admin about the situation.
     */
		model.addAttribute("error", error);

		return "updateTag";
	}

	/**
	 * This method is the answer for the request to '/updatedTag'. It updates a
	 * particular tag with the given information and retrieves the updated tag.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param TagEntity
	 *            The tag to be updated
	 * @param BindingResult
	 *            The parameter in charge of the validation result
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to update one of the available
	 *            tags
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/updatedTag", method = RequestMethod.POST)
	public String updateTag(HttpServletRequest request, Model model, @ModelAttribute("tag") TagEntity tag,
			BindingResult bindingResult, RedirectAttributes redirectAttributes,
			@LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");

		if (!userAccount.isPresent())
			return "noUser";

		TagEntity tagToBeUpdated = tagsRepository.findOne(id);

		tagToBeUpdated.setName(name);

		TagValidator tagValidator = new TagValidator();
		tagValidator.validate(tagToBeUpdated, bindingResult);
		/*
		 * If there are errors in the parameters of the tag to be updated, the
		 * admin is redirected to the tag-update form again.
		 */
		if (bindingResult.hasErrors()) {
			System.out.println("Invalid tag: " + bindingResult.getAllErrors().toString());
			redirectAttributes.addFlashAttribute("id", id);
			return "redirect:updateTag";
		}
		
		/*
     * If the tag to be updated has the same name as an already existing tag, 
     * the admin is redirected to the tag-update form again.
     */
		TagEntity possibleExistingTag = tagsRepository.findByNameIgnoreCase
		                                (tagToBeUpdated.getName().trim());
		if (possibleExistingTag != null) {
		  redirectAttributes.addFlashAttribute("id", id);
		  redirectAttributes.addFlashAttribute("error", "This tag already exists");
		  return "redirect:updateTag";
		}

		/*
		 * Calling save() on an object with predefined id will update the
		 * corresponding database record rather than insert a new one.
		 */
		model.addAttribute("result", tagsRepository.save(tagToBeUpdated));
		return "updatedTag";
	}

	/**
	 * This method is the answer for the request to '/deletedTag'. It retrieves
	 * the tag that the admin wants to delete, set a default tag for all the
	 * goods and activities related to the tag that he wants to remove and then
	 * deletes it.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to delete one of the available
	 *            tags
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/deletedTag", method = RequestMethod.POST)
	public String deleteTag(HttpServletRequest request, Model model, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));

		if (!userAccount.isPresent())
			return "noUser";

		TagEntity tag = tagsRepository.findOne(id);
		TagEntity defaultTag = tagsRepository.findByName(TagEntity.OTHERS);

		// If the tag is null, the admin is redirected to the available tags
		// again.
		if (tag == null) {
			model.addAttribute("wantedAction", "delete");
			return "noSuchTag";
		}

		goodsRepository.setTagToDefaultTag(defaultTag, tag);
		activitiesRepository.setTagToDefaultTag(defaultTag, tag);
		tagsRepository.delete(id);

		model.addAttribute("result", tag);
		return "deletedTag";
	}

	/**
	 * This method is the answer for the request to '/addNewTag'. It allows the
	 * admin to add a new tag in order to let the users use that tag for their
	 * offers.
	 * 
	 * @param Model
	 *            The model to add response's attributes
	 * @param TagEntity
	 *            The tag to be filled and created
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to add the new tag
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/addNewTag", method = RequestMethod.GET)
	public String showFormToAddNewTag(Model model, @ModelAttribute("tag") TagEntity tag,
			@LoggedIn Optional<UserAccount> userAccount) {
		if (!userAccount.isPresent()) return "noUser";

		/*
     * If the tag that the admin wanted to create already exists, the name of
     * the tag and an error message are returned to inform the admin about the
     * situation.
     */
		String name = (String) model.asMap().get("name");
		String error = (String) model.asMap().get("error");
		
		model.addAttribute("name", name);
		model.addAttribute("error", error);
		return "addNewTag";
	}

  /**
	 * This method is the answer for the request to '/addedNewTag'. It saves and
	 * retrieves the tag that the admin wants to add for the tags table.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param Tagntity
	 *            The tag to be created
	 * @param BindingResult
	 *            The parameter in charge of the validation result
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to add the new tag
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/addedNewTag", method = RequestMethod.POST)
	public String addNewTag(HttpServletRequest request, Model model, 
	                        @ModelAttribute("tag") TagEntity tag,
			                    BindingResult bindingResult, 
			                    RedirectAttributes redirectAttributes,
			                    @LoggedIn Optional<UserAccount> userAccount) {
		String name = request.getParameter("name");

		if (!userAccount.isPresent())
			return "noUser";

		TagEntity tagToSave = new TagEntity(name);

		TagValidator tagValidator = new TagValidator();
		tagValidator.validate(tagToSave, bindingResult);
		/*
		 * If there are errors in the parameters of the tag, the admin is
		 * redirected to the tag form again.
		 */
		if (bindingResult.hasErrors()) {
			System.out.println("Invalid tag: " + bindingResult.getAllErrors().toString());
			return "redirect:addNewTag";
		}
		
		/*
     * If the tag to be created has the same name as an already existing tag, 
     * the admin is redirected to the tag-add-new form again.
     */
    TagEntity possibleExistingTag = tagsRepository.findByNameIgnoreCase
                                    (tagToSave.getName().trim());
    if (possibleExistingTag != null) {
      redirectAttributes.addFlashAttribute("name", tagToSave.getName());
      redirectAttributes.addFlashAttribute("error", "This tag already exists");
      return "redirect:addNewTag";
    }

		TagEntity savedTag = tagsRepository.save(tagToSave);

		model.addAttribute("result", savedTag);
		return "addedNewTag";
	}

	/**
	 * This method is the answer for the request to
	 * '/offeredGoodsAndActivities'. It finds and retrieves all the offered
	 * goods and activities.
	 * 
	 * @param Model
	 *            The model to add response's attributes
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to see all offered goods and
	 *            activities
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/offeredGoodsAndActivities", method = RequestMethod.GET)
	public String listAllOfferedGoodsAndActivities(Model model, @LoggedIn Optional<UserAccount> userAccount) {
		/*
		 * If there wasn't a log in instance, then the admin is redirected to an
		 * error page.
		 */
		if (!userAccount.isPresent())
			return "noUser";

		model.addAttribute("resultGoods", goodsRepository.findAll());
		model.addAttribute("resultActivities", activitiesRepository.findAll());
		return "offeredGoodsAndActivities";
	}

	/**
	 * This method is the answer for the request to '/updateGoodByAdmin'. It
	 * finds and retrieves the particular good that the admin wants to update.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param GoodEntity
	 *            The good to be refilled and updated
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to update one of the offered
	 *            goods
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/updateGoodByAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String showGoodToUpdate(HttpServletRequest request, Model model, @ModelAttribute("good") GoodEntity good,
			@LoggedIn Optional<UserAccount> userAccount) {
		long id;

		/*
		 * This condition is made because the way parameters are read is
		 * different for get and post requests.
		 */
		if (request.getMethod().equals(GoodsManagementController.POST)) {
			id = Long.parseLong(request.getParameter("id"));
		} else
			id = (Long) model.asMap().get("id");

		if (!userAccount.isPresent())
			return "noUser";

		GoodEntity goodToUpdate = goodsRepository.findOne(id);

		/*
		 * If the good is null, the admin is redirected to all offered goods and
		 * activities again.
		 */
		if (goodToUpdate == null) {
			model.addAttribute("wantedAction", "update");
			return "noSuchGood";
		}

		model.addAttribute("good", goodToUpdate);

		/*
		 * Every tag is sent to the update view except for the current tag. The
		 * current tag is already known and it's put as the default value
		 * whereas the other tags are there, so that the admin can change the
		 * existing one.
		 */
		model.addAttribute("tags", tagsRepository.findByIdNotOrderByNameAsc(goodToUpdate.getTag().getId()));
		return "updateGoodByAdmin";
	}

	/**
	 * This method is the answer for the request to '/updatedGoodByAdmin'. It
	 * allows the admin to update a particular good with the given information
	 * and retrieves the updated good.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param GoodEntity
	 *            The good to be updated
	 * @param BindingResult
	 *            The parameter in charge of the validation result
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to update one of the offered
	 *            goods
	 * @return String The name of the view to be shown after processing
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/updatedGoodByAdmin", method = RequestMethod.POST)
	public String updateGood(HttpServletRequest request, Model model, @ModelAttribute("good") GoodEntity good,
			BindingResult bindingResult, RedirectAttributes redirectAttributes,
			@LoggedIn Optional<UserAccount> userAccount) throws IOException, ServletException {
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		long tagId = Long.parseLong(request.getParameter("tagId"));
		Part picture = request.getPart("pict");

		if (!userAccount.isPresent())
			return "noUser";

		GoodEntity goodToBeUpdated = goodsRepository.findOne(id);
		TagEntity tag = tagsRepository.findOne(tagId);
		User goodsUser = goodToBeUpdated.getUser();

		goodToBeUpdated.setName(name);
		goodToBeUpdated.setDescription(description);
		goodToBeUpdated.setTag(tag);
		goodToBeUpdated.setPicture(GoodEntity.createPicture(picture));
		goodToBeUpdated.setUser(goodsUser);

		GoodValidator goodValidator = new GoodValidator();
		goodValidator.validate(goodToBeUpdated, bindingResult);
		/*
		 * If there are errors in the parameters of the good to be updated, the
		 * admin is redirected to the good-update form again.
		 */
		if (bindingResult.hasErrors()) {
			System.out.println("Invalid good: " + bindingResult.getAllErrors().toString());
			redirectAttributes.addFlashAttribute("id", id);
			return "redirect:updateGoodByAdmin";
		}

		goodsUser.addGood(goodToBeUpdated);
		userRepository.save(goodsUser);

		/*
		 * Calling save() on an object with predefined id will update the
		 * corresponding database record rather than insert a new one.
		 */
		model.addAttribute("result", goodsRepository.save(goodToBeUpdated));
		return "updatedGood";
	}

	/**
	 * This method is the answer for the request to '/deletedGoodByAdmin'. It
	 * retrieves the good that the admin wants to delete, removes it from the
	 * respective user and then deletes it from the goods table.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to delete one of the offered
	 *            goods
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/deletedGoodByAdmin", method = RequestMethod.POST)
	public String deleteGood(HttpServletRequest request, Model model, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));

		if (!userAccount.isPresent())
			return "noUser";

		GoodEntity good = goodsRepository.findOne(id);

		/*
		 * If the good is null, the admin is redirected to all offered goods and
		 * activities again.
		 */
		if (good == null) {
			model.addAttribute("wantedAction", "delete");
			return "noSuchGood";
		}

		User goodsUser = good.getUser();
		goodsUser.removeGood(good);
		userRepository.save(goodsUser);
		goodsRepository.delete(id);

		model.addAttribute("result", good);
		return "deletedGood";
	}

	/**
	 * This method is the answer for the request to '/updateActivityByAdmin'. It
	 * finds and retrieves the particular activity that the admin wants to
	 * update.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param ActivityEntity
	 *            The activity to be refilled and updated
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to update one of the offered
	 *            activities
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/updateActivityByAdmin", method = { RequestMethod.GET, RequestMethod.POST })
	public String showActivityToUpdate(HttpServletRequest request, Model model,
			@ModelAttribute("activity") ActivityEntity activity, @LoggedIn Optional<UserAccount> userAccount) {
		long id;

		/*
		 * This condition is made because the way parameters are read is
		 * different for get and post requests.
		 */
		if (request.getMethod().equals(GoodsManagementController.POST)) {
			id = Long.parseLong(request.getParameter("id"));
		} else
			id = (Long) model.asMap().get("id");

		if (!userAccount.isPresent())
			return "noUser";

		ActivityEntity activityToUpdate = activitiesRepository.findOne(id);

		/*
		 * If the activity is null, the admin is redirected to all offered goods
		 * and activities again.
		 */
		if (activityToUpdate == null) {
			model.addAttribute("wantedAction", "update");
			return "noSuchActivity";
		}

		model.addAttribute("activity", activityToUpdate);

		/*
		 * Every tag is sent to the update view except for the current tag. The
		 * current tag is already known and it's put as the default value
		 * whereas the other tags are there, so that the admin can change the
		 * existing one.
		 */
		model.addAttribute("tags", tagsRepository.findByIdNotOrderByNameAsc(activityToUpdate.getTag().getId()));
		return "updateActivityByAdmin";
	}

	/**
	 * This method is the answer for the request to '/updatedActivityByAdmin'.
	 * It allows the admin to update a particular activity with the given
	 * information and retrieves the updated activity.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param ActivityEntity
	 *            The activity to be updated
	 * @param BindingResult
	 *            The parameter in charge of the validation result
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to update one of the offered
	 *            activities
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/updatedActivityByAdmin", method = RequestMethod.POST)
	public String updateActivity(HttpServletRequest request, Model model,
			@ModelAttribute("activity") ActivityEntity activity, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		long tagId = Long.parseLong(request.getParameter("tagId"));
		String picture = request.getParameter("picture");
		String startDateInString = request.getParameter("startDate");
		String endDateInString = request.getParameter("endDate");

		if (!userAccount.isPresent())
			return "noUser";

		ActivityEntity activityToBeUpdated = activitiesRepository.findOne(id);
		TagEntity tag = tagsRepository.findOne(tagId);

		Date startDate = ActivityEntity.convertStringIntoDate(startDateInString);
		Date endDate = ActivityEntity.convertStringIntoDate(endDateInString);

		User activitysUser = activityToBeUpdated.getUser();

		activityToBeUpdated.setName(name);
		activityToBeUpdated.setDescription(description);
		activityToBeUpdated.setTag(tag);
		activityToBeUpdated.setPicture(ActivityEntity.createPicture(picture));
		activityToBeUpdated.setStartDate(startDate);
		activityToBeUpdated.setEndDate(endDate);
		activityToBeUpdated.setUser(activitysUser);

		ActivityValidator activityValidator = new ActivityValidator();
		activityValidator.validate(activityToBeUpdated, bindingResult);
		/*
		 * If there are errors in the parameters of the activity to be updated,
		 * the admin is redirected to the activity-update form again.
		 */
		if (bindingResult.hasErrors()) {
			System.out.println("Invalid activity: " + bindingResult.getAllErrors().toString());
			redirectAttributes.addFlashAttribute("id", id);
			return "redirect:updateActivityByAdmin";
		}

		activitysUser.addActivity(activityToBeUpdated);
		userRepository.save(activitysUser);

		/*
		 * Calling save() on an object with predefined id will update the
		 * corresponding database record rather than insert a new one.
		 */
		model.addAttribute("result", activitiesRepository.save(activityToBeUpdated));
		return "updatedActivity";
	}

	/**
	 * This method is the answer for the request to '/deletedActivityByAdmin'.
	 * It retrieves the activity that the admin wants to delete, removes it from
	 * the respective user and then deletes it from the activities table.
	 * 
	 * @param HttpServletRequest
	 *            The request with its information
	 * @param Model
	 *            The model to add response's attributes
	 * @param Optional<UserAccount>
	 *            The admin's account who wants to delete one of the offered
	 *            activities
	 * @return String The name of the view to be shown after processing
	 */
	@RequestMapping(value = "/deletedActivityByAdmin", method = RequestMethod.POST)
	public String deleteActivity(HttpServletRequest request, Model model, @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));

		if (!userAccount.isPresent())
			return "noUser";

		ActivityEntity activity = activitiesRepository.findOne(id);

		/*
		 * If the activity is null, the admin is redirected to all offered goods
		 * and activities again.
		 */
		if (activity == null) {
			model.addAttribute("wantedAction", "delete");
			return "noSuchActivity";
		}

		User activitysUser = activity.getUser();
		activitysUser.removeActivity(activity);
		userRepository.save(activitysUser);
		activitiesRepository.delete(id);

		model.addAttribute("result", activity);
		return "deletedActivity";
	}

}

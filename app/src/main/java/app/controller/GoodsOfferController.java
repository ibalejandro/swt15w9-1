package app.controller;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import app.model.TagEntity;
import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;
import app.validator.GoodValidator;

/**
* <h1>GoodsOfferController</h1>
* The GoodsOfferController is used to offer and view offered goods/activities 
* by the users.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@Controller
public class GoodsOfferController {

	/////////////////////////////////////////////////////Ergänzung Userzuordnung
	private final UserRepository userRepository;
	private final GoodsRepository goodsRepository;
	private final ActivitiesRepository activitiesRepository;
	private final TagsRepository tagsRepository;

	/**
   * Autowire.
   * @param UserRepository The repository for the users
   * @param GoodsRepository The repository for the goods
   * @param ActivitiesRepository The repository for the activities
   * @param TagsRepository The repository for the tags
   */
	@Autowired
	public GoodsOfferController(UserRepository userRepository,
	                            GoodsRepository goodsRepository,
	                            ActivitiesRepository activitiesRepository,
	                            TagsRepository tagsRepository) {
		this.userRepository = userRepository;
		this.goodsRepository = goodsRepository;
		this.activitiesRepository = activitiesRepository;
		this.tagsRepository = tagsRepository;
	}
	/////////////////////////////////////////////////////////end

	/**
   * This method is the answer for the request to '/home'. It finds
   * and retrieves all the offered goods/activities by the users.
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
  public String listAllGoodsAndActivities(Model model) {
	  model.addAttribute("resultGoods", goodsRepository.findAll());
	  model.addAttribute("resultActivities", activitiesRepository.findAll());
		return "home";
  }

  /**
   * This method is the answer for the request to '/offer'. It retrieves and
   * and populates the tags dropdown with the whole available tags.
   * @param Model The model to add response's attributes
   * @param GoodEntity The good to be filled and created
   * @param Optional<UserAccount> The user's account who wants to offer the good
   * @return String The name of the view to be shown after processing
   */
  @RequestMapping(value = "/offer", method = RequestMethod.GET)
  public String populateTagsDropdown(Model model,
                                     @ModelAttribute("good") GoodEntity good,
                                     @LoggedIn Optional<UserAccount> 
                                     userAccount) {
    if (!userAccount.isPresent()) return "noUser";
    
    model.addAttribute("tags", tagsRepository.findAllByOrderByNameAsc());
    return "offer";
  }

	/**
   * This method is the answer for the request to '/offeredGood'. It saves
   * and retrieves the good that the user wants to offer and associates it with
   * him.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param GoodEntity The good to be created
   * @param BindingResult The parameter in charge of the validation result
   * @param Optional<UserAccount> The user's account who wants to offer the good
   * @return String The name of the view to be shown after processing
	 * @throws ServletException 
	 * @throws IOException 
   */
	@RequestMapping(value = "/offeredGood", method = RequestMethod.POST)
  public String saveGood(HttpServletRequest request, Model model,
                         @ModelAttribute("good") GoodEntity good, 
                         BindingResult bindingResult,
  					             @LoggedIn Optional<UserAccount> userAccount) throws IOException, ServletException {
	  String name = request.getParameter("name");
	  String description = request.getParameter("description");
  	long tagId = Long.parseLong(request.getParameter("tagId"));
  	Part picture = request.getPart("pict");

  	//////////////////////////////////////////////suchen des aktiven Users:
  	if (!userAccount.isPresent()) return "redirect:noUser";
  	User user = userRepository.findByUserAccount(userAccount.get());
  	//////////////////////////////////////////////////////////////end
    
  	TagEntity tag = tagsRepository.findOne(tagId);
  	GoodEntity goodToSave = new GoodEntity(name, description, tag, picture, 
  	                                       user);
  	
  	GoodValidator goodValidator = new GoodValidator();
    goodValidator.validate(goodToSave, bindingResult);
    /*
     * If there are errors in the parameters of the good, the user is redirected
     * to the good form again.
     */
    if (bindingResult.hasErrors()) {
      System.out.println("Invalid good: " + 
                         bindingResult.getAllErrors().toString());
      return "redirect:offer";
    }
    
  	GoodEntity savedGood = goodsRepository.save(goodToSave);
  	///////////////////////////////////////////////////hinzufügen in User:
  	user.addGood(savedGood);
  	userRepository.save(user);
  	////////////////////////////////////////////////////////////end
  	model.addAttribute("result", savedGood);
  	return "offeredGood";
  }

}

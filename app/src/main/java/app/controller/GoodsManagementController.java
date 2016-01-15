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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import app.repository.TagsRepository;
import app.validator.GoodValidator;
import app.model.GoodEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodsRepository;

/**
* <h1>GoodsManagementController</h1>
* The GoodsManagementController is used to show, update and delete the offered
* goods by every particular user.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@Controller
public class GoodsManagementController {

	/////////////////////////////////////////////////////Ergänzung Userzuordnung
	private final UserRepository userRepository;
	private final GoodsRepository goodsRepository;
	private final TagsRepository tagsRepository;
	
	public static final String POST = "POST";

	/**
   * Autowire.
   * @param UserRepository The repository for the users
   * @param GoodsRepository The repository for the goods
   */
	@Autowired
	public GoodsManagementController(UserRepository userRepository,
									                 GoodsRepository goodsRepository,
									                 TagsRepository tagsRepository) {
		this.userRepository = userRepository;
		this.goodsRepository = goodsRepository;
		this.tagsRepository = tagsRepository;
	}
	/////////////////////////////////////////////////////////end

	/**
   * This method is the answer for the request to '/myOfferedGoods'. It finds
   * and retrieves all the goods associated with a particular user.
   * @param Model The model to add response's attributes
   * @param Optional<UserAccount> The user's account who wants to see his
   *                              offered goods
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/myOfferedGoods", method = RequestMethod.GET)
	public String listUserOfferedGoods
	(Model model, @LoggedIn Optional<UserAccount> userAccount) {
	  /*
     * If there wasn't a log in instance, then the user is redirected to an
     * error page.
     */
    if (!userAccount.isPresent()) return "noUser";
		User loggedUser = userRepository.findByUserAccount(userAccount.get());
		
		//model.addAttribute("resultGoods", loggedUser.getGoods());
		model.addAttribute("resultGoods", goodsRepository.findByUser(loggedUser));
		return "myOfferedGoods";
	}

	/**
   * This method is the answer for the request to '/update'. It finds and
   * retrieves the particular good that the user wants to update.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param GoodEntity The good to be refilled and updated
   * @param Optional<UserAccount> The user's account who wants to update one of
   *                              his offered goods
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/update", 
	                method = {RequestMethod.GET, RequestMethod.POST})
  public String showGoodToUpdate(HttpServletRequest request, Model model,
                                 @ModelAttribute("good") GoodEntity good,
                                 @LoggedIn Optional<UserAccount> userAccount) {
	  long id;
	  
	  /*
	   * This condition is made because the way parameters are read is different 
	   * for get and post requests.
	   */
	  if (request.getMethod().equals(POST)) {
	    id = Long.parseLong(request.getParameter("id"));
	  }
	  else id = (Long) model.asMap().get("id");
	  
	  if (!userAccount.isPresent()) return "noUser";
	  User loggedUser = userRepository.findByUserAccount(userAccount.get());
	  
		GoodEntity goodToUpdate = goodsRepository.findOne(id);
    
    // If the good is null, the user is redirected to its offered goods again.
    if (goodToUpdate == null) {
      model.addAttribute("wantedAction", "update");
      return "noSuchGood";
    }
    
    /*
     * If the good's owner is different from the user trying to update the good,
     * the user is redirected to its offered goods again.
     */
    if (goodToUpdate.getUser() != loggedUser) {
      model.addAttribute("wantedAction", "update");
      return "notYourGood";
    }

		model.addAttribute("good", goodToUpdate);
		/*
		 * Every tag is sent to the update view except for the current tag. The
		 * current tag is already known and it's put as the default value whereas
		 * the other tags are there, so that the user can change the existing one.
		 */
		model.addAttribute("tags", 
		                   tagsRepository.findByIdNotOrderByNameAsc
		                   (goodToUpdate.getTag().getId()));
		return "update";
  }

	/**
   * This method is the answer for the request to '/updatedGood'. It updates
   * a particular good with the given information and retrieves the updated
   * good.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param GoodEntity The good to be updated
   * @param BindingResult The parameter in charge of the validation result
   * @param Optional<UserAccount> The user's account who wants to update one of
   *                              his offered goods
   * @return String The name of the view to be shown after processing
	 * @throws ServletException 
	 * @throws IOException 
   */
	@RequestMapping(value = "/updatedGood", method = RequestMethod.POST)
  public String updateGood(HttpServletRequest request, Model model, 
                           @ModelAttribute("good") GoodEntity good, 
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes,
                           @LoggedIn Optional<UserAccount> userAccount) throws IOException, ServletException {
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
    String description = request.getParameter("description");
    long tagId = Long.parseLong(request.getParameter("tagId"));
    Part picture = request.getPart("pict");

    ///////////////////////////////Zuordnung User=Aktiver User
    if (!userAccount.isPresent()) return "noUser";
    User loggedUser = userRepository.findByUserAccount(userAccount.get());
    ////////////////////////////////////////end
    
    GoodEntity goodToBeUpdated = goodsRepository.findOne(id);
    TagEntity tag = tagsRepository.findOne(tagId);
    
  	goodToBeUpdated.setName(name);
  	goodToBeUpdated.setDescription(description);
  	goodToBeUpdated.setTag(tag);
  	if (picture!=null && picture.getSize()!=0L){
  	goodToBeUpdated.setPicture(GoodEntity.createPicture(picture));
  	}
		goodToBeUpdated.setUser(loggedUser);
		
		GoodValidator goodValidator = new GoodValidator();
    goodValidator.validate(goodToBeUpdated, bindingResult);
    /*
     * If there are errors in the parameters of the good to be updated, the user
     * is redirected to the good-update form again.
     */
    if (bindingResult.hasErrors()) {
      System.out.println("Invalid good: " + 
                         bindingResult.getAllErrors().toString());
      redirectAttributes.addFlashAttribute("id", id);
      return "redirect:update";
    }
    
    /*
     * If the good's owner is different from the user trying to update the good,
     * the user is redirected to its offered goods again.
     */
    if (goodToBeUpdated.getUser() != loggedUser) {
      model.addAttribute("wantedAction", "update");
      return "notYourGood";
    }
    
		loggedUser.addGood(goodToBeUpdated);
    userRepository.save(loggedUser);

  	/*
  	 * Calling save() on an object with predefined id will update the
  	 * corresponding database record rather than insert a new one.
  	 */
  	model.addAttribute("result", goodsRepository.save(goodToBeUpdated));
		return "updatedGood";
  }

	/**
   * This method is the answer for the request to '/deletedGood'. It retrieves
   * the good that the user wants to delete and then deletes it.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param Optional<UserAccount> The user's account who wants to delete one of
   *                              his offered goods
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/deletedGood", method = RequestMethod.POST)
  public String deleteGood(HttpServletRequest request, Model model, 
                           @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));

		if (!userAccount.isPresent()) return "noUser";
		User loggedUser = userRepository.findByUserAccount(userAccount.get());
    
		GoodEntity good = goodsRepository.findOne(id);

		// If the good is null, the user is redirected to its offered goods again.
    if (good == null) {
      model.addAttribute("wantedAction", "delete");
      return "noSuchGood";
    }
    
    /*
     * If the good's owner is different from the user trying to delete the good,
     * the user is redirected to its offered goods again.
     */
    if (good.getUser() != loggedUser) {
      model.addAttribute("wantedAction", "delete");
      return "notYourGood";
    }
    
	  loggedUser.removeGood(good);
    userRepository.save(loggedUser);
	  goodsRepository.delete(id);

    model.addAttribute("result", good);
		return "deletedGood";
  }
	
	/* Ferdinand's Code */
	@RequestMapping(value="/{id}/image", method=RequestMethod.GET, produces = "image/jpg")
	public @ResponseBody byte[] getImg(@PathVariable("id") long id){
		System.out.println(id);
		return goodsRepository.findOne(id).getPicture();
	}
}

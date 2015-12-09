package app.controller;

import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import app.repository.TagsRepository;
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

	/**
   * Autowire.
   * @param UserRepository The repository for the users
   * @param GoodsRepository The repository for the goods
   */
	@Autowired
	public GoodsManagementController(UserRepository userRepository,
									                 GoodsRepository goodsRepository,
									                 TagsRepository tagsRepository){
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
		
		model.addAttribute("result", loggedUser.getGoods());
		return "myOfferedGoods";
	}

	/**
   * This method is the answer for the request to '/update'. It finds and
   * retrieves the particular good that the user wants to update.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
  public String showGoodToUpdate(HttpServletRequest request, Model model, 
                                 @LoggedIn Optional<UserAccount> userAccount) {
	  long id = Long.parseLong(request.getParameter("id"));

	  if (!userAccount.isPresent()) return "noUser";
	  
		GoodEntity good = goodsRepository.findOne(id);

		// If the entity doesn't exist, an empty entity is created.
		if (good == null) good = GoodEntity.createEmptyGood();

		model.addAttribute("result", good);
		/*
		 * Every tag is sent to the update view except for the current tag. The
		 * current tag is already known and it's put as the default value whereas
		 * the other tags are there, so that the user can change the existing one.
		 */
		model.addAttribute("tags", tagsRepository
		                   .findByIdNot(good.getTag().getId()));
		return "update";
  }

	/**
   * This method is the answer for the request to '/updatedGood'. It updates
   * a particular good with the given information and retrieves the updated
   * good.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param Optional<UserAccount> The user's account who wants to update one of
   *                              his offered goods
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/updatedGood", method = RequestMethod.POST)
  public String updateGood(HttpServletRequest request, Model model,
                           @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
    String description = request.getParameter("description");
    long tagId = Long.parseLong(request.getParameter("tagId"));
    String picture = request.getParameter("picture");

    ///////////////////////////////Zuordnung User=Aktiver User
    if (!userAccount.isPresent()) return "noUser";
    User loggedUser = userRepository.findByUserAccount(userAccount.get());
    ////////////////////////////////////////end
    
    GoodEntity goodToBeUpdated = goodsRepository.findOne(id);
    TagEntity tag = tagsRepository.findOne(tagId);
    
  	goodToBeUpdated.setName(name);
  	goodToBeUpdated.setDescription(description);
  	goodToBeUpdated.setTag(tag);
  	goodToBeUpdated.setPicture(picture);
		goodToBeUpdated.setUser(loggedUser);
		
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
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/deletedGood", method = RequestMethod.POST)
  public String deleteGood(HttpServletRequest request, Model model, 
                           @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));

		if (!userAccount.isPresent()) return "noUser";
		User loggedUser = userRepository.findByUserAccount(userAccount.get());
    
		GoodEntity good = goodsRepository.findOne(id);

		// This statement check if the entity to delete actually exists.
		if (good != null) {
		  loggedUser.removeGood(good);
	    userRepository.save(loggedUser);
		  goodsRepository.delete(id);
		}
    // If the entity doesn't exist, an empty entity is returned.
  	else good = GoodEntity.createEmptyGood();

    model.addAttribute("result", good);
		return "deletedGood";
  }

}

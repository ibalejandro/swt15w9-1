package app.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.GoodEntity;
import app.model.UserRepository;
import app.repository.GoodRepository;

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

	//@Autowired GoodRepository repository;
	/////////////////////////////////////////////////////Ergänzung Userzuordnung
	private final UserRepository repositoryUser;
	private final GoodRepository repository;
	
	/**
   * Autowire.
   * @param userRepository The repository for the users
   * @param description The repository for the goods
   */
	@Autowired
	public GoodsManagementController(UserRepository userRepository,
									                 GoodRepository repository){
		this.repositoryUser = userRepository;
		this.repository = repository;
	}
	/////////////////////////////////////////////////////////end
	
	/**
   * This method is the answer for the request to '/myOfferedGoods'. It finds
   * and retrieves all the goods associated with a particular user.
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/myOfferedGoods", method = RequestMethod.GET)
  public String listUserOfferedGoods(Model model) {
	  /*
  	 * This is just for the examples. The userId will be the real id of
  	 * the user, who wants to see his offered goods.
  	 */
  	long userId = 1L;
	
    model.addAttribute("result", repository.findByUserId(userId));
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
  public String showGoodToUpdate(HttpServletRequest request, Model model) {
	  long id = Long.parseLong(request.getParameter("id"));
		
		GoodEntity good = repository.findOne(id);
		
		String tagsAsString = "";
		
		/* 
		 * If the entity exists a parsed string for the tags is built, so that
		 * they can be easily updated by the user.
		 */
		if (good != null) {
			tagsAsString = good.getTagsAsString();
		}
		// If the entity doesn't exist, an empty entity is returned.
		else {
  		Set<String> emptyTags = new HashSet<>();
  		long invalidUserId = -1L;
  		String emptyPic = new String();
  		good = new GoodEntity("", "", emptyTags, emptyPic, invalidUserId);
    }
    	
    model.addAttribute("result", good);
   	model.addAttribute("parsedTags", tagsAsString);
		return "update";
  }
	
	/**
   * This method is the answer for the request to '/updatedGood'. It updates
   * a particular good with the given information and retrieves the updated
   * good.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/updatedGood", method = RequestMethod.POST)
  public String updateGood(HttpServletRequest request, Model model) {
		long id = Long.parseLong(request.getParameter("id"));
		
		GoodEntity goodToBeUpdated = repository.findOne(id);
		
		String name = request.getParameter("name");
    String description = request.getParameter("description");
    String tagsString = request.getParameter("tags");
    	
    Set<String> tags = new HashSet<String>
    					         (Arrays.asList(tagsString.split(", ")));
    	
  	goodToBeUpdated.setName(name);
  	goodToBeUpdated.setDescription(description);
  	goodToBeUpdated.setTags(tags);
    	
  	/*
  	 * This is just for the examples. The userId will be the real id of
  	 * the user, who is offering the good.
  	 */
  	long userId = 1L;
  	goodToBeUpdated.setUserId(userId);
    	
  	/*
  	 * Calling save() on an object with predefined id will update the
  	 * corresponding database record rather than insert a new one.
  	 */
  	model.addAttribute("result", repository.save(goodToBeUpdated));
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
  public String deleteGood(HttpServletRequest request, Model model) {
		long id = Long.parseLong(request.getParameter("id"));
		
		GoodEntity good = repository.findOne(id);
		
		// This statement check if the entity to delete actually exists.
		if (good != null) repository.delete(id);
    // If the entity doesn't exist, an empty entity is returned.
  	else {
  	  Set<String> emptyTags = new HashSet<>();
  		long invalidUserId = -1L;
  		String emptyPic = new String();
  		good = new GoodEntity("", "", emptyTags, emptyPic, invalidUserId);
  	}
    	
    model.addAttribute("result", good);
		return "deletedGood";
  }
	
}

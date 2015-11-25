package app.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodRepository;

/**
* <h1>GoodsOfferController</h1>
* The GoodsOfferController is used to offer and view offered goods by the users.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@Controller
public class GoodsOfferController {

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
	public GoodsOfferController(UserRepository userRepository,
	                            GoodRepository repository){
		this.repositoryUser = userRepository;
		this.repository = repository;
	}
	/////////////////////////////////////////////////////////end
	
	/**
   * This method is the answer for the request to '/home'. It finds
   * and retrieves all the offered goods by the users.
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/home", method = RequestMethod.GET)
  public String listAllGoods(Model model) {
	  model.addAttribute("result", repository.findAll());
		return "home";
  }
	
	/**
   * This method is the answer for the request to '/offeredGood'. It saves
   * and retrieves the good that the user wants to offer and associates it with
   * him.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/offeredGood", method = RequestMethod.POST)
  public String saveGood(HttpServletRequest request, Model model,
  					             @LoggedIn Optional<UserAccount> userAccount) {//!!!!
	  String name = request.getParameter("name");
	  String description = request.getParameter("description");
  	String tagsString = request.getParameter("tags");
  	
  	//////////////////////////////////////////////suchen des aktiven Users:
  	if (!(userAccount.isPresent())) {
  		return "errorpage0_empty";
  	}
  	User user = repositoryUser.findByUserAccount(userAccount.get());
  	//////////////////////////////////////////////////////////////end
  	
  	Set<String> tags = new HashSet<String>
  	                   (Arrays.asList(tagsString.split(", ")));
  	
  	GoodEntity good = new GoodEntity(name, description, tags, user.getId());
  	
  	GoodEntity savedGood = repository.save(good);
  	///////////////////////////////////////////////////hinzufügen in User:
  	user.addGood(savedGood);
  	////////////////////////////////////////////////////////////end
  	
  	model.addAttribute("result", savedGood);
  	return "offeredGood";
  }

}

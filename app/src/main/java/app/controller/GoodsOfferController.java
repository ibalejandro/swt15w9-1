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
import app.model.TagEntity;
import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;

/**
* <h1>GoodsOfferController</h1>
* The GoodsOfferController is used to offer and view offered goods by the users.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@Controller
public class GoodsOfferController {

	/////////////////////////////////////////////////////Ergänzung Userzuordnung
	private final UserRepository userRepository;
	private final GoodsRepository goodsRepository;
	private final TagsRepository tagsRepository;

	/**
   * Autowire.
   * @param UserRepository The repository for the users
   * @param GoodsRepository The repository for the goods
   * @param TagsRepository The repository for the tags
   */
	@Autowired
	public GoodsOfferController(UserRepository userRepository,
	                            GoodsRepository goodsRepository,
	                            TagsRepository tagsRepository){
		this.userRepository = userRepository;
		this.goodsRepository = goodsRepository;
		this.tagsRepository = tagsRepository;
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
	  model.addAttribute("result", goodsRepository.findAll());
		return "home";
  }

  /**
   * This method is the answer for the request to '/offer'. It retrieves and
   * and populates the tags dropdown with the whole available tags.
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
  @RequestMapping(value = "/offer", method = RequestMethod.GET)
  public String populateTagsDropdown(Model model) {
    model.addAttribute("tags", tagsRepository.findAll());
    return "offer";
  }

	/**
   * This method is the answer for the request to '/offeredGood'. It saves
   * and retrieves the good that the user wants to offer and associates it with
   * him.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param Optional<UserAccount> The user's account who wants to offer the good
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/offeredGood", method = RequestMethod.POST)
  public String saveGood(HttpServletRequest request, Model model,
  					             @LoggedIn Optional<UserAccount> userAccount) {
	  String name = request.getParameter("name");
	  String description = request.getParameter("description");
  	long tagId = Long.parseLong(request.getParameter("tagId"));
  	String picture = request.getParameter("picture");
  	
  	System.out.println(name);
  	System.out.println(description);
  	System.out.println(picture);

  	//////////////////////////////////////////////suchen des aktiven Users:
  	if (!userAccount.isPresent()) return "noUser";
  	User user = userRepository.findByUserAccount(userAccount.get());
  	//////////////////////////////////////////////////////////////end

  	TagEntity tag = tagsRepository.findOne(tagId);
  	GoodEntity good = new GoodEntity(name, description, tag, picture, user);
  	GoodEntity savedGood = goodsRepository.save(good);

  	///////////////////////////////////////////////////hinzufügen in User:
  	user.addGood(savedGood);
  	userRepository.save(user);
  	////////////////////////////////////////////////////////////end
  	model.addAttribute("result", savedGood);
  	return "offeredGood";
  }

}

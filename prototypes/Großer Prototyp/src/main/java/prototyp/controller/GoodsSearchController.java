package prototyp.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import prototyp.model.UserRepository;
import prototyp.repository.GoodRepository;

/**
* <h1>GoodsSearchController</h1>
* The GoodsSearchController is used to search a good by its name or any of
* its tags.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@Controller
public class GoodsSearchController {
	
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
	public GoodsSearchController(UserRepository userRepository,
								               GoodRepository repository){
		this.repositoryUser = userRepository;
		this.repository = repository;
	}
	/////////////////////////////////////////////////////////end
	
	/**
   * This method is the answer for the request to '/searchResultsByName'. It
   * finds and retrieves a list of goods that matches with the the given
   * good's name.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/searchResultsByName", method = RequestMethod.POST)
  public String searchGoodByName(HttpServletRequest request, Model model) {
  	String parameterType = "name";
  	String name = request.getParameter("name");
  	
  	/*
  	 * The type of parameter and the parameter itself for the search are 
  	 * sent to the view, so that the user can see his search criteria.
  	 */
  	model.addAttribute("resultParameterType", parameterType);
  	model.addAttribute("resultParameter", name);
  	model.addAttribute("result",
  	                   repository.findByNameStartingWithIgnoreCase(name));
  	return "searchResults";
  }
	
	 /**
   * This method is the answer for the request to '/searchResultsByTag'. It
   * finds and retrieves a list of goods that matches with the the given
   * good's tag.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/searchResultsByTag", method = RequestMethod.POST)
  public String searchGoodByTag(HttpServletRequest request, Model model) {
		String parameterType = "tag";
		String tag = request.getParameter("tag");
		
		/*
		 * The type of parameter and the parameter itself for the search are 
		 * sent to the view, so that the user can see his search criteria.
		 */
		model.addAttribute("resultParameterType", parameterType);
		model.addAttribute("resultParameter", tag);
		model.addAttribute("result",
						           repository.findByTagsContainingIgnoreCase(tag));
		return "searchResults";
  }

}

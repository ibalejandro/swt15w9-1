package app.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import app.model.ActivityEntity;
import app.model.GoodEntity;
import app.model.TagEntity;
import app.repository.ActivitiesRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;

/**
* <h1>GoodsSearchController</h1>
* The GoodsSearchController is used to search a good/activity by a given tag.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
@Controller
public class GoodsSearchController {

	private final GoodsRepository goodsRepository;
	private final ActivitiesRepository activitiesRepository;
	private final TagsRepository tagsRepository;

	/**
   * Autowire.
   * @param GoodsRepository The repository for the goods
   * @param ActivitiesRepository The repository for the activities
   * @param TagsRepository The repository for the tags
   */
	@Autowired
	public GoodsSearchController(GoodsRepository goodsRepository,
	                             ActivitiesRepository activitiesRepository,
								               TagsRepository tagsRepository){
		this.goodsRepository = goodsRepository;
		this.activitiesRepository = activitiesRepository;
		this.tagsRepository = tagsRepository;
	}
	/////////////////////////////////////////////////////////end

	/**
   * This method is the answer for the request to '/search'. It retrieves and
   * populates the tags dropdown with the whole available tags.
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/search", method = RequestMethod.GET)
  public String populateTagsDropdown(Model model) {
    model.addAttribute("navTags", tagsRepository.findAllByOrderByNameAsc());
    return "search";
  }

	 /**
   * This method is the answer for the request to '/searchResultsByTag'. It
   * finds and retrieves a list of goods/activities that matches with the given 
   * tag.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/searchResultsByTag", method = RequestMethod.POST)
  public String searchGoodOrActivityByTag(HttpServletRequest request, 
                                          Model model) {
		String parameterType = "tag";
		long tagId = Long.parseLong(request.getParameter("tagId"));

		/*
     * The type of parameter and the parameter itself for the search are
     * sent to the view, so that the user can see his search criteria.
     */
		Iterable<GoodEntity> goodsFound;
		Iterable<ActivityEntity> activitiesFound;
		if (tagId != -1L) {
		  TagEntity tag = tagsRepository.findOne(tagId);
		  model.addAttribute("resultParameter", tag.getName());
		  goodsFound = goodsRepository.findByTag(tag);
		  activitiesFound = activitiesRepository.findByTag(tag);
		}
		/*
     * If the tagId is -1L that means that the user doesn't want to filter by
     * any search criteria.
     */
		else {
		  model.addAttribute("resultParameter", "All");
		  goodsFound = goodsRepository.findAll();
		  activitiesFound = activitiesRepository.findAll();
		}
		
		model.addAttribute("result", goodsFound);
		model.addAttribute("resultActivity", activitiesFound);
		model.addAttribute("resultParameterType", parameterType);
		model.addAttribute("numberOfResults", 
		                   GoodEntity.getIterableSize(goodsFound));
		model.addAttribute("numberOfResultsActivities", 
                       ActivityEntity.getIterableSize(activitiesFound));
		return "searchResults";
  }

}

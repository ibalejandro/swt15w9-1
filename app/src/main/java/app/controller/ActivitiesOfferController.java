package app.controller;

import java.io.IOException;
import java.util.Date;
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
import app.model.ActivityEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.TagsRepository;
import app.validator.ActivityValidator;

/**
* <h1>ActivitiesOfferController</h1>
* The ActivitiesOfferController is used to offer and view offered activities by 
* the users.
*
* @author Alejandro Sánchez Aristizábal
* @since  30.12.2015
*/
@Controller
public class ActivitiesOfferController {

	/////////////////////////////////////////////////////Ergänzung Userzuordnung
	private final UserRepository userRepository;
	private final ActivitiesRepository activitiesRepository;
	private final TagsRepository tagsRepository;

	/**
   * Autowire.
   * @param UserRepository The repository for the users
   * @param ActivitiesRepository The repository for the activities
   * @param TagsRepository The repository for the tags
   */
	@Autowired
	public ActivitiesOfferController(UserRepository userRepository,
	                                 ActivitiesRepository activitiesRepository,
	                                 TagsRepository tagsRepository){
		this.userRepository = userRepository;
		this.activitiesRepository = activitiesRepository;
		this.tagsRepository = tagsRepository;
	}
	/////////////////////////////////////////////////////////end
	
  /**
   * This method is the answer for the request to '/offerActivity'. It retrieves 
   * and and populates the tags dropdown with the whole available tags.
   * @param Model The model to add response's attributes
   * @param ActivityEntity The activity to be filled and created
   * @param Optional<UserAccount> The user's account who wants to offer the 
   *                              activity
   * @return String The name of the view to be shown after processing
   */
  @RequestMapping(value = "/offerActivity", method = RequestMethod.GET)
  public String populateTagsDropdown
  (Model model, @ModelAttribute("activity") ActivityEntity activity,
   @LoggedIn Optional<UserAccount> userAccount) {
    if (!userAccount.isPresent()) return "noUser";
    
    model.addAttribute("tags", tagsRepository.findAllByOrderByNameAsc());
    return "offerActivity";
  }

	/**
   * This method is the answer for the request to '/offeredActivity'. It saves
   * and retrieves the activity that the user wants to offer and associates it 
   * with him.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param ActivityEntity The activity to be created
   * @param BindingResult The parameter in charge of the validation result
   * @param Optional<UserAccount> The user's account who wants to offer the 
   *                              activity
   * @return String The name of the view to be shown after processing
	 * @throws ServletException 
	 * @throws IOException 
   */
	@RequestMapping(value = "/offeredActivity", method = RequestMethod.POST)
  public String saveActivity(HttpServletRequest request, Model model, 
                             @ModelAttribute("activity") 
                             ActivityEntity activity,
                             BindingResult bindingResult, 
                             @LoggedIn Optional<UserAccount> userAccount) throws IOException, ServletException {
	  String name = request.getParameter("name");
	  String description = request.getParameter("description");
  	long tagId = Long.parseLong(request.getParameter("tagId"));
  	Part picture = request.getPart("pict");
  	String startDateInString = request.getParameter("startDate");
  	String endDateInString = request.getParameter("endDate");

  	//////////////////////////////////////////////suchen des aktiven Users:
  	if (!userAccount.isPresent()) return "redirect:noUser";
  	User user = userRepository.findByUserAccount(userAccount.get());
  	//////////////////////////////////////////////////////////////end
    
  	TagEntity tag = tagsRepository.findOne(tagId);
  	
  	Date startDate = ActivityEntity.convertStringIntoDate(startDateInString);
  	Date endDate = ActivityEntity.convertStringIntoDate(endDateInString);
  	
  	ActivityEntity activityToSave = new ActivityEntity(name, description, tag, 
  	                                                   picture, user, startDate, 
  	                                                   endDate);
  	
  	ActivityValidator activityValidator = new ActivityValidator();
    activityValidator.validate(activityToSave, bindingResult);
    /*
     * If there are errors in the parameters of the activity, the user is 
     * redirected to the activity form again.
     */
    if (bindingResult.hasErrors()) {
      System.out.println("Invalid activity: " + 
                         bindingResult.getAllErrors().toString());
      return "redirect:offerActivity";
    }
    
  	ActivityEntity savedActivity = activitiesRepository.save(activityToSave);
  	///////////////////////////////////////////////////hinzufügen in User:
  	user.addActivity(savedActivity);
  	userRepository.save(user);
  	////////////////////////////////////////////////////////////end
  	model.addAttribute("result", savedActivity);
  	return "offeredActivity";
  }

}

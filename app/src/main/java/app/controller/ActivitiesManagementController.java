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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import app.repository.TagsRepository;
import app.validator.ActivityValidator;
import app.model.ActivityEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;

/**
* <h1>ActivitiesManagementController</h1>
* The ActivitiesManagementController is used to show, update and delete the 
* offered activities by every particular user.
*
* @author Alejandro Sánchez Aristizábal
* @since  02.01.2016
*/
@Controller
public class ActivitiesManagementController {

	/////////////////////////////////////////////////////Ergänzung Userzuordnung
	private final UserRepository userRepository;
	private final ActivitiesRepository activitiesRepository;
	private final TagsRepository tagsRepository;
	
	private static final String POST = "POST";

	/**
   * Autowire.
   * @param UserRepository The repository for the users
   * @param ActivitiesRepository The repository for the activities
   */
	@Autowired
	public ActivitiesManagementController(UserRepository userRepository,
	                                      ActivitiesRepository 
	                                      activitiesRepository,
	                                      TagsRepository tagsRepository) {
		this.userRepository = userRepository;
		this.activitiesRepository = activitiesRepository;
		this.tagsRepository = tagsRepository;
	}
	/////////////////////////////////////////////////////////end

	/**
   * This method is the answer for the request to '/myOfferedActivities'. It 
   * finds and retrieves all the activities associated with a particular user.
   * @param Model The model to add response's attributes
   * @param Optional<UserAccount> The user's account who wants to see his
   *                              offered activities
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/myOfferedActivities", method = RequestMethod.GET)
	public String listUserOfferedActivities
	(Model model, @LoggedIn Optional<UserAccount> userAccount) {
	  /*
     * If there wasn't a log in instance, then the user is redirected to an
     * error page.
     */
    if (!userAccount.isPresent()) return "noUser";
		User loggedUser = userRepository.findByUserAccount(userAccount.get());
		
		//model.addAttribute("resultGoods", loggedUser.getActivities());
		model.addAttribute("resultActivities", activitiesRepository.findByUser(loggedUser));
		
		return "myOfferedActivities";
	}

	/**
   * This method is the answer for the request to '/updateActivity'. It finds 
   * and retrieves the particular activity that the user wants to update.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param ActivityEntity The activity to be refilled and updated
   * @param Optional<UserAccount> The user's account who wants to update one of
   *                              his offered activities
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/updateActivity", 
	                method = {RequestMethod.GET, RequestMethod.POST})
  public String showActivityToUpdate
  (HttpServletRequest request, Model model,
   @ModelAttribute("activity") ActivityEntity activityEntity,
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
	  
		ActivityEntity activityToUpdate = activitiesRepository.findOne(id);
    
    /*
     *  If the activity is null, the user is redirected to its offered 
     *  activities again. 
     */
    if (activityToUpdate == null) {
      model.addAttribute("wantedAction", "update");
      return "noSuchActivity";
    }
    
    /*
     * If the activitie's owner is different from the user trying to update the 
     * activity, the user is redirected to its offered activities again.
     */
    if (activityToUpdate.getUser() != loggedUser) {
      model.addAttribute("wantedAction", "update");
      return "notYourActivity";
    }

		model.addAttribute("activity", activityToUpdate);
		/*
		 * Every tag is sent to the update view except for the current tag. The
		 * current tag is already known and it's put as the default value whereas
		 * the other tags are there, so that the user can change the existing one.
		 */
		model.addAttribute("tags", 
		                   tagsRepository.findByIdNotOrderByNameAsc
		                   (activityToUpdate.getTag().getId()));
		return "updateActivity";
  }

	/**
   * This method is the answer for the request to '/updatedActivity'. It updates
   * a particular activity with the given information and retrieves the updated
   * activity.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param ActivityEntity The activity to be updated
   * @param BindingResult The parameter in charge of the validation result
   * @param Optional<UserAccount> The user's account who wants to update one of
   *                              his offered activities
   * @return String The name of the view to be shown after processing
	 * @throws ServletException 
	 * @throws IOException 
   */
	@RequestMapping(value = "/updatedActivity", method = RequestMethod.POST)
  public String updateActivity(HttpServletRequest request, Model model, 
                               @ModelAttribute("activity") 
                               ActivityEntity activityEntity, 
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @LoggedIn Optional<UserAccount> userAccount) throws IOException, ServletException {
		long id = Long.parseLong(request.getParameter("id"));
		String name = request.getParameter("name");
    String description = request.getParameter("description");
    long tagId = Long.parseLong(request.getParameter("tagId"));
    Part picture = request.getPart("pict");
    String startDateInString = request.getParameter("startDate");
    String endDateInString = request.getParameter("endDate");

    ///////////////////////////////Zuordnung User=Aktiver User
    if (!userAccount.isPresent()) return "noUser";
    User loggedUser = userRepository.findByUserAccount(userAccount.get());
    ////////////////////////////////////////end
    
    ActivityEntity activityToBeUpdated = activitiesRepository.findOne(id);
    TagEntity tag = tagsRepository.findOne(tagId);
    
    Date startDate = ActivityEntity.convertStringIntoDate(startDateInString);
    Date endDate = ActivityEntity.convertStringIntoDate(endDateInString);
    
    activityToBeUpdated.setName(name);
    activityToBeUpdated.setDescription(description);
    activityToBeUpdated.setTag(tag);
  	if (picture!=null && picture.getSize()!=0L){
  		activityToBeUpdated.setPicture(ActivityEntity.createPicture(picture));
  	}
    activityToBeUpdated.setStartDate(startDate);
    activityToBeUpdated.setEndDate(endDate);
    activityToBeUpdated.setUser(loggedUser);
		
		ActivityValidator activityValidator = new ActivityValidator();
		activityValidator.validate(activityToBeUpdated, bindingResult);
    /*
     * If there are errors in the parameters of the activity to be updated, the 
     * user is redirected to the activity-update form again.
     */
    if (bindingResult.hasErrors()) {
      System.out.println("Invalid activity: " + 
                         bindingResult.getAllErrors().toString());
      redirectAttributes.addFlashAttribute("id", id);
      return "redirect:updateActivity";
    }
    
    /*
     * If the activity's owner is different from the user trying to update the 
     * activity, the user is redirected to its offered activities again.
     */
    if (activityToBeUpdated.getUser() != loggedUser) {
      model.addAttribute("wantedAction", "update");
      return "notYourActivity";
    }
    
		loggedUser.addActivity(activityToBeUpdated);
    userRepository.save(loggedUser);

  	/*
  	 * Calling save() on an object with predefined id will update the
  	 * corresponding database record rather than insert a new one.
  	 */
  	model.addAttribute("result", 
  	                   activitiesRepository.save(activityToBeUpdated));
		return "updatedActivity";
  }

	/**
   * This method is the answer for the request to '/deletedActivity'. It 
   * retrieves the activity that the user wants to delete and then deletes it.
   * @param HttpServletRequest The request with its information
   * @param Model The model to add response's attributes
   * @param Optional<UserAccount> The user's account who wants to delete one of
   *                              his offered activities
   * @return String The name of the view to be shown after processing
   */
	@RequestMapping(value = "/deletedActivity", method = RequestMethod.POST)
  public String deleteActivity(HttpServletRequest request, Model model, 
                               @LoggedIn Optional<UserAccount> userAccount) {
		long id = Long.parseLong(request.getParameter("id"));

		if (!userAccount.isPresent()) return "noUser";
		User loggedUser = userRepository.findByUserAccount(userAccount.get());
    
		ActivityEntity activity = activitiesRepository.findOne(id);

		/* If the activity is null, the user is redirected to its offered activities 
		 * again.
		 */
    if (activity == null) {
      model.addAttribute("wantedAction", "delete");
      return "noSuchActivity";
    }
    
    /*
     * If the activity's owner is different from the user trying to delete the 
     * activity, the user is redirected to its offered activities again.
     */
    if (activity.getUser() != loggedUser) {
      model.addAttribute("wantedAction", "delete");
      return "notYourActivity";
    }
    
	  loggedUser.removeActivity(activity);
    userRepository.save(loggedUser);
	  activitiesRepository.delete(id);

    model.addAttribute("result", activity);
		return "deletedActivity";
  }
	
	@RequestMapping(value="/{id}/activityimage", method=RequestMethod.GET, produces = "image/jpg")
	public @ResponseBody byte[] getImg(@PathVariable("id") long id){
		System.out.println(id);
		return activitiesRepository.findOne(id).getPicture();
	}
}

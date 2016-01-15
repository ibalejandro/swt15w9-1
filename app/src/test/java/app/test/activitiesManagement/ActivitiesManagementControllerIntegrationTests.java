package app.test.activitiesManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Date;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import app.model.ActivityEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.ActivitiesRepository;
import app.repository.TagsRepository;

public class ActivitiesManagementControllerIntegrationTests extends 
AbstractWebIntegrationTests {
  
  private ActivityEntity activity1, activity2, activity3, activity4, activity5;
  private TagEntity tag1, tag2, tag3;
  private static int iterableSizeWithOnlyOneSave = 1;
  private static int iterableSize = 4;
  private static int user1Activities = 0;
  
  @Autowired ActivitiesRepository activitiesRepository;
  @Autowired TagsRepository tagsRepository;
  @Autowired UserRepository userRepository;
  @Autowired UserAccountManager userAccountManager;
  
  @Autowired AuthenticationManager authenticationManager;
  
  protected void login(String userName, String password) {
    Authentication authentication = new UsernamePasswordAuthenticationToken
                                    (userName, password);
    SecurityContextHolder.getContext()
    .setAuthentication(authenticationManager.authenticate(authentication));
  }

  @Before
  public void createActivityEntities() {
    String name1 = "Trip to Leipzig";
    String description1 = "Come and visit Leipzig with us";
    String name2 = "Shopping";
    String description2 = "Come to the mall, shop and eat some ice cream";
    String name3 = "Berlin Trip";
    String description3 = "The most important german city is waiting for you!";
    String name4 = "Make your T-Shirt real";
    String description4 = "Come and make the T-Shirt yourself you ever dream"
                          + " of";
    String name5 = "Toy-Bears hospital";
    String description5 = "Who wants to care of some litte toy-bears?";
    
    tag1 = tagsRepository.findByName("Tickets & Experiences");
    tag2 = tagsRepository.findByName("Clothes, Shoes and Accesories");
    tag3 = tagsRepository.findByName("Dolls & Bears");
    
    Date date = ActivityEntity.getZeroTimeDate(new Date());
    
    User user1 = userRepository.findByUserAccount
                 (userAccountManager.findByUsername("Lisa").get());
    User user2 = userRepository.findByUserAccount
                 (userAccountManager.findByUsername("Peter").get());
    
    login(user1.getUserAccount().getUsername(), "pw");
    
    activity1 = new ActivityEntity(name1, description1, tag1, null, user1, date, 
                                   date);
    activity2 = new ActivityEntity(name2, description2, tag2, null, user2, date,
                                   date);
    activity3 = new ActivityEntity(name3, description3, tag1, null, user1, date,
                                   date);
    activity4 = new ActivityEntity(name4, description4, tag2, null, user1, date,
                                   date);
    activity5 = new ActivityEntity(name5, description5, tag3, null, user1, date,
                                   date);
  }
  
  @Test
  public void testListUserOfferedActivities() throws Exception {
    activitiesRepository.save(activity1);
    activitiesRepository.save(activity2);
    mvc.perform(get("/myOfferedActivities"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultActivities", is(not(emptyIterable()))));
  }
  
  @Test
  public void testShowActivityToUpdate() throws Exception {
    ActivityEntity savedActivity = activitiesRepository.save(activity3);
    mvc.perform(post("/updateActivity")
                .param("id", String.valueOf(savedActivity.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("activity", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("activity", Matchers.hasProperty
                        ("name", Matchers.equalTo(activity3.getName()))))
     .andExpect
     (model().attribute("activity",
                        Matchers.hasProperty("description", Matchers.equalTo
                                             (activity3.getDescription()))))
     /*.andExpect
     (model().attribute("activity",
                        Matchers.hasProperty("tag", Matchers.equalTo
                                             (activity.getTag()))))
     
     .andExpect
     (model().attribute("activity", Matchers.hasProperty
                        ("startDate", Matchers.equalTo
                         (ActivityEntity.getStringFromDateForInput
                          (activity3.getStartDate())))))
     
     .andExpect
     (model().attribute("activity", Matchers.hasProperty
                        ("endDate", Matchers.equalTo
                         (ActivityEntity.getStringFromDateForInput
                          (activity3.getEndDate())))))*/
     
     .andExpect
     (model().attribute("activity",
                        Matchers.hasProperty("user", Matchers.equalTo
                                             (activity3.getUser()))));
  }
  
  @Test
  public void testUpdateActivity() throws Exception {
    ActivityEntity savedActivity = activitiesRepository.save(activity4);
    mvc.perform(post("/updatedActivity")
                .param("id", String.valueOf(savedActivity.getId()))
                .param("name", "Updated name")
                .param("description", "Updated description")
                .param("tagId", String.valueOf(activity1.getTag().getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo("Updated name"))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("description", Matchers.equalTo
                                             ("Updated description"))))
     /*.andExpect
     (model().attribute("result",
                        Matchers.hasProperty("tag", Matchers.equalTo
                                             (activity1.getTag()))))
      
     .andExpect
     (model().attribute("result", Matchers.hasProperty
                        ("startDate", Matchers.equalTo
                         (ActivityEntity.getStringFromDateForInput
                          (activity4.getStartDate())))))
     
     .andExpect
     (model().attribute("result", Matchers.hasProperty
                        ("endDate", Matchers.equalTo
                         (ActivityEntity.getStringFromDateForInput
                          (activity4.getEndDate())))))*/
     
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("user", Matchers.equalTo
                                             (activity4.getUser()))));
  }
  
  @Test
  public void testDeleteActivity() throws Exception {
    activitiesRepository.save(activity5);
    mvc.perform(post("/deletedActivity")
                .param("id", String.valueOf(activity5.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result", Matchers.hasProperty
                       ("name", Matchers.equalTo(activity5.getName()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("description", Matchers.equalTo
                                             (activity5.getDescription()))))
     /*.andExpect
     (model().attribute("result",
                        Matchers.hasProperty("tag", Matchers.equalTo
                                             (activity5.getTag()))))
     .andExpect
     (model().attribute("result", Matchers.hasProperty
                        ("startDate", Matchers.equalTo
                         (ActivityEntity.getStringFromDateForInput
                          (activity5.getStartDate())))))
     
     .andExpect
     (model().attribute("result", Matchers.hasProperty
                        ("endDate", Matchers.equalTo
                         (ActivityEntity.getStringFromDateForInput
                          (activity5.getEndDate())))))*/
     
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("user", Matchers.equalTo
                                             (activity5.getUser()))));
    
     mvc.perform(get("/myOfferedActivities"))
     .andExpect(status().isOk())
     .andExpect(model().attribute("resultActivities", 
                                  is(not(emptyIterable()))));
  }
  
  public String getRequiredStringFromDate(Date date) {
    return ActivityEntity.getStringFromDateForInput
           (ActivityEntity.getZeroTimeDate(date));
  }
  
}

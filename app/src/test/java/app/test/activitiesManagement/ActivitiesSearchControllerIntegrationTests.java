package app.test.activitiesManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

public class ActivitiesSearchControllerIntegrationTests extends 
AbstractWebIntegrationTests {
  
  private ActivityEntity activity1, activity2, activity3, activity4, activity5;
  private TagEntity tag1, tag2, tag3, nonMatchingTag;
  private static int iterableSizeForTagSportingGoods = 2;
  private static int iterableSizeForTagClothesShoesAndAccesories = 2;
  private static int iterableSizeForTagDollsAndBears = 1;
  private static int iterableSizeForAllActivities = 5;
  private static int activitiesInTestDaten = 0;
  
  @Autowired ActivitiesRepository activitiessRepository;
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
    nonMatchingTag = tagsRepository.findByName("Baby");
    
    Date date = new Date();
    
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
    
    activitiessRepository.save(activity1);
    activitiessRepository.save(activity2);
    activitiessRepository.save(activity3);
    activitiessRepository.save(activity4);
    activitiessRepository.save(activity5);
  }
 
  @Test
  public void testSearchActivityByTag() throws Exception {
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tag1.getId()))
                .param("distance", "-1"))
   .andExpect(status().isOk())
   .andExpect(model().attribute("resultActivities", is(not(emptyIterable()))))
   .andExpect(model().attribute("resultParameter", 
                                Matchers.equalTo(tag1.getName())))
   .andExpect
   (model().attribute("resultActivities", 
                      is(iterableWithSize(iterableSizeForTagSportingGoods))));
    
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tag2.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultActivities", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(tag2.getName())))
    .andExpect
    (model()
     .attribute("resultActivities", is(iterableWithSize
                (iterableSizeForTagClothesShoesAndAccesories))));
    
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tag3.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultActivities", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(tag3.getName())))
    .andExpect
    (model().attribute("resultActivities", 
                       is(iterableWithSize(iterableSizeForTagDollsAndBears))));
    
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(nonMatchingTag.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultActivities", is(emptyIterable())))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(nonMatchingTag.getName())));
    
    // A tag's id equals -1 means that all activities have to be returned.
    mvc.perform(post("/searchResultsByTag").param("tagId", "-1")
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultActivities", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo("All")))
    .andExpect
    (model()
     .attribute("resultActivities", is(iterableWithSize
                (iterableSizeForAllActivities + activitiesInTestDaten))));
  }
  
}

package app.test.goodsManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodRepository;

public class GoodsManagementControllerIntegrationTests extends 
AbstractWebIntegrationTests {
  
  private GoodEntity good1, good2, good3, good4, good5;
  private static int iterableSizeWithOnlyOneSave = 1;
  private static int iterableSize = 4;
  
  @Autowired GoodRepository repository;
  @Autowired UserRepository userRepository;
  @Autowired UserAccountManager userAccountManager;

  
  @Before
  public void createGoodEntities() {
    //long userId1 = 1L;
	//long userId2 = 2L;
	  /////////////////////////////////Korrektur aufgrund Konstruktoränderung
	  User userId1= userRepository.findByUserAccount(userAccountManager.findByUsername("Lisa").get());
	  User userId2= userRepository.findByUserAccount(userAccountManager.findByUsername("Peter").get());
	  /////////////////////////////end
    
    Set<String> tags1 = new HashSet<String>(Arrays.asList("Transport", "Kids"));
    String picLink1 = "http://i.imgur.com/C2csOAA.jpg";
    String picLink2 = "http://i.imgur.com/Xr50D6D.jpg";
    good1 = new GoodEntity("Bicycle", "This bicycle is for girls under 12 years"
                           + " old. It's pink and purple", tags1, picLink1, userId1);
    
    Set<String> tags2 = new HashSet<String>
                        (Arrays.asList("Winter", "Men", "Clothes"));
    good2 = new GoodEntity("Jacket", "The jacket is for men. It's black with a"
                           + " gray hood", tags2, picLink2, userId2);
    
    Set<String> tags3 = new HashSet<String>
                        (Arrays.asList("Soccer", "Teenagers", "Sports"));
    good3 = new GoodEntity("Soccer ball", "This is an almost new soccer ball"
                           + " for teenagers.", tags3, picLink2, userId1);
    
    Set<String> tags4 = new HashSet<String>
                        (Arrays.asList("Winter", "Women", "Clothes"));
    good4 = new GoodEntity("Winter gloves", "The winter gloves are for women's"
                           + " size S.", tags4, picLink1, userId1);
    
    Set<String> tags5 = new HashSet<String>
                        (Arrays.asList("Sleep", "Winter", "Kids"));
    good5 = new GoodEntity("Sleeping", "This sleeping is for kids and is useful"
                           + "to keep them warm in the night.", tags5, picLink1, userId1);
  }
  
  @Test
  public void testListUserOfferedGoods() throws Exception {
    repository.save(good1);
    repository.save(good2);
    mvc.perform(get("/myOfferedGoods"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result", 
                       is(iterableWithSize(iterableSizeWithOnlyOneSave))));
  }
  
  @Test
  public void testShowGoodToUpdate() throws Exception {
    GoodEntity savedGood = repository.save(good3);
    mvc.perform(post("/update").param("id", String.valueOf(savedGood.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo(good3.getName()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("description", Matchers.equalTo
                                             (good3.getDescription()))));
  }
  
  @Test
  public void testUpdateGood() throws Exception {
    GoodEntity savedGood = repository.save(good4);
    mvc.perform(post("/updatedGood")
                .param("id", String.valueOf(savedGood.getId()))
                .param("name", "Updated name")
                .param("description", "Updated description")
                .param("tags", good4.getTagsAsString()))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo("Updated name"))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("description", Matchers.equalTo
                                             ("Updated description"))));
  }
  
  @Test
  public void testDeleteGood() throws Exception {
    repository.save(good5);
    mvc.perform(post("/deletedGood")
                .param("id", String.valueOf(good5.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo(good5.getName()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("description", Matchers.equalTo
                                             (good5.getDescription()))));
    mvc.perform(get("/myOfferedGoods"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect(model().attribute("result", 
                                 is(iterableWithSize(iterableSize - 1))));
  }
  
}

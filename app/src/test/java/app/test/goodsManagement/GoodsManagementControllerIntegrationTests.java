package app.test.goodsManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import app.model.GoodEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;

public class GoodsManagementControllerIntegrationTests extends 
AbstractWebIntegrationTests {
  
  private GoodEntity good1, good2, good3, good4, good5;
  private static int iterableSizeWithOnlyOneSave = 1;
  private static int iterableSize = 5;
  
  @Autowired GoodsRepository goodsRepository;
  @Autowired TagsRepository tagsRepository;
  @Autowired UserRepository userRepository;
  @Autowired UserAccountManager userAccountManager;

  @Before
  public void createGoodEntities() {
    String name1 = "Bicycle";
    String name2 = "Jacket";
    String name3 = "Soccer ball";
    String name4 = "Winter gloves";
    String name5 = "Beautiful bear";
    String description1 = "This bicycle is for girls under 12 years old. It's "
                          + "pink and purple";
    String description2 = "The jacket is for men. It's black with a gray hood";
    String description3 = "This is an almost new soccer ball for teenagers";
    String description4 = "The winter gloves are for women's size S";
    String description5 = "A beautiful little bear for beautiful little girls";
    TagEntity tag1 = new TagEntity("Sporting Goods");
    TagEntity tag2 = new TagEntity("Clothes, Shoes and Accesories");
    TagEntity tag3 = new TagEntity("Dolls & Bears");
    String picture1 = "http://i.imgur.com/C2csOAA.jpg";
    String picture2 = "http://i.imgur.com/Xr50D6D.jpg";
	  User user1 = userRepository.findByUserAccount
	               (userAccountManager.findByUsername("Lisa").get());
	  User user2 = userRepository.findByUserAccount
	               (userAccountManager.findByUsername("Peter").get());
    
    good1 = new GoodEntity(name1, description1, tag1, picture1, user1);
    good2 = new GoodEntity(name2, description2, tag2, picture1, user2);
    good3 = new GoodEntity(name3, description3, tag1, picture1, user1);
    good4 = new GoodEntity(name4, description4, tag2, picture2, user1);
    good5 = new GoodEntity(name5, description5, tag3, picture2, user1);
    
    tagsRepository.save(tag1);
    tagsRepository.save(tag2);
    tagsRepository.save(tag3);
  }
  
  @Test
  public void testListUserOfferedGoods() throws Exception {
    goodsRepository.save(good1);
    goodsRepository.save(good2);
    mvc.perform(get("/myOfferedGoods"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result", 
                       is(iterableWithSize(iterableSizeWithOnlyOneSave))));
  }
  
  @Test
  public void testShowGoodToUpdate() throws Exception {
    GoodEntity savedGood = goodsRepository.save(good3);
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
                                             (good3.getDescription()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("tag", Matchers.equalTo
                                             (good3.getTag()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("picture", Matchers.equalTo
                                             (good3.getPicture()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("user", Matchers.equalTo
                                             (good3.getUser()))));
     
  }
  
  @Test
  public void testUpdateGood() throws Exception {
    GoodEntity savedGood = goodsRepository.save(good4);
    mvc.perform(post("/updatedGood")
                .param("id", String.valueOf(savedGood.getId()))
                .param("name", "Updated name")
                .param("description", "Updated description")
                .param("tagId", String.valueOf(good1.getTag().getId())))
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
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("tag", Matchers.equalTo
                                             (good1.getTag()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("picture", Matchers.equalTo
                                             (good4.getPicture()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("user", Matchers.equalTo
                                             (good4.getUser()))));
  }
  
  @Test
  public void testDeleteGood() throws Exception {
    goodsRepository.save(good5);
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
                                             (good5.getDescription()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("tag", Matchers.equalTo
                                             (good5.getTag()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("picture", Matchers.equalTo
                                             (good5.getPicture()))))
     .andExpect
     (model().attribute("result",
                        Matchers.hasProperty("user", Matchers.equalTo
                                             (good5.getUser()))));
    
    mvc.perform(get("/myOfferedGoods"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect(model().attribute("result", 
                                 is(iterableWithSize(iterableSize - 1))));
  }
  
}

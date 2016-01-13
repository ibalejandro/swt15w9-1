package app.test.goodsManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

public class GoodsSearchControllerIntegrationTests extends 
AbstractWebIntegrationTests {
  
  private GoodEntity good1, good2, good3, good4, good5;
  private TagEntity tag1, tag2, tag3, nonMatchingTag;
  private static int iterableSizeForTagSportingGoods = 2;
  private static int iterableSizeForTagClothesShoesAndAccesories = 2;
  private static int iterableSizeForTagDollsAndBears = 1;
  
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
    tag1 = new TagEntity("Sporting Goods");
    tag2 = new TagEntity("Clothes, Shoes and Accesories");
    tag3 = new TagEntity("Dolls & Bears");
    nonMatchingTag = new TagEntity("Non-matching");
    String picture1 = "http://i.imgur.com/C2csOAA.jpg";
    String picture2 = "http://i.imgur.com/Xr50D6D.jpg";
    User user1 = userRepository.findByUserAccount
                 (userAccountManager.findByUsername("Lisa").get());
    User user2 = userRepository.findByUserAccount
                 (userAccountManager.findByUsername("Peter").get());
    
    good1 = new GoodEntity(name1, description1, tag1, null, user1);
    good2 = new GoodEntity(name2, description2, tag2, null, user2);
    good3 = new GoodEntity(name3, description3, tag1, null, user1);
    good4 = new GoodEntity(name4, description4, tag2, null, user1);
    good5 = new GoodEntity(name5, description5, tag3, null, user1);
    
    tagsRepository.save(tag1);
    tagsRepository.save(tag2);
    tagsRepository.save(tag3);
    tagsRepository.save(nonMatchingTag);
    
    goodsRepository.save(good1);
    goodsRepository.save(good2);
    goodsRepository.save(good3);
    goodsRepository.save(good4);
    goodsRepository.save(good5);
  }
    
  @Test
  public void testSearchGoodByTag() throws Exception {
    mvc.perform(post("/searchResultsByTag").param("tagId", 
                                                  String.valueOf(tag1.getId())))
   .andExpect(status().isOk())
   .andExpect(model().attribute("result", is(not(emptyIterable()))))
   .andExpect(model().attribute("resultParameter", 
                                Matchers.equalTo(tag1.getName())))
   .andExpect
   (model()
    .attribute("result", is(iterableWithSize
                            (iterableSizeForTagSportingGoods))));
    
    mvc.perform(post("/searchResultsByTag").param("tagId", 
                                                  String.valueOf(tag2.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(tag2.getName())))
    .andExpect
    (model()
     .attribute("result", is(iterableWithSize
                             (iterableSizeForTagClothesShoesAndAccesories))));
    
    mvc.perform(post("/searchResultsByTag").param("tagId", 
                                                  String.valueOf(tag3.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(tag3.getName())))
    .andExpect
    (model()
     .attribute("result", is(iterableWithSize
                             (iterableSizeForTagDollsAndBears))));
    
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(nonMatchingTag.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(emptyIterable())))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(nonMatchingTag.getName())));
  }
  
}

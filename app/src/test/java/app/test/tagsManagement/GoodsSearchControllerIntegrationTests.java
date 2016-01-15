package app.test.tagsManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
  private static int iterableSizeForAllGoods = 5;
  private static int goodsInTestDaten = 5;
  
  @Autowired GoodsRepository goodsRepository;
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
  public void createGoodEntities() {
    String name1 = "Bicycle";
    String description1 = "This bicycle is for girls under 12 years old. It's "
                          + "pink and purple";
    String name2 = "Jacket";
    String description2 = "The jacket is for men. It's black with a gray hood";
    String name3 = "Soccer ball";
    String description3 = "This is an almost new soccer ball for teenagers";
    String name4 = "Winter gloves";
    String description4 = "The winter gloves are for women's size S";
    String name5 = "Beautiful bear";
    String description5 = "A beautiful little bear for beautiful little girls";
    
    tag1 = tagsRepository.findByName("Sporting Goods");
    tag2 = tagsRepository.findByName("Clothes, Shoes and Accesories");
    tag3 = tagsRepository.findByName("Dolls & Bears");
    nonMatchingTag = tagsRepository.findByName("Baby");
    
    User user1 = userRepository.findByUserAccount
                 (userAccountManager.findByUsername("Lisa").get());
    User user2 = userRepository.findByUserAccount
                 (userAccountManager.findByUsername("Peter").get());
    
    login(user1.getUserAccount().getUsername(), "pw");
    login(user2.getUserAccount().getUsername(), "pw");
    
    good1 = new GoodEntity(name1, description1, tag1, null, user1);
    good2 = new GoodEntity(name2, description2, tag2, null, user2);
    good3 = new GoodEntity(name3, description3, tag1, null, user1);
    good4 = new GoodEntity(name4, description4, tag2, null, user1);
    good5 = new GoodEntity(name5, description5, tag3, null, user1);
    
    goodsRepository.save(good1);
    goodsRepository.save(good2);
    goodsRepository.save(good3);
    goodsRepository.save(good4);
    goodsRepository.save(good5);
  }
    
  @Ignore
  @Test
  public void testSearchGoodByTag() throws Exception {
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tag1.getId()))
                .param("distance", "-1"))
   .andExpect(status().isOk())
   .andExpect(model().attribute("resultGoods", is(not(emptyIterable()))))
   .andExpect(model().attribute("resultParameter", 
                                Matchers.equalTo(tag1.getName())))
   .andExpect
   (model().attribute("resultGoods", 
                      is(iterableWithSize(iterableSizeForTagSportingGoods))));
    
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tag2.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultGoods", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(tag2.getName())))
    .andExpect
    (model()
     .attribute("resultGoods", is(iterableWithSize
                (iterableSizeForTagClothesShoesAndAccesories))));
    
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tag3.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultGoods", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(tag3.getName())))
    .andExpect
    (model().attribute("resultGoods", 
                       is(iterableWithSize(iterableSizeForTagDollsAndBears))));
    
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(nonMatchingTag.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultGoods", is(emptyIterable())))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo(nonMatchingTag.getName())));
    
    // A tag's id equals -1 means that all goods have to be returned.
    mvc.perform(post("/searchResultsByTag").param("tagId", "-1")
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultGoods", is(not(emptyIterable()))))
    .andExpect(model().attribute("resultParameter", 
                                 Matchers.equalTo("All")))
    .andExpect
    (model()
     .attribute("resultGoods", is(iterableWithSize
                (iterableSizeForAllGoods + goodsInTestDaten))));
  }
  
}

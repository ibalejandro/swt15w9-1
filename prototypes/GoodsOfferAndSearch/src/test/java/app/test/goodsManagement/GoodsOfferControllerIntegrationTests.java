package app.test.goodsManagement;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import app.controller.GoodsOfferController;
import app.model.GoodEntity;
import app.model.TagEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodsRepository;
import app.repository.TagsRepository;

public class GoodsOfferControllerIntegrationTests extends AbstractWebIntegrationTests {

  private GoodEntity good1, good2;
  private static int iterableSize = 2;
  
  @Autowired GoodsOfferController controller;
  @Autowired GoodsRepository goodsRepository;
  @Autowired TagsRepository tagsRepository;
  @Autowired UserRepository userRepository;
  @Autowired UserAccountManager userAccountManager;
  
  @Before
  public void createGoodEntities() {
    String name1 = "Jacket";
    String name2 = "Beautiful Bear";
    String description1 = "The jacket is for men. It's black with a gray hood";
    String description2 = "A beautiful little bear for beautiful little girls";
    TagEntity tag1 = new TagEntity("Clothes, Shoes and Accesories");
    TagEntity tag2 = new TagEntity("Dolls & Bears");
    String picture1 = "http://i.imgur.com/C2csOAA.jpg";
    String picture2 = "http://i.imgur.com/Xr50D6D.jpg";
    User user = userRepository.findByUserAccount(userAccountManager
                                                 .findByUsername("Lisa").get());
    
    good1 = new GoodEntity(name1, description1, tag1, picture1, user);
    good2 = new GoodEntity(name2, description2, tag2, picture2, user);
    
    tagsRepository.save(tag1);
    tagsRepository.save(tag2);
  }
  
  @Test
  public void testSaveGood() throws Exception {
    mvc.perform(post("/offeredGood").param("name", good1.getName())
                .param("description", good1.getDescription())
                .param("tagId", String.valueOf(good1.getTag().getId()))
                .param("picture", String.valueOf(good1.getPicture())))
   .andExpect(status().isOk())
   .andExpect(model().attribute("result", is(not(emptyIterable()))))
   .andExpect
   (model().attribute("result",
                      Matchers.hasProperty("name", 
                                           Matchers.equalTo(good1.getName()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("description", Matchers.equalTo
                                            (good1.getDescription()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("tag", Matchers.equalTo
                                            (good1.getTag()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("picture", Matchers.equalTo
                                            (good1.getPicture()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("user", Matchers.equalTo
                                            (good1.getUser()))));
    
    
    mvc.perform(post("/offeredGood").param("name", good2.getName())
                .param("description", good2.getDescription())
                .param("tags", String.valueOf(good2.getTag().getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo(good2.getName()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("description", Matchers.equalTo
                                            (good2.getDescription()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("tag", Matchers.equalTo
                                            (good2.getTag()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("picture", Matchers.equalTo
                                            (good2.getPicture()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("user", Matchers.equalTo
                                            (good2.getUser()))));
  }
  
  @Test
  @SuppressWarnings("unchecked")
  public void testListAllGoods() {
    Model model = new ExtendedModelMap();

    String returnedView = controller.listAllGoodsAndActivities(model);

    assertThat(returnedView, is("home"));

    Iterable<Object> object = (Iterable<Object>) model.asMap().get("result");
    assertThat(object, is(iterableWithSize(iterableSize)));
  }
  
}

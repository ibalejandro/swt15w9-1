package app.test.goodsManagement;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import app.controller.GoodsOfferController;
import app.model.GoodEntity;
import app.repository.GoodRepository;

public class GoodsOfferControllerIntegrationTests extends AbstractWebIntegrationTests {

  private GoodEntity good1, good2;
  private static int iterableSize = 2;
  
  @Autowired GoodsOfferController controller;
  @Autowired GoodRepository repository;
  
  @Before
  public void createGoodEntities() {
    long userId = 1L;
    Set<String> tags1 = new HashSet<String>(Arrays.asList("Transport", "Kids"));
    good1 = new GoodEntity("Bicycle", "This bicycle is for girls under 12 years"
                           + " old. It's pink and purple", tags1, userId);
    
    Set<String> tags2 = new HashSet<String>
                        (Arrays.asList("Winter", "Men", "Clothes"));
    good2 = new GoodEntity("Jacket", "The jacket is for men. It's black with a"
                           + " gray hood", tags2, userId);
    
    repository.save(good1);
    repository.save(good2);
  }
  
  /*
  @Test
  public void testSaveGood() throws Exception {
    mvc.perform(post("/offeredGood").param("name", good1.getName())
                .param("description", good1.getDescription())
                .param("tags", good1.getTagsAsString()))
   .andExpect(status().isOk())
   .andExpect(model().attribute("result", is(not(emptyIterable()))))
   .andExpect
   (model().attribute("result",
                      Matchers.hasProperty("name", 
                                           Matchers.equalTo(good1.getName()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("description", Matchers.equalTo
                                            (good1.getDescription()))));
    
    mvc.perform(post("/offeredGood").param("name", good2.getName())
                .param("description", good2.getDescription())
                .param("tags", good2.getTagsAsString()))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo(good2.getName()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("description", Matchers.equalTo
                                            (good2.getDescription()))));
  }
  */
  
  @Test
  @SuppressWarnings("unchecked")
  public void testListAllGoods() {

    Model model = new ExtendedModelMap();

    String returnedView = controller.listAllGoods(model);

    assertThat(returnedView, is("home"));

    Iterable<Object> object = (Iterable<Object>) model.asMap().get("result");
    assertThat(object, is(iterableWithSize(iterableSize)));
  }
  
}

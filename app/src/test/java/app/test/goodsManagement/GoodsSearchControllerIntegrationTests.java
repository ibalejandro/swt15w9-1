package app.test.goodsManagement;

import static org.hamcrest.Matchers.emptyIterable;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import app.model.GoodEntity;
import app.repository.GoodRepository;

public class GoodsSearchControllerIntegrationTests extends 
AbstractWebIntegrationTests {
  
  private GoodEntity good1, good2, good3, good4, good5;
  private static String tag = "Winter";
  private static int iterableSizeForSearchByName = 2;
  private static int iterableSizeForSearchByTag = 3;
  
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
    
    Set<String> tags3 = new HashSet<String>
                        (Arrays.asList("Transport", "Teenagers"));
    good3 = new GoodEntity("Bicycle with new tires", "This bicycle is not new"
                           + " but it is renewed with fresh tires.", tags3, 
                           userId);
    Set<String> tags4 = new HashSet<String>
                        (Arrays.asList("Winter", "Women", "Clothes"));
    good4 = new GoodEntity("Winter gloves", "The winter gloves are for women's"
                           + " size S.", tags4, userId);

    Set<String> tags5 = new HashSet<String>
                        (Arrays.asList("Sleep", "Winter", "Kids"));
    good5 = new GoodEntity("Sleeping", "This sleeping is for kids and is useful"
                           + "to keep them warm in the night.", tags5, userId);
    
    repository.save(good1);
    repository.save(good2);
    repository.save(good3);
    repository.save(good4);
    repository.save(good5);
  }
  
  /*
  @Test
  public void testSearchGoodByName() throws Exception {
    mvc.perform(post("/searchResultsByName").param("name", good1.getName()))
   .andExpect(status().isOk())
   .andExpect(model().attribute("result", is(not(emptyIterable()))))
   .andExpect
   (model().attribute("result",  
                      is(iterableWithSize(iterableSizeForSearchByName))));
    
    
    mvc.perform(post("/searchResultsByName").param("name", "bIcY"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",  
                       is(iterableWithSize(iterableSizeForSearchByName))));
    
  }
  */
  
  @Test
  public void testSearchGoodByTag() throws Exception {
    mvc.perform(post("/searchResultsByTag").param("tag", tag))
   .andExpect(status().isOk())
   .andExpect(model().attribute("result", is(not(emptyIterable()))))
   .andExpect
   (model().attribute("result",  
                      is(iterableWithSize(iterableSizeForSearchByTag))));
    
    mvc.perform(post("/searchResultsByTag").param("tag", "wInT"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",  
                       is(iterableWithSize(iterableSizeForSearchByTag))));
  }
  
}

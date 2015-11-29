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
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManager;
import org.springframework.beans.factory.annotation.Autowired;

import app.model.Address;
import app.model.GoodEntity;
import app.model.User;
import app.model.UserRepository;
import app.repository.GoodRepository;

public class GoodsSearchControllerIntegrationTests extends 
AbstractWebIntegrationTests {
  
  private GoodEntity good1, good2, good3, good4, good5;
  private static String tag = "Winter";
  private static int iterableSizeForSearchByName = 2;
  private static int iterableSizeForSearchByTag = 3;
  
  @Autowired GoodRepository repository;
  @Autowired UserRepository userRepository;
  @Autowired UserAccountManager userAccountManager;
  
  @Before
  public void createGoodEntities() {
    //long userId = 1L;///////////////////Korrektur aufgrund Konstruktoränderung
	 User userId2= userRepository.findByUserAccount(userAccountManager.findByUsername("Lisa").get());
	 /*Role testRole = new Role("ROLE_NORMAL");
	 UserAccount uaTest2=userAccountManager.create("Hans", "pw", testRole);
	 Address addressTest=new Address("Mittelstraße"," 1", "11587","Dresden");
	 userAccountManager.save(uaTest2);
	 User userId2= new User(uaTest2, addressTest);
	 userRepository.save(userId2);*/
    ////////////////////////end
    Set<String> tags1 = new HashSet<String>(Arrays.asList("Transport", "Kids"));
    String picLink1 = "http://i.imgur.com/C2csOAA.jpg";
    String picLink2 = "http://i.imgur.com/Xr50D6D.jpg";
    good1 = new GoodEntity("Bicycle", "This bicycle is for girls under 12 years"
                           + " old. It's pink and purple", tags1, picLink1, userId2);
    
    Set<String> tags2 = new HashSet<String>
                        (Arrays.asList("Winter", "Men", "Clothes"));
    good2 = new GoodEntity("Jacket", "The jacket is for men. It's black with a"
                           + " gray hood", tags2, picLink2, userId2);
    
    Set<String> tags3 = new HashSet<String>
                        (Arrays.asList("Transport", "Teenagers"));
    good3 = new GoodEntity("Bicycle with new tires", "This bicycle is not new"
                           + " but it is renewed with fresh tires.", tags3, picLink2, 
                           userId2);
    Set<String> tags4 = new HashSet<String>
                        (Arrays.asList("Winter", "Women", "Clothes"));
    good4 = new GoodEntity("Winter gloves", "The winter gloves are for women's"
                           + " size S.", tags4, picLink1, userId2);

    Set<String> tags5 = new HashSet<String>
                        (Arrays.asList("Sleep", "Winter", "Kids"));
    good5 = new GoodEntity("Sleeping", "This sleeping is for kids and is useful"
                           + "to keep them warm in the night.", tags5, picLink2, userId2);
    
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

package app.test.tagsManagement;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import app.controller.AdminController;
import app.model.TagEntity;
import app.repository.TagsRepository;

public class AdminControllerIntegrationTests extends 
AbstractWebIntegrationTests {

  private TagEntity newTag, tag1, tag2, tagOthers;
  private static int offersWithTagBooks = 4;
  
  @Autowired AdminController controller;
  @Autowired TagsRepository tagsRepository;
  
  @Autowired AuthenticationManager authenticationManager;
  
  protected void login(String userName, String password) {
    Authentication authentication = new UsernamePasswordAuthenticationToken
                                    (userName, password);
    SecurityContextHolder.getContext()
    .setAuthentication(authenticationManager.authenticate(authentication));
  }
  
  @Before
  public void createGoodEntities() {
    String name = "This is a new tag";
    newTag = new TagEntity(name);
    
    tag1 = tagsRepository.findByName("Books");
    tag2 = tagsRepository.findByName("Dolls & Bears");
    tagOthers = tagsRepository.findByName("Others");
    
    login("admin", "123");
  }
  
  @Test
  public void testAvailableTags() throws Exception {
    mvc.perform(get("/availableTags"))
   .andExpect(status().isOk())
   .andExpect(model().attribute("result", is(not(emptyIterable()))))
   .andExpect
   (model().attribute("result", 
                      is(iterableWithSize((int)(tagsRepository.count() - 1)))));
  }
  
  @Test
  public void testShowTagToUpdate() throws Exception {
    mvc.perform(post("/updateTag").param("id", String.valueOf(tag1.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("tag", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("tag",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo(tag1.getName()))));
  }
  
  @Test
  public void testUpdateTag() throws Exception {
    mvc.perform(post("/updatedTag")
                .param("id", String.valueOf(tag2.getId()))
                .param("name", "Updated name"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo("Updated name"))));
  }
  
  @Test
  public void testDeleteTag() throws Exception {
    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tagOthers.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultGoods", is(emptyIterable())));
    
    mvc.perform(post("/deletedTag")
                .param("id", String.valueOf(tag1.getId())))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result",
                       Matchers.hasProperty("name", 
                                            Matchers.equalTo(tag1.getName()))));

    mvc.perform(post("/searchResultsByTag")
                .param("tagId", String.valueOf(tagOthers.getId()))
                .param("distance", "-1"))
    .andExpect(status().isOk())
    .andExpect(model().attribute("resultGoods", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("resultGoods", 
                       is(iterableWithSize(offersWithTagBooks))));
  }
  
  @Test
  public void testAddNewTag() throws Exception {
    mvc.perform(post("/addedNewTag")
                .param("name", newTag.getName()))
    .andExpect(status().isOk())
    .andExpect(model().attribute("result", is(not(emptyIterable()))))
    .andExpect
    (model().attribute("result", Matchers.hasProperty
                       ("name", Matchers.equalTo(newTag.getName()))));
  }
  
  @Test
  public void testCreatedTag() {
    TagEntity newCreatedTag = tagsRepository.findByName(newTag.getName());
    String newCreatedTagName = newCreatedTag.getName();
    assertThat(newCreatedTagName, is(newTag.getName()));
  }
  
}

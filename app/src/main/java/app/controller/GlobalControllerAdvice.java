package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import app.model.TagEntity;
import app.repository.TagsRepository;

/**
* <h1>GlobalControllerAdvice</h1>
* The GlobalControllerAdvice is used to have global variables available as 
* models to use data in the views without calling any controller.
*
* @author Alejandro Sánchez Aristizábal
* @since  13.12.2015
*/
@ControllerAdvice
public class GlobalControllerAdvice {
  
  private final TagsRepository tagsRepository;
  
  /**
   * Autowire.
   * @param TagsRepository The repository for the tags
   */
  @Autowired
  public GlobalControllerAdvice(TagsRepository tagsRepository ) {
    Assert.notNull(tagsRepository, "TagsRepository must not be null!");
    this.tagsRepository = tagsRepository;
  }
  
  /**
   * This method retrieves a list with all the available tags so that the search
   * navigation bar can be populated.
   * @return Iterable<TagEntity> A list with all available tags
   */
  @ModelAttribute("navTags")
  public Iterable<TagEntity> populateSearchDropdown() {
    return tagsRepository.findAll();
  }
  
}

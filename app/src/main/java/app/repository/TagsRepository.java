package app.repository;

import org.springframework.data.repository.CrudRepository;
import app.model.TagEntity;

/**
* <h1>TagsRepository</h1>
* The TagsRepository is a repository where all available tags are saved.
*
* @author Alejandro Sánchez Aristizábal
* @since  29.11.2015
*/
public interface TagsRepository extends CrudRepository<TagEntity, Long> {
  /**
   * This method finds all tags except for the tag associated with the given 
   * tag's id.
   * @param id The given tag's id
   * @return Iterable<TagEntity> A list with all matches for all tag's ids 
   *                             different from the given tag's id
   */
  Iterable<TagEntity> findByIdNot(long id);
}


package prototyp.repository;

import org.springframework.data.repository.CrudRepository;
import prototyp.model.GoodEntity;

/**
* <h1>GoodRepository</h1>
* The GoodRepository is a repository where all offered goods are saved. When
* a good is updated or deleted, the GoodRepository is also modified.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
public interface GoodRepository extends CrudRepository<GoodEntity, Long> {
  /**
   * This method finds a good by a given name.
   * @param name The given name
   * @return Iterable<GoodEntity> A list with all matches for the given name
   */
	Iterable<GoodEntity> findByNameStartingWithIgnoreCase(String name);
	/**
   * This method finds a good by a given tag.
   * @param tag The given tag
   * @return Iterable<GoodEntity> A list with all matches for the given tag
   */
	Iterable<GoodEntity> findByTagsContainingIgnoreCase(String tag);
	/**
   * This method finds a good by a given user-ID.
   * @param userId The given user-ID
   * @return Iterable<GoodEntity> A list with all matches for the given user-ID
   */
	Iterable<GoodEntity> findByUserId(long userId);
	/**
   * This method deletes a good by a given good's id.
   * @param id The given good's id
   * @return Nothing
   */
	void delete(Long id);
}

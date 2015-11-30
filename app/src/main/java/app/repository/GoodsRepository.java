package app.repository;

import org.springframework.data.repository.CrudRepository;
import app.model.GoodEntity;
import app.model.TagEntity;
import app.model.User;

/**
* <h1>GoodsRepository</h1>
* The GoodsRepository is a repository where all offered goods are saved. When
* a good is updated or deleted, the GoodsRepository is also modified.
*
* @author Alejandro Sánchez Aristizábal
* @since  19.11.2015
*/
public interface GoodsRepository extends CrudRepository<GoodEntity, Long> {
	/**
   * This method finds a good by a given tag.
   * @param tag The given tag
   * @return Iterable<GoodEntity> A list with all matches for the given tag
   */
	Iterable<GoodEntity> findByTag(TagEntity tag);
	/**
   * This method finds a good by a given user.
   * @param user The given user
   * @return Iterable<GoodEntity> A list with all matches for the given user
   */
	Iterable<GoodEntity> findByUser(User user);
	/**
   * This method deletes a good by a given good's id.
   * @param id The given good's id
   * @return Nothing
   */
	void delete(Long id);
}

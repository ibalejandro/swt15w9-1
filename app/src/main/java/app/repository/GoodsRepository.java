package app.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
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
	   * This method finds a good by a given user and tag.
	   * @param tag The given tag
	   * @param user The given user
	   * @return Iterable<GoodEntity> A list with all matches for the given user
	   */
	Iterable<GoodEntity> findByTagAndUser(TagEntity tag, User user);
	
	/**
   * This method deletes a good by a given good's id.
   * @param id The given good's id
   * @return Nothing
   */
	@Override
	void delete(Long id);
	
	/**
   * This method updates a good's tag with the given default tag.
   * @param defaultTag The given default tag for the good
   * @param currentTag The good's current tag
   * @return Nothing
   */
	@Modifying
	@Transactional(readOnly = false)
	@Query("update GoodEntity g set g.tag = ?1 where g.tag = ?2")
	void setTagToDefaultTag(TagEntity defaultTag, TagEntity currentTag);
}

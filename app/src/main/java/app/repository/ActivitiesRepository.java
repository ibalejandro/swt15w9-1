package app.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import app.model.ActivityEntity;
import app.model.TagEntity;
import app.model.User;

/**
* <h1>ActivitiesRepository</h1>
* The ActivitiesRepository is a repository where all offered activities are 
* saved. When an activity isupdated or deleted, the ActivitiesRepository is 
* also modified.
*
* @author Alejandro Sánchez Aristizábal
* @since  30.12.2015
*/
public interface ActivitiesRepository extends CrudRepository<ActivityEntity, 
                                                             Long> {
	/**
   * This method finds an activity by a given tag.
   * @param tag The given tag
   * @return Iterable<ActivityEntity> A list with all matches for the given tag
   */
	Iterable<ActivityEntity> findByTag(TagEntity tag);
	
	/**
   * This method finds an activity by a given user.
   * @param user The given user
   * @return Iterable<ActivityEntity> A list with all matches for the given user
   */
	Iterable<ActivityEntity> findByUser(User user);
	
	/**
	   * This method finds an activity by a given user and tag.
	   * @param tag The given tag
	   * @param user The given user
	   * @return Iterable<ActivityEntity> A list with all matches for the given user
	   */
	Iterable<ActivityEntity> findByTagAndUser(TagEntity tag, User user);
	
	/**
   * This method deletes an activity by a given activitiy's id.
   * @param id The given activity's id
   * @return Nothing
   */
	@Override
	void delete(Long id);
	
	/**
   * This method updates an activity's tag with the given default tag.
   * @param defaultTag The given default tag for the activity
   * @param currentTag The activity's current tag
   * @return Nothing
   */
	@Modifying
	@Transactional(readOnly = false)
  @Query("update ActivityEntity a set a.tag = ?1 where a.tag = ?2")
  void setTagToDefaultTag(TagEntity defaultTag, TagEntity currentTag);
}

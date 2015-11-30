package app.repository;

import org.springframework.data.repository.CrudRepository;

import app.model.Dialog;
import app.model.User;

/**
 * <h1>DialogRepository</h1> The Dialog Repository is a repository where all
 * {@link Dialog}s are saved. When a dialog is updated or deleted, the
 * DialogRepository is also modified.
 * 
 * @author Mario Henze
 */
public interface DialogRepository extends CrudRepository<Dialog, Long> {
	/**
	 * This method finds a dialog by a given user.
	 * 
	 * @param user
	 *            The user by which the dialogs should be searched
	 * @return A list with all matches for the given user
	 */
	Iterable<Dialog> findByUserA(User user);

	/**
	 * This method finds a dialog by a given user.
	 * 
	 * @param user
	 *            The user by which the dialogs should be searched
	 * @return A list with all matches for the given user
	 */
	Iterable<Dialog> findByUserB(User user);

	void delete(Long id);
}

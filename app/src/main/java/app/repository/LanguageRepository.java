package app.repository;

import org.springframework.data.repository.CrudRepository;

import app.model.Language;

/**
 * <h1>LanguageRepository</h1> The LanguageRepository is a repository where all
 * available Languages are saved.
 * 
 * @author Friederike Kitzing
 */

public interface LanguageRepository extends CrudRepository<Language, Long> {
	/**
	 * This method finds a Language by it's name.
	 * 
	 * @param name
	 *            The given name
	 * @return Language The language object the given name belongs to
	 */
	Language findByName(String name);

	/**
	 * This method finds a Language by it's ISO-Code.
	 * 
	 * @param name
	 *            The given ISO
	 * @return Language The language object the given ISO belongs to
	 */
	Language findByKennung(String kennung);
}

package app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.model.InterfacePart;

public interface InterfaceRepository extends CrudRepository<InterfacePart, Long> {

	/**
	 * Returns all InterfaceParts with the specific ModuleId
	 * 
	 * @param moduleId
	 * @return
	 */
	List<InterfacePart> findByModuleId(long moduleId);

	/**
	 * Returns all InterfaceParts with the specific languageId
	 * 
	 * @param languageId
	 * @return
	 */
	List<InterfacePart> findByLanguageId(long languageId);

	/**
	 * Returns the InterfacePart with the specific Module- and languageId
	 * 
	 * @param moduleId
	 * @return
	 */
	InterfacePart findByLanguageIdAndModuleId(long languageId, long moduleId);
}

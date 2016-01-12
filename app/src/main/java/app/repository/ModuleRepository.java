package app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import app.model.Module;

public interface ModuleRepository extends CrudRepository<Module, Long> {

	/**
	 * List of all Module with the specific templateName
	 * 
	 * @param templateName
	 * @return
	 */
	List<Module> findByTemplateName(String templateName);

	/**
	 * Returns the Module with the specific templateName and TyhmeLeafName
	 * 
	 * @param templateName
	 *            the Template name
	 * @param tyhmeLeafName
	 *            the specific name in the Template
	 * @return
	 */
	Module findByTemplateNameAndTyhmeLeafName(String templateName, String tyhmeLeafName);
}

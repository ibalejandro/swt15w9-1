package app.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import app.model.InterfacePart;
import app.model.Language;
import app.model.TagEntity;
import app.repository.InterfaceRepository;
import app.repository.LanguageRepository;
import app.repository.ModuleRepository;
import app.repository.TagsRepository;

/**
 * <h1>GlobalControllerAdvice</h1> The GlobalControllerAdvice is used to have
 * global variables available as models to use data in the views without calling
 * any controller.
 *
 * @author Alejandro Sánchez Aristizábal
 * @since 13.12.2015
 */
@ControllerAdvice
public class GlobalControllerAdvice {

	private final TagsRepository tagsRepository;
	private final LanguageRepository languageRepository;
	private final InterfaceRepository interfaceRepository;
	private final ModuleRepository moduleRepository;

	/**
	 * Autowire.
	 * 
	 * @param TagsRepository
	 *            The repository for the tags
	 */
	@Autowired
	public GlobalControllerAdvice(TagsRepository tagsRepository, LanguageRepository languageRepository,
			InterfaceRepository interfaceRepository, ModuleRepository moduleRepository) {
		Assert.notNull(tagsRepository, "TagsRepository must not be null!");
		this.tagsRepository = tagsRepository;
		this.languageRepository = languageRepository;
		this.interfaceRepository = interfaceRepository;
		this.moduleRepository = moduleRepository;
	}

	/**
	 * This method retrieves a list with all the available tags so that the
	 * search navigation bar can be populated.
	 * 
	 * @return Iterable<TagEntity> A list with all available tags
	 */
	@ModelAttribute("navTags")
	public Iterable<TagEntity> populateSearchDropdown() {
		return tagsRepository.findAllByOrderByNameAsc();
	}

	@ModelAttribute("translationSystem")
	public void bla(HttpServletRequest request, Model model) {
		Language lan = languageRepository.findByKennung(request.getLocale().getLanguage());
		if (lan == null) {
			lan = languageRepository.findByKennung("de");
		}

		if (lan != null) {
			List<InterfacePart> inPLan = interfaceRepository.findByLanguageId(lan.getId());
			for (InterfacePart iP : inPLan) {
				if (moduleRepository.findOne(iP.getModuleId()) != null) {
					model.addAttribute(moduleRepository.findOne(iP.getModuleId()).getThymeLeafName(), iP);
					// System.out.println(
					// moduleRepository.findOne(iP.getModuleId()).getThymeLeafName()
					// + ", " + iP.getText());
				}
			}
		}
	}

}

package app.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import app.model.TagEntity;

/**
 * <h1>TagValidator</h1> The TagValidator is used to validate if a tag has all
 * the required attributes under certain conditions.
 *
 * @author Alejandro Sánchez Aristizábal
 * @since 07.01.2016
 */
public class TagValidator implements Validator {

	/**
	 * Supports.
	 * 
	 * @param Class<?>
	 *            The Class that this Validator is being asked if it can
	 *            validate
	 * @return boolean The status of the validation possibility
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return TagEntity.class.equals(clazz);
	}

	/**
	 * This method validates if the properties of a tag are correct under
	 * certain conditions and also detects error and returns the respective
	 * messages.
	 * 
	 * @param Object
	 *            The object that is to be validated
	 * @param Errors
	 *            The contextual state about the validation process
	 * @return Nothing
	 */
	@Override
	public void validate(Object target, Errors errors) {
		if (target != null) {
			TagEntity activity = (TagEntity) target;

			if (activity.getName().trim().isEmpty()) {
				errors.rejectValue("name", "required.name");
			}
		}
	}

}

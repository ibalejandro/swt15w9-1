package app.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import app.model.GoodEntity;

/**
 * <h1>GoodValidator</h1> The GoodValidator is used to validate if a good has
 * all the required attributes under certain conditions.
 *
 * @author Alejandro Sánchez Aristizábal
 * @since 15.12.2015
 */
public class GoodValidator implements Validator {

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
		return GoodEntity.class.equals(clazz);
	}

	/**
	 * This method validates if the properties of a good are correct under
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
			GoodEntity good = (GoodEntity) target;

			if (good.getName().trim().isEmpty()) {
				errors.rejectValue("name", "required.name");
			}
			if (good.getDescription().trim().isEmpty()) {
				errors.rejectValue("description", "required description");
			}
			if (good.getTag() == null)
				errors.rejectValue("tag", "required.tag");
		}
	}

}

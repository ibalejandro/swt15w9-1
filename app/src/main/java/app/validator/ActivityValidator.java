package app.validator;

import java.util.Date;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import app.model.ActivityEntity;

/**
 * <h1>ActivityValidator</h1> The ActivityValidator is used to validate if an
 * activity has all the required attributes under certain conditions.
 *
 * @author Alejandro Sánchez Aristizábal
 * @since 30.12.2015
 */
public class ActivityValidator implements Validator {

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
		return ActivityEntity.class.equals(clazz);
	}

	/**
	 * This method validates if the properties of an activity are correct under
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
			ActivityEntity activity = (ActivityEntity) target;

			if (activity.getName().trim().isEmpty()) {
				errors.rejectValue("name", "required.name");
			}
			if (activity.getDescription().trim().isEmpty()) {
				errors.rejectValue("description", "required description");
			}
			if (activity.getTag() == null)
				errors.rejectValue("tag", "required.tag");
			if (activity.getStartDate() != null && activity.getEndDate() == null) {
				errors.rejectValue("endDate", "required.endDate");
			}
			if (activity.getStartDate() == null && activity.getEndDate() != null) {
				errors.rejectValue("startDate", "required.startDate");
			}
			if (activity.getStartDate() != null && activity.getEndDate() != null) {
				if (activity.getStartDate().after(activity.getEndDate())) {
					errors.rejectValue("startDate", "invalid.startDate");
				}
				if (activity.getEndDate().before(activity.getStartDate())) {
					errors.rejectValue("endDate", "invalid.endDate");
				}
				if (activity.getStartDate().before(ActivityEntity.getZeroTimeDate(new Date()))) {
					errors.rejectValue("startDate", "invalid.startDate");
				}
			}
		}
	}

}

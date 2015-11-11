package dialog.web;

import org.hibernate.validator.constraints.NotBlank;

public interface MessageForm {
	@NotBlank
	String getMessage();
}

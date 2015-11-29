package app.controller;

import org.hibernate.validator.constraints.NotBlank;

public interface MessageForm {
	@NotBlank
	String getMessage();
}

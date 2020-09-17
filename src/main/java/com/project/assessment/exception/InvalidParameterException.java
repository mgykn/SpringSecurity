/**
 * @author altar.halil
 *
 */
package com.project.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidParameterException extends RuntimeException {
	private static final long serialVersionUID = 3153855952884126836L;

	public InvalidParameterException(String parameter) {
		super(parameter);
	}

}

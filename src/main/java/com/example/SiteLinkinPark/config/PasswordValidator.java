package com.example.SiteLinkinPark.config;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null || value.isEmpty()) {
			return false;
		}

		boolean hasMinLength = value.length() >= 8;
		boolean hasUpperCase = value.matches(".*[A-Z].*");
		boolean hasLowerCase = value.matches(".*[a-z].*");
		boolean hasDigit = value.matches(".*\\d.*");

		return hasMinLength && hasUpperCase && hasLowerCase && hasDigit;
	}
}

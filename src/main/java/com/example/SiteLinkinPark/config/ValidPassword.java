package com.example.SiteLinkinPark.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
	String message() default "Senha deve ter pelo menos 8 caracteres, incluindo uma letra maiúscula, uma letra minúscula e um número";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}

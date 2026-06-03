package com.example.SiteLinkinPark.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleValidationExceptions(MethodArgumentNotValidException ex,
			RedirectAttributes redirectAttributes) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage())
		);

		String errorMessage = errors.values().stream()
			.findFirst()
			.orElse("Erro de validação. Por favor, verifique os dados informados.");

		redirectAttributes.addFlashAttribute("erro", errorMessage);

		String referrer = ex.getBindingResult().getObjectName();
		if ("usuario".equals(referrer)) {
			return "redirect:/form_user";
		} else if ("playlist".equals(referrer)) {
			return "redirect:/musicas";
		}

		return "redirect:/";
	}

	@ExceptionHandler(RuntimeException.class)
	public String handleRuntimeException(RuntimeException ex,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("erro", ex.getMessage());
		return "redirect:/";
	}
}

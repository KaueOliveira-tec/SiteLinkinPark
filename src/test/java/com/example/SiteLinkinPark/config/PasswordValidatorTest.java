package com.example.SiteLinkinPark.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordValidatorTest {

    private final PasswordValidator validator = new PasswordValidator();

    @Test
    public void testSenhaValida() {
        assertTrue(validator.isValid("Abc123def", null), "Senha válida deve retornar true");
    }

    @Test
    public void testSenhaFracaMuitoCurta() {
        assertFalse(validator.isValid("Abc123", null), "Senha com 6 caracteres deve falhar");
    }

    @Test
    public void testSenhaFraSemMaiuscula() {
        assertFalse(validator.isValid("abc12345", null), "Senha sem maiúscula deve falhar");
    }

    @Test
    public void testSenhaFraSemMinuscula() {
        assertFalse(validator.isValid("ABC12345", null), "Senha sem minúscula deve falhar");
    }

    @Test
    public void testSenhaFraSemNumero() {
        assertFalse(validator.isValid("Abcdefgh", null), "Senha sem número deve falhar");
    }

    @Test
    public void testSenhaNull() {
        assertFalse(validator.isValid(null, null), "Senha null deve falhar");
    }

    @Test
    public void testSenhaVazia() {
        assertFalse(validator.isValid("", null), "Senha vazia deve falhar");
    }

    @Test
    public void testSenhaComEspacos() {
        assertTrue(validator.isValid("Abc12 345", null), "Senha com espaços é válida");
    }

    @Test
    public void testSenhaLonga() {
        assertTrue(validator.isValid("Abc123def456ghi789jkl", null), "Senha longa deve passar");
    }

    @Test
    public void testSenhaMinimumValid() {
        assertTrue(validator.isValid("Abcd1234", null), "Senha com exatamente 8 caracteres deve passar");
    }
}

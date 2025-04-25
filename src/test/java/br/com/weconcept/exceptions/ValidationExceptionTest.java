package br.com.weconcept.exceptions;

import org.junit.jupiter.api.Test;

import br.com.weconcept.business.exceptions.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String errorMessage = "Validation error occurred";
        ValidationException exception = new ValidationException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}

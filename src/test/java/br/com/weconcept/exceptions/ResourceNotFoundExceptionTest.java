package br.com.weconcept.exceptions;

import org.junit.jupiter.api.Test;

import br.com.weconcept.business.exceptions.ResourceNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        String errorMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
    }
}

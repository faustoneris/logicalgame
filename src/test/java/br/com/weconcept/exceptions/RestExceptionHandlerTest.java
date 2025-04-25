package br.com.weconcept.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import br.com.weconcept.business.exceptions.ErrorResponse;
import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.RestExceptionHandler;
import br.com.weconcept.business.exceptions.ValidationException;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestExceptionHandlerTest {

    @InjectMocks
    private RestExceptionHandler exceptionHandler;

    @Test
    void shouldHandleResourceNotFoundException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource/123");

        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        ResponseEntity<ErrorResponse> response =
            exceptionHandler.handleResourceNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().getError());
        assertEquals("Resource not found", response.getBody().getMessage());
        assertEquals("/api/resource/123", response.getBody().getPath());
    }

    @Test
    void shouldHandleValidationException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");

        ValidationException ex = new ValidationException("Invalid input");
        ResponseEntity<ErrorResponse> response =
            exceptionHandler.handleBusinessException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("BUSINESS_ERROR", response.getBody().getError());
        assertEquals("Invalid input", response.getBody().getMessage());
    }

    @Test
    void shouldHandleGenericException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/api/resource");

        Exception ex = new Exception("Unexpected error");
        ResponseEntity<ErrorResponse> response =
            exceptionHandler.handleGenericException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getError());
        assertEquals("Ocorreu um erro no servidor.", response.getBody().getMessage());
    }

    @Test
    void shouldMapFieldErrorToValidationErrorCorrectly() {
        FieldError fieldError = new FieldError("object", "field", "error message", false, null, null, "rejected value");
        RestExceptionHandler handler = new RestExceptionHandler();

        ErrorResponse.ValidationError validationError = handler.mapToValidationError(fieldError);

        assertEquals("field", validationError.getField());
        assertEquals("error message", validationError.getRejectedValue());
        assertEquals("rejected value", validationError.getMessage());
    }

    @Test
    void shouldHandleNullRejectedValueInValidationError() {
        FieldError fieldError = new FieldError("object", "field", "error message");
        RestExceptionHandler handler = new RestExceptionHandler();

        ErrorResponse.ValidationError validationError = handler.mapToValidationError(fieldError);

        assertNull(validationError.getRejectedValue());
    }
}

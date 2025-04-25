package br.com.weconcept.exceptions;

import org.junit.jupiter.api.Test;

import br.com.weconcept.business.exceptions.ErrorResponse;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void shouldCreateErrorResponseWithAllFields() {
        Instant timestamp = Instant.now();
        int status = 400;
        String error = "VALIDATION_ERROR";
        String message = "Validation failed";
        String path = "/api/resource";

        ErrorResponse response = ErrorResponse.ofError(timestamp, status, error, message, path);

        assertEquals(timestamp, response.getTimestamp());
        assertEquals(status, response.getStatus());
        assertEquals(error, response.getError());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertNull(response.getErrors());
    }

    @Test
    void shouldCreateErrorResponseWithValidationErrors() {
        Instant timestamp = Instant.now();
        int status = 400;
        String error = "VALIDATION_ERROR";
        String message = "Validation failed";
        String path = "/api/resource";

        List<ErrorResponse.ValidationError> validationErrors = List.of(
            new ErrorResponse.ValidationError("field1", "must not be null", null),
            new ErrorResponse.ValidationError("field2", "must be positive", "-1")
        );

        ErrorResponse response = ErrorResponse.ofErrorWithValidationError(
            timestamp, status, error, message, path, validationErrors);

        assertEquals(timestamp, response.getTimestamp());
        assertEquals(status, response.getStatus());
        assertEquals(error, response.getError());
        assertEquals(message, response.getMessage());
        assertEquals(path, response.getPath());
        assertEquals(2, response.getErrors().size());
        assertEquals("field1", response.getErrors().get(0).getField());
        assertEquals("must be positive", response.getErrors().get(1).getMessage());
    }

    @Test
    void validationErrorShouldContainCorrectData() {
        String field = "username";
        String message = "must not be empty";
        String rejectedValue = "";

        ErrorResponse.ValidationError error =
            new ErrorResponse.ValidationError(field, message, rejectedValue);

        assertEquals(field, error.getField());
        assertEquals(message, error.getMessage());
        assertEquals(rejectedValue, error.getRejectedValue());
    }
}

package br.com.weconcept.business.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<ValidationError> errors;

    ErrorResponse() {}

    ErrorResponse(Instant timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    ErrorResponse(Instant timestamp, int status, String error, String message, String path, List<ValidationError> errors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.errors = errors;
    }

    public static ErrorResponse ofError(Instant timestamp, int status, String error, String message, String path) {
        return new ErrorResponse(timestamp, status, error, message, path);
    }

    public static ErrorResponse ofErrorWithValidationError(Instant timestamp, int status, String error, String message, String path, List<ValidationError> errors) {
        return new ErrorResponse(timestamp, status, error, message, path, errors);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ValidationError {
        private String field;
        private String message;
        private String rejectedValue;
    }
}

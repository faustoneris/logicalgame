package br.com.weconcept.business.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.weconcept.business.exceptions.ErrorResponse;
import br.com.weconcept.business.exceptions.ResourceNotFoundException;
import br.com.weconcept.business.exceptions.ValidationException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleResourceNotFound(
                        ResourceNotFoundException ex, HttpServletRequest request) {

                return new ResponseEntity<>(
                                ErrorResponse.ofError(
                                                Instant.now(),
                                                HttpStatus.NOT_FOUND.value(),
                                                "RESOURCE_NOT_FOUND",
                                                ex.getMessage(),
                                                request.getRequestURI()),
                                HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(ValidationException.class)
        public ResponseEntity<ErrorResponse> handleBusinessException(
                        ValidationException ex, HttpServletRequest request) {

                return new ResponseEntity<>(
                                ErrorResponse.ofError(
                                                Instant.now(),
                                                HttpStatus.BAD_REQUEST.value(),
                                                "VALIDATION_ERROR",
                                                ex.getMessage(),
                                                request.getRequestURI()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationExceptions(
                        MethodArgumentNotValidException ex, HttpServletRequest request) {

                List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult().getFieldErrors()
                                .stream()
                                .map(this::mapToValidationError)
                                .collect(Collectors.toList());

                return new ResponseEntity<>(
                                ErrorResponse.ofErrorWithValidationError(Instant.now(), HttpStatus.BAD_REQUEST.value(),
                                                "VALIDATION_ERROR", "Preencha as informações corretamente.",
                                                request.getRequestURI(), validationErrors),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                        Exception ex, HttpServletRequest request) {

                return new ResponseEntity<>(
                                ErrorResponse.ofError(
                                                Instant.now(),
                                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                                ex.getMessage(),
                                                "Ocorreu um erro no servidor.",
                                                request.getRequestURI()),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        public ErrorResponse.ValidationError mapToValidationError(FieldError fieldError) {
                return new ErrorResponse.ValidationError(
                                fieldError.getField(),
                                fieldError.getDefaultMessage(),
                                fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString()
                                                : null);
        }
}

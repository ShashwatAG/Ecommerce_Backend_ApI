package com.playground.ecommerce.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiProblem> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.NOT_FOUND, "Resource not found.", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiProblem> handleBadRequest(RuntimeException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.BAD_REQUEST, "Bad request.", ex.getMessage(), request.getRequestURI(), null);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiProblem> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.UNAUTHORIZED, "Unauthorized.", "Invalid email or password.", request.getRequestURI(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiProblem> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.FORBIDDEN, "Forbidden.", "You do not have permission to access this resource.", request.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiProblem> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<String, String>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return buildProblem(HttpStatus.BAD_REQUEST, "Validation failed.", "One or more request fields are invalid.", request.getRequestURI(), errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiProblem> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error.", ex.getMessage(), request.getRequestURI(), null);
    }

    public static ApiProblem createProblem(HttpStatus status,
                                           String title,
                                           String detail,
                                           String instance,
                                           Map<String, String> errors) {
        ApiProblem problem = new ApiProblem();
        problem.setType("about:blank");
        problem.setTitle(title);
        problem.setStatus(status.value());
        problem.setDetail(detail);
        problem.setInstance(instance);
        problem.setTimestamp(OffsetDateTime.now());
        if (errors != null) {
            problem.setErrors(errors);
        }
        return problem;
    }

    private ResponseEntity<ApiProblem> buildProblem(HttpStatus status,
                                                    String title,
                                                    String detail,
                                                    String instance,
                                                    Map<String, String> errors) {
        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(createProblem(status, title, detail, instance, errors));
    }
}

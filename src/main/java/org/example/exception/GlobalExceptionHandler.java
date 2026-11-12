package org.example.exception;

import java.net.URI;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DuplicateCarException.class)
    public ProblemDetail handleDuplicateCar(DuplicateCarException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Resource Duplicate Conflict");
        problem.setType(URI.create("https://cars.ukma.edu/errors/duplicate"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(InvalidCarStateException.class)
    public ProblemDetail handleInvalidState(InvalidCarStateException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage());
        problem.setTitle("Invalid Business State");
        problem.setType(URI.create("https://cars.ukma.edu/errors/invalid-state"));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }

    @ExceptionHandler(CarNotFoundException.class)
    public ProblemDetail handleNotFound(CarNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Entity not found");
        pd.setType(URI.create("https://cars.ukma.edu.ua/errors/not-found"));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException ex,
            Locale locale) {

        Map<String, String> errors =
                ex.getBindingResult().getFieldErrors().stream()
                        .collect(Collectors.toMap(
                                FieldError::getField,
                                error -> error.getDefaultMessage() != null
                                        ? error.getDefaultMessage()
                                        : "Invalid value",
                                (first, second) -> first
                        ));

        String detail = messageSource.getMessage(
                "validation.error.message",
                null,
                "Data validation error",
                locale
        );

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, detail);
        problem.setTitle("Validation Failed");
        problem.setType(URI.create(
                "https://cars.ukma.edu.ua/errors/validation"));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler({IllegalArgumentException.class, InvalidCurrencyException.class})
    public ProblemDetail handleIllegalArgument(RuntimeException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Business rules error");
        pd.setType(URI.create("https://cars.ukma.edu.ua/errors/illegal-argument"));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMessageNotReadable(HttpMessageNotReadableException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, ex.getMessage());
        pd.setTitle("Accepted message not readable");
        pd.setType(URI.create("https://cars.ukma.edu.ua/errors/message-not-readable"));
        pd.setProperty("timestamp", Instant.now());
        return pd;
    }
}

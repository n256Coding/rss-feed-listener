package com.n256.rssfeedlistener.exception;

import com.n256.rssfeedlistener.dto.ErrorResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Manages custom error responses.
 */
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationFailedException.class)
    protected ResponseEntity<ErrorResponseDTO> handleValidationError(ValidationFailedException ex) {

        return ResponseEntity.status(400).body(ErrorResponseDTO.builder().message(ex.getMessage()).build());
    }
}

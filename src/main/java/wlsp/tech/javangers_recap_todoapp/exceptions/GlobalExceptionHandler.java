package wlsp.tech.javangers_recap_todoapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(TodoNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleTodoNotFoundException(TodoNotFoundException e) {
    ErrorResponse errResponse = new ErrorResponse(
            LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), "Resource Not Found", e.getMessage()
    );
    return new ResponseEntity<>(errResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            e.getMessage()
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}

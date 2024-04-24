package by.abakumova.weatherjava.error;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
@ControllerAdvice
public class ExceptionGlobal {

    private static final Logger LOGGER =
            LogManager.getLogger(ExceptionGlobal.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<Object> handleHttpClientErrorException(
            final HttpClientErrorException ex,
            final WebRequest request) {
        LOGGER.error("400 Bad Request");
        return ResponseEntity.status(
                HttpStatus.BAD_REQUEST).body("400 Bad Request");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotSupportedException(
            final HttpRequestMethodNotSupportedException ex,
            final WebRequest request) {
        LOGGER.error("405 Method Not Allowed");
        return ResponseEntity.status(
                HttpStatus.METHOD_NOT_ALLOWED).body("405 Method Not Allowed");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(
            final RuntimeException ex,
            final WebRequest request) {
        LOGGER.error("500 Internal Server Error");
        return ResponseEntity.status(
                HttpStatus.INTERNAL_SERVER_ERROR).body(
                "500 Internal Server Error");
    }
}
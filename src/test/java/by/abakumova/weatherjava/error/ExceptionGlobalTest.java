package by.abakumova.weatherjava.error;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExceptionGlobalTest {

    @Mock
    private WebRequest webRequest;

    @InjectMocks
    private ExceptionGlobal exceptionGlobal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHandleHttpClientErrorException() {
        // Create an HttpClientErrorException
        HttpClientErrorException ex = new HttpClientErrorException(HttpStatus.BAD_REQUEST);

        // Call the method to be tested
        ResponseEntity<Object> responseEntity = exceptionGlobal.handleHttpClientErrorException(ex, webRequest);

        // Verify that the method logs the error and returns a ResponseEntity with status 400 and body "400 Bad Request"
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("400 Bad Request", responseEntity.getBody());
    }

    @Test
    void testHandleMethodNotSupportedException() {
        // Create an HttpRequestMethodNotSupportedException
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("Method not supported");

        // Call the method to be tested
        ResponseEntity<Object> responseEntity = exceptionGlobal.handleMethodNotSupportedException(ex, webRequest);

        // Verify that the method logs the error and returns a ResponseEntity with status 405 and body "405 Method Not Allowed"
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, responseEntity.getStatusCode());
        assertEquals("405 Method Not Allowed", responseEntity.getBody());
    }

    @Test
    void testHandleRuntimeException() {
        // Create a RuntimeException
        RuntimeException ex = new RuntimeException("Internal Server Error");

        // Call the method to be tested
        ResponseEntity<Object> responseEntity = exceptionGlobal.handleRuntimeException(ex, webRequest);

        // Verify that the method logs the error and returns a ResponseEntity with status 500 and body "500 Internal Server Error"
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("500 Internal Server Error", responseEntity.getBody());
    }
}

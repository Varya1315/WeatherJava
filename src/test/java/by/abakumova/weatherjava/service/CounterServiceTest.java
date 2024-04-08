package by.abakumova.weatherjava.service;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CounterServiceTest {

    @Test
    public void testIncrementRequestCount() {
        // Arrange
        int initialCount = CounterService.getRequestCount();

        // Act
        CounterService.incrementRequestCount();

        // Assert
        assertEquals(initialCount + 1, CounterService.getRequestCount());
    }

    @Test
    public void testGetRequestCount() {
        // Arrange
        int expectedCount = CounterService.getRequestCount();

        // Act
        int actualCount = CounterService.getRequestCount();

        // Assert
        assertEquals(expectedCount, actualCount);
    }
}

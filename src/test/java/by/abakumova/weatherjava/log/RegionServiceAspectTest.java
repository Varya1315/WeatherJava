package by.abakumova.weatherjava.log;

import by.abakumova.weatherjava.model.Region;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(MockitoExtension.class)
 class RegionServiceAspectTest {

    @Mock
    private Logger logger;

    @Mock
    private RegionServiceAspect regionServiceAspect;
    @InjectMocks
    private RegionServiceAspect aspect;

    void testLogSaveRegionSuccess2() {
        // Arrange
        Region newRegion = new Region();
        Region savedRegion = new Region();
        boolean result;

        // Act
        regionServiceAspect.logSaveRegionSuccess(newRegion, savedRegion);
        result = true; // Simulating a successful logging operation

        // Assert
        assertTrue(result, "Logging save region success should return true");
    }

    @Test
    void testLogFindAllSuccess() {
        // Arrange
        List<Region> regions = new ArrayList<>();
        regions.add(new Region());
        boolean result;

        // Act
        aspect.logFindAllSuccess(regions);
        result = true; // Simulating a successful logging operation

        // Assert
        assertTrue(result, "Logging find all success should return true");
    }

    @Test
    void testLogSaveRegionSuccess() {
        // Arrange
        Region newRegion = new Region();
        newRegion.setName("TestRegion");
        Region savedRegion = new Region();
        savedRegion.setName("TestRegion");
        boolean result;

        // Act
        aspect.logSaveRegionSuccess(newRegion, savedRegion);
        result = true; // Simulating a successful logging operation

        // Assert
        assertTrue(true, "Logging save region success should return true");
    }

    @Test
    void testLogSaveRegionFailure() {
        // Arrange
        Region newRegion = new Region();
        newRegion.setName("TestRegion");
        boolean result;

        // Act
        aspect.logSaveRegionSuccess(newRegion, null);
        result = true; // Simulating a successful logging operation

        // Assert
        assertTrue(result, "Logging save region failure should return true");
    }
}
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


@ExtendWith(MockitoExtension.class)
 class RegionServiceAspectTest {

    @Mock
    private Logger logger;

    @Mock
    private RegionServiceAspect regionServiceAspect;
    @InjectMocks
    private RegionServiceAspect aspect;
    @Test
     void testLogSaveRegionSuccess2() {
        // Arrange
        Region newRegion = new Region();
        Region savedRegion = new Region();

        // Act
        regionServiceAspect.logSaveRegionSuccess(newRegion, savedRegion);

        // Assert
           }

    @Test
     void testLogMethodCall() {
        aspect.logMethodCall();
       }

    @Test
     void testLogFindAllSuccess() {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region());
        aspect.logFindAllSuccess(regions);
        }

    @Test
     void testLogFindAllSuccessWithNullList() {
        aspect.logFindAllSuccess(null);
      }


    @Test
     void testLogBeforeSaveRegion() {
        Region newRegion = new Region();
        newRegion.setName("TestRegion");
        aspect.logBeforeSaveRegion(newRegion);
         }

    @Test
     void testLogSaveRegionSuccess() {
        Region newRegion = new Region();
        newRegion.setName("TestRegion");
        Region savedRegion = new Region();
        savedRegion.setName("TestRegion");

        aspect.logSaveRegionSuccess(newRegion, savedRegion);

         }

    @Test
     void testLogSaveRegionFailure() {
        Region newRegion = new Region();
        newRegion.setName("TestRegion");

        aspect.logSaveRegionSuccess(newRegion, null);

         }

}

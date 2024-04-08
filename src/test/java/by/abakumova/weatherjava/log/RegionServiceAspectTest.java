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
public class RegionServiceAspectTest {

    @Mock
    private Logger logger;

    @Mock
    private RegionServiceAspect regionServiceAspect;
    @InjectMocks
    private RegionServiceAspect aspect;
    @Test
    public void testLogSaveRegionSuccess2() {
        // Arrange
        Region newRegion = new Region();
        Region savedRegion = new Region();

        // Act
        regionServiceAspect.logSaveRegionSuccess(newRegion, savedRegion);

        // Assert
           }

    @Test
    public void testLogMethodCall() {
        aspect.logMethodCall();
       }

    @Test
    public void testLogFindAllSuccess() {
        List<Region> regions = new ArrayList<>();
        regions.add(new Region());
        aspect.logFindAllSuccess(regions);
        }

    @Test
    public void testLogFindAllSuccessWithNullList() {
        aspect.logFindAllSuccess(null);
      }


    @Test
    public void testLogBeforeSaveRegion() {
        Region newRegion = new Region();
        newRegion.setName("TestRegion");
        aspect.logBeforeSaveRegion(newRegion);
         }

    @Test
    public void testLogSaveRegionSuccess() {
        Region newRegion = new Region();
        newRegion.setName("TestRegion");
        Region savedRegion = new Region();
        savedRegion.setName("TestRegion");

        aspect.logSaveRegionSuccess(newRegion, savedRegion);

         }

    @Test
    public void testLogSaveRegionFailure() {
        Region newRegion = new Region();
        newRegion.setName("TestRegion");

        aspect.logSaveRegionSuccess(newRegion, null);

         }

    @Test
    public void testLogSaveRegionError() {
        // Arrange
        Region newRegion = new Region();
        Region savedRegion = new Region(); // Вот изменения здесь

        // Act
        regionServiceAspect.logSaveRegionSuccess(newRegion, savedRegion);

        // Assert
      }
}

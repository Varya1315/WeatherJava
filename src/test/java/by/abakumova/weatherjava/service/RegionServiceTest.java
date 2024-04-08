package by.abakumova.weatherjava.service;

import by.abakumova.weatherjava.cache.Cache;
import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.repository.RegionRepository;
import by.abakumova.weatherjava.repository.TownRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {


    @Mock
    private Cache<String, Region> regionCache;

    @Mock
    private RegionRepository repository;

    @Mock
    private TownRepository repositoryT;

    @Mock
    private Logger LOG;

    @InjectMocks
    private RegionService regionService;


    @Test
    void testFindTownsByRegionAndInterestingFact() {
        // Arrange
        String regionName = "TestRegion";
        String interestingFact = "Interesting fact";
        List<Towns> expectedTowns = new ArrayList<>();
        expectedTowns.add(new Towns());

        // Регион не найден в кэше
        when(regionCache.get(regionName)).thenReturn(null);
        // Регион из репозитория
        Region regionFromRepository = new Region();
        regionFromRepository.setTowns(expectedTowns);
        regionFromRepository.setName(regionName);
        when(repository.findByName(regionName)).thenReturn(regionFromRepository);
        // Города из репозитория
        when(repository.findTownsByRegionAndInterestingFact(regionName, interestingFact)).thenReturn(expectedTowns);

        // Act
        List<Towns> result = regionService.findTownsByRegionAndInterestingFact(regionName, interestingFact);

        // Assert
        assertEquals(expectedTowns, result);

        // Verify interactions
        verify(regionCache, times(1)).get(regionName);
        verify(repository, times(1)).findByName(regionName);
        verify(regionCache, times(1)).put(regionName, regionFromRepository);
        verify(repository, times(1)).findTownsByRegionAndInterestingFact(regionName, interestingFact);
    }


    @Test
    void testUpdateRegionByName_ExistingRegionInCache() {
        // Arrange
        String name = "TestRegion";
        String newName = "NewTestRegion";

        // Создаем заглушку для существующего региона в кэше
        Region existingRegionInCache = new Region();
        existingRegionInCache.setName(name);
        when(regionCache.get(name)).thenReturn(existingRegionInCache);

        // Act
        Region updatedRegion = regionService.updateRegionByName(name, newName);

        // Assert
        assertNotNull(updatedRegion);
        assertEquals(newName, updatedRegion.getName());
        verify(repository, times(1)).save(updatedRegion);
        verify(regionCache, times(1)).put(newName, updatedRegion);
    }

    @Test
    void testUpdateRegionByName_ExistingRegionInRepository() {
        // Arrange
        String name = "TestRegion";
        String newName = "NewTestRegion";

        // Создаем заглушку для существующего региона в репозитории
        Region existingRegionInRepository = new Region();
        existingRegionInRepository.setName(name);
        when(regionCache.get(name)).thenReturn(null);
        when(repository.findByName(name)).thenReturn(existingRegionInRepository);

        // Act
        Region updatedRegion = regionService.updateRegionByName(name, newName);

        // Assert
        assertNotNull(updatedRegion);
        assertEquals(newName, updatedRegion.getName());
        verify(regionCache, times(1)).put(newName, updatedRegion);
    }

    @Test
    void testUpdateRegionByName_NonExistingRegion() {
        // Arrange
        String name = "NonExistingRegion";
        String newName = "NewTestRegion";

        // Создаем заглушку для несуществующего региона
        when(regionCache.get(name)).thenReturn(null);
        when(repository.findByName(name)).thenReturn(null);

        // Act
        Region updatedRegion = regionService.updateRegionByName(name, newName);

        // Assert
        assertNull(updatedRegion);
        verify(regionCache, never()).put(any(), any());
    }

    @Test
    void testFindByNameRegion_RegionInCache1() {
        // Arrange
        String regionName = "TestRegion";
        Region regionInCache = new Region();
        regionInCache.setName(regionName);

        // Stubbing
        when(regionCache.get(regionName)).thenReturn(regionInCache);

        // Act
        Region result = regionService.findByNameRegion(regionName);

        // Assert
        verify(regionCache, times(1)).get(regionName);
        verify(repository, never()).findByName(anyString());
        assertEquals(regionInCache, result);
    }
    @Test
    void testFindByNameRegion_RegionInDatabase() {
        // Arrange
        String regionName = "TestRegion";
        Region regionInDatabase = new Region();
        regionInDatabase.setName(regionName);

        // Stubbing
        when(regionCache.get(regionName)).thenReturn(null);
        when(repository.findByName(regionName)).thenReturn(regionInDatabase);

        // Act
        Region result = regionService.findByNameRegion(regionName);

        // Assert
        verify(regionCache, times(1)).get(regionName);
        verify(repository, times(1)).findByName(regionName);
        verify(regionCache, times(1)).put(regionName, regionInDatabase);
        assertEquals(regionInDatabase, result);
    }

    @Test
    void testFindByNameRegion_RegionNotFound() {
        // Arrange
        String regionName = "NonExistentRegion";

        // Stubbing
        when(regionCache.get(regionName)).thenReturn(null);
        when(repository.findByName(regionName)).thenReturn(null);

        // Act
        Region result = regionService.findByNameRegion(regionName);

        // Assert
        verify(regionCache, times(1)).get(regionName);
        verify(repository, times(1)).findByName(regionName);
        assertNull(null);
    }
    @Test
    void testDeleteRegionByName_RegionInCache() {
        // Arrange
        String regionName = "TestRegion";
        Region regionInCache = new Region();
        regionInCache.setName(regionName);

        // Stubbing
        when(regionCache.get(regionName)).thenReturn(regionInCache);

        // Act
        String result = regionService.deleteRegionByName(regionName);

        // Assert
        verify(regionCache, times(1)).get(regionName);
        verify(repository, never()).findByName(anyString());
        verify(repository, times(1)).delete(regionInCache);
        verify(regionCache, times(1)).remove(regionName);
        assertEquals("Delete", result);
    }
    @Test
    void testDeleteRegionByName_RegionInDatabase() {
        // Arrange
        String regionName = "TestRegion";
        Region regionInDatabase = new Region();
        regionInDatabase.setName(regionName);

        // Stubbing
        when(regionCache.get(regionName)).thenReturn(null);
        when(repository.findByName(regionName)).thenReturn(regionInDatabase);

        // Act
        String result = regionService.deleteRegionByName(regionName);

        // Assert
        verify(regionCache, times(1)).get(regionName);
        verify(repository, times(1)).findByName(regionName);
        verify(repositoryT, times(1)).deleteAll(regionInDatabase.getTowns());
        verify(repository, times(1)).delete(regionInDatabase);
        assertEquals("Delete", result);
    }



    @Test
    void testDeleteRegionByName_RegionNotFound() {
        // Arrange
        String name = "NonExistingRegion";

        // Создаем заглушку для несуществующего региона
        when(regionCache.get(name)).thenReturn(null);
        when(repository.findByName(name)).thenReturn(null);

        // Act
        String result = regionService.deleteRegionByName(name);

        // Assert
        assertEquals("Region not found", result);
        verify(repository, never()).deleteAll(any());
        verify(repository, never()).delete(any());
        verify(regionCache, never()).remove(any());
    }

    @Test
     void testSaveRegion_NewRegion_Success() {
        // Arrange
        Region newRegion = new Region();
        newRegion.setName("Test Region");
        List<Towns> towns = new ArrayList<>();
        Towns town1 = new Towns();
        town1.setNameTowns("Town1");
        Towns town2 = new Towns();
        town2.setNameTowns("Town2");
        towns.add(town1);
        towns.add(town2);
        newRegion.setTowns(towns);

        // Stubbing repository methods
        when(repository.findByName(anyString())).thenReturn(null);
        when(repository.save(any(Region.class))).thenReturn(newRegion);

        // Act
        Region savedRegion = regionService.saveRegion(newRegion);

        // Assert
        assertNotNull(savedRegion);
        assertEquals("Test Region", savedRegion.getName());
        assertEquals(2, savedRegion.getTowns().size());
        verify(repository, times(1)).findByName(anyString());
         }

    @Test
     void testSaveRegion_ExistingRegion_Failure() {
        // Arrange
       Region existingRegion = new Region();
        existingRegion.setName("Test Region");
        when(repository.findByName(anyString())).thenReturn(existingRegion);
        Region newRegion = new Region();
        newRegion.setName("Test Region");

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> regionService.saveRegion(newRegion));
        verify(repository, times(1)).findByName(anyString());
        verify(repository, never()).save(any(Region.class));
        verify(regionCache, never()).put(anyString(), any(Region.class));
    }


    @Test
     void testSaveRegions_Success() {
        // Arrange
        Region region1 = new Region();
        region1.setName("Region1");
        List<Towns> towns1 = new ArrayList<>();
        Towns town1 = new Towns();
        town1.setNameTowns("Town1");
        towns1.add(town1);
        region1.setTowns(towns1);

        Region region2 = new Region();
        region2.setName("Region2");
        List<Towns> towns2 = new ArrayList<>();
        Towns town2 = new Towns();
        town2.setNameTowns("Town2");
        towns2.add(town2);
        region2.setTowns(towns2);

        List<Region> regions = List.of(region1, region2);

        // Stubbing repository method
        when(repository.save(any(Region.class))).thenReturn(region1).thenReturn(region2);

        // Act
        List<Region> savedRegions = regionService.saveRegions(regions);

        // Assert
        assertNotNull(savedRegions);
        assertEquals(2, savedRegions.size());
        assertEquals("Region1", savedRegions.get(0).getName());
        assertEquals("Region2", savedRegions.get(1).getName());
        verify(repository, times(2)).save(any(Region.class));
        verify(regionCache, times(2)).put(anyString(), any(Region.class));
    }

    @Test
     void testSaveRegions_InvalidRegionName_ExceptionThrown() {
        // ArrangeMockitoAnnotations.initMocks(this);
        Region region = new Region();
        region.setName("va");
        List<Region> regions = List.of(region);

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> regionService.saveRegions(regions));
        verify(repository, never()).save(any(Region.class));
        verify(regionCache, never()).put(anyString(), any(Region.class));
    }
    @Test
     void testFindAll() {
        // Arrange
        List<Region> expectedRegions = new ArrayList<>();
        Region region1 = new Region();
        region1.setName("Region1");
        Region region2 = new Region();
        region2.setName("Region2");
        expectedRegions.add(region1);
        expectedRegions.add(region2);

        // Stubbing repository method
        when(repository.findAll()).thenReturn(expectedRegions);

        // Act
        List<Region> actualRegions = regionService.findAll();

        // Assert
        assertEquals(expectedRegions.size(), actualRegions.size());
        for (int i = 0; i < expectedRegions.size(); i++) {
            assertEquals(expectedRegions.get(i).getName(), actualRegions.get(i).getName());
        }
        verify(repository, times(1)).findAll();
        verify(regionCache, times(2)).put(anyString(), any(Region.class)); // Assuming there are 2 regions in the list
     }
}
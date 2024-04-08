package by.abakumova.weatherjava.service;

import by.abakumova.weatherjava.cache.Cache;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.repository.TownRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

 class TownServiceTest {

    @Mock
    private Cache<String, Towns> townCache;

    @Mock
    private TownRepository repository;

    @InjectMocks
    private TownService service;
     @Mock
     private Logger logger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testFindAllTowns() {
        // Arrange
        List<Towns> expectedTowns = new ArrayList<>();
        expectedTowns.add(new Towns());
        expectedTowns.add(new Towns());
        when(repository.findAll()).thenReturn(expectedTowns);

        // Act
        List<Towns> result = service.findAllTowns();

        // Assert
        assertEquals(expectedTowns, result);
        verify(repository, times(1)).findAll(); // Ensure repository.findAll() was called once
    }

    @Test
    void testSaveTowns() {
        // Arrange
        Towns townsToSave = new Towns();
        townsToSave.setNameTowns("TownName");
        when(repository.save(townsToSave)).thenReturn(townsToSave); // Mocking the repository save method to return the same instance

        // Act
        Towns savedTown = service.saveTowns(townsToSave);

        // Assert
        verify(townCache, times(1)).put(townsToSave.getNameTowns(), townsToSave); // Verifying if townCache put method is called once with correct arguments
        verify(repository, times(1)).save(townsToSave); // Verifying if repository save method is called once with correct argument
        assertEquals(townsToSave, savedTown); // Verifying if the returned Towns object is the same as the one passed to the method
    }
    @Test
    void testDeleteTownsByNameTowns_ExistingTown() {
        // Arrange
        String townName = "TownName";
        Towns townToDelete = new Towns();
        townToDelete.setNameTowns(townName);
        when(townCache.get(townName)).thenReturn(townToDelete); // Mocking cache to return townToDelete
        when(repository.findByNameTowns(townName)).thenReturn(townToDelete); // Mocking repository to return townToDelete

        // Act
        String result = service.deleteTownsByNameTowns(townName);

        // Assert
        verify(townCache, times(1)).remove(townName); // Verifying if townCache remove method is called once with correct argument
        verify(repository, times(1)).delete(townToDelete); // Verifying if repository delete method is called once with correct argument
        assertEquals("Delete", result); // Verifying if the correct message is returned
    }

    @Test
    void testDeleteTownsByNameTowns_NonExistingTown() {
        // Arrange
        String townName = "NonExistingTown";
        when(townCache.get(townName)).thenReturn(null); // Mocking cache to return null
        when(repository.findByNameTowns(townName)).thenReturn(null); // Mocking repository to return null

        // Act
        String result = service.deleteTownsByNameTowns(townName);

        // Assert
        verify(townCache, never()).remove(townName); // Verifying if townCache remove method is never called
        verify(repository, never()).delete(any(Towns.class)); // Verifying if repository delete method is never called
        assertNull(result); // Verifying if the result is null
    }
    @Test
    void testFindByNameTowns_TownInCache() {
        // Arrange
        String townName = "ExistingTown";
        Towns cachedTown = new Towns();
        when(townCache.get(townName)).thenReturn(cachedTown); // Mocking cache to return cachedTown

        // Act
        Towns result = service.findByNameTowns(townName);

        // Assert
        verify(repository, never()).findByNameTowns(anyString()); // Verifying if repository method is never called
        assertEquals(cachedTown, result); // Verifying if the correct town is returned
    }

    @Test
    void testFindByNameTowns_TownInDatabase() {
        // Arrange
        String townName = "ExistingTown";
        Towns townFromDatabase = new Towns();
        when(townCache.get(townName)).thenReturn(null); // Mocking cache to return null
        when(repository.findByNameTowns(townName)).thenReturn(townFromDatabase); // Mocking repository to return townFromDatabase

        // Act
        Towns result = service.findByNameTowns(townName);

        // Assert
        verify(repository, times(1)).findByNameTowns(townName); // Verifying if repository method is called once
        verify(townCache, times(1)).put(townName, townFromDatabase); // Verifying if townCache put method is called once with correct arguments
        assertEquals(townFromDatabase, result); // Verifying if the correct town is returned
    }

    @Test
    void testFindByNameTowns_TownNotInDatabase() {
        // Arrange
        String townName = "NonExistingTown";
        when(townCache.get(townName)).thenReturn(null); // Mocking cache to return null
        when(repository.findByNameTowns(townName)).thenReturn(null); // Mocking repository to return null

        // Act
        Towns result = service.findByNameTowns(townName);

        // Assert
        verify(repository, times(1)).findByNameTowns(townName); // Verifying if repository method is called once
        verify(townCache, never()).put(anyString(), any(Towns.class)); // Verifying if townCache put method is never called
        assertNull(result); // Verifying if null is returned
    }
    @Test
    void testUpdateTownByName_TownExistsInCache() {
        // Arrange
        String townName = "ExistingTown";
        String newCoordinates = "NewCoordinates";
        Towns existingTown = new Towns();
        existingTown.setNameTowns(townName);
        existingTown.setCoordinates("OldCoordinates");
        when(townCache.get(townName)).thenReturn(existingTown); // Mocking cache to return existingTown
        when(repository.save(existingTown)).thenReturn(existingTown); // Mocking repository to return existingTown

        // Act
        Towns result = service.updateTownByName(townName, newCoordinates);

        // Assert
        verify(townCache, times(1)).put(townName, existingTown); // Verifying if townCache put method is called once with correct arguments
        verify(repository, times(1)).save(existingTown); // Verifying if repository method is called once with correct arguments
        assertEquals(newCoordinates, existingTown.getCoordinates()); // Verifying if town's coordinates are updated correctly
        assertEquals(existingTown, result); // Verifying if the correct town is returned
    }

    @Test
    void testUpdateTownByName_TownDoesNotExist() {
        // Arrange
        String townName = "NonExistingTown";
        String newCoordinates = "NewCoordinates";
        when(townCache.get(townName)).thenReturn(null); // Mocking cache to return null
        when(repository.findByNameTowns(townName)).thenReturn(null); // Mocking repository to return null

        // Act
        Towns result = service.updateTownByName(townName, newCoordinates);

        // Assert
        verify(townCache, never()).put(anyString(), any(Towns.class)); // Verifying if townCache put method is never called
        verify(repository, never()).save(any(Towns.class)); // Verifying if repository save method is never called
        assertNull(result); // Verifying if null is returned
    }

}

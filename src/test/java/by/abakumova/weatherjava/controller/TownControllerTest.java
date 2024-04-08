package by.abakumova.weatherjava.controller;

import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.service.TownService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

 class TownControllerTest {

    @Mock
    private TownService townService;

    @InjectMocks
    private TownController townController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAllTowns() {
        List<Towns> towns = new ArrayList<>();
        towns.add(new Towns());
        towns.add(new Towns());

        when(townService.findAllTowns()).thenReturn(towns);

        assertEquals(towns, townController.findAllTowns());
    }

    @Test
    void saveTowns() {
        Towns town = new Towns();
        when(townService.saveTowns(town)).thenReturn(town);

        assertEquals(town, townController.saveTowns(town));
    }

    @Test
    void findByNameTowns() {
        Towns town = new Towns();
        when(townService.findByNameTowns("Test Town")).thenReturn(town);

        assertEquals(town, townController.findByNameTowns("Test Town"));
    }

    @Test
    void deleteRegionByName() {
        when(townService.deleteTownsByNameTowns("Test Town")).thenReturn("Delete");
        ResponseEntity<String> response = townController.deleteRegionByName("Test Town");
        assertEquals("Delete", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        when(townService.deleteTownsByNameTowns("Unknown Town")).thenReturn(null);
        response = townController.deleteRegionByName("Unknown Town");
        assertEquals("Region not found", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateTownByName() {
        Towns updatedTown = new Towns();
        when(townService.updateTownByName("Test Town", "Updated Coordinates")).thenReturn(updatedTown);

        assertEquals(updatedTown, townController.updateTownByName("Test Town", "Updated Coordinates"));
    }
}

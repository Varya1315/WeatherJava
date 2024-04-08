package by.abakumova.weatherjava.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.service.RegionService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

 class RegionControllerTest {

    @Mock
    private RegionService regionService;

    @InjectMocks
    private RegionController regionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void findAllRegion() {
//        List<Region> regions = new ArrayList<>();
//        regions.add(new Region());
//        regions.add(new Region());
//
//        when(regionService.findAll()).thenReturn(regions);
//
//        assertEquals(regions, regionController.findAllRegion());
//    }

//    @Test
//    void saveRegion() {
//        Region region = new Region();
//        when(regionService.saveRegion(region)).thenReturn(region);
//
//        assertEquals(region, regionController.saveRegion(region));
//    }

    @Test
    void findByNameRegion() {
        Region region = new Region();
        when(regionService.findByNameRegion("Test Region")).thenReturn(region);

        assertEquals(region, regionController.findByNameRegion("Test Region"));
    }

    @Test
    void deleteRegionByName() {
        when(regionService.deleteRegionByName("Test Region")).thenReturn("Delete");
        ResponseEntity<String> response = regionController.deleteRegionByName("Test Region");
        assertEquals("Delete", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        when(regionService.deleteRegionByName("Unknown Region")).thenReturn(null);
        response = regionController.deleteRegionByName("Unknown Region");
        assertEquals("Region not found", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateRegionByName() {
        Region updatedRegion = new Region();
        when(regionService.updateRegionByName("Test Region", "Updated Region")).thenReturn(updatedRegion);

        ResponseEntity<String> response = regionController.updateRegionByName("Test Region", "Updated Region");
        assertEquals("Updated region: ", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        when(regionService.updateRegionByName("Unknown Region", "Updated Region")).thenReturn(null);
        response = regionController.updateRegionByName("Unknown Region", "Updated Region");
        assertEquals("Region not found", response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void getTownsWithFact() {
        List<Towns> towns = new ArrayList<>();
        towns.add(new Towns());
        towns.add(new Towns());

        when(regionService.findTownsByRegionAndInterestingFact("Region", "Fact")).thenReturn(towns);

        ResponseEntity<List<Towns>> response = regionController.getTownsWithFact("Region", "Fact");
        assertEquals(towns, response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());

        when(regionService.findTownsByRegionAndInterestingFact("Unknown Region", "Fact")).thenReturn(new ArrayList<>());
        response = regionController.getTownsWithFact("Unknown Region", "Fact");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    }
package by.abakumova.weatherjava.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class TownsTest {

    private Towns town;

    @BeforeEach
     void setUp() {
        town = new Towns();
    }

    @Test
     void testIdGetterAndSetter() {
        Long id = 1L;
        town.setId(id);
        assertEquals(id, town.getId());
    }

    @Test
     void testCoordinatesGetterAndSetter() {
        String coordinates = "51.5074° N, 0.1278° W";
        town.setCoordinates(coordinates);
        assertEquals(coordinates, town.getCoordinates());
    }

    @Test
     void testNameTownsGetterAndSetter() {
        String nameTowns = "London";
        town.setNameTowns(nameTowns);
        assertEquals(nameTowns, town.getNameTowns());
    }

    @Test
     void testTimeGetterAndSetter() {
        int time = 3600; // 1 hour in seconds
        town.setTime(time);
        assertEquals(time, town.getTime());
    }

    @Test
     void testPositionSunGetterAndSetter() {
        String positionSun = "East";
        town.setPositionSun(positionSun);
        assertEquals(positionSun, town.getPositionSun());
    }

    @Test
     void testInterestingFactGetterAndSetter() {
        String interestingFact = "London is the capital of England.";
        town.setInterestingFact(interestingFact);
        assertEquals(interestingFact, town.getInterestingFact());
    }

    @Test
     void testNoArgsConstructor() {
        assertNotNull(town);
    }

    @Test
     void testAllArgsConstructor() {
        Long id = 1L;
        String coordinates = "40.7128° N, 74.0060° W";
        String nameTowns = "New York";
        int time = 3;
        String positionSun = "West";
        String interestingFact = "New York City is the largest city in the United States.";

        Towns town = new Towns(id, coordinates, nameTowns, time, positionSun, interestingFact);

        // Проверяем, что все параметры конструктора установлены корректно
        assertEquals(id, town.getId());
        assertEquals(coordinates, town.getCoordinates());
        assertEquals(nameTowns, town.getNameTowns());
        assertEquals(time, town.getTime());
        assertEquals(positionSun, town.getPositionSun());
        assertEquals(interestingFact, town.getInterestingFact());
    }




    @Test
     void testGetName() {
        String name = "Test Town";
        town.setNameTowns(name);
        assertEquals(name, town.getNameTowns());
    }
}

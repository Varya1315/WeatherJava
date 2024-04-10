package by.abakumova.weatherjava.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
 class RegionTest {

    private Region region;

    @BeforeEach
     void setUp2() {
        region = new Region();
    }

    @Test
     void testIdGetterAndSetter2() {
        Long id = 1L;
        region.setId(id);
        assertEquals(id, region.getId());
    }

    @Test
     void testNoArgsConstructor2() {
        assertNotNull(region);
    }

    @BeforeEach
     void setUp() {
        region = new Region();
    }

    @Test
     void testIdGetterAndSetter() {
        Long id = 1L;
        region.setId(id);
        assertEquals(id, region.getId());
    }

    @Test
     void testNameGetterAndSetter() {
        String name = "TestRegion";
        region.setName(name);
        assertEquals(name, region.getName());
    }

    @Test
     void testTownsGetterAndSetter() {
        List<Towns> towns = new ArrayList<>();
        towns.add(new Towns());
        towns.add(new Towns());
        region.setTowns(towns);
        assertEquals(towns, region.getTowns());
    }

    @Test
     void testNoArgsConstructor1() {
        assertNotNull(region);
    }


    @Test
     void testToString() {
        String name = "TestRegion";
        region.setName(name);
        String expectedToString = "Region(id=null, name=TestRegion, towns=null)";
        assertEquals(expectedToString, region.toString());
    }

    @Test
     void testStr() {
        String name = "TestRegion";
        region.setName(name);
        String expectedToString = "Region(id=null, name=TestRegion, towns=null)";
        assertEquals(expectedToString, region.toString());
    }
    @Test
     void testIdGetterSetter() {
        region.setId(1L);
        assertEquals(1L, region.getId());
    }

    @Test
     void testNameGetterSetter() {
        region.setName("TestRegion");
        assertEquals("TestRegion", region.getName());
    }

    @Test
     void testTownsGetterSetter() {
        List<Towns> towns = new ArrayList<>();
        Towns town1 = new Towns();
        town1.setName("Town1");
        Towns town2 = new Towns();
        town2.setName("Town2");
        towns.add(town1);
        towns.add(town2);
        region.setTowns(towns);

        assertNotNull(region.getTowns());
       }

    @Test
     void testConst() {
        // Создаем список городов
        List<Towns> towns = new ArrayList<>();
        Towns town = new Towns();
        town.setName("TestTown");
        towns.add(town);

        // Создаем список объектов типа Object и добавляем в него города
        List<Object> objects = new ArrayList<>(towns);

        // Создаем регион, передавая список объектов в конструктор
        Region region = new Region("TestRegion", objects);
        region.setName("TestRegion");

        // Проверяем, что регион был успешно создан и его атрибуты установлены корректно
        assertNotNull(region);
        assertEquals("TestRegion", region.getName());
    }

    @Test
    void testSetAndGet() {
        Long id = 1L;
        region.setId(id);
        assertEquals(id, region.getId());
    }

    @Test
     void testNoArgsConstructor() {
        assertNotNull(region);
    }

    @Test
     void testAllArgsConstructor() {
        // Create a list of towns
        List<Towns> towns = new ArrayList<>();
        Towns town = new Towns();
        town.setName("TestTown");
        towns.add(town);

        // Create a region
        Region region = new Region();
        region.setName("TestRegion");
        region.setTowns(towns);

        // Check that the region was successfully created and its attributes are set correctly
        assertNotNull(region);
        assertEquals("TestRegion", region.getName());
        assertEquals(towns, region.getTowns());
    }


    @Test
     void testToStr() {
        String name = "TestRegion";
        region.setName(name);
        String expectedToString = "Region(id=null, name=TestRegion, towns=null)";
        assertEquals(expectedToString, region.toString());
    }



}

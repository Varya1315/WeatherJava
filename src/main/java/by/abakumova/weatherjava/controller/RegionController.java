package by.abakumova.weatherjava.controller;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.service.RegionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Контроллер для работы с регионами.
 */
@RestController
@RequestMapping("/api/v2/region")
@AllArgsConstructor
public class RegionController {


    /**
     * Logger for logging messages related to the RegionController class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(
            RegionController.class);

    /**
     * Service for managing region-related operations.
     */
    private final RegionService service;

    /**
     * Получить все регионы.
     *
     * @return Список всех регионов.
     */
    @GetMapping()
    public List<Region> findAllRegion() {
        return service.findAll();
    }

    /**
     * Сохранить регион.
     *
     * @param region Регион для сохранения.
     * @return Сохраненный регион.
     */
    @PostMapping("/saveRegion")
    public Region saveRegion(
            @RequestBody final Region region) {
        return service.saveRegion(region);
    }
    /**
     * Сохраняет список регионов.
     *
     * @param regions Список регионов для сохранения.
     * @return Список сохраненных регионов.
     */
    @PostMapping("/saveRegions")
    public List<Region> saveRegions(
            @RequestBody final List<Region> regions) {
        return service.saveRegions(regions);
    }

    /**
     * Найти регион по имени.
     *
     * @param name Имя региона.
     * @return Найденный регион.
     */
    @GetMapping("findName")
    public Region findByNameRegion(
            @RequestParam final String name) {
        return service.findByNameRegion(name);
    }
    /**
     * Удалить регион по имени.
     *
     * @param name Имя региона.
     * @return Сообщение об успешном удалении или об ошибке.
     */
    @DeleteMapping("deleteByName")
    public ResponseEntity<String> deleteRegionByName(
            @RequestParam final String name) {
        String result = service.deleteRegionByName(name);
        if ("Delete".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Region not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Обновить регион по имени.
     *
     * @param name    Имя региона.
     * @param newName Новое имя региона.
     * @return Сообщение об успешном обновлении или об ошибке.
     */
    @PatchMapping("updateByName")
    public ResponseEntity<String> updateRegionByName(
            @RequestParam final String name,
            @RequestParam final String newName) {
        Region updatedRegion = service.updateRegionByName(name, newName);
        if (updatedRegion != null) {
            return new ResponseEntity<>(
                    "Updated region: ", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Region not found", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Получить список городов с интересным фактом для указанного региона.
     *
     * @param regionName      Имя региона.
     * @param interestingFact Интересный факт.
     * @return Список городов с интересным фактом.
     */
    @GetMapping("/townAndFact")
    public ResponseEntity<List<Towns>> getTownsWithFact(
            @RequestParam("regionName") final String regionName,
            @RequestParam("interestingFact") final String interestingFact) {
        List<Towns> towns = service.findTownsByRegionAndInterestingFact(
                regionName, interestingFact);
        if (!towns.isEmpty()) {
            return ResponseEntity.ok(towns);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
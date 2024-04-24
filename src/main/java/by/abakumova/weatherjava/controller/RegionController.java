package by.abakumova.weatherjava.controller;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.service.RegionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


/**
 * Контроллер для работы с регионами.
 */
@Controller
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


    @GetMapping("/regions")
    public String findAllRegion(final Model model) {
        List<Region> regions = service.findAll();
        model.addAttribute("regions", regions);
        return "region";
    }

    @GetMapping("/saveRegion")
    public String showSaveRegionForm(Model model) {
        model.addAttribute("region", new Region());
        return "saveRegion";
    }

    // Сохранение нового региона
    @PostMapping("/saveRegion")
    public String saveRegion(@ModelAttribute("region") Region region, RedirectAttributes redirectAttributes) {
        service.saveRegion(region);
        redirectAttributes.addFlashAttribute("message", "Region saved successfully");
        return "redirect:/api/v2/region/regions"; // Перенаправление на страницу списка регионов
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
   /* @DeleteMapping("deleteByName")
    public ResponseEntity<String> deleteRegionByName(
            @RequestParam final String name) {
        String result = service.deleteRegionByName(name);
        if ("Delete".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Region not found", HttpStatus.NOT_FOUND);
        }
    }*/

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



    @GetMapping("/region/edit/{name}")
    public String editRegionForm(@PathVariable String name, Model model) {
        model.addAttribute("region", service.findByNameRegion(name));
        return "editRegion"; // Предполагается, что у вас есть представление с именем "editRegion"
    }

    @PostMapping("/region/{name}")
    public String updateRegion(@PathVariable String name,
                               @ModelAttribute("region") Region region,
                               Model model) {
        // Получить регион из базы данных по имени
        Region existingRegion = service.findByNameRegion(name);

        // Обновить данные существующего региона
        existingRegion.setName(region.getName());
        existingRegion.setTowns(region.getTowns()); // Обновить города, если нужно

        // Сохранить обновленный объект региона
        service.saveRegion(existingRegion);

        return "redirect:/api/v2/region/regions"; // Перенаправление на страницу списка регионов
    }

    // Метод для обработки запроса на удаление региона
    @GetMapping("/region/delete/{name}")
    public String deleteRegion(@PathVariable String name) {
        service.deleteRegionByName(name);
        return "redirect:/api/v2/region/regions"; // Перенаправление на страницу списка регионов
    }
    @GetMapping("/search")
    public String searchTownByCriteria(@RequestParam("regionName") String regionName,
                                       @RequestParam("interestingFact") String interestingFact,
                                       Model model) {
        ResponseEntity<List<Towns>> response = getTownsWithFact(regionName, interestingFact);
        if (response.getStatusCode().is2xxSuccessful()) {
            model.addAttribute("towns", response.getBody());
            return "townSearchResult"; // Предполагается, что у вас есть представление с именем "townSearchResult"
        } else {
            return "error500"; // Предполагается, что у вас есть представление с именем "errorPage"
        }
    }

}
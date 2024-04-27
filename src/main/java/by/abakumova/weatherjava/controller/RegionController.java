package by.abakumova.weatherjava.controller;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.repository.TownRepository;
import by.abakumova.weatherjava.service.RegionService;
import by.abakumova.weatherjava.service.TownService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
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
    private final TownService serviceT;


    @GetMapping("/regions")
    public String findAllRegion(final Model model) {
        List<Region> regions = service.findAll();
        model.addAttribute("regions", regions);
        return "region";
    }



    @GetMapping("/saveRegion")
    public String showSaveRegionForm(final Model model) {
      //  model.addAttribute("region", new Region());
        model.addAttribute("region", new Region());
        model.addAttribute("town", new Towns());
        return "saveRegion";
    }

    // Сохранение нового региона
    @PostMapping("/saveRegion")
    public String saveRegion(@ModelAttribute("region") Region region,@ModelAttribute("town") Towns town) {

        List<Towns> towns = new ArrayList<>();
        town.setRegion(region);
         towns.add(town);
        region.setTowns(towns);
        service.saveRegion(region);
//        redirectAttributes.addFlashAttribute("message", "Region saved successfully");
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


//    @GetMapping("/createRegionWithTowns")
//    public String showCreateRegionWithTownsForm(Model model) {
//        // Здесь можно добавить логику, если это необходимо для отображения формы
//        return "createRegionWithTowns"; // Имя представления, которое отображает форму создания региона с городами
//    }
//
//    @PostMapping("/createRegionWithTowns")
//    public String createRegionWithTowns(
//            @RequestParam String regionName,
//            @RequestParam List<String> townNames,
//            RedirectAttributes redirectAttributes) {
//        List<Towns> towns = new ArrayList<>();
//        for (String townName : townNames) {
//            Towns town = new Towns();
//            town.setName(townName);
//            towns.add(town);
//        }
//
//        Region newRegion = new Region();
//        newRegion.setName(regionName);
//        newRegion.setTowns(towns);
//
//        try {
//            Region createdRegion = service.saveRegion(newRegion);
//            redirectAttributes.addFlashAttribute("message", "Region with towns saved successfully");
//        } catch (IllegalArgumentException e) {
//            redirectAttributes.addFlashAttribute("error", e.getMessage());
//        }
//
//        return "redirect:/api/v2/region/regions"; // Перенаправление на главную страницу
//    }


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

//    @GetMapping("/townAndFact")
//    public ResponseEntity<List<Towns>> getTownsWithFact(
//            @RequestParam("regionName") final String regionName,
//            @RequestParam("interestingFact") final String interestingFact) {
//        List<Towns> towns = service.findTownsByRegionAndInterestingFact(
//                regionName, interestingFact);
//        if (!towns.isEmpty()) {
//            return ResponseEntity.ok(towns);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }



    @GetMapping("/region/edit/{name}")
    public String editRegionForm(@PathVariable String name, Model model, Model model1) {
      //  System.out.println(service.findByNameRegion(name));
        model.addAttribute("nameRegion", "");
        Region region = service.findByNameRegion(name);
        System.out.println(region.getName());
        model1.addAttribute("regionName", region.getName());
        return "editRegion"; // Предполагается, что у вас есть представление с именем "editRegion"
    }


    @PostMapping("/region/edits")
    public String updateRegion( @RequestParam("nameRegion") final String nameRegion,@RequestParam("regionName") final String regionName) {
        // Получить регион из базы данных по имени

        Region existingRegion = service.findByNameRegion(regionName);
        existingRegion.setName(nameRegion);

        service.saveRegion(existingRegion);

        return "redirect:/api/v2/region/regions";
    }

    @GetMapping("/region/delete/{name}")
    public String deleteRegion(@PathVariable String name) {
        service.deleteRegionByName(name);
        return "redirect:/api/v2/region/regions"; // Перенаправление на страницу списка регионов
    }

}
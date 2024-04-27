package by.abakumova.weatherjava.controller;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.service.RegionService;
import by.abakumova.weatherjava.service.TownService;
import by.abakumova.weatherjava.model.Towns;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller //requstbody -show in web
@RequestMapping("/api/v1/weather")
@AllArgsConstructor
public final class TownController {

    private final TownService service;
    private  final RegionService regionService;

    /**
     * Retrieve all towns.
     *
     * @return List of all towns
     */
    @GetMapping("/towns")
    public String findAllTowns(final Model model) {
        List<Towns> towns = service.findAllTowns();
        model.addAttribute("towns", towns);
        return "towns";
    }
    @GetMapping("/saveTown")
    public String showSaveTownForm(Model model) {
        model.addAttribute("town", new Towns());
        return "saveTown"; // Предполагается, что у вас есть представление с именем "saveTown"
    }

    @PostMapping("/saveTown")
    public String saveTown(@ModelAttribute("town") Towns town, RedirectAttributes redirectAttributes) {
        service.saveTowns(town);
        redirectAttributes.addFlashAttribute("message", "Town saved successfully");
        return "redirect:/api/v1/weather/towns"; // Перенаправление на страницу списка городов
    }

    /**
     * Save a town.
     *
     * @param towns The town to save
     * @return The saved town
     */
    @PostMapping("saveTowns")
    public Towns saveTowns(
            @RequestBody final Towns towns) {
        return service.saveTowns(towns);
    }

    /**
     * Find town by name.
     *
     * @param nameTowns The name of the town to find
     * @return The found town
     */
    @GetMapping("findName")
    public Towns findByNameTowns(
            @RequestParam final String nameTowns) {
        return service.findByNameTowns(nameTowns);
    }

    /**
     * Delete town by name.
     *
     * @param nameTowns The name of the town to delete
     * @return ResponseEntity indicating success or failure
     */
    @DeleteMapping("deleteByName")
    public ResponseEntity<String> deleteRegionByName(
            @RequestParam final String nameTowns) {
        String result = service.deleteTownsByNameTowns(nameTowns);
        if ("Delete".equals(result)) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    "Region not found", HttpStatus.NOT_FOUND);
        }
    }

//    /**
//     * Update town by name.
//     *
//     * @param nameTowns   The name of the town to update
//     * @param coordinates The new coordinates of the town
//     * @return The updated town
//     */
//    @PutMapping("updateByName")
//    public Towns updateTownByName(@RequestParam final String nameTowns,
//                                  @RequestParam final String coordinates) {
//        return service.updateTownByName(nameTowns, coordinates);
//    }

    @GetMapping("/edit/{nameTowns}")
    public String editTownForm(@PathVariable String nameTowns, Model model) {
        Towns town = service.findByNameTowns(nameTowns);
        if (town != null) {
            model.addAttribute("town", town);
            return "editTown"; // Предполагается, что у вас есть представление с именем "editTown"
        } else {
            // Обработка случая, когда город не найден
            return "error404"; // Предполагается, что у вас есть представление с именем "error404"
        }
    }

    @PostMapping("/updateByName")
    public String updateTown(@RequestParam final String nameTowns,
                             @RequestParam final String coordinates,
                             Model model) {
        Towns updatedTown = service.updateTownByName(nameTowns, coordinates);
        if (updatedTown != null) {
            return "redirect:/api/v1/weather/towns"; // Перенаправление на страницу списка городов
        } else {

            model.addAttribute("error", "Город не найден");
            return "error500"; // Предполагается, что у вас есть представление с именем "errorPage"
        }
    }

//    @PostMapping("/town/{nameTowns}")
//    public String updateTown(@PathVariable String nameTowns,
//                             @ModelAttribute("town") Towns town,
//                             Model model) {
//        Towns updatedTown = service.updateTownByName(nameTowns, town.getCoordinates());
//        if (updatedTown != null) {
//            return "redirect:/api/v1/weather/towns"; // Перенаправление на страницу списка городов
//        } else {
//            // Город не найден, выполните необходимые действия, например, выведите сообщение об ошибке
//            model.addAttribute("error", "Город не найден");
//            return "error500"; // Предполагается, что у вас есть представление с именем "errorPage"
//        }
//    }



    @GetMapping("/info/{nameTowns}")
    public String getTownInfo(@PathVariable String nameTowns, Model model) {
        Towns town = service.findByNameTowns(nameTowns);
        model.addAttribute("town", town);
        return "townInfo"; // Возвращаем название HTML-шаблона для отображения информации о городе
    }


    // Метод для обработки запроса на удаление города
    @GetMapping("/delete/{nameTowns}")
    public String deleteTown(@PathVariable String nameTowns) {
        service.deleteTownsByNameTowns(nameTowns);
        return "redirect:/api/v1/weather/towns"; // Перенаправление на страницу списка городов
    }

    // В контроллере

//    @GetMapping("/region/{regionId}/towns")
//    public String getTownsByRegionId(@PathVariable Long regionId, Model model) {
//        Region region = regionService.findById(regionId);
//        List<Towns> towns = service.getTownsByRegionId(regionId);
//
//        model.addAttribute("region", region);
//        model.addAttribute("towns", towns);
//        return "regionowns"; // Имя HTML-шаблона
//    }

}
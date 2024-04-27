package by.abakumova.weatherjava.controller;

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

@Controller
@RequestMapping("/api/v1/weather")
@AllArgsConstructor
public final class TownController {

    private final TownService service;
    private  final RegionService regionService;

    @GetMapping("/towns")
    public String findAllTowns(final Model model) {
        List<Towns> towns = service.findAllTowns();
        model.addAttribute("towns", towns);
        return "towns";
    }
    @GetMapping("/saveTown")
    public String showSaveTownForm(Model model) {
        model.addAttribute("town", new Towns());
        return "saveTown";
    }

    @PostMapping("/saveTown")
    public String saveTown(@ModelAttribute("town") Towns town, RedirectAttributes redirectAttributes) {
        service.saveTowns(town);
        redirectAttributes.addFlashAttribute("message", "Town saved successfully");
        return "redirect:/api/v1/weather/towns";
    }

    @PostMapping("saveTowns")
    public Towns saveTowns(
            @RequestBody final Towns towns) {
        return service.saveTowns(towns);
    }


    @GetMapping("findName")
    public Towns findByNameTowns(
            @RequestParam final String nameTowns) {
        return service.findByNameTowns(nameTowns);
    }

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

    @GetMapping("/edit/{nameTowns}")
    public String editTownForm(@PathVariable String nameTowns, Model model) {
        Towns town = service.findByNameTowns(nameTowns);
        if (town != null) {
            model.addAttribute("town", town);
            return "editTown";
        } else {
            return "error404";
        }
    }

    @PostMapping("/updateByName")
    public String updateTown(@RequestParam final String nameTowns,
                             @RequestParam final String coordinates,
                             Model model) {
        Towns updatedTown = service.updateTownByName(nameTowns, coordinates);
        if (updatedTown != null) {
            return "redirect:/api/v1/weather/towns";
        } else {
            model.addAttribute("error", "Город не найден");
            return "error500";
        }
    }

    @GetMapping("/info/{nameTowns}")
    public String getTownInfo(@PathVariable String nameTowns, Model model) {
        Towns town = service.findByNameTowns(nameTowns);
        model.addAttribute("town", town);
        return "townInfo";
    }

    @GetMapping("/delete/{nameTowns}")
    public String deleteTown(@PathVariable String nameTowns) {
        service.deleteTownsByNameTowns(nameTowns);
        return "redirect:/api/v1/weather/towns";
    }

}
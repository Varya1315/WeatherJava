package by.abakumova.weatherjava.controller;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.model.Towns;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/v2/region")
@AllArgsConstructor
public class RegionController {
    private static final Logger LOG = LoggerFactory.getLogger(
            RegionController.class);

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
        model.addAttribute("region", new Region());
        model.addAttribute("town", new Towns());
        return "saveRegion";
    }

    @PostMapping("/saveRegion")
    public String saveRegion(@ModelAttribute("region") Region region,@ModelAttribute("town") Towns town) {

        List<Towns> towns = new ArrayList<>();
        town.setRegion(region);
         towns.add(town);
        region.setTowns(towns);
        service.saveRegion(region);
        return "redirect:/api/v2/region/regions";
    }

    @PostMapping("/saveRegions")
    public List<Region> saveRegions(
            @RequestBody final List<Region> regions) {
        return service.saveRegions(regions);
    }

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

    @GetMapping("/region/edit/{name}")
    public String editRegionForm(@PathVariable String name, Model model, Model model1) {
        model.addAttribute("nameRegion", "");
        Region region = service.findByNameRegion(name);
        System.out.println(region.getName());
        model1.addAttribute("regionName", region.getName());
        return "editRegion";
    }

    @PostMapping("/region/edits")
    public String updateRegion( @RequestParam("nameRegion") final String nameRegion,
                                @RequestParam("regionName") final String regionName) {
        Region existingRegion = service.findByNameRegion(regionName);
        existingRegion.setName(nameRegion);
        service.saveRegion(existingRegion);
        return "redirect:/api/v2/region/regions";
    }

    @GetMapping("/region/delete/{name}")
    public String deleteRegion(@PathVariable String name) {
        service.deleteRegionByName(name);
        return "redirect:/api/v2/region/regions";
    }
}
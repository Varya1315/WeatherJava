package by.abakumova.weatherjava.service;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.repository.RegionRepository;
import by.abakumova.weatherjava.repository.TownRepository;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.cache.Cache;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
@Primary
@Transactional
public class RegionService {
    private final RegionRepository repository;
    private final TownRepository repos;
    private final Cache<String, Region> regionCache;
    private static final Logger LOG =
            LoggerFactory.getLogger(RegionService.class);


    public List<Region> findAll() {

        CounterService.incrementRequestCount();
        int requestCount = CounterService.getRequestCount();

        LOG.info("Текущее количество запросов: {}", requestCount);

        List<Region> regions = repository.findAll();

        regions.forEach(region ->
                regionCache.put(region.getName(), region));

        return regions;
    }

    public List<Region> saveRegions(final List<Region> regions) {
        List<Region> newRegions = new ArrayList<>();
        regions.forEach(region -> {
            newRegions.add(saveRegion(region));
        });
        return newRegions;
    }

    public Region saveRegion(final Region newRegion) {
        Region existingRegion = repository.findByName(newRegion.getName());
        if (existingRegion != null) {
            throw new IllegalArgumentException("Region with name '"
                    + newRegion.getName() + "' already exists");
        }
        List<Towns> towns = newRegion.getTowns();
        repos.saveAll(towns);
        Region savedRegion = repository.save(newRegion);
        towns.forEach(town ->
                regionCache.put(town.getNameTowns(), savedRegion));

        LOG.info("Region '{}' saved and added to cache", savedRegion.getName());
        return savedRegion;
    }

    public Region findByNameRegion(final String name) {
        return Optional.ofNullable(regionCache.get(name))
                .map(cachedRegion -> {
                    LOG.info("Region found in cache");
                    return cachedRegion;
                })
                .orElseGet(() -> {
                    Region region = repository.findByName(name);
                    if (region != null) {
                        regionCache.put(name, region);
                        LOG.info("Region found in database and added to cache");
                    } else {
                        LOG.info("Region not found");
                    }
                    return region;
                });
    }


    public String deleteRegionByName(final String name) {
        Region regionToDelete = regionCache.get(name);

        if (regionToDelete == null) {
            regionToDelete = repository.findByName(name);
        }

        if (regionToDelete != null) {
            Hibernate.initialize(regionToDelete.getTowns());

            List<Towns> temp = regionToDelete.getTowns();
            repos.deleteAll(temp);
            repository.delete(regionToDelete);
            regionCache.remove(name);
            return "Delete";
        } else {
            return "Region not found";
        }
    }

    public Region updateRegionByName(
            final String name,
            final String newName) {
        Region existingRegion = regionCache.get(name);

        if (existingRegion == null) {
            existingRegion = repository.findByName(name);
            if (existingRegion != null) {
                LOG.info("Region retrieved from database");
            }
        } else {
            LOG.info("Region retrieved from cache");
        }

        if (existingRegion != null) {
            existingRegion.setName(newName);
            repository.save(existingRegion);
            regionCache.put(newName, existingRegion);
            return existingRegion;
        } else {
            return null;
        }
    }
}
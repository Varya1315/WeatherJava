package by.abakumova.weatherjava.service;

import by.abakumova.weatherjava.repository.TownRepository;
import by.abakumova.weatherjava.cache.Cache;
import by.abakumova.weatherjava.model.Towns;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@AllArgsConstructor
@Primary
@Transactional
public class TownService {
    private final Cache<String, Towns> townCache;

    private final TownRepository repository;

    private static final Logger LOG =
            LoggerFactory.getLogger(TownService.class);

    public List<Towns> findAllTowns() {
        return repository.findAll();
    }

    public Towns findByNameTowns(final String nameTowns) {

        return repository.findByNameTowns(nameTowns);
    }

    public Towns saveTowns(
            final Towns towns) {
        townCache.put(towns.getNameTowns(), towns);
        return repository.save(towns);
    }

    public String deleteTownsByNameTowns(
            final String nameTowns) {
        if (townCache.get(nameTowns) != null) {
            townCache.remove(nameTowns);
            LOG.info("Town removed from cache");
        }
        Towns townToDelete = repository.findByNameTowns(nameTowns);
        if (townToDelete != null) {
            repository.delete(townToDelete);
            return "Delete";
        } else {
            return null;
        }
    }

    public Towns updateTownByName(
            final String nameTowns,
            final String coordinates) {
        Towns existingTown = findByNameTowns(nameTowns);
        if (existingTown != null) {
            existingTown.setCoordinates(coordinates);

            System.out.println(nameTowns);

            if (townCache.get(nameTowns) != null) {
                townCache.put(nameTowns, existingTown);
                LOG.info("Town updated in cache");
            } else {
                LOG.info("Town updated in database");
            }
            return repository.save(existingTown);
        } else {
            LOG.warn("Town update operation failed due to not found in cache or repository");
            return null;
        }
    }
}
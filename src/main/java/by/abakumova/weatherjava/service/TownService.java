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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Primary
@Transactional
public class TownService {
    private final Cache<String, Towns> townCache;

    private final TownRepository repository;

    private static final Logger LOG =
            LoggerFactory.getLogger(TownService.class);
    /**
     * Возвращает список всех городов, сначала ищет в кэше, затем в базе данных.
     *
     * @return Список всех городов.
     */
    public List<Towns> findAllTowns() {
        List<Towns> cachedTowns = new ArrayList<>();
        for (Map.Entry<String, Towns> entry : townCache.getNativeCache()) {
            cachedTowns.add(entry.getValue());
        }

        if (!cachedTowns.isEmpty()) {
            LOG.info("Found {} towns in cache", cachedTowns.size());
            return cachedTowns;
        }

        List<Towns> towns = repository.findAll();
        for (Towns town : towns) {
            townCache.put(town.getNameTowns(), town);
        }
        LOG.info("Fetched {} towns from database and saved to cache",
                towns.size());

        return towns;
    }

    /**
     * Находит город по его имени.
     *
     * @param nameTowns Название города.
     * @return Объект города, найденный по указанному имени.
     */
    public Towns findByNameTowns(final String nameTowns) {
        return Optional.ofNullable(townCache.get(nameTowns))
                .map(town -> {
                    LOG.info("Town found in cache");
                    return town;
                })
                .orElseGet(() -> {
                    Towns town = repository.findByNameTowns(nameTowns);
                    if (town != null) {
                        LOG.info("Town found in database");
                        townCache.put(nameTowns, town);
                    } else {
                        LOG.info("Town not found in database");
                    }
                    return town;
                });
    }

    /**
     * Сохраняет город в кэш и базу данных.
     *
     * @param towns Город для сохранения.
     * @return Сохраненный город.
     */
    public Towns saveTowns(
            final Towns towns) {
        townCache.put(towns.getNameTowns(), towns);
        return repository.save(towns);
    }
    /**
     * Удаляет город по его имени.
     *
     * @param nameTowns Название города для удаления.
     * @return Строка "Delete" в случае успешного удаления города,
     * в противном случае возвращает null.
     */
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

    /**
     * Обновляет данные о городе по его имени.
     *
     * @param nameTowns    Название города.
     * @param coordinates  Новые координаты города.
     * @return Обновленный объект города.
     */
    public Towns updateTownByName(
            final @RequestParam String nameTowns,
            final @RequestParam String coordinates) {
        Towns existingTown = findByNameTowns(nameTowns);
        if (existingTown != null) {
            existingTown.setCoordinates(coordinates);
            if (townCache.get(nameTowns) != null) {
                townCache.put(nameTowns, existingTown);
                LOG.info("Town updated in cache");
            } else {
                LOG.info("Town updated in database");
            }
            return repository.save(existingTown);
        } else {
            LOG.warn("Town update operation failed due"
                    + " to not found in cache or repository");
            return null;
        }
    }

}
package by.abakumova.weatherjava.service;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.repository.RegionRepository;
import by.abakumova.weatherjava.repository.TownRepository;
import by.abakumova.weatherjava.model.Towns;
import by.abakumova.weatherjava.cache.Cache;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

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

    /**
     * Получает список всех регионов.
     *
     * @return Список всех регионов.
     */
    public List<Region> findAll() {

        CounterService.incrementRequestCount();
        int requestCount = CounterService.getRequestCount();

        LOG.info("Текущее количество запросов: {}", requestCount);

        List<Region> regions = repository.findAll();

        regions.forEach(region ->
                regionCache.put(region.getName(), region));

        return regions;
    }


    /**
     * Сохраняет список регионов.
     * <p>
     * Этот метод может быть переопределен в подклассах
     * для изменения способа сохранения регионов.
     *
     * @param regions Список регионов для сохранения.
     * @return Список сохраненных регионов.
     */
    public List<Region> saveRegions(final List<Region> regions) {
        List<Region> newRegions = new ArrayList<>();
        regions.forEach(region -> {
            if ("va".equals(region.getName())) {
                throw new HttpClientErrorException(
                        HttpStatus.BAD_REQUEST,
                        "Ошибка 400: Некорректное имя региона 'va'");
            }
            newRegions.add(saveRegion(region));
        });
        return newRegions;
    }


    /**
     * Сохраняет новый регион.
     *
     * @param newRegion Новый регион для сохранения.
     * @return Сохраненный регион.
     * @throws IllegalArgumentException Если регион с именем уже существует.
     */
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


    /**
     * Находит регион по его имени.
     *
     * @param name Имя региона для поиска.
     * @return Регион с указанным именем,
     * если найден; в противном случае возвращает null.
     */
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

    /**
     * Возвращает список городов по указанному региону и интересному факту.
     *
     * @param regionName      Название региона.
     * @param interestingFact Интересный факт о городах.
     * @return Список городов, удовлетворяющих указанному
     * региону и интересному факту.
     */
    public List<Towns> findTownsByRegionAndInterestingFact(
            final String regionName,
            final String interestingFact) {
        // Проверяем, есть ли регион в кеше
        Region cachedRegion = regionCache.get(regionName);
        if (cachedRegion != null) {
            LOG.info("Region found in cache."
                    + "Retrieving towns by interesting fact");
            // Если регион найден в кеше, возвращаем список городов из кеша
            return cachedRegion.getTowns();
        }
        LOG.info("Region not found in cache."
                + "Retrieving towns by interesting fact from repository");
        // Если регион не найден в кеше, получаем список городов из репозитория
        List<Towns> towns = repository.findTownsByRegionAndInterestingFact(
                regionName, interestingFact);

        // Если список городов не пустой, добавляем регион в кеш
        if (!towns.isEmpty()) {
            Region region = repository.findByName(regionName);
            regionCache.put(regionName, region);
            LOG.info("Region retrieved from repository and added to cache");
        }

        return towns;
    }


    /**
     * Удаляет регион по его имени.
     *
     * @param name Имя региона для удаления.
     * @return Сообщение о результате операции удаления.
     */
    public String deleteRegionByName(
            final String name) {
        Region regionToDelete = regionCache.get(name);

        if (regionToDelete == null) {
            regionToDelete = repository.findByName(name);
        }
        if (regionToDelete != null) {
            List<Towns> temp = regionToDelete.getTowns();
            repos.deleteAll(temp);
            repository.delete(regionToDelete);
            regionCache.remove(name);
            return "Delete";
        } else {
            return "Region not found";
        }
    }


    /**
     * Обновляет название региона с указанным именем.
     *
     * @param name    Имя региона для обновления.
     * @param newName Новое имя региона.
     * @return Обновленный объект региона или null,
     * если регион с указанным именем не найден.
     */

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
/*
 * Данный файл является частью пакета weather.springwea.repository.
 */

package by.abakumova.weatherjava.repository;

import by.abakumova.weatherjava.model.Region;
import by.abakumova.weatherjava.model.Towns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс репозитория для работы с сущностями Region.
 */
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    /**
     * Найти регион по имени.
     *
     * @param name Имя региона.
     * @return Найденный регион.
     */
    Region findByName(String name);

    /**
     * Найти города в регионе по интересному факту.
     *
     * @param regionName      Имя региона.
     * @param interestingFact Интересный факт.
     * @return Список городов, соответствующих критериям поиска.
     */
    @Query("SELECT t FROM Region r JOIN r.towns t WHERE"
            + " r.name = :regionName AND t.interestingFact = :interestingFact")
    List<Towns> findTownsByRegionAndInterestingFact(@Param("regionName")
                                                    String regionName, @Param("interestingFact") String interestingFact);
    /**
     * Найти регионы с количеством городов больше заданного значения.
     *
     * @param townCount Количество городов.
     * @return Список регионов, удовлетворяющих условию.
     */

    @Query("SELECT r FROM Region r WHERE SIZE(r.towns) > :townCount")
    List<Region> findRegionsWithMoreTowns(@Param("townCount") int townCount);
}
package by.abakumova.weatherjava.repository;

import by.abakumova.weatherjava.model.Towns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TownRepository extends  JpaRepository<Towns, Long> {
 Towns findByNameTowns(String nameTowns);

  List<Towns> findByRegionId(Long regionId);
 }

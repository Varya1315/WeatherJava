package by.abakumova.weatherjava.repository;

import by.abakumova.weatherjava.model.Towns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends  JpaRepository<Towns, Long> {
 Towns findByNameTowns(String nameTowns);

 }

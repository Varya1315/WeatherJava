package by.abakumova.weatherjava.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Represents a town entity.
 */
@Data
@Entity
@EnableCaching
public class Towns {
    /**
     * Marks this field as the primary key for the entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The coordinates of the town.
     */
    private String coordinates;

    /**
     * The name of the town.
     */
    private String nameTowns;

    /**
     * The time of the town.
     */
    private int time;

    /**
     * The position of the sun in the town.
     */
    private String positionSun;

    /**
     * An interesting fact about the town.
     */
    private String interestingFact;

    public Towns() {

    }

    public void setName(String town1) {
    }

    public String getName() {
        return nameTowns;
    }
    public Towns(Long id, String coordinates, String nameTowns, int time, String positionSun, String interestingFact) {
        this.id = id;
        this.coordinates = coordinates;
        this.nameTowns = nameTowns;
        this.time = time;
        this.positionSun = positionSun;
        this.interestingFact = interestingFact;
    }
}

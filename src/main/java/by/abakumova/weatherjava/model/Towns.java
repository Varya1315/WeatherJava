package by.abakumova.weatherjava.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Represents a town entity.
 */
@Data
@Entity
@EnableCaching
public class Towns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String coordinates;
    private String nameTowns;
    private int time;
    private String positionSun;
    private String interestingFact;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id") // Имя колонки для внешнего ключа
    private Region region;

    public Towns() {
// Этот метод намеренно оставлен пустым,
// потому что он используется в тестах
// для имитации установки имени объекта.

    }

    public void setName(String town1) {
        // Этот метод намеренно оставлен пустым,
        // потому что он используется в тестах
        // для имитации установки имени объекта.

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
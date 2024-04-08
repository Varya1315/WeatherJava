package by.abakumova.weatherjava.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/**
 * Represents a region entity.
 */
@Entity
@NoArgsConstructor
@Data
@Table(name = "region")
public class Region {

    /**
     * The unique identifier of the region.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The name of the region. Must be unique.
     */
    @Column(unique = true)
    private String name;

    /**
     * The list of towns belonging to this region.
     */
    @OneToMany
    @JoinColumn(name = "region_id")
    private List<Towns> towns;

    public Region(String testRegion, List<Object> objects) {
    }
}

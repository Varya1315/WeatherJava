package by.abakumova.weatherjava.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name = "region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    // Use FetchType.EAGER to enable eager loading
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "region", cascade = CascadeType.ALL)
    private List<Towns> towns;

    public Region(String name, List<Towns> towns) {
        this.name = name;
        this.towns = towns;
    }

}
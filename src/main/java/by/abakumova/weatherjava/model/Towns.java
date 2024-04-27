package by.abakumova.weatherjava.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.cache.annotation.EnableCaching;

@Data
@Entity
@EnableCaching
@Table(name = "towns")
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
    @JoinColumn(name = "region_id")
    private Region region;

}
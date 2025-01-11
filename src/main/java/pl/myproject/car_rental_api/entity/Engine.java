package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "engines")
public class Engine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "capacity", precision = 3, scale = 1)
    private BigDecimal capacity;

    @Column(name = "horsepower")
    private int horsepower;

    @Column(name = "torque")
    private int torque;

    @Column(name = "fuel_type")
    private String fuelType;

    @Column(name = "cylinder_configuration")
    private String cylinderConfiguration;

    @Column(name = "engine_type")
    private String engineType;

    public Engine(BigDecimal capacity, int horsepower, int torque, String fuelType, String cylinderConfiguration, String engineType) {
        this.capacity = capacity;
        this.horsepower = horsepower;
        this.torque = torque;
        this.fuelType = fuelType;
        this.cylinderConfiguration = cylinderConfiguration;
        this.engineType = engineType;
    }
}

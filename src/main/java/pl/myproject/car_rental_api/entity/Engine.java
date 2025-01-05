package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
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

    @Column(name = "capacity")
    private double capacity;

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

    public Engine(double capacity, int horsepower, int torque, String fuelType, String cylinderConfiguration, String engineType) {
        this.capacity = capacity;
        this.horsepower = horsepower;
        this.torque = torque;
        this.fuelType = fuelType;
        this.cylinderConfiguration = cylinderConfiguration;
        this.engineType = engineType;
    }
}

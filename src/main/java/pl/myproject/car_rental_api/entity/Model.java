package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "models")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "production_year")
    private LocalDate productionYear;

    @Column(name = "brand")
    private String brand;

    @Column(name = "brand_country")
    private String brandCountry;

    @Column(name = "color")
    private String color;

    @Column(name = "type_of_drive")
    private String typeOfDrive;

    @Column(name = "number_of_doors")
    private int numberOfDoors;

    @Column(name = "body_type")
    private String bodyType;

    @Column(name = "number_of_seats")
    private int numberOfSeats;

    @Column(name = "environmental_label")
    private String environmentalLabel;

    @Column(name = "fuel_consumption", precision = 4, scale = 2)
    private BigDecimal fuelConsumption;

    @Column(name = "CO2_emissions", precision = 5, scale = 1)
    private BigDecimal CO2Emissions;

    @Column(name = "weight")
    private int weight;

    @Column(name = "0-100_time", precision = 4, scale = 2)
    private BigDecimal accelerationTime;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "average_price")
    private int averagePrice;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "gearbox_id")
    private Gearbox gearbox;

    public Model(String description, int averagePrice, String photoUrl, BigDecimal accelerationTime, int weight, BigDecimal CO2Emissions, BigDecimal fuelConsumption, String environmentalLabel, int numberOfSeats, String bodyType, int numberOfDoors, String typeOfDrive, String color, String brandCountry, String brand, LocalDate productionYear, String type, String name) {
        this.description = description;
        this.averagePrice = averagePrice;
        this.photoUrl = photoUrl;
        this.accelerationTime = accelerationTime;
        this.weight = weight;
        this.CO2Emissions = CO2Emissions;
        this.fuelConsumption = fuelConsumption;
        this.environmentalLabel = environmentalLabel;
        this.numberOfSeats = numberOfSeats;
        this.bodyType = bodyType;
        this.numberOfDoors = numberOfDoors;
        this.typeOfDrive = typeOfDrive;
        this.color = color;
        this.brandCountry = brandCountry;
        this.brand = brand;
        this.productionYear = productionYear;
        this.type = type;
        this.name = name;
    }

    public Model(int id, String name, String type, LocalDate productionYear, String brand, String brandCountry, String color, String typeOfDrive, int numberOfDoors, String bodyType, int numberOfSeats, String environmentalLabel, BigDecimal fuelConsumption, BigDecimal CO2Emissions, int weight, BigDecimal accelerationTime, String photoUrl, int averagePrice, String description) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.productionYear = productionYear;
        this.brand = brand;
        this.brandCountry = brandCountry;
        this.color = color;
        this.typeOfDrive = typeOfDrive;
        this.numberOfDoors = numberOfDoors;
        this.bodyType = bodyType;
        this.numberOfSeats = numberOfSeats;
        this.environmentalLabel = environmentalLabel;
        this.fuelConsumption = fuelConsumption;
        this.CO2Emissions = CO2Emissions;
        this.weight = weight;
        this.accelerationTime = accelerationTime;
        this.photoUrl = photoUrl;
        this.averagePrice = averagePrice;
        this.description = description;
    }
}

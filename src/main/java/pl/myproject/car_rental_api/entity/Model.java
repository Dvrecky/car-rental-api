package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
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

    @Column(name = "fuel_consumption")
    private String fuelConsumption;

    @Column(name = "CO2_emissions")
    private String CO2Emissions;

    @Column(name = "weight")
    private int weight;

    @Column(name = "0-100_time")
    private double accelerationTime;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "average_price")
    private int averagePrice;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "engine_id")
    private Engine engine;

    @ManyToOne
    @JoinColumn(name = "gearbox_id")
    private Gearbox gearbox;

    public Model(String description, int averagePrice, String photoUrl, double accelerationTime, int weight, String CO2Emissions, String fuelConsumption, String environmentalLabel, int numberOfSeats, String bodyType, int numberOfDoors, String typeOfDrive, String color, String brandCountry, String brand, LocalDate productionYear, String type, String name) {
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
}

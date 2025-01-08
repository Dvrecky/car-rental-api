package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "cars")
@NamedEntityGraph(
        name = "car-model",
        attributeNodes = {
                @NamedAttributeNode(value = "model", subgraph = "model-engine-gearbox"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "model-engine-gearbox",
                        attributeNodes = {
                                @NamedAttributeNode("engine"),
                                @NamedAttributeNode("gearbox")
                        }
                )
        }
)
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "last_service_date")
    private LocalDate lastServiceDate;

    @Column(name = "mileage")
    private int mileage;

    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate;

    @Column(name = "rental_price_per_day")
    private double rentalPricePerDay;

    @Column(name = "base_price")
    private double basePrice;

    @OneToOne
    @JoinColumn(name = "model_id")
    private Model model;

    public Car(int basePrice, int rentalPricePerDay, LocalDate insuranceExpiryDate, int mileage, LocalDate lastServiceDate, String registrationNumber) {
        this.basePrice = basePrice;
        this.rentalPricePerDay = rentalPricePerDay;
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.mileage = mileage;
        this.lastServiceDate = lastServiceDate;
        this.registrationNumber = registrationNumber;
    }

    public Car(int id, String registrationNumber, LocalDate lastServiceDate, int mileage, LocalDate insuranceExpiryDate, double rentalPricePerDay, double basePrice) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.lastServiceDate = lastServiceDate;
        this.mileage = mileage;
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.rentalPricePerDay = rentalPricePerDay;
        this.basePrice = basePrice;
    }
}

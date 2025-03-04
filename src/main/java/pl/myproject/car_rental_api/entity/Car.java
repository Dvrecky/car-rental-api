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
                @NamedAttributeNode("carCondition")
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

    @Column(name = "vin")
    private String vin;

    @Column(name = "last_service_date")
    private LocalDate lastServiceDate;

    @Column(name = "mileage")
    private int mileage;

    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "model_id")
    private Model model;

    @OneToOne(mappedBy = "car", fetch = FetchType.LAZY,cascade = { CascadeType.PERSIST, CascadeType.REMOVE})
    private CarCondition carCondition;

    public Car(int basePrice, int rentalPricePerDay, LocalDate insuranceExpiryDate, int mileage, LocalDate lastServiceDate, String registrationNumber) {
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.mileage = mileage;
        this.lastServiceDate = lastServiceDate;
        this.registrationNumber = registrationNumber;
    }

    public Car(int id, String registrationNumber, LocalDate lastServiceDate, int mileage, LocalDate insuranceExpiryDate, int rentalPricePerDay, int basePrice) {
        this.id = id;
        this.registrationNumber = registrationNumber;
        this.lastServiceDate = lastServiceDate;
        this.mileage = mileage;
        this.insuranceExpiryDate = insuranceExpiryDate;
    }

    public void setCarCondition(CarCondition carCondition) {
        this.carCondition = carCondition;
        carCondition.setCar(this);
    }

    public String toStringDetails() {
        return "Car{" +
                "id=" + id +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", vin='" + vin + '\'' +
                ", lastServiceDate=" + lastServiceDate +
                ", mileage=" + mileage +
                ", insuranceExpiryDate=" + insuranceExpiryDate +
                '}';
    }

}

package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "cars")
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
    private String mileage;

    @Column(name = "insurance_expiry_date")
    private LocalDate insuranceExpiryDate;

    @Column(name = "rental_price_per_day")
    private int rentalPricePerDay;

    @Column(name = "base_price")
    private int basePrice;

    @OneToOne
    @JoinColumn(name = "model_id")
    private Model model;

    public Car(int basePrice, int rentalPricePerDay, LocalDate insuranceExpiryDate, String mileage, LocalDate lastServiceDate, String registrationNumber) {
        this.basePrice = basePrice;
        this.rentalPricePerDay = rentalPricePerDay;
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.mileage = mileage;
        this.lastServiceDate = lastServiceDate;
        this.registrationNumber = registrationNumber;
    }
}

package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "car_availability")
public class CarAvailability {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "status")
    private String status;

    @Column(name = "from")
    private LocalDate from;

    @Column(name = "to")
    private LocalDate to;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}

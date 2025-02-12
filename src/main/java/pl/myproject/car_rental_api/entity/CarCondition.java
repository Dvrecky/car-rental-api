package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "car")
@Entity
@Table(name = "car_conditions")
public class CarCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_rentable")
    private short isRentable;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "car_id")
    @OneToOne(fetch = FetchType.LAZY)
    private Car car;
}

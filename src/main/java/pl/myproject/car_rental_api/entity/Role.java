package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.myproject.car_rental_api.enums.UserRole;

@Entity
@Getter@Setter
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private UserRole role;
}

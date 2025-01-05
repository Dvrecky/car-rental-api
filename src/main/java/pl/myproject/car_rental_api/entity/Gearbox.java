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
@Table(name = "gearboxes")
public class Gearbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "producent")
    private String producent;

    @Column(name = "number_of_gears")
    private int numberOfGears;

    @Column(name = "type")
    private String type;

    public Gearbox(String type, byte numberOfGears, String producent, String name) {
        this.type = type;
        this.numberOfGears = numberOfGears;
        this.producent = producent;
        this.name = name;
    }
}

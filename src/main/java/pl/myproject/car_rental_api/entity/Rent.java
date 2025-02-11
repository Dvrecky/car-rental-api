package pl.myproject.car_rental_api.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "rents")
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "rental_start_date")
    private LocalDateTime startDate;

    @Column(name = "rental_end_date")
    private LocalDateTime endDate;

    @Column(name = "damage_report")
    private String damageReport;

    @Column(name = "penalty_fee")
    private int penaltyFee;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "status")
    private String status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}

package pl.myproject.car_rental_api.dto.rent;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class RentDTO {

    private long id;
    private long reservationId;
    private LocalDateTime startDate;
    private LocalDateTime updateDate;
    private String status;
    private LocalDateTime endDate;
    private String damageReport;
    private int penaltyFee;
}

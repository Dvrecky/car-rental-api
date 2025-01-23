package pl.myproject.car_rental_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UpdateReservationDateDTO {
    private LocalDateTime newStartDate;
    private LocalDateTime newEndDate;
}

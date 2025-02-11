package pl.myproject.car_rental_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AddRentDTO {
    
    private long reservationId;
    private LocalDateTime startDate;
    private LocalDateTime updateDate;
    private String status;
}

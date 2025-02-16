package pl.myproject.car_rental_api.dto.rent;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class UpdateRentDTO {

    private long id;
    private LocalDateTime endDate;
    private LocalDateTime updateDate;
    private String status;
}

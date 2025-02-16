package pl.myproject.car_rental_api.dto.other;


import lombok.*;

import java.time.LocalDate;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StatusDTO {

    private LocalDate from;
    private LocalDate to;
    private String status;
}

package pl.myproject.car_rental_api.dto.reservation;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AddReservationDTO {

    private long id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int totalPrice;
    private String paymentMethod;
    private String remarks;
    private int carId;
    private long userId;
}

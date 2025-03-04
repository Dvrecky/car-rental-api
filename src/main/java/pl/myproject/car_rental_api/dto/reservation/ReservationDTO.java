package pl.myproject.car_rental_api.dto.reservation;

import lombok.*;
import pl.myproject.car_rental_api.dto.car.CarDetailsDTO;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ReservationDTO {

    private long id;
    private LocalDateTime bookingDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int totalPrice;
    private String paymentMethod;
    private String status;
    private String remarks;
    private int carId;
    private long userId;
}

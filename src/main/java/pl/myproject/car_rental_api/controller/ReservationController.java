package pl.myproject.car_rental_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.myproject.car_rental_api.dto.reservation.AddReservationDTO;
import pl.myproject.car_rental_api.dto.reservation.ReservationDTO;
import pl.myproject.car_rental_api.dto.reservation.UpdateReservationDateDTO;
import pl.myproject.car_rental_api.projection.ClientReservationBaseView;
import pl.myproject.car_rental_api.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> addReservation(@RequestBody AddReservationDTO addReservationDTO) {

        addReservationDTO.setId(0);

        ReservationDTO savedReservation = reservationService.addReservation(addReservationDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @PatchMapping("/{id}/period")
    public ResponseEntity<ReservationDTO> changeReservationPeriod(@RequestBody UpdateReservationDateDTO updateReservationDateDTO, @PathVariable("id") long reservationId) {

        // handover new reservation period and reservation id
        ReservationDTO reservationDTO = reservationService.updateReservationPeriod(updateReservationDateDTO, reservationId);
        return ResponseEntity.ok(reservationDTO);
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable("id") long reservationId) {

        ReservationDTO canceledReservationDTO = reservationService.cancelReservation(reservationId);

        return ResponseEntity.ok(canceledReservationDTO);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<ClientReservationBaseView>> getReservationsByClientId(@PathVariable("id") long clientId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.getReservationsByClientId(clientId));
    }
}

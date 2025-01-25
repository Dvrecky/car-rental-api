package pl.myproject.car_rental_api.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.Reservation;

import java.time.LocalDateTime;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Long> {

    // query for updating reservation period
    @Transactional
    @Modifying
    @Query("UPDATE Reservation SET startDate = :newStartDate, endDate = :newEndDate WHERE id = :id")
    void updateReservationPeriod(@Param("id") long id, @Param("newStartDate") LocalDateTime startDate, @Param("newEndDate") LocalDateTime endDate);
}

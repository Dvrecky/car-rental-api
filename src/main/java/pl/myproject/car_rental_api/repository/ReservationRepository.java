package pl.myproject.car_rental_api.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.Reservation;
import pl.myproject.car_rental_api.projection.ClientReservationBaseView;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository <Reservation, Long> {

    // query for updating reservation period
    @Transactional
    @Modifying
    @Query("UPDATE Reservation SET startDate = :newStartDate, endDate = :newEndDate WHERE id = :id")
    void updateReservationPeriod(@Param("id") long id, @Param("newStartDate") LocalDateTime startDate, @Param("newEndDate") LocalDateTime endDate);

    // query for fetching reservation together with car data
    @EntityGraph(attributePaths = {"car", "car.model", "car.model.engine", "car.model.gearbox"})
    @Query("SELECT r FROM Reservation r WHERE r.id = :id")
    Optional<Reservation> findReservationWithCarDetailsById(long id);

    // query for fetching reservation with car
    @EntityGraph(attributePaths = "car")
    @Query("SELECT r FROM Reservation r WHERE r.id = :id")
    Optional<Reservation> findReservationWithCarById(long id);

    @Transactional
    @Modifying
    @Query("UPDATE Reservation SET status = :status WHERE id = :id")
    void changeStatus(@Param("id") long reservationId, @Param("status") String newStatus);

    @Query("""
            SELECT
                r.id as id,
                r.bookingDate as bookingDate,
                r.startDate as startDate,
                r.endDate as endDate,
                r.status as status,
                c.id as carId,
                concat(m.name, ' ',m.brand) as carFullName,
                m.photoUrl as photoUrl
            FROM Reservation r
            JOIN r.car c
            JOIN c.model m
            WHERE r.user.id = :userId
            """)
    List<ClientReservationBaseView> findAllReservationsBaseView(long userId);
}

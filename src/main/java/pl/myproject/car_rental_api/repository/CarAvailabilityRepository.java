package pl.myproject.car_rental_api.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.CarAvailability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarAvailabilityRepository extends JpaRepository<CarAvailability, Long> {

    List<CarAvailability> findAllByCarId(long id);

    @Query("SELECT c FROM CarAvailability c WHERE c.car.id = :id AND c.startDate <= :startDate AND c.endDate >= :endDate AND c.status='AVAILABLE'")
    Optional<CarAvailability> isCarAvailable(@Param("id") long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET endDate = :newEndDate WHERE id = :id")
    void updateEndDate(long id, @Param("newEndDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET endDate = :newEndDate, status = :newStatus WHERE id =:id")
    void updateEndDateAndStatus(long id, @Param("newEndDate")LocalDate endDate, @Param("newStatus") String status);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET startDate = :newStartDate, status = :status WHERE id=:id")
    void updateStartDateAndStatus( long id, @Param("newStartDate") LocalDate startDate,String status);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET status = :newStatus WHERE id = :id")
    void updateStatus(long id, @Param("newStatus") String status);

    // query for checking if new reservation date is available
    @Query("SELECT c FROM CarAvailability c WHERE c.car.Id = :carId AND c.status = 'AVAILABLE' AND " +
            "((:newStartDate between c.startDate AND c.endDate ) OR " +
            "(:newEndDate between c.startDate AND c.endDate))")
    Optional<CarAvailability> checkIfNewDateIsAvailable(long carId, @Param("newStartDate") LocalDate startDate, @Param("newEndDate") LocalDate endDate);

    // query for checking if new reservation date is available
    @Query("SELECT c FROM CarAvailability c WHERE c.car.Id = :carId AND c.status = 'AVAILABLE' AND " +
            "((:newStartDate between c.startDate AND c.endDate ) OR " +
            "(:newEndDate between c.startDate AND c.endDate))")
    Optional<List<CarAvailability>> checkIfNewPeriodIsAvailable(long carId, @Param("newStartDate") LocalDate startDate, @Param("newEndDate") LocalDate endDate);

    // query for getting a car availability for given car and period
    @Query("SELECT c FROM CarAvailability c WHERE c.car.Id = :carId AND c.startDate = :startDate AND c.endDate = :endDate")
    Optional<CarAvailability> getCarAvailability(@Param("carId") long carId , @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET startDate = :newStartDate  WHERE id = :id")
    void updateStartDate(long id, @Param("newStartDate") LocalDate startDate);

    @Query("SELECT c FROM CarAvailability c WHERE c.car.id = :carId AND c.startDate = :startDate AND c.status = :status")
    Optional<CarAvailability> checkStatusByStartDate(long carId, LocalDate startDate, String status);

    @Query("SELECT c FROM CarAvailability c WHERE c.car.id = :carId AND c.endDate = :endDate AND c.status = :status")
    Optional<CarAvailability> checkStatusByEndDate(long carId, LocalDate endDate, String status);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET startDate = :startDate, endDate = :endDate WHERE id = :id")
    void changePeriod(long id, LocalDate startDate, LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET startDate = :startDate, endDate = :endDate, status = :status WHERE id = :id")
    void changePeriodAndStatus(long id, LocalDate startDate, LocalDate endDate, String status);


}

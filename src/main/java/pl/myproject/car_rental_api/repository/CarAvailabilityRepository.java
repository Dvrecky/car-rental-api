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

    @Query("SELECT c FROM CarAvailability c WHERE c.car.id = :id AND c.startDate < :startDate AND c.endDate > :endDate AND c.status='AVAILABLE'")
    Optional<CarAvailability> isCarAvailable(@Param("id") long id, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional
    @Modifying
    @Query("UPDATE CarAvailability SET endDate = :newEndDate WHERE id = :id")
    void updateEndDate(@Param("newEndDate") LocalDate endDate, long id);
}

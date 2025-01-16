package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.CarAvailability;

import java.util.List;

@Repository
public interface CarAvailabilityRepository extends JpaRepository<CarAvailability, Long> {

    List<CarAvailability> findAllByCarId(long id);
}

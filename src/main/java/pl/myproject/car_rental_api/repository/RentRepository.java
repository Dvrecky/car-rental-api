package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.Rent;

@Repository
public interface RentRepository extends JpaRepository <Rent, Long> {
}

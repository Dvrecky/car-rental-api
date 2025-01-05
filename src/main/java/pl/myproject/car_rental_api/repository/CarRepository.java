package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository <Car, Integer> {

    @EntityGraph(value = "car-model", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Car c")
    List<Car> getAllCarsWithDetails();
}

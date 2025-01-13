package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.entity.Car;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository <Car, Integer> {

    @EntityGraph(value = "car-model", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Car c")
    List<Car> getAllCarsWithDetails();

    @EntityGraph(value = "car-model", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Car c WHERE c.id = :id")
    Optional<Car> findByIdWithDetails(@Param("id") int id);
}

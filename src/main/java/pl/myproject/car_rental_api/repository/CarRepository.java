package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.dto.CarBaseInfoDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.projection.CarBaseInfoProjection;

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

    Optional<Car> findByVin(String vin);

//    @Query("""
//            SELECT
//                c.id as id,
//                c.rentalPricePerDay as rentalPricePerDay,
//                concat(m.brand, ' ', m.name) as fullName,
//                m.typeOfDrive as typeOfDrive,
//                m.numberOfSeats as numberOfSeats,
//                m.accelerationTime as accelerationTime,
//                e.capacity as capacity,
//                e.horsepower as horsepower,
//                e.torque as torque,
//                e.cylinderConfiguration as cylinderConfiguration,
//                g.numberOfGears as numberOfGears
//            FROM Car c
//            JOIN c.model m
//            JOIN m.engine e
//            JOIN m.gearbox g
//            """)
//    List<CarBaseInfoProjection> findAllCarBaseInfo();

        @Query("""
            SELECT  
                new pl.myproject.car_rental_api.dto.CarBaseInfoDTO(
                    c.id as id,
                    c.rentalPricePerDay as rentalPricePerDay,
                    concat(m.brand, ' ', m.name) as fullName,
                    m.typeOfDrive as typeOfDrive,
                    m.numberOfSeats as numberOfSeats,
                    m.accelerationTime as accelerationTime,
                    e.capacity as capacity,
                    e.horsepower as horsepower,
                    e.torque as torque,
                    e.cylinderConfiguration as cylinderConfiguration,
                    g.numberOfGears as numberOfGears
                )
            FROM Car c
            JOIN c.model m
            JOIN m.engine e
            JOIN m.gearbox g
            """)
    List<CarBaseInfoDTO> findAllCarBaseInfo();
}

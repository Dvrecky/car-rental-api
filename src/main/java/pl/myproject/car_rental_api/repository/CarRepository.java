package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.dto.car.CarBaseInfoDTO;
import pl.myproject.car_rental_api.dto.car.CarSummaryInfoDTO;
import pl.myproject.car_rental_api.entity.Car;
import pl.myproject.car_rental_api.projection.CarListViewProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository <Car, Integer> {

    @EntityGraph(value = "car-model", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Car c")
    List<Car> findAllCarsWithDetails();

    @EntityGraph(value = "car-model", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT c FROM Car c WHERE c.id = :id")
    Optional<Car> findCarWithDetailsById(@Param("id") int id);

    Optional<Car> findCarByVin(String vin);

//    @Query("""
//            SELECT
//                c.id as id,
//                c.rentalPricePerDay as rentalPricePerDay,
//                concat(m.brand, ' ', m.name) as fullName,
//                m.typeOfDrive as typeOfDrive,
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
                new pl.myproject.car_rental_api.dto.car.CarBaseInfoDTO(
                    c.id,
                    c.rentalPricePerDay,
                    concat(m.brand, ' ', m.name),
                    m.typeOfDrive,
                    m.accelerationTime,
                    e.capacity,
                    e.horsepower,
                    e.torque,
                    e.cylinderConfiguration,
                    g.type
                )
            FROM Car c
            JOIN c.model m
            JOIN m.engine e
            JOIN m.gearbox g
            """)
    List<CarBaseInfoDTO> findAllCarsBaseInfo();

    @Query("""
            SELECT
                new pl.myproject.car_rental_api.dto.car.CarSummaryInfoDTO(
                    c.id,
                    c.rentalPricePerDay,
                    new pl.myproject.car_rental_api.dto.model.ModelSummaryInfoDTO(
                        m.name,
                        m.type,
                        m.productionYear,
                        m.brand,
                        m.brandCountry,
                        m.color,
                        m.typeOfDrive,
                        m.numberOfDoors,
                        m.bodyType,
                        m.numberOfSeats,
                        m.weight,
                        m.accelerationTime,
                        m.description,
                        new pl.myproject.car_rental_api.dto.engine.EngineSummaryInfoDTO(
                            e.capacity,
                            e.horsepower,
                            e.torque,
                            e.fuelType,
                            e.cylinderConfiguration,
                            e.engineType
                        ),
                        new pl.myproject.car_rental_api.dto.gearbox.GearboxSummaryInfoDTO(
                            g.name,
                            g.producer,
                            g.numberOfGears,
                            g.type
                        )
                    )
                )
            FROM Car c
            JOIN c.model m
            JOIN m.engine e
            JOIN m.gearbox g
            WHERE c.id = :id
            """)
    CarSummaryInfoDTO findCarSummaryInfoById(@Param("id") int id);

    @Query("""
            SELECT
                c.id as id,
                c.registrationNumber as registrationNumber,
                c.vin as vin,
                concat(m.name, ' ', m.brand) as fullName
            FROM Car c
            JOIN c.model m
            """)
    List<CarListViewProjection> findAllCarsListView();
}

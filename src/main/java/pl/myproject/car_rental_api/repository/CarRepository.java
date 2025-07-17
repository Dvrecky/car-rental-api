package pl.myproject.car_rental_api.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.myproject.car_rental_api.dto.car.CarAdminSummaryDTO;
import pl.myproject.car_rental_api.dto.car.CarSummaryDTO;
import pl.myproject.car_rental_api.dto.car.CarSummaryInfoDTO;
import pl.myproject.car_rental_api.entity.Car;

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

    @EntityGraph(attributePaths = {"model", "model.engine", "model.gearbox"})
    @Query("SELECT c FROM Car c WHERE c.id = :id")
    Optional<Car> findForAdminById(@Param("id") int id);

    Optional<Car> findCarByVin(String vin);

    @Query(
            """
                SELECT new pl.myproject.car_rental_api.dto.car.CarSummaryDTO(
                    c.id,
                    c.rentalPricePerDay,
                    m.name,
                    m.numberOfSeats,
                    m.accelerationTime,
                    m.photoUrl,
                    e.horsepower,
                    e.torque,
                    g.type as gearboxType
                )
                FROM Car c
                JOIN c.model m
                JOIN m.engine e
                JOIN m.gearbox g
            """
    )
    List<CarSummaryDTO> findAllCarsSummary();

    @Query(
            """
                SELECT new pl.myproject.car_rental_api.dto.car.CarAdminSummaryDTO(
                    c.id,
                    c.registrationNumber,
                    c.vin
                )
                FROM Car c
            """
    )
    List<CarAdminSummaryDTO> findAllCarsSummaryForAdmin();

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
}

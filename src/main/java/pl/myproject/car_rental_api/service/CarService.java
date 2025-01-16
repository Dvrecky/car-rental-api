package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.CarDTO;
import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.entity.Car;

import java.util.List;

public interface CarService {

    List<CarDTO> getAllCarsWithDetails();

    CarDTO saveCar(CarDTO carDTO);

    CarDTO getCarByIdWithDetails(int id);

    void deleteCarById(int id);

    CarDTO updateCar(CarDTO carDTO);

    List<StatusDTO> getCarAvailabilityList(long carId);
}

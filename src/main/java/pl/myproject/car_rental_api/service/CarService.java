package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.CarDTO;

import java.util.List;

public interface CarService {

    List<CarDTO> getAllCarsWithDetails();

    CarDTO saveCar(CarDTO carDTO);

    CarDTO getCarByIdWithDetails(int id);
}

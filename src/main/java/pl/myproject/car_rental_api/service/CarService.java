package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.car.CarBaseInfoDTO;
import pl.myproject.car_rental_api.dto.car.CarDetailsDTO;
import pl.myproject.car_rental_api.dto.car.CarListViewDTO;
import pl.myproject.car_rental_api.dto.car.CarSummaryInfoDTO;
import pl.myproject.car_rental_api.entity.Car;

import java.util.List;

public interface CarService {

    List<CarDetailsDTO> getAllCarsWithDetails();

    CarDetailsDTO saveCar(CarDetailsDTO carDTO);

    List<CarBaseInfoDTO> getCarsBaseView();

    List<CarListViewDTO> getCarListView();

    CarSummaryInfoDTO getCarSummaryById(int id);

    CarDetailsDTO getCarDTOWithDetailsById(int id);

    Car getCarWithDetailsById(int id);

    void deleteCarById(int id);

    CarDetailsDTO updateCar(CarDetailsDTO carDTO);
}

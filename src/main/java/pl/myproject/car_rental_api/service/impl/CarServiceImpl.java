package pl.myproject.car_rental_api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.car.*;
import pl.myproject.car_rental_api.entity.Car;

import pl.myproject.car_rental_api.exception.ResourceNotFoundException;
import pl.myproject.car_rental_api.mapper.CarMapper;
import pl.myproject.car_rental_api.repository.CarRepository;
import pl.myproject.car_rental_api.service.CarService;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, @Qualifier("defaultModelMapper") ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CarDetailsDTO> getAllCarsWithDetails() {

        return carRepository.findAllCarsWithDetails()
                .stream()
                .map(entity -> modelMapper.map(entity, CarDetailsDTO.class))
                .toList();
    }

    /**
     * @return list of car summaries for client
     */
    @Override
    public List<CarSummaryDTO> getCarsSummary() {
        return this.carRepository.findAllCarsSummary();
    }

    /**
     * @return list of car summaries for admin
     */
    @Override
    public List<CarAdminSummaryDTO> getCarsSummaryForAdmin() {
        return this.carRepository.findAllCarsSummaryForAdmin();
    }


    @Override
    public CarDetailsDTO saveCar(CarDetailsDTO carDTO) {
        Car car = modelMapper.map(carDTO, Car.class);

        // 🔥 Ręcznie ustawiamy referencję dla relacji dwustronnej
//        if (car.getCarCondition() != null) {
//            car.getCarCondition().setCar(car);
//        }

        Car newCar = carRepository.save(car);

        return modelMapper.map(newCar, CarDetailsDTO.class);
    }

    @Override
    public CarSummaryInfoDTO getCarSummaryById(int id) {
        return carRepository.findCarSummaryInfoById(id);
    }

    /**
     * @param id of the car
     * @return Car DTO for Admin
     */
    @Override
    public CarAdminDTO getForAdminById(int id) {
        return CarMapper.toDto(carRepository.findForAdminById(id).orElseThrow(
                () -> new ResourceNotFoundException("Car resource with ID: " + id + "has not been found")));
    }


    public Car getCarWithDetailsById(int id) {
        return carRepository.findCarWithDetailsById(id).get();
    }

    @Override
    public void deleteCarById(int id) {
        carRepository.deleteById(id);
    }

    @Override
    public CarDetailsDTO updateCar(CarDetailsDTO carDTO) {

        Car car = modelMapper.map(carDTO, Car.class);
        Car updatedCar = carRepository.save(car);
        return modelMapper.map(updatedCar, CarDetailsDTO.class);
    }
}

package pl.myproject.car_rental_api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.car.CarBaseInfoDTO;
import pl.myproject.car_rental_api.dto.car.CarDetailsDTO;
import pl.myproject.car_rental_api.dto.car.CarListViewDTO;
import pl.myproject.car_rental_api.dto.car.CarSummaryInfoDTO;
import pl.myproject.car_rental_api.entity.Car;

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

    @Override
    public CarDetailsDTO saveCar(CarDetailsDTO carDTO) {
        Car car = modelMapper.map(carDTO, Car.class);

        // 🔥 Ręcznie ustawiamy referencję dla relacji dwustronnej
        if (car.getCarCondition() != null) {
            car.getCarCondition().setCar(car);
        }

        Car newCar = carRepository.save(car);

        return modelMapper.map(newCar, CarDetailsDTO.class);
    }

    @Override
    public List<CarBaseInfoDTO> getCarsBaseView() {

        return carRepository.findAllCarsBaseInfo();

//        return carRepository.findAllCarBaseInfo()
//                .stream()
//                .map( carInfo -> modelMapper.map(carInfo, CarBaseInfoDTO.class))
//                .toList();
    }

    @Override
    public List<CarListViewDTO> getCarListView() {
        return carRepository.findAllCarsListView()
                .stream()
                .map( car -> modelMapper.map(car, CarListViewDTO.class))
                .toList();
    }

    @Override
    public CarSummaryInfoDTO getCarSummaryById(int id) {
        return carRepository.findCarSummaryInfoById(id);
    }

    @Override
    public CarDetailsDTO getCarDTOWithDetailsById(int id) {
        Car car = carRepository.findCarWithDetailsById(id).get();
        return modelMapper.map(car, CarDetailsDTO.class);
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

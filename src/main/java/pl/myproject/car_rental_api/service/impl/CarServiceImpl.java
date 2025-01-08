package pl.myproject.car_rental_api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.CarDTO;
import pl.myproject.car_rental_api.repository.CarRepository;
import pl.myproject.car_rental_api.service.CarService;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CarDTO> getAllCarsWithDetails() {

        return carRepository.getAllCarsWithDetails()
                .stream()
                .map(entity -> modelMapper.map(entity, CarDTO.class))
                .toList();
    }
}

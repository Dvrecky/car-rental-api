package pl.myproject.car_rental_api.service.impl;

import org.springframework.stereotype.Service;
import pl.myproject.car_rental_api.dto.StatusDTO;
import pl.myproject.car_rental_api.entity.CarAvailability;
import pl.myproject.car_rental_api.repository.CarAvailabilityRepository;
import pl.myproject.car_rental_api.service.CarAvailabilityService;

import java.util.List;

@Service
public class CarAvailabilityServiceImpl implements CarAvailabilityService {

    private final CarAvailabilityRepository carAvailabilityRepository;

    public CarAvailabilityServiceImpl(CarAvailabilityRepository carAvailabilityRepository) {
        this.carAvailabilityRepository = carAvailabilityRepository;
    }

    public List<StatusDTO> getCarAvailabilityList(long carId) {
        List<CarAvailability> carAvailabilities = carAvailabilityRepository.findAllByCarId(carId);
        List<StatusDTO> carAvailabilityList = carAvailabilities.stream()
                .map(carAv -> new StatusDTO(carAv.getStartDate(), carAv.getEndDate(), carAv.getStatus()))
                .toList();
        return carAvailabilityList;
    }
}

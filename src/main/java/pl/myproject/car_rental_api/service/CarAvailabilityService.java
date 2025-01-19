package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.StatusDTO;

import java.util.List;

public interface CarAvailabilityService {

    List<StatusDTO> getCarAvailabilityList(long carId);
}

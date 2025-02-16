package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.rent.AddRentDTO;
import pl.myproject.car_rental_api.dto.rent.RentDTO;
import pl.myproject.car_rental_api.dto.rent.UpdateRentDTO;

public interface RentService {

    AddRentDTO addRent(AddRentDTO addRentDTO);

    RentDTO updateRent(UpdateRentDTO updateRentDTO);
}

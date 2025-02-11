package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.AddRentDTO;
import pl.myproject.car_rental_api.dto.RentDTO;
import pl.myproject.car_rental_api.dto.UpdateRentDTO;

public interface RentService {

    AddRentDTO addRent(AddRentDTO addRentDTO);

    RentDTO updateRent(UpdateRentDTO updateRentDTO);
}

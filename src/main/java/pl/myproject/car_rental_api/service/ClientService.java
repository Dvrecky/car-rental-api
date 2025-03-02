package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.user.AddUserDTO;
import pl.myproject.car_rental_api.dto.user.UserDTO;

public interface ClientService {

    UserDTO addUser(AddUserDTO addUserDTO);
}

package pl.myproject.car_rental_api.service;

import pl.myproject.car_rental_api.dto.login.LoginRequest;
import pl.myproject.car_rental_api.dto.login.LoginResponse;
import pl.myproject.car_rental_api.dto.user.AddUserDTO;
import pl.myproject.car_rental_api.dto.user.UserDTO;

public interface EmployeeService {

    UserDTO addUser(AddUserDTO addUserDTO);

    LoginResponse login(LoginRequest loginRequest);
}

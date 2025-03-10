package pl.myproject.car_rental_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.myproject.car_rental_api.dto.login.LoginRequest;
import pl.myproject.car_rental_api.dto.login.LoginResponse;
import pl.myproject.car_rental_api.dto.user.AddUserDTO;
import pl.myproject.car_rental_api.dto.user.UserDTO;
import pl.myproject.car_rental_api.service.EmployeeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<UserDTO> addEmployee(@RequestBody AddUserDTO addUserDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employeeService.addUser(addUserDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(employeeService.login(loginRequest));
    }
}

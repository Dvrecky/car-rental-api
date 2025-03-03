package pl.myproject.car_rental_api.dto.login;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class LoginRequest {

    private String email;
    private String password;
}

package pl.myproject.car_rental_api.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter@Setter
public class LoginResponse {

    private String status;
    private String token;
}

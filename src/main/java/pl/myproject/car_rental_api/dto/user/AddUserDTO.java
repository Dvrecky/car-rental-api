package pl.myproject.car_rental_api.dto.user;

import lombok.*;

@Builder
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddUserDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNumber;
}

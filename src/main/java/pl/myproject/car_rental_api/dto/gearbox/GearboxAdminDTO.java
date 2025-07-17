package pl.myproject.car_rental_api.dto.gearbox;

import lombok.Data;

@Data
public class GearboxAdminDTO {

    private int id;
    private String name;
    private String producer;
    private int numberOfGears;
    private String type;
}

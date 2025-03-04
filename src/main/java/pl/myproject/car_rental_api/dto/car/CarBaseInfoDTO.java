package pl.myproject.car_rental_api.dto.car;

import java.math.BigDecimal;

//@Getter
//public class CarBaseInfoDTO {
//
//    private int id;
//    private int rentalPricePerDay;
//    private String fullName;
//    private String typeOfDrive;
//    private BigDecimal accelerationTime;
//    private BigDecimal capacity;
//    private int horsepower;
//    private int torque;
//    private String cylinderConfiguration;
//    private String gearboxType;
//}

public record CarBaseInfoDTO(int id, String fullName, String typeOfDrive,
                             BigDecimal accelerationTime, BigDecimal capacity, int horsepower, int torque,
                             String cylinderConfiguration, String gearboxType)
{}
